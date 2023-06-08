package com.fit2cloud.charging.constants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/7  16:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum VmServerStateConstants {
    NotCreate('0', "未创建"),
    Running('1', "运行中"),
    Stopped('2', "停止"),
    Failed('3', "失败"),
    Deleted('4', "已删除"),
    unknown('4', "未知");
    private final char code;
    private final String message;

    VmServerStateConstants(char code, String message) {
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
