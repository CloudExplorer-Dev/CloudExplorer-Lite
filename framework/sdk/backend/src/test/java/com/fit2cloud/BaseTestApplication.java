package com.fit2cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.PropertySource;


@PropertySource(value = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class BaseTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseTestApplication.class, args);
    }

}
