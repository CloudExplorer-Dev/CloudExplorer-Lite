package com.fit2cloud.event;

import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.event.annotaion.Event;
import com.fit2cloud.service.IJobStepService;
import com.fit2cloud.service.ISyncService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  17:54}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Event(value = "")
@Component
public class EventListener {
    @Resource
    private ISyncService syncService;

    @Resource
    private IBaseCloudAccountService baseCloudAccountService;
    @Resource
    private IJobStepService jobStepService;

    @Event("CREATE::CLOUD_ACCOUNT")
    public void createCloudAccount(String cloudAccountId) {
        // 创建当前云账号的定时任务
        baseCloudAccountService.initCloudAccountJob(cloudAccountId);
        // 创建云账号需要同步所有实例
        syncService.syncInstance(cloudAccountId);
    }

    @Event("DELETE::CLOUD_ACCOUNT")
    public void deleteCloudAccount() {
        jobStepService.deleteNotFountCloudAccountData(null, null);

    }


    @Event("DELETE_BATCH::CLOUD_ACCOUNT")
    public void deleteBatchCloudAccount() {
        jobStepService.deleteNotFountCloudAccountData(null, null);
    }
}
