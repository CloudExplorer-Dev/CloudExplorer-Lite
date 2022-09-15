package com.fit2cloud.controller.editor.advice;

import com.fit2cloud.controller.editor.OrderEditor;
import com.fit2cloud.request.pub.OrderRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * @Author:张少虎
 * @Date: 2022/9/14  5:29 PM
 * @Version 1.0
 * @注释:
 */

@ControllerAdvice
public class GlobalControllerAdiviceController {
    /**
     * 控制器初始化时调用
     * SpringMVC 使用WebDataBinder处理<请求消息,方法入参>的绑定工作
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OrderRequest.class, new OrderEditor());
    }
}
