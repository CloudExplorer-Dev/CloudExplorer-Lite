package com.fit2cloud.provider.impl.tencent.entity.csv;


import com.fit2cloud.provider.convert.EmptyNumberConvert;
import com.fit2cloud.provider.convert.TrimStringConvert;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/24  10:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class TencentCsvModel implements Serializable {

    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "支付者UIN")
    private String payerUin;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "使用者UIN")
    private String userUin;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "操作者UIN")
    private String operatorUin;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "产品名称")
    private String productName;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "计费模式")
    private String billingMode;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "项目名称")
    private String entryName;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "地域")
    private String region;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "可用区")
    private String availabilityZone;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "国内国际")
    private String domesticAndInternational;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "资源ID")
    private String resourceId;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "实例名称")
    private String instanceName;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "子产品名称")
    private String subProductName;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "交易类型")
    private String transactionType;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "订单ID")
    private String orderId;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "交易ID")
    private String transactionId;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "扣费时间")
    private String deductionTime;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "开始使用时间")
    private String startTime;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "结束使用时间")
    private String endTime;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "组件类型")
    private String componentType;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "组件名称")
    private String componentName;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "组件刊例价")
    private Double componentPublicationPrice;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "组件单价")
    private Double componentUnitPrice;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "组件价格单位")
    private String componentUnitPriceCompany;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "组件用量")
    private Double componentUsage;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "组件用量单位")
    private String componentUsageCompany;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "使用时长")
    private String useDuration;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "时长单位")
    private String durationUnit;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "组件原价")
    private Double componentOriginalPrice;
    @CsvCustomBindByName(converter = TrimStringConvert.class, column = "折扣率")
    private String discountRate;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "优惠后总价")
    private Double discountTotalPrice;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "代金券支出(元)")
    private Double voucherExpenditure;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "赠送账户支出(元)")
    private Double expenseOfGiftAccount;
    @CsvCustomBindByName(converter = EmptyNumberConvert.class, column = "现金账户支出(元)")
    private Double cashAccountExpenses;
    /**
     * 资源标签
     */
    private Map<String, String> tag;

}
