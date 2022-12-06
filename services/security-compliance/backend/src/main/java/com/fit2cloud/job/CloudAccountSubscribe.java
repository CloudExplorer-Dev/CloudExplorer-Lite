package com.fit2cloud.job;

/**
 * Author: LiuDi
 * Date: 2022/10/1 10:53 AM
 */

import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.redis.RedisService;
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
    IBaseCloudAccountService cloudAccountService;


}
