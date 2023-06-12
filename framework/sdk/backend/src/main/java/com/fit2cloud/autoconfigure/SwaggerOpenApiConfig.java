package com.fit2cloud.autoconfigure;

import com.fit2cloud.common.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.parameters.Parameter;

@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "${ce.revision}"
        ),
        servers = @Server(url = "/" + "${spring.application.name}")
)
public class SwaggerOpenApiConfig {

    @Value("${ce.revision}")
    private String version;

//    @Bean
//    public OpenAPI springShopOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("CloudExplorer Lite API")
//                        .description("CloudExplorer Lite API")
//                        .version(version)
//                        .license(new License().name("CE Lite").url("https://www.fit2cloud.com/")));
////                .externalDocs(new ExternalDocumentation()
////                        .description("SpringShop Wiki Documentation")
////                        .url("https://springshop.wiki.github.org/docs"));
//    }

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
