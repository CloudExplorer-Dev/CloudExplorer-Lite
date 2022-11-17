package com.fit2cloud.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class BillDetailResponse {
    @Id
    private String id;
    @ApiModelProperty(value = "组织层级树", notes = "组织层级树")
    private Map<String, Object> orgTree;

    @ApiModelProperty(value = "工作空间id", notes = "工作空间id")

    private String workspaceId;
    @ApiModelProperty(value = "工作空间名称", notes = "工作空间名称")

    private String workspaceName;
    @ApiModelProperty(value = "组织id", notes = "组织id")

    private String organizationId;
    @ApiModelProperty(value = "组织名称", notes = "组织名称")

    private String organizationName;

    @ApiModelProperty(value = "资源id", notes = "资源id")
    private String resourceId;

    @ApiModelProperty(value = "资源名称", notes = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "企业项目id", notes = "企业项目id")
    private String projectId;

    @ApiModelProperty(value = "企业项目名称", notes = "企业项目名称")
    private String projectName;

    @ApiModelProperty(value = "付款账号", notes = "付款账号")
    private String payAccountId;

    @ApiModelProperty(value = "账期", notes = "账期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime billingCycle;

    @ApiModelProperty(value = "账单开始时间", notes = "账单开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime usageStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "账单结束时间", notes = "账单结束时间")
    private LocalDateTime usageEndDate;

    @ApiModelProperty(value = "云平台", notes = "云平台")
    private String provider;


    @ApiModelProperty(value = "产品id", notes = "产品id")
    private String productId;

    @ApiModelProperty(value = "产品名称", notes = "产品名称")
    private String productName;

    @ApiModelProperty(value = "计费模式", notes = "计费模式")
    private String billMode;

    @ApiModelProperty(value = "区域id", notes = "区域id")
    private String regionId;

    @ApiModelProperty(value = "区域名称", notes = "区域名称")
    private String regionName;


    @ApiModelProperty(value = "可用区", notes = "可用区")
    private String zone;

    @ApiModelProperty(value = "云账户id", notes = "云账户id")
    private String cloudAccountId;

    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    private String cloudAccountName;

    @ApiModelProperty(value = "标签对象", notes = "标签对象")
    private Map<String, Object> tags;

    @ApiModelProperty(value = "原价", notes = "原件")
    private BigDecimal totalCost;

    @ApiModelProperty(value = "优惠后总价", notes = "优惠后总价")
    private BigDecimal realTotalCost;
}
