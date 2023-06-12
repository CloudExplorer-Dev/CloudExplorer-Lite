package com.fit2cloud.gateway;

import com.fit2cloud.autoconfigure.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {
        SecurityConfig.class,
        ServletWebServerFactoryAutoConfiguration.class,
        JobSettingConfig.class,
        LocaleConfig.class,
        SwaggerOpenApiConfig.class,
        RedisConfig.class,
        QuartzConfig.class,
        ChargingConfig.class
})
@EnableDiscoveryClient
@PropertySource(value = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
