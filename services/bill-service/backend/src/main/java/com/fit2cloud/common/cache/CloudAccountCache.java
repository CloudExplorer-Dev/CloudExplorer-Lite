package com.fit2cloud.common.cache;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.base.mapper.BaseOrganizationMapper;
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
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:29 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CloudAccountCache {
    /**
     * 云账号id -> name的缓存 使用软引用,在虚拟机内存不足的情况下会被清除
     */
    private static SoftReference<Map<String, String>> cloudAccountCache = new SoftReference<>(new HashMap<>());

    /**
     * 设置缓存
     *
     * @param cloudAccountCacheData 云账号数据 id -> name
     */
    private synchronized static void setCache(Map<String, String> cloudAccountCacheData) {
        cloudAccountCache = new SoftReference<>(cloudAccountCacheData);
    }

    /**
     * 更新缓存
     */
    public static void updateCache() {
        BaseCloudAccountMapper cloudAccountMapper = SpringUtil.getBean(BaseCloudAccountMapper.class);
        List<CloudAccount> cloudAccounts = cloudAccountMapper.selectList(null);
        Map<String, String> cloudAccountCacheData = cloudAccounts.stream().map(w -> {
            return new DefaultKeyValue<>(w.getId(), w.getName());
        }).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));
        setCache(cloudAccountCacheData);
    }

    /**
     * 获取缓存根据组织id
     *
     * @param cloudAccountId 云账号id
     * @return 组织名称
     */
    public synchronized static String getCache(String cloudAccountId) {
        Map<String, String> cloudAccountMap = cloudAccountCache.get();
        return MapUtils.isEmpty(cloudAccountMap) ? null : cloudAccountMap.get(cloudAccountId);
    }

    public synchronized static String getCacheOrUpdate(String cloudAccountId) {
        String cache = getCache(cloudAccountId);
        if (StringUtils.isEmpty(cache)) {
            BaseCloudAccountMapper cloudAccountMapper = SpringUtil.getBean(BaseCloudAccountMapper.class);
            CloudAccount cloudAccount = cloudAccountMapper.selectById(cloudAccountId);
            if (Objects.nonNull(cloudAccount)) {
                updateCache();
                return cloudAccount.getName();
            }
            return cloudAccountId;
        } else {
            return cache;
        }

    }
}
