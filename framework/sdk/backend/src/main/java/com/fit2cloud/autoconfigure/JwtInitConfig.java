package com.fit2cloud.autoconfigure;

import com.fit2cloud.common.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import java.util.concurrent.TimeUnit;

@Slf4j
public class JwtInitConfig {

    @Resource
    private RedissonClient redissonClient;

    @Value("${jwt.expire.minutes:30}")
    private int JWT_EXPIRE_MINUTES;

    @PostConstruct
    public void initJwtKey() {
        RLock lock = redissonClient.getLock(JwtTokenUtils.JWT_LOCK);
        try {
            //lock.lock();
            //10s后自动解锁
            lock.lock(10, TimeUnit.SECONDS);
            //redis 读取 jwt key
            RBucket<String> bucket = redissonClient.getBucket(JwtTokenUtils.JWT_BUCKET);
            String jwtKey = null;
            try {
                jwtKey = bucket.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isBlank(jwtKey)) {
                jwtKey = JwtTokenUtils.initJwtKey();

                //保存到 redis
                bucket.set(jwtKey);
            }

            JwtTokenUtils.setJwtKey(jwtKey); //key 保存在内存里
            JwtTokenUtils.setJwtExpireMinutes(JWT_EXPIRE_MINUTES); //保存jwt的超时时间

            log.info("load jwt key: {}", jwtKey);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }


}
