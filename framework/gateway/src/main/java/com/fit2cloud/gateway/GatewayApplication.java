package com.fit2cloud.gateway;

//import com.fit2cloud.autoconfigure.*;
import com.fit2cloud.autoconfigure.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {
        /*ShiroConfig.class,
        CommonsDatabaseConfig.class,
        RsaConfig.class,
        SessionConfig.class,
        WebSocketConfig.class,*/
        SecurityConfig.class,
        ServletWebServerFactoryAutoConfiguration.class
})
@EnableDiscoveryClient
@PropertySource(value = {
        "classpath:commons.properties",
        "${ce.config.file}"
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
