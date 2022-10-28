package com.fit2cloud.controller;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/12  10:56 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/base")
@Api("云平台资源获取")
public class ProviderResourceController {

    @GetMapping("provider/{clazz}/{method}")
    private ResultHolder<Object> getProviderResourceByMethod(@PathVariable("clazz") String clazz,
                                                             @PathVariable("method") String method,
                                                             @RequestParam Map<String, Object> params) {
        return ResultHolder.success(FormUtil.exec(clazz, false, method, params));
    }

    @GetMapping("service/{clazz}/{method}")
    private ResultHolder<Object> getProviderResourceByServiceMethod(@PathVariable("clazz") String clazz,
                                                                    @PathVariable("method") String method,
                                                                    @RequestParam Map<String, Object> params) {
        return ResultHolder.success(FormUtil.exec(clazz, true, method, params));
    }
}
