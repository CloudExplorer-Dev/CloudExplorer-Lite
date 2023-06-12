package com.fit2cloud.event;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.cache.CloudAccountCache;
import com.fit2cloud.common.cache.OrganizationCache;
import com.fit2cloud.common.cache.WorkSpaceCache;
import com.fit2cloud.common.event.annotaion.Event;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.SyncService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

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
    private SyncService syncService;

    @Resource
    private IBaseCloudAccountService baseCloudAccountService;

    @Event("CREATE::CLOUD_ACCOUNT")
    public void createCloudAccount(String cloudAccountId) {
        // 创建当前云账号的定时任务
        baseCloudAccountService.initCloudAccountJob(cloudAccountId);
        CloudAccount cloudAccount = baseCloudAccountService.getById(cloudAccountId);
        if (ICloudProvider.support(cloudAccount.getPlatform())) {
            // 创建云账号需要同步12个月数据
            syncService.syncBill(cloudAccountId, MonthUtil.getMonths(12));
        }
    }

    @Event("UPDATE::CLOUD_ACCOUNT")
    public void updateCloudAccount(String cloudAccountId) {
        CloudAccountCache.updateCache();
    }


    @Event("DELETE::CLOUD_ACCOUNT")
    public void deleteCloudAccount(String cloudAccountId) {
        SyncService service = SpringUtil.getBean(SyncService.class);
        service.deleteDataSource(cloudAccountId);
    }


    @Event("DELETE_BATCH::CLOUD_ACCOUNT")
    public void deleteBatchCloudAccount(List<String> cloudAccountIdList) {
        SyncService service = SpringUtil.getBean(SyncService.class);
        for (String cloudAccountId : cloudAccountIdList) {
            service.deleteDataSource(cloudAccountId);
        }
    }

    @Event("CREATE::WORKSPACE")
    public void createWorkspace(String workspaceId) {
        WorkSpaceCache.updateCache();
    }

    @Event("CREATE_BATCH::WORKSPACE")
    public void createWorkspace(List<String> workspaceIdList) {
        WorkSpaceCache.updateCache();
    }

    @Event("UPDATE::WORKSPACE")
    public void updateWorkspace(String workspaceId) {
        WorkSpaceCache.updateCache();
    }

    @Event("DELETE::WORKSPACE")
    public void deleteWorkspace(String workspaceId) {
        WorkSpaceCache.updateCache();
    }

    @Event("DELETE_BATCH::WORKSPACE")
    public void deleteBatchWorkspace(List<String> workspaceIdList) {
        WorkSpaceCache.updateCache();
    }

    @Event("CREATE::ORGANIZATION")
    public void createOrganization(String organizationId) {
        OrganizationCache.updateCache();
    }

    @Event("CREATE_BATCH::ORGANIZATION")
    public void createBatchOrganization(List<String> organizationIdList) {
        OrganizationCache.updateCache();
    }

    @Event("UPDATE::ORGANIZATION")
    public void updateOrganization(String organizationId) {
        OrganizationCache.updateCache();
    }

    @Event("DELETE::ORGANIZATION")
    public void deleteOrganization(String organizationId) {
        OrganizationCache.updateCache();
    }

    @Event("DELETE_BATCH::ORGANIZATION")
    public void deleteBatchOrganization(List<String> organizationIdList) {
        OrganizationCache.updateCache();
    }
}
