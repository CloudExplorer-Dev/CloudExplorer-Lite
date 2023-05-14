package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.mapping.NestedProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.job.context.JobContext;
import com.fit2cloud.common.job.result.Result;
import com.fit2cloud.common.job.result.impl.SimpleResult;
import com.fit2cloud.common.job.step.JobStep;
import com.fit2cloud.common.job.step.impl.SimpleJobStep;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceScanResourceResult;
import com.fit2cloud.dao.entity.ComplianceScanResult;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.*;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/12  20:16}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class JobStepServiceImpl implements IJobStepService {
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private IComplianceScanService complianceScanService;
    @Resource
    private IComplianceRuleService complianceRuleService;
    @Resource
    private IComplianceScanResultService complianceScanResultService;
    @Resource
    private IComplianceScanResourceResultService complianceScanResourceResultService;

    @Override
    public Result checkSupportResource(JobContext<Context> context, JobStep<Context> step) {
        context.getContext().cloudAccount = cloudAccountService.getById(context.getContext().cloudAccountId);
        if (Objects.isNull(context.getContext().cloudAccount)) {
            step.setChildren(List.of(SimpleJobStep.of("DELETE::NOT_FOUNT_CLOUD_ACCOUNT", "清除当前云账号数据", (c, s) -> {
                deleteResourceDataByCloudAccountId(c.getContext().cloudAccountId);
                return SimpleResult.of(200, "", true);
            })));
            return SimpleResult.of(500, "云账号不存在", true);
        }
        Class<? extends ICloudProvider> iCloudProviderClazz = ICloudProvider.of(context.getContext().cloudAccount.getPlatform());
        List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> map = CommonUtil.exec(iCloudProviderClazz, ICloudProvider::getResourceSyncDimensionConstants);
        // 如果不存在同步粒度则为不支持的资源类型
        Optional<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> first = map
                .stream()
                .filter(item -> item.getKey().equals(context.getContext().instanceType))
                .findFirst();

        if (first.isPresent()) {
            DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants> resourceTypeConstantsSyncDimensionConstantsDefaultKeyValue = first.get();
            context.getContext().syncDimensionConstants = resourceTypeConstantsSyncDimensionConstantsDefaultKeyValue.getValue();
            return SimpleResult.of(500, "不支持的资源类型", true);
        }
        return SimpleResult.of(200, "", false);
    }

    @Override
    public Result checkCloudAccount(JobContext<Context> context, JobStep<Context> step) {
        CloudAccount cloudAccount = cloudAccountService.getById(context.getContext().cloudAccountId);
        if (Objects.isNull(cloudAccount)) {
            step.setChildren(List.of(SimpleJobStep.of("DELETE::NOT_FOUNT_CLOUD_ACCOUNT", "清除当前云账号数据", (c, s) -> {
                deleteResourceDataByCloudAccountId(c.getContext().cloudAccountId);
                return SimpleResult.of(200, "", true);
            })));
            return SimpleResult.of(500, "云账号不存在", true);
        }
        context.getContext().credential = Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential());
        try {
            boolean verification = context.getContext().credential.verification();
            if (verification) {
                cloudAccount.setState(true);
                cloudAccountService.updateById(cloudAccount);
                return SimpleResult.of(200, "", false);
            }
        } catch (Exception ignored) {
        }
        cloudAccount.setState(false);
        cloudAccountService.updateById(cloudAccount);
        return SimpleResult.of(500, "云账号校验失败", true);
    }

    @Override
    public Result syncResource(JobContext<Context> context, JobStep<Context> step) {
        if (SyncDimensionConstants.REGION.equals(context.getContext().syncDimensionConstants)) {
            // 同步区域
            List<JobStep<Context>> jobSteps = context.getContext().credential.regions().stream()
                    .map(region -> SimpleJobStep.of("SYNC::" + region.getRegionId(), "同步:" + region.getName(), this::syncResourceChildren, region))
                    .map(simpleJobStep -> (JobStep<Context>) simpleJobStep)
                    .toList();
            context.getContext().syncDimensionConstants = SyncDimensionConstants.REGION;
            // 设置子步骤
            jobSteps = new ArrayList<>(jobSteps);
            // 插入数据
            jobSteps.add(SimpleJobStep.of("SAVE::RESOURCE_DATA", "插入原始数据", this::saveOrUpdateData));
            step.setChildren(jobSteps);
        } else {
            context.getContext().syncDimensionConstants = SyncDimensionConstants.CloudAccount;
            // 设置子步骤
            step.setChildren(List.of(SimpleJobStep.of("SYNC::RESOURCE_CHILDREN", "同步资源", this::syncResourceChildren)
                    , SimpleJobStep.of("SAVE::RESOURCE_DATA", "插入原始数据", this::saveOrUpdateData)));
        }
        return SimpleResult.of(200, "", false);
    }

    @Override
    public Result syncResourceChildren(JobContext<Context> context, JobStep<Context> step) {
        Credential.Region region = (Credential.Region) step.getStepContext();
        Credential credential = Credential.of(context.getContext().cloudAccount.getPlatform(), context.getContext().cloudAccount.getCredential());
        Class<? extends ICloudProvider> iCloudProviderClazz = ICloudProvider.of(context.getContext().cloudAccount.getPlatform());
        List<ResourceInstance> result;
        try {
            result = CommonUtil.exec(iCloudProviderClazz,
                    JsonUtil.toJSONString(Objects.equals(context.getContext().syncDimensionConstants, SyncDimensionConstants.REGION) ?
                            Map.of("regionId", region.getRegionId(), "credential", credential) : Map.of("credential", credential)),
                    context.getContext().instanceType.getExec());
            result = CollectionUtils.isEmpty(result) ? new ArrayList<>() : result;
            result.forEach(resource -> {
                resource.setCloudAccountId(context.getContext().cloudAccountId);
                resource.setPlatform(context.getContext().cloudAccount.getPlatform());
            });
            // 将数据添加到上下文
            context.getContext().resourceInstances.addAll(result);
            return SimpleResult.of(200, "同步", false, StepResultData.of(result.size(), region));
        } catch (SkipPageException e) {
            return SimpleResult.of(200, "同步", false, StepResultData.of(0, region));
        }
    }

    @Override
    public Result saveOrUpdateData(JobContext<Context> context, JobStep<Context> step) {
        try {
            saveOrUpdateData(context.getContext().cloudAccount, context.getContext().instanceType, context.getContext().resourceInstances);
            deleteNotFountCloudAccountData();
        } catch (IOException e) {
            return SimpleResult.of(500, "", false);
        }
        return SimpleResult.of(200, "", false);
    }

    @Override
    public Result scan(JobContext<Context> context, JobStep<Context> step) {
        scan(context.getContext().instanceType, context.getContext().cloudAccountId);
        return SimpleResult.of(200, "", false);
    }

    @Override
    public Result updateScanTime(JobContext<Context> context, JobStep<Context> step) {
        complianceRuleService.update(new LambdaUpdateWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, context.getContext().instanceType.name()).set(ComplianceRule::getUpdateTime, context.getContext().syncTime));
        return SimpleResult.of(200, "", false);
    }

    @Override
    public Result deleteNotFountCloudAccountData(JobContext<Context> context, JobStep<Context> step) {
        deleteNotFountCloudAccountData();
        return SimpleResult.of(200, "", false);
    }

    public void deleteNotFountCloudAccountData() {
        // 所有的云账号
        List<CloudAccount> cloudAccounts = cloudAccountService.list();
        Query query = new BoolQuery.Builder().mustNot(new Query.Builder().terms(new TermsQuery.Builder()
                .terms(new TermsQueryField.Builder().value(cloudAccounts.stream().map(CloudAccount::getId)
                        .map(FieldValue::of).toList()).build()).field("cloudAccountId").build()).build()).build()._toQuery();
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest.Builder().query(query).refresh(Boolean.TRUE)
                .index(ResourceInstance.class.getAnnotation(Document.class).indexName()).build();
        try {
            // 删除es数据
            elasticsearchClient.deleteByQuery(deleteByQueryRequest);
            // 删除扫描数据
            complianceScanResultService.remove(new LambdaQueryWrapper<ComplianceScanResult>()
                    .notIn(ComplianceScanResult::getCloudAccountId, (cloudAccounts.stream().map(CloudAccount::getId).toList())));
            // 删除扫描资源数据
            complianceScanResourceResultService.remove(new LambdaQueryWrapper<ComplianceScanResourceResult>()
                    .notIn(ComplianceScanResourceResult::getCloudAccountId, (cloudAccounts.stream().map(CloudAccount::getId).toList())));
        } catch (Exception ignored) {
        }
    }

    @SneakyThrows
    private void deleteResourceDataByCloudAccountId(String cloudAccountId) {
        Query query = new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()._toQuery();
        DeleteByQueryRequest build = new DeleteByQueryRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName()).query(query).refresh(true).build();
        elasticsearchClient.deleteByQuery(build);
        complianceScanResultService.remove(new LambdaQueryWrapper<ComplianceScanResult>()
                .eq(ComplianceScanResult::getCloudAccountId, cloudAccountId));
        complianceScanResourceResultService.remove(
                new LambdaQueryWrapper<ComplianceScanResourceResult>()
                        .eq(ComplianceScanResourceResult::getCloudAccountId, cloudAccountId)
        );
    }


    /**
     * 扫描原始资源
     *
     * @param instanceType 实例类型
     */
    private void scan(ResourceTypeConstants instanceType, String cloudAccountId) {
        complianceScanService.scanComplianceOrSave(instanceType, cloudAccountId);
        complianceScanService.scanComplianceResourceOrSave(instanceType, cloudAccountId);
    }

    /**
     * 插入或者更新数据
     *
     * @param cloudAccount         云账号id
     * @param instanceType         资源实例类型
     * @param resourceInstancesAll 资源数据
     * @throws IOException 插入可能抛出的异常
     */
    private synchronized void saveOrUpdateData(CloudAccount cloudAccount, ResourceTypeConstants instanceType, List<ResourceInstance> resourceInstancesAll) throws IOException {
        //  构建历史数据查询
        Query query = new Query.Builder().bool(new BoolQuery.Builder()
                        .must(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccount.getId()).build()).build(),
                                new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(instanceType.name()).build()).build()).build())
                .build();
        DeleteByQueryRequest build = new DeleteByQueryRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName()).query(query).refresh(true).build();
        //  删除数据
        elasticsearchClient.deleteByQuery(build);
        if (CollectionUtils.isEmpty(resourceInstancesAll)) {
            return;
        }
        //  处理嵌套数组问题
        List<String> filterArrayKeys = resourceInstancesAll.stream().map(ResourceInstance::getFilterArray).filter(Objects::nonNull).flatMap(f -> f.keySet().stream()).distinct().toList();
        for (String filterArrayKey : filterArrayKeys) {
            elasticsearchClient.indices().putMapping(b -> b.properties(Map.of("filterArray." + filterArrayKey, Property.of(p -> p.nested(NestedProperty.of(n -> n)))))
                    .index(ResourceInstance.class.getAnnotation(Document.class).indexName()));
        }

        BulkRequest bulkRequest = new BulkRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName())
                .operations(resourceInstancesAll.stream().map(source -> new BulkOperation.Builder()
                        .index(new IndexOperation.Builder<>().document(source).build()).build()).toList()).refresh(Refresh.True).build();
        //  插入数据
        elasticsearchClient.bulk(bulkRequest);
    }
}
