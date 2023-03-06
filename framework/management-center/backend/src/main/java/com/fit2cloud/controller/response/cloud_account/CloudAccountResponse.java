package com.fit2cloud.controller.response.cloud_account;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.common.constants.CloudAccountConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

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
    @ApiModelProperty(value = "主键id", notes = "主键id")
    private String id;

    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    private String name;

    @ApiModelProperty(value = "云平台", notes = "云平台")
    private String platform;

    @ApiModelProperty(value = "凭证字段", notes = "凭证字段")
    private String credential;

    @ApiModelProperty(value = "云账号状态", notes = "云账号状态")
    private Boolean state;

    @ApiModelProperty(value = "同步状态", notes = "同步状态")
    private CloudAccountConstants.Status status;

    @TableField("create_time")
    @ApiModelProperty(value = "创建时间", notes = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField("update_time")
    @ApiModelProperty(value = "修改时间", notes = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
