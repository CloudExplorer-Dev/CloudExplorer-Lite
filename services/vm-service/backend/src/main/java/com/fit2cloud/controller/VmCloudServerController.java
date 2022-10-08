package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.service.IVmCloudServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jianneng
 * @date 2022/9/27 14:31
 **/
@RestController
@RequestMapping("/api/server")
@Validated
@Api("虚拟机相关接口")
public class VmCloudServerController {
    @Resource
    private IVmCloudServerService iVmCloudServerService;

    @ApiOperation(value = "分页查询虚拟机", notes = "分页查询虚拟机")
    @GetMapping("/page")
    public ResultHolder<IPage<VmCloudServerDTO>> list(@Validated PageVmCloudServerRequest pageVmCloudServerRequest) {
        return ResultHolder.success(iVmCloudServerService.pageVmCloudServer(pageVmCloudServerRequest));
    }
}
