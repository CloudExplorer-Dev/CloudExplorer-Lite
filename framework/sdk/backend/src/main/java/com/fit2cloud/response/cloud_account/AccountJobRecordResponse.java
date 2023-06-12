package com.fit2cloud.response.cloud_account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "云账户id", description = "云账户id")
    private String accountId;

    @Schema(title = "任务记录id", description = "任务记录id")
    private String jobRecordId;

    @Schema(title = "定时任务类型", description = "定时任务类型")
    private JobTypeConstants type;

    @Schema(title = "任务状态", description = "任务状态")
    private JobStatusConstants status;

    @Schema(title = "任务描述", description = "任务描述")
    private String description;

    @Schema(title = "任务参数", description = "任务参数")
    private List<Map<String, Object>> params;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
