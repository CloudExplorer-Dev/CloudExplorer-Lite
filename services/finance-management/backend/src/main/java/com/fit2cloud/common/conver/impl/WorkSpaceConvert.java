package com.fit2cloud.common.conver.impl;

import com.fit2cloud.common.cache.WorkSpaceCache;
import com.fit2cloud.common.conver.Convert;
import org.apache.commons.lang3.StringUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  3:29 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class WorkSpaceConvert implements Convert {
    @Override
    public String conver(String workSpaceId) {
        String cache = WorkSpaceCache.getCache(workSpaceId);
        if (StringUtils.isEmpty(cache)) {
            return workSpaceId;
        }
        return cache;
    }
}
