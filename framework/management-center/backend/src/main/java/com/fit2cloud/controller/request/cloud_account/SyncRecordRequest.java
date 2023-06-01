package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.request.pub.PageOrderRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.util.Date;

/**
 * Author: LiuDi
 * Date: 2022/10/11 4:26 PM
 */
@Data
public class SyncRecordRequest extends PageOrderRequest {

    @Serial
    private static final long serialVersionUID = 6366717914967520378L;
    /**
     * 云账号id
     */
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    @NotNull(message = "{i18n.cloud_account.id.is.not.empty}")
    @CustomValidated(mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
    private String cloudAccountId;

    @ApiModelProperty(value = "同步类型")
    private String description;

    @ApiModelProperty(value = "任务状态", notes = "任务状态")
    private JobStatusConstants status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
