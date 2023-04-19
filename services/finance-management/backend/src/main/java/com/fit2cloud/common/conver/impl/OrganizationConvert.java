package com.fit2cloud.common.conver.impl;

import com.fit2cloud.common.cache.OrganizationCache;
import com.fit2cloud.common.conver.Convert;
import org.apache.commons.lang3.StringUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  3:28 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class OrganizationConvert implements Convert {
    @Override
    public String conver(String orgId) {
        String cache = OrganizationCache.getCache(orgId);
        if (StringUtils.isEmpty(cache)) {
            return orgId;
        }
        return cache;
    }
}
