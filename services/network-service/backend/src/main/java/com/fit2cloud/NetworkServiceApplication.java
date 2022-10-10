package com.fit2cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication()
@EnableDiscoveryClient
@PropertySource(value = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
}, encoding = "UTF-8", ignoreResourceNotFound = true)
@ServletComponentScan
public class NetworkServiceApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT")));
        SpringApplication.run(NetworkServiceApplication.class, args);
    }

}
