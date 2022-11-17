package com.fit2cloud.es.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fit2cloud.common.annotaion.BillField;
import com.fit2cloud.common.conver.impl.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  3:35 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Document(indexName = "ce-cloud-bill")
@Data
public class CloudBill {
    @Id
    private String id;

    @BillField(label = "组织层级树", group = true, conver = OrganizationConvert.class)
    @Field(type = FieldType.Object)
    private Map<String, Object> orgTree;

    @BillField(label = "工作空间id", group = true, conver = WorkSpaceConvert.class)
    @Field(type = FieldType.Keyword)
    private String workspaceId;

    @BillField(label = "组织id", group = true, conver = OrganizationConvert.class)
    @Field(type = FieldType.Keyword)
    private String organizationId;

    @BillField(label = "资源id")
    @Field(type = FieldType.Keyword)
    private String resourceId;

    @BillField(label = "资源名称", group = true)
    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String resourceName;

    @BillField(label = "企业项目Id")
    @Field(type = FieldType.Keyword)
    private String projectId;

    @BillField(label = "企业项目", group = true, authorize = true)
    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String projectName;

    @BillField(label = "付款账号")
    @Field(type = FieldType.Keyword)
    private String payAccountId;

    @BillField(label = "账期")
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime billingCycle;

    @BillField(label = "账单开始时间")
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime usageStartDate;

    @BillField(label = "账单结束时间")
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime usageEndDate;

    @BillField(label = "云平台", group = true, authorize = true, conver = ProviderConvert.class)
    @Field(type = FieldType.Keyword)
    private String provider;

    @BillField(label = "产品id")
    @Field(type = FieldType.Keyword)
    private String productId;

    @BillField(label = "产品名称", group = true, authorize = true)
    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String productName;

    @BillField(label = "计费模式", group = true, conver = BillModeConvert.class)
    @Field(type = FieldType.Keyword)
    private String billMode;

    @BillField(label = "区域Id")
    @Field(type = FieldType.Keyword)
    private String regionId;

    @BillField(label = "区域", group = true)
    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String regionName;

    @BillField(label = "可用区")
    @Field(type = FieldType.Keyword)
    private String zone;

    @BillField(label = "云账号", group = true, authorize = true, conver = CloudAccountConvert.class)
    @Field(type = FieldType.Keyword)
    private String cloudAccountId;

    @BillField(label = "标签", authorize = true)
    @Field(type = FieldType.Object)
    private Map<String, Object> tags;

    @BillField(label = "原价")
    @Field(type = FieldType.Double)
    private BigDecimal totalCost;

    @BillField(label = "优惠后总价")
    @Field(type = FieldType.Double)
    private BigDecimal realTotalCost;
}
