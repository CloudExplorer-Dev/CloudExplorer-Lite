package com.fit2cloud.service;

import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.dto.module.Menu;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private DiscoveryClient discoveryClient;

    private static final String MENUS_MAP = "MENUS_MAP";

    public void init(String module, List<Menu> menus) {
        RMap<String, List<Menu>> map = redissonClient.getMap(MENUS_MAP);
        RReadWriteLock lock = map.getReadWriteLock(MENUS_MAP);
        try {
            lock.writeLock().lock(10, TimeUnit.SECONDS);
            map.put(module, menus);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<String, List<Menu>> getAvailableMenus() {
        List<String> ids = discoveryClient.getServices();
        ids.add(ServerInfo.module);
        //排除网关
        return readMenusFromRedis(
                ids.stream().filter(id -> !StringUtils.equalsIgnoreCase(id, "gateway")).collect(Collectors.toList())
        );
    }

    public Map<String, List<Menu>> readMenusFromRedis(List<String> modules) {

        if (CollectionUtils.isEmpty(modules)) {
            return new HashMap<>();
        }

        RMap<String, List<Menu>> map = redissonClient.getMap(MENUS_MAP);
        RReadWriteLock lock = map.getReadWriteLock(MENUS_MAP);
        try {
            lock.readLock().lock(10, TimeUnit.SECONDS);
            return map.getAll(new HashSet<>(modules));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }

        return new HashMap<>();
    }

}
