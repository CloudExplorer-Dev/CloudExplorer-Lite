package com.fit2cloud.redis;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * Author: LiuDi
 * Date: 2022/10/1 10:24 AM
 */
@Service
public class RedisService {

    @Resource
    private RedissonClient redissonClient;


    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     */
    public <T> void publish(String channelKey, T msg) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.publish(msg);
    }


    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    public <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.publish(msg);
        consumer.accept(msg);
    }


    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    public <T> void subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
    }

}
