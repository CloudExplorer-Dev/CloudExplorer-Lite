package com.fit2cloud.constants;

/**
 * 计费类型
 *
 * @author jianneng
 * @date 2023/3/15 15:03
 **/
public enum ResourcePaymentOptionsEnum {
    /**
     *
     */
    POST_PAID("PostPaid", "按需按量"),
    PRE_PAID("PrePaid", "包年包月"),
    SPOT_PAID("SpotPaid", "竞价"),

    ;

    /**
     * 名称
     */
    private final String value;

    /**
     * 描述
     */
    private final String description;

    ResourcePaymentOptionsEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
