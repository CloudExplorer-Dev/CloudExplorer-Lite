package com.fit2cloud.charging.constants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/7  17:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum VmDiskStateConstants {

    NotCreate('0', "计费预留字段,用于当月占位"),

    available('1', "可用"),

    deleted('2', "已删除"),

    in_use('3', "使用中"),

    unknown('4', "未知");

    private final char code;
    private final String message;

    VmDiskStateConstants(char code, String message) {
        this.code = code;
        this.message = message;
    }

    public char getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
