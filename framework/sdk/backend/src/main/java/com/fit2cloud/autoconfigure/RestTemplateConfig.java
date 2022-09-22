package com.fit2cloud.autoconfigure;

import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.security.filter.JwtTokenAuthFilter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/7/11  11:38 AM
 * @Version 1.0
 * @注释:
 */
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(60000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getInterceptors().add(new TokenRequestInterceptor());
        return restTemplate;
    }

    public static class TokenRequestInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            List<MediaType> mediaTypes = new ArrayList<>();
            for (MediaType mediaType : request.getHeaders().getAccept()) {
                if (!mediaType.equals(MediaType.APPLICATION_XML) && !mediaType.equals(MediaType.TEXT_XML)) {
                    mediaTypes.add(mediaType);
                }
            }
            request.getHeaders().setAccept(mediaTypes);

            if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getCredentials() != null) {
                UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                request.getHeaders().add(JwtTokenUtils.TOKEN_NAME, SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
                request.getHeaders().add(RoleConstants.ROLE_TOKEN, userDto.getCurrentRole().name());
                request.getHeaders().add(JwtTokenAuthFilter.CE_SOURCE_TOKEN,userDto.getCurrentSource());
                request.getHeaders().add("content-type", "application/json;charset=utf-8");
            }
            return execution.execute(request, body);
        }

    }
}
