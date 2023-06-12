package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.PageBillDetailedRequest;
import com.fit2cloud.controller.response.BillDetailResponse;
import com.fit2cloud.service.IBillDetailedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@Tag(name = "账单详情相关接口")
@RequestMapping("/api/bill_detailed")
public class BillDetailedController {
    @Resource
    private IBillDetailedService billDetailedService;

    @GetMapping("/{currentPage}/{pageSize}")
    @Operation(summary = "分页查询账单详情", description = "分页查询账单详情")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_DETAILED:READ')")
    public ResultHolder<IPage<BillDetailResponse>> page(@Parameter(name = "当前页") @PathVariable("currentPage") Integer currentPage, @Parameter(name = "每页显示多少条") @PathVariable("pageSize") Integer pageSize, PageBillDetailedRequest request) {
        IPage<BillDetailResponse> page = billDetailedService.page(currentPage, pageSize, request);
        return ResultHolder.success(page);
    }

}
