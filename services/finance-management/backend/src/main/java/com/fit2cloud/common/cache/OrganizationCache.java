package com.fit2cloud.common.cache;

import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.common.utils.SpringUtil;
import jodd.util.StringUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  2:53 PM}
 * {@code @Version 1.0}
 * {@code @注释: 组织缓存}
 */
public class OrganizationCache {
    /**
     * 组织id -> name的缓存 使用软引用,在云主机内存不足的情况下会被清除
     */
    private static SoftReference<Map<String, String>> organizationCache = new SoftReference<>(new HashMap<>());

    /**
     * 设置缓存
     *
     * @param organizationCacheData 组织数据 id -> name
     */
    private synchronized static void setCache(Map<String, String> organizationCacheData) {
        organizationCache = new SoftReference<>(organizationCacheData);
    }

    /**
     * 更新缓存
     */
    public static void updateCache() {
        BaseOrganizationMapper organizationMapper = SpringUtil.getBean(BaseOrganizationMapper.class);
        List<Organization> organizations = organizationMapper.selectList(null);
        Map<String, String> organizationCacheData = organizations.stream().map(w -> {
            return new DefaultKeyValue<>(w.getId(), w.getName());
        }).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));
        setCache(organizationCacheData);
    }

    /**
     * 获取缓存根据组织id
     *
     * @param orgId 组织id
     * @return 组织名称
     */
    public synchronized static String getCache(String orgId) {
        Map<String, String> orgMap = organizationCache.get();
        return MapUtils.isEmpty(orgMap) ? null : orgMap.get(orgId);
    }

    /**
     * 获取缓存组织并且更新
     *
     * @param orgId 组织id
     * @return 组织名称
     */
    public synchronized static String getCacheOrUpdate(String orgId) {
        String cache = getCache(orgId);
        if (StringUtil.isEmpty(cache)) {
            BaseOrganizationMapper organizationMapper = SpringUtil.getBean(BaseOrganizationMapper.class);
            Organization organization = organizationMapper.selectById(orgId);
            if (Objects.nonNull(organization)) {
                updateCache();
                return organization.getName();
            }
        }
        return cache;
    }

}
