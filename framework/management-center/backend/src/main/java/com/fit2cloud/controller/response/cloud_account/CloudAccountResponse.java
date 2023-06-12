package com.fit2cloud.controller.response.cloud_account;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.common.constants.CloudAccountConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author:张少虎
 * @Date: 2022/10/9  6:08 PM
 * @Version 1.0
 * @注释:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudAccountResponse {

    @Schema(title = "主键id", description = "主键id")
    private String id;

    @Schema(title = "云账号名称", description = "云账号名称")
    private String name;

    @Schema(title = "云平台", description = "云平台")
    private String platform;

    @Schema(title = "凭证字段", description = "凭证字段")
    private String credential;

    @Schema(title = "云账号状态", description = "云账号状态")
    private Boolean state;

    @Schema(title = "同步状态", description = "同步状态")
    private CloudAccountConstants.Status status;

    @TableField("create_time")
    @Schema(title = "创建时间", description = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(title = "编辑时间", description = "编辑时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
