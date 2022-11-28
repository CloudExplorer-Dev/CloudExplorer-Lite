package com.fit2cloud.provider.impl.huawei.entity.csv;

import com.fit2cloud.provider.convert.EmptyNumberConvert;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/25  10:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class HuaweiBillCsvModel {
    @CsvBindByName(column = "账期")
    private String billingCycle;
    @CsvBindByName(column = "企业项目ID")
    private String projectId;
    @CsvBindByName(column = "企业项目")
    private String projectName;
    @CsvBindByName(column = "账号")
    private String accountId;
    @CsvBindByName(column = "产品类型编码")
    private String productTypeCode;
    @CsvBindByName(column = "产品类型")
    private String productType;
    @CsvBindByName(column = "产品编码")
    private String productCode;
    @CsvBindByName(column = "产品")
    private String productName;
    @CsvBindByName(column = "产品ID")
    private String productId;
    @CsvBindByName(column = "计费模式")
    private String billMode;
    @CsvBindByName(column = "消费时间")
    private String consumeDate;
    @CsvBindByName(column = "使用开始时间")
    private String startTime;
    @CsvBindByName(column = "使用结束时间")
    private String endTime;
    @CsvBindByName(column = "订单号/交易号")
    private String orderId;
    @CsvBindByName(column = "交易时间")
    private String transactionTime;
    @CsvBindByName(column = "账单类型")
    private String billType;
    @CsvBindByName(column = "资源ID")
    private String resourceId;
    @CsvBindByName(column = "资源名称")
    private String resourceName;
    @CsvBindByName(column = "资源标签")
    private String resourceTag;
    @CsvBindByName(column = "SKU编码")
    private String skuCode;
    @CsvBindByName(column = "规格")
    private String specs;
    @CsvBindByName(column = "区域编码")
    private String regionId;
    @CsvBindByName(column = "区域")
    private String regionName;
    @CsvBindByName(column = "可用区")
    private String zone;
    @CsvBindByName(column = "使用量类型编码")
    private String usageTypeCode;
    @CsvBindByName(column = "单价(¥)")
    private String unitPrice;
    @CsvBindByName(column = "单价单位")
    private String unitPriceUnit;
    @CsvCustomBindByName(column = "使用量", converter = EmptyNumberConvert.class)
    private Double usage;
    @CsvBindByName(column = "使用量单位")
    private String usageUnit;
    @CsvBindByName(column = "套餐内使用量")
    private String inPackageUsage;
    @CsvBindByName(column = "预留实例内使用量")
    private String reservedInstanceUsage;
    @CsvCustomBindByName(column = "官网价(¥)", converter = EmptyNumberConvert.class)
    private Double officialPrice;
    @CsvCustomBindByName(column = "优惠金额(¥)", converter = EmptyNumberConvert.class)
    private Double discountAmount;
    @CsvCustomBindByName(column = "应付金额(¥)", converter = EmptyNumberConvert.class)
    private Double amountPayable;
    @CsvCustomBindByName(column = "现金支付(¥)", converter = EmptyNumberConvert.class)
    private Double cashPayable;
    @CsvCustomBindByName(column = "信用额度支付(¥)", converter = EmptyNumberConvert.class)
    private Double paymentByCreditLimit;
    @CsvCustomBindByName(column = "代金券抵扣(¥)", converter = EmptyNumberConvert.class)
    private Double voucherDeduction;
    @CsvCustomBindByName(column = "现金券抵扣(¥)", converter = EmptyNumberConvert.class)
    private Double cashCouponDeduction;
    @CsvCustomBindByName(column = "储值卡抵扣(¥)", converter = EmptyNumberConvert.class)
    private Double storedValueCardDeduction;
    @CsvCustomBindByName(column = "欠费金额(¥)", converter = EmptyNumberConvert.class)
    private Double amountOwed;
    @CsvCustomBindByName(column = "已核销金额(¥)", converter = EmptyNumberConvert.class)
    private Double amountWrittenOff;
    @CsvBindByName(column = "优惠类型")
    private String preferenceType;
    @CsvBindByName(column = "优惠信息")
    private String preferentialInformation;
    @CsvBindByName(column = "订单类型")
    private String orderType;
    @CsvBindByName(column = "订单数量")
    private String orderNumber;
    @CsvBindByName(column = "订单周期数")
    private String numberOfOrderCycles;
    @CsvBindByName(column = "订单周期单位")
    private String orderCycleUnit;
    @CsvBindByName(column = "RI购买小时数")
    private String riPurchaseHours;
    @CsvBindByName(column = "退订原因")
    private String reasonForUnsubscription;
    @CsvBindByName(column = "退订手续费(¥)")
    private String serviceChargeForCancellation;
    @CsvBindByName(column = "原订单号")
    private String resourceOrderId;
    @CsvBindByName(column = "是否竞价实例")
    private String biddingInstance;
}
