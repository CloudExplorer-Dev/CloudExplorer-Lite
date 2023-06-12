package com.fit2cloud.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.base.entity.json_entity.PackagePriceBillingPolicy;
import com.fit2cloud.base.handler.BillingFieldArrayTypeHandler;
import com.fit2cloud.base.handler.MapTypeHandler;
import com.fit2cloud.base.handler.PackagePriceBillingPolicyTypeHandler;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "bill_policy_details", autoResultMap = true)
public class BillPolicyDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 策略id
     */
    @TableField("bill_policy_id")
    private String billPolicyId;

    /**
     * 资源类型
     */
    @TableField("resource_type")
    private String resourceType;

    /**
     * 单价(按需)计费策略
     */
    @TableField(value = "unit_price_on_demand_billing_policy", typeHandler = BillingFieldArrayTypeHandler.class)
    private List<BillingField> unitPriceOnDemandBillingPolicy;

    /**
     * 单价(包年包月)计费策略
     */
    @TableField(value = "unit_price_monthly_billing_policy", typeHandler = BillingFieldArrayTypeHandler.class)
    private List<BillingField> unitPriceMonthlyBillingPolicy;

    /**
     * 套餐计费策略
     */
    @TableField(value = "package_price_billing_policy", typeHandler = PackagePriceBillingPolicyTypeHandler.class)
    private List<PackagePriceBillingPolicy> packagePriceBillingPolicy;

    @TableField(value = "global_config_meta", typeHandler = MapTypeHandler.class)
    private Map<String, Object> globalConfigMeta;

    @TableField("create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField("update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BillPolicyDetails billPolicyDetails) {
            return CommonUtil.strSort(JsonUtil.toJSONString(Map.of("unitPriceOnDemandBillingPolicy", billPolicyDetails.unitPriceOnDemandBillingPolicy,
                            "unitPriceMonthlyBillingPolicy", billPolicyDetails.unitPriceMonthlyBillingPolicy,
                            "packagePriceBillingPolicy", billPolicyDetails.packagePriceBillingPolicy
                            , "globalConfigMeta", billPolicyDetails.globalConfigMeta)))
                    .equals(CommonUtil.strSort(JsonUtil.toJSONString(Map.of("unitPriceOnDemandBillingPolicy", this.unitPriceOnDemandBillingPolicy,
                            "unitPriceMonthlyBillingPolicy", this.unitPriceMonthlyBillingPolicy,
                            "packagePriceBillingPolicy", this.packagePriceBillingPolicy,
                            "globalConfigMeta", this.globalConfigMeta))));
        } else {
            return false;
        }
    }
}
