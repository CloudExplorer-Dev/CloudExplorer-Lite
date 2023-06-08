package com.fit2cloud.common.log.cache;

import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 云主机ID名称缓存
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:29 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class BaseCloudServerCache {
    /**
     * 云账号id -> name的缓存 使用软引用,在云主机内存不足的情况下会被清除
     */
    private static SoftReference<Map<String, String>> cloudServerCache = new SoftReference<>(new HashMap<>());

    /**
     * 设置缓存
     *
     * @param cloudServerCacheData 云主机数据 id -> name
     */
    private synchronized static void setCache(Map<String, String> cloudServerCacheData) {
        cloudServerCache = new SoftReference<>(cloudServerCacheData);
    }

    /**
     * 更新缓存
     */
    public static void updateCache() {
        BaseVmCloudServerMapper cloudServerMapper = SpringUtil.getBean(BaseVmCloudServerMapper.class);
        List<VmCloudServer> cloudServers = cloudServerMapper.selectList(null);
        Map<String, String> cloudServerCacheData = cloudServers.stream().map(w -> {
            return new DefaultKeyValue<>(w.getId(), w.getInstanceName());
        }).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));
        setCache(cloudServerCacheData);
    }

    /**
     * 获取缓存根据组织id
     *
     * @param cloudServerId 云账号id
     * @return 组织名称
     */
    public synchronized static String getCache(String cloudServerId) {
        Map<String, String> cloudServerMap = cloudServerCache.get();
        return MapUtils.isEmpty(cloudServerMap) ? null : cloudServerMap.get(cloudServerId);
    }

    public synchronized static String getCacheOrUpdate(String cloudServerId) {
        String cache = getCache(cloudServerId);
        if (StringUtils.isEmpty(cache)) {
            BaseVmCloudServerMapper cloudServerMapper = SpringUtil.getBean(BaseVmCloudServerMapper.class);
            VmCloudServer cloudServer = cloudServerMapper.selectById(cloudServerId);
            if (Objects.nonNull(cloudServer)) {
                updateCache();
                return cloudServer.getInstanceName();
            }
        }
        return cache;

    }
}
