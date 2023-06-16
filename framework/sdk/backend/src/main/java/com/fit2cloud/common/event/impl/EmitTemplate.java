package com.fit2cloud.common.event.impl;

import com.fit2cloud.common.event.Emit;
import com.fit2cloud.common.utils.ServiceUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/9  9:47}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class EmitTemplate implements Emit {
    @Resource
    private RestTemplate restTemplate;

    @Resource(name = "workThreadPool")
    private ThreadPoolExecutor workThreadPool;

    @Override
    public void emit(String event, @NotNull String[] excludes, Object... args) {
        Set<String> services = ServiceUtil.getServices(excludes);
        for (String service : services) {
            CompletableFuture.runAsync(() -> {
                String httpUrl = ServiceUtil.getHttpUrl(service, "/api/event/on/" + event);
                restTemplate.exchange(httpUrl, HttpMethod.POST, new HttpEntity<>(args), new ParameterizedTypeReference<ResultHolder<String>>() {
                });
            }, new DelegatingSecurityContextExecutor(workThreadPool));
        }
    }

    @Override
    public void emit(String event, Object... args) {
        emit(event, new String[]{"gateway"}, args);
    }
}
