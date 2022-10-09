package com.fit2cloud.response.cloud_account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/10/9  9:19 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class AccountJobRecordResponse {
    @ApiModelProperty(value = "云账户id", notes = "云账户id")
    private String accountId;

    @ApiModelProperty(value = "任务记录id",notes = "任务记录id")
    private String jobRecordId;
    @ApiModelProperty(value = "定时任务类型", notes = "定时任务类型")
    private JobTypeConstants type;

    @ApiModelProperty(value = "任务状态", notes = "任务状态")
    private JobStatusConstants status;

    @ApiModelProperty(value = "任务描述", notes = "任务描述")
    private String description;

    @ApiModelProperty(value = "任务参数", notes = "任务参数")
    private List<Map<String, Object>> params;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
