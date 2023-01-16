package com.fit2cloud.provider.entity;

import java.util.Arrays;
import java.util.List;

import static com.fit2cloud.provider.entity.InstanceFieldCompare.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/7  14:28}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum InstanceFieldType {
    /**
     * 枚举
     */
    Enum(EQ, NotEq),
    /**
     * 字符串
     */
    String(EQ),
    /**
     * 数字
     */
    Number(GE, LE, EQ),
    /**
     * 数组字符串
     */
    ArrayString(IN),
    /**
     * 数组枚举
     */
    ArrayEnum(IN),
    /**
     * 数组数字
     */
    ArrayNumber(AVG_LE, AVG_GE, SUM_GE, SUM_LE, LE, GE, IN),
    /**
     * 时间
     */
    Date(GE, LE);

    /**
     * 字段对比器
     */
    private final List<InstanceFieldCompare> compares;

    InstanceFieldType(InstanceFieldCompare... compares) {
        this.compares = Arrays.stream(compares).toList();
    }

    public List<InstanceFieldCompare> getCompares() {
        return compares;
    }
}
