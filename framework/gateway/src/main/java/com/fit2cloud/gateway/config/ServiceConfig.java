package com.fit2cloud.gateway.config;

import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.service.PerfMonitorService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
        basePackages = {"com.fit2cloud.service", "com.fit2cloud.base.service"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {PerfMonitorService.class, IBaseCloudAccountService.class})
        }
)
@Configuration
public class ServiceConfig {

}
