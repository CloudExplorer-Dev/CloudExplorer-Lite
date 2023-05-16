package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.job.actuator.JobActuator;
import com.fit2cloud.common.job.context.impl.SimpleJobContext;
import com.fit2cloud.common.job.job.Job;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceRuleGroup;
import com.fit2cloud.quartz.CloudAccountSyncJob;
import com.fit2cloud.service.*;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  14:29}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class SyncServiceImpl extends BaseSyncService implements ISyncService {
    @Resource
    private IJobService jobService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private IComplianceRuleService complianceRuleService;
    @Resource
    private IComplianceRuleGroupService complianceRuleGroupService;

    @SneakyThrows
    @Override
    public void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType) {
        syncInstance(cloudAccountId, instanceType,
                List.of(JobActuator.ExecuteStepData.of("VERIFICATION::CLOUD_ACCOUNT"),
                        JobActuator.ExecuteStepData.of("SYNC::RESOURCE"),
                        JobActuator.ExecuteStepData.of("SCAN::RESOURCE"),
                        JobActuator.ExecuteStepData.of("UPDATE::SCAN_TIME")));
    }


    @Override
    public void syncInstanceByInstanceType(String cloudAccountId, List<String> instanceType) {
        for (String type : instanceType) {
            syncInstance(cloudAccountId, ResourceTypeConstants.valueOf(type));
        }
    }

    @Override
    public void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType, List<JobActuator.ExecuteStepData> executeStepList) {
        // 加锁
        RLock lock = redissonClient.getLock(cloudAccountId + instanceType.name());
        // 如果指定时间拿不到锁就不执行同步
        if (!lock.tryLock()) {
            return;
        }
        Job<IJobStepService.Context> job = jobService.of("扫描" + instanceType.getMessage());
        JobActuator<IJobStepService.Context> contextJobActuator = jobService.ofJobActuator(job, JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB,
                cloudAccountId,
                instanceType,
                executeStepList);
        try {
            // 启动任务
            contextJobActuator.run();
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

    }

    @Override
    public void scanInstance(String complianceRuleId, List<JobActuator.ExecuteStepData> executeStepList) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>()
                        .eq(CloudAccount::getPlatform, complianceRule.getPlatform()))
                .forEach(cloudAccount -> CloudAccountSyncJob.SyncScanJob.run(() ->
                        syncInstance(cloudAccount.getId(),
                                ResourceTypeConstants.valueOf(complianceRule.getResourceType()),
                                executeStepList))
                );


    }


    @Override
    public void syncInstance(String cloudAccountId, List<String> ruleGroupId) {
        syncInstance(cloudAccountId, ruleGroupId, () -> cloudAccountId + ":RULE_GROUP");
    }


    public void syncInstance(String cloudAccountId, List<String> ruleGroupId, Supplier<String> getLockKey) {
        RLock lock = redissonClient.getLock(getLockKey.get());
        if (lock.isLocked()) {
            return;
        }
        CloudAccountSyncJob.SyncScanJob.run(() -> {
            if (lock.tryLock()) {
                try {
                    CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
                    // 获取同步资源
                    List<String> instanceTypes = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>()
                                    .in(ComplianceRule::getRuleGroupId, ruleGroupId)
                                    .eq(Objects.nonNull(cloudAccount), ComplianceRule::getPlatform, Objects.nonNull(cloudAccount) ? cloudAccount.getPlatform() : null))
                            .stream()
                            .map(ComplianceRule::getResourceType)
                            .distinct()
                            .toList();

                    // 一个云账号最多使用三个线程
                    List<List<String>> lists = split(instanceTypes, 3);
                    List<CompletableFuture<Void>> completableFutures = lists.stream()
                            .filter(CollectionUtils::isNotEmpty)
                            .map(group -> CloudAccountSyncJob.SyncScanJob.run(() -> syncInstanceByInstanceType(cloudAccountId, group)))
                            .toList();
                    completableFutures.forEach(CompletableFuture::join);
                } finally {
                    if (lock.isLocked()) {
                        lock.unlock();
                    }
                }
            }
        });

    }

    @Override
    public void syncInstance(String cloudAccountId) {
        List<ComplianceRuleGroup> list = complianceRuleGroupService.list();
        // 指定云账号同步的 与 指定云账号和规则组同步 不适用一个lock
        syncInstance(cloudAccountId, list.stream().map(ComplianceRuleGroup::getId).toList(), () -> cloudAccountId);
    }

    /**
     * 拆分为指定长度的二维数组
     *
     * @param sourceDataList 元数据
     * @param splitNum       指定的长度
     * @param <T>            数据泛型
     * @return 拆分后的二维数组
     */
    private <T> List<List<T>> split(List<T> sourceDataList, int splitNum) {
        List<List<T>> splitRes = new ArrayList<>();
        for (int i = 0; i < splitNum; i++) {
            splitRes.add(new ArrayList<>());
        }
        for (int index = 0; index < sourceDataList.size(); index++) {
            splitRes.get(index % splitNum).add(sourceDataList.get(index));
        }
        return splitRes;
    }


}
