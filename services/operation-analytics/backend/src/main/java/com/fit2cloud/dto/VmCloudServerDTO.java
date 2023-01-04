package com.fit2cloud.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.base.entity.VmCloudServer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author jianneng
 * @date 2022/12/24 11:25
 **/
@Data
public class VmCloudServerDTO extends VmCloudServer {

    @ApiModelProperty("组织名称")
    private String organizationName;
    @ApiModelProperty("工作空间名称")
    private String workspaceName;
    @ApiModelProperty("云账号名称")
    private String accountName;
    @ApiModelProperty("企业项目")
    private String cloudProjectName;
    @ApiModelProperty("云平台")
    private String platform;

    private String chargeType;

    private String createMonth;

    private BigDecimal cpuMinimum;
    private BigDecimal cpuMaximum;
    private BigDecimal cpuAverage;

    private BigDecimal memoryMinimum;
    private BigDecimal memoryMaximum;
    private BigDecimal memoryAverage;

    private BigDecimal diskAverage;

}
