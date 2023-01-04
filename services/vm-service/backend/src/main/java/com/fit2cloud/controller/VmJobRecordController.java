package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.jobrecord.PageJobRecordRequest;
import com.fit2cloud.dto.JobRecordDTO;
import com.fit2cloud.service.IJobRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/jobs")
@Validated
@Api("任务相关接口")
public class VmJobRecordController {
    @Resource
    private IJobRecordService iJobRecordService;

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("hasAnyCePermission('JOBS:READ')")
    public ResultHolder<IPage<JobRecordDTO>> list(@Validated PageJobRecordRequest pageJobRecordRequest) {
        return ResultHolder.success(iJobRecordService.pageJobRecord(pageJobRecordRequest));
    }

    @ApiOperation(value = "根据任务ID查询", notes = "根据任务ID查询")
    @PreAuthorize("hasAnyCePermission('JOBS:READ')")
    @GetMapping
    public ResultHolder<JobRecordDTO> getRecord(@ApiParam("任务ID")
                                                @RequestParam("id")
                                                String id) {
        return ResultHolder.success(iJobRecordService.getRecord(id));
    }


}
