package com.fit2cloud.service;

import com.fit2cloud.common.utils.JwtTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class TokenPoolService {

    @Resource
    private RedissonClient redissonClient;


    private static final String JWT_TOKEN = "JWT_TOKEN:";

    private static String getKey(String userId, String jwtId) {
        return JWT_TOKEN + userId + ":" + jwtId;
    }

    public void saveJwt(String userId, String jwtId) {
        String key = getKey(userId, jwtId);
        try {
            int expireMinutes = JwtTokenUtils.JWT_EXPIRE_MINUTES;
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.setAsync(jwtId, expireMinutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeJwt(String userId, String jwtId) {
        String key = getKey(userId, jwtId);
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.deleteAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean JWTValid(String userId, String jwtId) {
        String key = getKey(userId, jwtId);
        try {
            RBucket<String> bucket = redissonClient.getBucket(key);
            String value = bucket.get();
            return StringUtils.isNotEmpty(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //todo 禁用用户时，需要删除该用户所有的有效 token id

}
