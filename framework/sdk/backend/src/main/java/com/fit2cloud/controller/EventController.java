package com.fit2cloud.controller;

import com.fit2cloud.common.event.impl.EventListenerTemplate;
import com.fit2cloud.controller.handler.ResultHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  17:34}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/event")
public class EventController {
    @Resource
    private EventListenerTemplate eventListenerTemplate;

    @PostMapping("/on/{event}")
    @ApiIgnore
    public ResultHolder<String> on(@PathVariable String event, @RequestBody Object[] args) {
        eventListenerTemplate.on(event, args);
        return ResultHolder.success("接收到事件");
    }
}
