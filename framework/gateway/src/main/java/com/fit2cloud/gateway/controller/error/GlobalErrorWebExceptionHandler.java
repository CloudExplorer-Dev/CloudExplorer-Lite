package com.fit2cloud.gateway.controller.error;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends
        AbstractErrorWebExceptionHandler {

    @Value("classpath:/public/index.html")
    private Resource html;

    public GlobalErrorWebExceptionHandler(GlobalErrorAttributes gea, ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(gea, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(
            ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(
            ServerRequest request) {

        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());

        //针对非api接口，要把url路径交给前端去路由 (获取页面都是GET方法)
        if (HttpMethod.GET.matches(request.methodName()) &&
                (HttpStatus.NOT_FOUND.equals(HttpStatus.valueOf((Integer) errorPropertiesMap.get("status")))
                        && !StringUtils.startsWith((String) errorPropertiesMap.get("path"), "/api/")
                        && !StringUtils.equals((String) errorPropertiesMap.get("path"), "/api")
                        || StringUtils.equals((String) errorPropertiesMap.get("path"), "/login"))) {
            return ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(html);
        }

        //将错误都以json格式返回
        return ServerResponse.status(HttpStatus.valueOf((Integer) errorPropertiesMap.get("status")))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
