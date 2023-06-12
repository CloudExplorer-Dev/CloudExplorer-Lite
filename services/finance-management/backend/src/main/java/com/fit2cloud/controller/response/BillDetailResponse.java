package com.fit2cloud.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class BillDetailResponse {
    @Id
    private String id;

    @Schema(title = "组织层级树", description = "组织层级树")
    private Map<String, Object> orgTree;

    @Schema(title = "工作空间id", description = "工作空间id")

    private String workspaceId;
    @Schema(title = "工作空间名称", description = "工作空间名称")

    private String workspaceName;
    @Schema(title = "组织id", description = "组织id")

    private String organizationId;
    @Schema(title = "组织名称", description = "组织名称")

    private String organizationName;

    @Schema(title = "资源id", description = "资源id")
    private String resourceId;

    @Schema(title = "资源名称", description = "资源名称")
    private String resourceName;

    @Schema(title = "企业项目id", description = "企业项目id")
    private String projectId;

    @Schema(title = "企业项目名称", description = "企业项目名称")
    private String projectName;

    @Schema(title = "付款账号", description = "付款账号")
    private String payAccountId;

    @Schema(title = "账期", description = "账期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime billingCycle;

    @Schema(title = "账单开始时间", description = "账单开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime usageStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(title = "账单结束时间", description = "账单结束时间")
    private LocalDateTime usageEndDate;

    @Schema(title = "云平台", description = "云平台")
    private String provider;


    @Schema(title = "产品id", description = "产品id")
    private String productId;

    @Schema(title = "产品名称", description = "产品名称")
    private String productName;

    @Schema(title = "产品详情", description = "产品详情")
    private String productDetail;

    @Schema(title = "计费模式", description = "计费模式")
    private String billMode;

    @Schema(title = "区域id", description = "区域id")
    private String regionId;

    @Schema(title = "区域名称", description = "区域名称")
    private String regionName;


    @Schema(title = "可用区", description = "可用区")
    private String zone;

    @Schema(title = "云账户id", description = "云账户id")
    private String cloudAccountId;

    @Schema(title = "云账号名称", description = "云账号名称")
    private String cloudAccountName;

    @Schema(title = "标签对象", description = "标签对象")
    private Map<String, Object> tags;

    @Schema(title = "原价", description = "原件")
    private BigDecimal totalCost;

    @Schema(title = "优惠后总价", description = "优惠后总价")
    private BigDecimal realTotalCost;
}
