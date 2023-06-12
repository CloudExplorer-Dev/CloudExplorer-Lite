package com.fit2cloud.controller;


import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.job.JobModuleInfo;
import com.fit2cloud.dto.module.Module;
import com.fit2cloud.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ModuleController {

    @Resource
    private CommonService commonService;

    @GetMapping("modules")
    public ResultHolder<List<Module>> modules() {
        return ResultHolder.success(commonService.getModules());
    }

    @GetMapping("moduleJobs")
    public ResultHolder<List<JobModuleInfo>> moduleJobs() {
        List<JobModuleInfo> moduleJobs = commonService.getModuleJobs();
        return ResultHolder.success(moduleJobs);
    }
}
