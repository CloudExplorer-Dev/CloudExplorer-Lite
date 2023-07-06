package com.fit2cloud.controller;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.utils.ServiceUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/12  10:56 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/base")
@Tag(name = "云平台资源获取", description = "云平台资源获取")
public class ProviderResourceController {
    @Resource
    private RestTemplate restTemplate;

    @PostMapping("provider/{clazz}/{method}")
    private ResultHolder<Object> getProviderResourceByMethod(@PathVariable("clazz") String clazz,
                                                             @PathVariable("method") String method,
                                                             @RequestParam(required = false) String execModule,
                                                             @RequestParam(required = false) String pluginId,
                                                             @RequestBody Map<String, Object> params) {
        if (StringUtils.isNotEmpty(execModule)) {
            String httpUrl = ServiceUtil.getHttpUrl(execModule, "/api/base/provider/" + clazz + '/' + method);
            return restTemplate.exchange(httpUrl, HttpMethod.POST, new HttpEntity<>(params), new ParameterizedTypeReference<ResultHolder<Object>>() {
            }).getBody();
        }
        return ResultHolder.success(FormUtil.exec(clazz, false, method, pluginId, params));
    }

    @PostMapping("service/{clazz}/{method}")
    private ResultHolder<Object> getProviderResourceByServiceMethod(@PathVariable("clazz") String clazz,
                                                                    @PathVariable("method") String method,
                                                                    @RequestParam(required = false) String execModule,
                                                                    @RequestParam(required = false) String pluginId,
                                                                    @RequestBody Map<String, Object> params) {
        if (StringUtils.isNotEmpty(execModule)) {
            String httpUrl = ServiceUtil.getHttpUrl(execModule, "/api/base/service/" + clazz + '/' + method);
            return restTemplate.exchange(httpUrl, HttpMethod.POST, new HttpEntity<>(params), new ParameterizedTypeReference<ResultHolder<Object>>() {
            }).getBody();
        }
        return ResultHolder.success(FormUtil.exec(clazz, true, method, pluginId, params));
    }
}
