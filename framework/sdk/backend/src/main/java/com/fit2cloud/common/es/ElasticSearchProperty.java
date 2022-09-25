package com.fit2cloud.common.es;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author jianneng
 * @date 2022/9/22 11:39
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperty {

    /**
     * 集群地址，如果有多个用“,”隔开
     */
    private String address;

    /**
     * 连接超时时间
     */
    private int connectTimeout;

    /**
     * Socket 连接超时时间
     */
    private int socketTimeout;

    /**
     * 获取连接的超时时间
     */
    private int connectionRequestTimeout;

    /**
     * 连接ES的用户名
     */
    private String username;

    /**
     * 密码
     */
    private String passwd;
}
