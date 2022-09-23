package com.fit2cloud.gateway.config;

import com.fit2cloud.gateway.utils.WriteMessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Value("classpath:/public/index.html")
    private Resource html;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //网关转发过滤

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getPath().value();

        if (HttpMethod.GET.matches(request.getMethodValue())) {
            String[] vs = StringUtils.split(path, "/");
            String var = null;
            if (vs.length > 1) {
                var = vs[1];
            }

            //api和assets资源不需要判断
            if (var == null || !(StringUtils.equals("api", var) || StringUtils.equals("assets", var))) {
                //获取子模块的静态页面如果没有这个头说明是直接访问的子模块，需要返回基座，让基座转发
                String microApp = request.getHeaders().getFirst("ce-micro-app");
                if (StringUtils.isBlank(microApp)) {
                    //第一次访问页面需要跳基座
                    try {
                        return WriteMessageUtil.write(response, HttpStatus.OK, html);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
