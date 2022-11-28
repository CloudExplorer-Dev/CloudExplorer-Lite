package com.fit2cloud.common.conver.impl;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.common.cache.CloudAccountCache;
import com.fit2cloud.common.conver.Convert;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:26 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CloudAccountConvert implements Convert {
    @Override
    public String conver(String cloudAccountId) {
        String cache = CloudAccountCache.getCache(cloudAccountId);
        if (StringUtils.isEmpty(cache)) {
            BaseCloudAccountMapper cloudAccountMapper = SpringUtil.getBean(BaseCloudAccountMapper.class);
            CloudAccount cloudAccount = cloudAccountMapper.selectById(cloudAccountId);
            if (Objects.nonNull(cloudAccount)) {
                CloudAccountCache.updateCache();
                return cloudAccount.getName();
            }
            return cloudAccountId;
        }
        return cache;
    }
}
