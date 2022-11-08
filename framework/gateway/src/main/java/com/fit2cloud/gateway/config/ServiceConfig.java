package com.fit2cloud.gateway.config;

import com.fit2cloud.service.PerfMonitorService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
        basePackages = {"com.fit2cloud.service", "com.fit2cloud.base.service", "com.fit2cloud.common.scheduler"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {PerfMonitorService.class})
        }
)
@Configuration
public class ServiceConfig {

}
