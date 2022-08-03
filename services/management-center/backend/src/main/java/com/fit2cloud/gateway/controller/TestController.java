package com.fit2cloud.gateway.controller;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Resource
    private DiscoveryClient discoveryClient;


    @GetMapping("test")
    public List<String> test() {

        return discoveryClient.getServices();

        //return "login";
    }

}
