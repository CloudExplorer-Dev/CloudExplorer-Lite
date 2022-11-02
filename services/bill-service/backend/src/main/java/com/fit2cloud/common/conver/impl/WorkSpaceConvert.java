package com.fit2cloud.common.conver.impl;

import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.mapper.BaseWorkspaceMapper;
import com.fit2cloud.common.cache.WorkSpaceCache;
import com.fit2cloud.common.conver.Convert;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

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
            //todo 如果缓存中没有数据,判断当前工作空间是否存在在库里
            BaseWorkspaceMapper workspaceMapper = SpringUtil.getBean(BaseWorkspaceMapper.class);
            Workspace workspace = workspaceMapper.selectById(workSpaceId);
            if (Objects.nonNull(workspace)) {
                //todo 需要更新缓存数据
                WorkSpaceCache.updateCache();
                return workspace.getName();
            }
            return workSpaceId;
        }
        return cache;
    }
}
