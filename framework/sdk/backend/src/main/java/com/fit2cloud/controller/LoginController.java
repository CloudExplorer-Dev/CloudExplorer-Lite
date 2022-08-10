package com.fit2cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class LoginController {
    @Controller
    @RequestMapping
    public static class IndexController {

        @GetMapping(value = "/")
        public String index() {
            return "index.html";
        }

        @GetMapping(value = "/login")
        public String login() {
            return "index.html";
        }

    }
}
