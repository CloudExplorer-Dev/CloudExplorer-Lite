package com.fit2cloud.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;

/**
 * 网关不做security认证
 * 由于包引用的问题，必须引入security，所以这里配置是全部放行
 */
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    @Bean
    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
        http
                //关闭csrf
                .csrf().disable()
                //网关全部允许
                .authorizeExchange((exchanges) -> exchanges
                        .anyExchange().permitAll()
                )
                //禁用session
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .headers().frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)
                .and()
                .httpBasic().disable()
                .formLogin().disable();

        return http.build();
    }


}
