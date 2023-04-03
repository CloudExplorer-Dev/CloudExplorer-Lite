package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


@RestController
@RequestMapping("/api/licence")
@Validated
@Api("证书相关接口")
public class LicenceController {

    @GetMapping("version")
    @ApiOperation(value = "获取版本号", notes = "获取版本号")
    @PreAuthorize("hasAnyCePermission('ABOUT:READ')")
    public ResultHolder<String> getVersion() {
        File versionFile = new File("/opt/cloudexplorer/VERSION");
        String version = StringUtils.trim(FileUtils.txt2String(versionFile));
        return ResultHolder.success(version);
    }

}
