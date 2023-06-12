package com.fit2cloud.request.cloud_account;

import com.fit2cloud.dto.module.ModuleInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/16  1:40 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class CloudAccountModuleJob extends ModuleInfo {

    @Schema(title = "模块名称", description = "模块名称")
    private String module;

    @Schema(title = "定时任务", description = "定时任务")
    private List<CloudAccountJobItem> jobDetailsList;
}
