package com.fit2cloud.controller.error;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;


@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions options) {
        Throwable error = super.getError(request);

        Map<String, Object> map = super.getErrorAttributes(request, options);

        map.putIfAbsent("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (error instanceof BadCredentialsException) {
            map.put("status", HttpStatus.UNAUTHORIZED.value());
            map.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        //自定义全局错误message内容
        map.put("message", error == null ? map.get("message") : error.getMessage());

        return map;
    }

}
