package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.request.PageOrganizationRequest;
import com.fit2cloud.service.IOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  11:06 AM
 * @Version 1.0
 * @注释:
 */
@RestController
@RequestMapping("/organization")
@Api("组织相关接口")
@Validated
public class OrganizationController {
}
