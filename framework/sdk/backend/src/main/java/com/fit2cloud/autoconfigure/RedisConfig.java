package com.fit2cloud.autoconfigure;

import com.fit2cloud.dto.UserDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
public class RedisConfig {
    @Resource
    private ServerInfo serverInfo;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer()))
                .disableCachingNullValues()
                .prefixCacheNameWith(serverInfo.getModule() + ":")
                .entryTtl(Duration.ofHours(1));
        //专门指定某些缓存空间的配置，如果过期时间【主要这里的key为缓存空间名称】
        Map<String, RedisCacheConfiguration> map = new HashMap<>();
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(map)
                .build();
    }


    @Bean("authKeyGenerator")
    public KeyGenerator authKeyGenerator() {
        return (target, method, params) -> {
            SecurityContext context = SecurityContextHolder.getContext();
            StringBuilder sb = new StringBuilder();
            if (context.getAuthentication().isAuthenticated()) {
                UserDto userDto = (UserDto) context.getAuthentication().getPrincipal();
                // 添加当前角色
                sb.append(userDto.getCurrentRole()).append(":");
                // 添加用户id
                sb.append(userDto.getUsername()).append(":");
            }
            // 添加类名
            sb.append(target.getClass().getSimpleName()).append(":");
            // 添加方法名
            sb.append(method.getName()).append(":");
            //调用SimpleKey的key生成器
            Object key = SimpleKeyGenerator.generateKey(params);
            return sb.append(key);
        };
    }

    @Bean("notAuthKeyGenerator")
    public KeyGenerator notAuthKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            // 添加类名
            sb.append(target.getClass().getSimpleName()).append(":");
            // 添加方法名
            sb.append(method.getName()).append(":");
            //调用SimpleKey的key生成器
            Object key = SimpleKeyGenerator.generateKey(params);
            return sb.append(key);
        };
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = serializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        //hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


    public GenericJackson2JsonRedisSerializer serializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

}
