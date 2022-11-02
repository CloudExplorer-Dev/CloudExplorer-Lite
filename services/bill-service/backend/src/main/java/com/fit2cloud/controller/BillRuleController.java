package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.response.BillRules;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.service.IBillRuleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/29  6:23 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@Api("账单规则相关接口")
@RequestMapping("/api/bill_rule")
public class BillRuleController {
    @Resource
    private IBillRuleService billRuleService;

    @GetMapping("/list")
    public ResultHolder<List<BillRule>> list() {
        return ResultHolder.success(billRuleService.list());
    }

    @GetMapping("/page")
    public ResultHolder<Page<BillRule>> page() {
        return null;
    }
}
