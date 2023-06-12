package com.fit2cloud.gateway.config;

import com.fit2cloud.common.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "${ce.revision}"
        ),
        servers = @Server(url = "/")
)
public class GatewaySwaggerConfig {

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            if (!"login".equals(handlerMethod.getMethod().getName())) {
                return operation
                        .addParametersItem(new Parameter().in("header").required(true).name(JwtTokenUtils.TOKEN_NAME));
            }
            return operation;
        };
    }
}
