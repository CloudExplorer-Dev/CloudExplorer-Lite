package com.fit2cloud.provider.impl.aliyun.entity.csv;

import com.fit2cloud.provider.convert.EmptyNumberConvert;
import com.opencsv.bean.*;
import lombok.Data;

import java.util.Date;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/24  10:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class AliBillCsvModel {
    @CsvBindByName(column = "账期", required = true)
    @CsvDate(value = "yyyy-MM")
    private Date billingCycle;
    @CsvBindByName(column = "财务单元")
    private String financialUnit;
    @CsvBindByName(column = "账号ID")
    private String accountId;
    @CsvBindByName(column = "账号")
    private String accountName;
    @CsvBindByName(column = "Owner账号ID")
    private String ownerAccountId;
    @CsvBindByName(column = "产品Code")
    private String productCode;
    @CsvBindByName(column = "产品")
    private String productName;
    @CsvBindByName(column = "产品明细Code")
    private String productDescCode;
    @CsvBindByName(column = "产品明细")
    private String productDescName;
    @CsvBindByName(column = "业务类型")
    private String serviceType;
    @CsvBindByName(column = "消费类型")
    private String subscriptionType;
    @CsvBindByName(column = "服务时长")
    private Long serviceDuration;
    @CsvBindByName(column = "时长单位")
    private String serviceDurationUnit;
    @CsvBindByName(column = "账单类型")
    private String billType;
    @CsvBindByName(column = "计费方式")
    private String billMethod;
    @CsvBindByName(column = "实例ID")
    private String instanceId;
    @CsvBindByName(column = "实例昵称")
    private String instanceName;
    @CsvBindByName(column = "资源组")
    private String sourceGroup;
    @CsvBindByName(column = "实例标签")
    private String instanceTag;
    @CsvBindByName(column = "实例配置")
    private String instanceConfig;
    @CsvBindByName(column = "实例规格")
    private String instanceType;
    @CsvBindByName(column = "公网IP")
    private String publicNetworkId;
    @CsvBindByName(column = "私网IP")
    private String privateNetworkId;
    @CsvBindByName(column = "地域")
    private String regionName;
    @CsvBindByName(column = "可用区")
    private String zone;
    @CsvBindByName(column = "计费项")
    private String billItem;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "单价")
    private Double unitPrice;
    @CsvBindByName(column = "单价单位")
    private String unitPriceUnit;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "用量")
    private Double consumption;
    @CsvBindByName(column = "用量单位")
    private String consumptionUnit;
    @CsvBindByName(column = "资源包抵扣")
    private String resourcePackageDeduction;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "抵扣量（含RI）")
    private Double deductionAmount;
    @CsvBindByName(column = "官网价")
    private Double officialWebsitePrice;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "优惠金额")
    private Double discountAmount;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "优惠券抵扣")
    private Double couponDeduction;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "应付金额")
    private Double amountPayable;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "现金支付")
    private Double cashPayment;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "代金券抵扣")
    private Double voucherDeduction;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "储值卡支付金额")
    private Double paymentAmountOfStoredValueCard;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "欠费金额")
    private Double amountOwed;
    @CsvBindByName(column = "币种")
    private String currency;
    @CsvBindByName(column = "节省计划抵扣金额")
    private Double savingsPlanDeductionAmount;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "信用额度退款抵扣")
    private Double creditLineRefundDeduction;
    @CsvBindByName(column = "计费项Code")
    private String billingItemCode;
}
