package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.request.cloud_account.CloudAccountModuleJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/13  11:23 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class UpdateJobsRequest {
    /**
     * 云账号id
     */
    @Schema(title = "云账号id", description = "云账号id")
    @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
    @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = true)
    private String cloudAccountId;

    /**
     * 云账号同步定时任务
     */
    @Size(min = 1, message = "{i18n.cloud_acoount.update.job.size.ge.one}")
    private List<CloudAccountModuleJob> cloudAccountModuleJobs;
    /**
     * 选中的区域
     */
    private List<Credential.Region> selectRegion;
}
