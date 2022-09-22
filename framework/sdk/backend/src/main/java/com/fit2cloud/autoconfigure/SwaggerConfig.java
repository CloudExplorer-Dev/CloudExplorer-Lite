package com.fit2cloud.autoconfigure;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author:张少虎
 * @Date: 2022/6/26  4:39 PM
 * @Version 1.0
 * @注释: swagger配置类
 */
@EnableSwagger2 //swagger注解
public class SwaggerConfig {


    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.fit2cloud"))
                .build();

    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("云管4.0API")
                .description("云管4.0操作api")
                .version("1.0")
                .contact(new Contact("java", "https://www.fit2cloud.com/", ""))
                .build();
    }
}
