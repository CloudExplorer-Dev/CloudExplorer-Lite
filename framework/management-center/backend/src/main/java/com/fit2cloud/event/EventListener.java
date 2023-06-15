package com.fit2cloud.event;

import com.fit2cloud.base.service.IBaseResourceInstanceService;
import com.fit2cloud.base.service.IBaseResourceInstanceStateService;
import com.fit2cloud.common.event.annotaion.Event;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/12  9:57}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Event(value = "")
@Component
public class EventListener {

    @Resource
    private IBaseResourceInstanceService resourceInstanceService;
    @Resource
    private IBaseResourceInstanceStateService resourceInstanceStateService;

    @Event("DELETE::CLOUD_ACCOUNT")
    public void deleteCloudAccount(String cloudAccountId) {
        resourceInstanceService.cleanDeletedCloudAccount();
        resourceInstanceStateService.cleanDeletedCloudAccount();
    }

    @Event("DELETE_BATCH::CLOUD_ACCOUNT")
    public void deleteBatchCloudAccount(List<String> cloudAccountIdList) {
        resourceInstanceService.cleanDeletedCloudAccount();
        resourceInstanceStateService.cleanDeletedCloudAccount();
    }
}
