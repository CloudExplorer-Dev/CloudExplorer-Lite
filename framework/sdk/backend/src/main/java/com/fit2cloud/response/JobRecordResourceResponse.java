package com.fit2cloud.response;

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
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/10/18 10:55
 **/
@Data
public class JobRecordResourceResponse {

    @Schema(title = "资源id", description = "资源id")
    private String resourceId;

    @Schema(title = "资源类型", description = "资源类型")
    private String resourceType;

    @Schema(title = "任务记录id", description = "任务记录id")
    private String jobRecordId;

    @Schema(title = "定时任务类型", description = "定时任务类型")
    private JobTypeConstants type;

    @Schema(title = "任务状态", description = "任务状态")
    private JobStatusConstants status;

    @Schema(title = "任务描述", description = "任务描述")
    private String description;

    @Schema(title = "任务结果", description = "任务结果")
    private String result;

    @Schema(title = "任务参数", description = "任务参数")
    private Map<String, Object> params;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
