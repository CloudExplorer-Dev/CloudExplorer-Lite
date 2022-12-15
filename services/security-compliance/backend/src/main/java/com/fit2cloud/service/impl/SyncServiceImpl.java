package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.ISyncService;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  14:29}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class SyncServiceImpl implements ISyncService {
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ElasticsearchClient elasticsearchClient;

    @SneakyThrows
    @Override
    public void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        Credential credential = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential());
        ArrayList<ResourceInstance> resourceInstancesAll = new ArrayList<>();
        for (Credential.Region region : credential.regions()) {
            String execParams = getExecParams(cloudAccount, region.getRegionId());
            Class<? extends ICloudProvider> iCloudProviderClazz = ICloudProvider.of(cloudAccount.getPlatform());
            try {
                List<ResourceInstance> resourceInstances = CommonUtil.exec(iCloudProviderClazz, execParams, ICloudProvider::listEcsInstance);
                resourceInstances.forEach(resourceInstance -> resourceInstance.setCloudAccountId(cloudAccountId));
                resourceInstancesAll.addAll(resourceInstances);
            } catch (SkipPageException ignored) {
                // 跳过当前区域
            }
        }
        Query query = new Query.Builder().bool(new BoolQuery.Builder()
                        .must(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build(),
                                new Query.Builder().term(new TermQuery.Builder().field("instanceType").value(instanceType.name()).build()).build()).build())
                .build();
        DeleteByQueryRequest build = new DeleteByQueryRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName()).query(query).refresh(true).build();
        elasticsearchClient.deleteByQuery(build);
        elasticsearchTemplate.save(resourceInstancesAll);
    }

    private String getExecParams(CloudAccount cloudAccount, String region) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("credential", JsonUtil.parseObject(cloudAccount.getCredential(), Map.class));
        params.put("regionId", region);
        return JsonUtil.toJSONString(params);
    }
}
