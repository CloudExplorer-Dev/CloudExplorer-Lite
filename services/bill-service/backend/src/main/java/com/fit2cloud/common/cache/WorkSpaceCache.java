package com.fit2cloud.common.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.mapper.BaseWorkspaceMapper;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  2:52 PM}
 * {@code @Version 1.0}
 * {@code @注释: 工作空间缓存}
 */
public class WorkSpaceCache {
    /**
     * 工作空间id -> name的缓存 使用软引用,在虚拟机内存不足的情况下会被清除
     */
    private static SoftReference<Map<String, String>> workSpaceCache = new SoftReference<>(new HashMap<>());

    /**
     * 设置缓存
     * <p>
     * 加入synchronized  类锁 保证在写入数据的时候,不被读取
     *
     * @param workSpaceCacheData 工作空间数据 id -> name
     */
    private synchronized static void setCache(Map<String, String> workSpaceCacheData) {
        workSpaceCache = new SoftReference<>(workSpaceCacheData);
    }

    /**
     * 更新缓存
     */
    public static void updateCache() {
        BaseWorkspaceMapper workspaceMapper = SpringUtil.getBean(BaseWorkspaceMapper.class);
        List<Workspace> workspaces = workspaceMapper.selectList(null);
        Map<String, String> workSpaceCacheData = workspaces.stream().map(w -> {
            return new DefaultKeyValue<>(w.getId(), w.getName());
        }).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));
        setCache(workSpaceCacheData);
    }

    /**
     * 获取缓存根据组织id
     * <p>
     * 加入synchronized  类锁 保证在写入数据的时候,不被读取
     *
     * @param workspaceId 工作空间id
     * @return 组织名称
     */
    public synchronized static String getCache(String workspaceId) {
        Map<String, String> workspaceMap = workSpaceCache.get();
        return MapUtils.isEmpty(workspaceMap) ? null : workspaceMap.get(workspaceId);
    }
}
