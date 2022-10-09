package com.fit2cloud.job;

/**
 * Author: LiuDi
 * Date: 2022/10/1 10:53 AM
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.RedisConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.redis.RedisService;
import com.fit2cloud.service.IVmCloudDiskService;
import com.fit2cloud.service.IVmCloudImageService;
import com.fit2cloud.service.IVmCloudServerService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Topic 订阅
 */
@Component
public class CloudAccountSubscribe {

    @Resource
    private RedisService redisService;

    @Resource
    IVmCloudImageService cloudImageService;

    @Resource
    IVmCloudDiskService cloudDiskService;

    @Resource
    IVmCloudServerService cloudServerService;

    @Resource
    IBaseCloudAccountService cloudAccountService;

    /**
     * 启动之后订阅 topic
     */
    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            redisService.subscribe(RedisConstants.Topic.CLOUD_ACCOUNT_DELETE, String.class, msg -> {
                this.cloudAccountDelete(msg);
            });
        } catch (Exception e) {
            LogUtil.error("failed to subscribe topic: " + RedisConstants.Topic.CLOUD_ACCOUNT_DELETE, e);
            throw new RuntimeException(e);
        }
    }

    private void cloudAccountDelete(String accountId) {
        // 删除本模块云账号同步下来的资源
        QueryWrapper wrapper = Wrappers.query();
        wrapper.eq("account_id", accountId);
        cloudImageService.remove(wrapper);
        cloudDiskService.remove(wrapper);
        cloudServerService.remove(wrapper);

        // 删除本模块云账号相关的定时任务
        cloudAccountService.deleteJobByCloudAccountId(accountId);
    }
}
