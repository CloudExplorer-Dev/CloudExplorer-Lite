package com.fit2cloud.gateway.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {"com.fit2cloud.service", "com.fit2cloud.base.service", "com.fit2cloud.common.scheduler"})
@Configuration
public class ServiceConfig {

}
