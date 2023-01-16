package com.fit2cloud;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fit2cloud.common.utils.JsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SecurityComplianceApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT")));
        JsonUtil.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonUtil.mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        SpringApplication.run(SecurityComplianceApplication.class, args);

    }

}
