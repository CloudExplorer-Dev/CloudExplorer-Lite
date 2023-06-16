package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.jobrecord.PageJobRecordRequest;
import com.fit2cloud.dto.JobRecordDTO;
import com.fit2cloud.service.IJobRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;


@RestController
@RequestMapping("/api/jobs")
@Validated
@Tag(name = "任务相关接口")
public class VmJobRecordController {
    @Resource
    private IJobRecordService iJobRecordService;

    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@cepc.hasAnyCePermission('JOBS:READ')")
    public ResultHolder<IPage<JobRecordDTO>> list(@Validated PageJobRecordRequest pageJobRecordRequest) {
        return ResultHolder.success(iJobRecordService.pageJobRecord(pageJobRecordRequest));
    }

    @Operation(summary = "根据任务ID查询", description = "根据任务ID查询")
    @PreAuthorize("@cepc.hasAnyCePermission('JOBS:READ')")
    @GetMapping
    public ResultHolder<JobRecordDTO> getRecord(@Parameter(description = "任务ID")
                                                @RequestParam("id")
                                                String id) {
        return ResultHolder.success(iJobRecordService.getRecord(id));
    }


}
