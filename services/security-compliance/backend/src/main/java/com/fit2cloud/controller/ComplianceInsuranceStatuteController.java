package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.compliance_insurance_statute.ComplianceInsuranceStatuteRequest;
import com.fit2cloud.controller.response.compliance_insurance_statute.ComplianceInsuranceStatuteResponse;
import com.fit2cloud.service.IComplianceInsuranceStatuteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/30  9:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_insurance_statute")
@Validated
@Api(value = "等保条例相关接口", tags = "等保条例相关接口")
public class ComplianceInsuranceStatuteController {
    @Resource
    private IComplianceInsuranceStatuteService complianceInsuranceStatuteService;

    @ApiOperation("分页查询等保条例")
    @GetMapping("/{currentPage}/{limit}")
    @PreAuthorize("hasAnyCePermission('INSURANCE:READ')")
    public ResultHolder<IPage<ComplianceInsuranceStatuteResponse>> page(@NotNull(message = "当前页不能为空")
                                                                        @Min(message = "当前页不能小于0", value = 1)
                                                                        @PathVariable("currentPage")
                                                                        Integer currentPage,
                                                                        @NotNull(message = "每页大小不能为空")
                                                                        @Min(message = "每页大小不能小于1", value = 1)
                                                                        @PathVariable("limit")
                                                                        Integer limit, ComplianceInsuranceStatuteRequest request) {
        return ResultHolder.success(complianceInsuranceStatuteService.page(currentPage, limit, request));
    }


    @ApiOperation("获取所有的等保条例")
    @GetMapping
    @PreAuthorize("hasAnyCePermission('INSURANCE:READ','RULE:READ','SCAN:READ')")
    public ResultHolder<List<ComplianceInsuranceStatuteResponse>> list(ComplianceInsuranceStatuteRequest request) {
        return ResultHolder.success(complianceInsuranceStatuteService.list(request));
    }
}
