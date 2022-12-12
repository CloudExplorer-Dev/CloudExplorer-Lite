package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.dto.VmCloudDatastoreDTO;
import com.fit2cloud.dto.VmCloudHostDTO;
import com.fit2cloud.service.IBaseResourceAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 基础资源分析
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
@RestController
@RequestMapping("/api/baseResourceAnalysis")
@Validated
@Api("基础资源分析相关接口")
public class BaseResourceAnalysisController {



    @Resource
    private IBaseResourceAnalysisService iBaseResourceAnalysisService;

    @ApiOperation(value = "分页查询宿主机", notes = "分页查询宿主机")
    @GetMapping("/host/page")
    public ResultHolder<IPage<VmCloudHostDTO>> pageHostList(@Validated PageHostRequest pageHostRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.pageHost(pageHostRequest));
    }

    @ApiOperation(value = "分页查询存储器", notes = "分页查询存储器")
    @GetMapping("/datastore/page")
    public ResultHolder<IPage<VmCloudDatastoreDTO>> pageDatastoreList(@Validated PageDatastoreRequest pageDatastoreRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.pageDatastore(pageDatastoreRequest));
    }
}
