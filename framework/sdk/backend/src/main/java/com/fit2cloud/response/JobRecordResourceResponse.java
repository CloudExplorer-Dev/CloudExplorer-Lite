package com.fit2cloud.response;

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
 * @author jianneng
 * @date 2022/10/18 10:55
 **/
@Data
public class JobRecordResourceResponse {

    @ApiModelProperty(value = "资源id", notes = "资源id")
    private String resourceId;

    @ApiModelProperty(value = "任务记录id",notes = "任务记录id")
    private String jobRecordId;
    @ApiModelProperty(value = "定时任务类型", notes = "定时任务类型")
    private JobTypeConstants type;

    @ApiModelProperty(value = "任务状态", notes = "任务状态")
    private JobStatusConstants status;

    @ApiModelProperty(value = "任务描述", notes = "任务描述")
    private String description;

    @ApiModelProperty(value = "任务结果", notes = "任务结果")
    private String result;

    @ApiModelProperty(value = "任务参数", notes = "任务参数")
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
