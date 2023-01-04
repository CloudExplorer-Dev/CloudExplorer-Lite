package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.BaseSyncService;
import com.fit2cloud.service.IComplianceRuleService;
import com.fit2cloud.service.IComplianceScanService;
import com.fit2cloud.service.ISyncService;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
public class SyncServiceImpl extends BaseSyncService implements ISyncService {
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private IComplianceScanService complianceScanService;
    @Resource
    private IComplianceRuleService complianceRuleService;

    @SneakyThrows
    @Override
    public void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        LocalDateTime syncTime = getSyncTime();
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
                                new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(instanceType.name()).build()).build()).build())
                .build();
        DeleteByQueryRequest build = new DeleteByQueryRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName()).query(query).refresh(true).build();
        elasticsearchClient.deleteByQuery(build);
        elasticsearchTemplate.save(resourceInstancesAll);
        complianceScanService.updateCacheScanComplianceByInstanceType(instanceType);
        complianceRuleService.update(new LambdaUpdateWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, instanceType.name()).set(ComplianceRule::getUpdateTime, syncTime));
    }

    private String getExecParams(CloudAccount cloudAccount, String region) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("credential", JsonUtil.parseObject(cloudAccount.getCredential(), Map.class));
        params.put("regionId", region);
        return JsonUtil.toJSONString(params);
    }
}
