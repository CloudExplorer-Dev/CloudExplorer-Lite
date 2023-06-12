package com.fit2cloud.dto.optimization;


/**
 * 描述：优化字段比较
 *
 * @author jianneng
 */
public enum OptimizationRuleFieldCompare {

    /**
     * 字段不等于
     */
    NOT_EQ("不等于", "!="),
    /**
     * 字段等于
     */
    EQ("等于", "=="),

    /**
     * 小于等于
     */
    LE("小于等于", "<="),
    /**
     * 小于
     */
    LT("小于", "<"),
    /**
     * 大于等于
     */
    GE("大于等于", ">="),
    /**
     * 大于
     */
    GT("大于", ">"),
    ;

    /**
     * 比较器提示
     */
    private final String message;
    private final String compare;

    OptimizationRuleFieldCompare(String message, String compare) {
        this.message = message;
        this.compare = compare;
    }

    public String getMessage() {
        return message;
    }

    public String getCompare() {
        return compare;
    }


}
