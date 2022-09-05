package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.editor.OrderEditor;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.cloud_account.CloudAccountRequest;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.service.ICloudAccountService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @Author:张少虎
 * @Date: 2022/8/31  10:49 AM
 * @Version 1.0
 * @注释: 云账号相关接口
 */
@RestController
@RequestMapping("cloud_account")
public class CloudAccountController {
    @Resource
    private MessageSource messageSource;
    @Resource
    private ICloudAccountService cloudAccountService;

    @GetMapping("page")
    public ResultHolder<IPage<CloudAccount>> page(CloudAccountRequest cloudAccountRequest) {
        return ResultHolder.success(cloudAccountService.page(cloudAccountRequest));
    }

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
