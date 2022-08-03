package com.fit2cloud.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModuleRouterConfig {

    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder builder) {

        builder.routes();

        return builder.routes().build();
    }

}
