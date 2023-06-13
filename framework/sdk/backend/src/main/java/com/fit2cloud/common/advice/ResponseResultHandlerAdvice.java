package com.fit2cloud.common.advice;

import com.fit2cloud.common.advice.annnotaion.TokenRenewal;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.service.TokenPoolService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/9  15:27}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@ControllerAdvice
@Slf4j
public class ResponseResultHandlerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        Annotation[] methodAnnotations = returnType.getMethodAnnotations();
        Optional<Annotation> first = Arrays.stream(methodAnnotations).filter(a -> a.annotationType().equals(TokenRenewal.class)).findFirst();
        if (first.isPresent()) {
            TokenRenewal tokenRenewal = (TokenRenewal) first.get();
            return tokenRenewal.support();
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType, @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NotNull ServerHttpRequest request, ServerHttpResponse response) {
        HttpHeaders headers = request.getHeaders();
        if (headers.containsKey(JwtTokenUtils.TOKEN_NAME)) {
            String token = headers.getFirst(JwtTokenUtils.TOKEN_NAME);
            Map<String, String> map = JwtTokenUtils.renewalToken(token);
            String newToken = map.get("jwt");
            response.getHeaders().add(JwtTokenUtils.TOKEN_NAME, newToken);
            try {
                SpringUtil.getBean(TokenPoolService.class).saveJwt(map.get("userId"), map.get("jwtId"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return body;
    }
}
