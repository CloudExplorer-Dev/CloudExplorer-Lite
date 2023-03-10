package com.fit2cloud.constants;

import com.fit2cloud.common.utils.LocaleUtil;

/**
 * {@code  @Author:张少虎}
 * {@code  @Date: 2022/8/30  2:36 PM}
 * {@code  @Version 1.0}
 * {@code  @注释: 模块-菜单-操作按钮}
 * 账单模块 4000000
 * - provider异常  300000000
 */
public enum ErrorCodeConstants {
    ;
    /**
     * 提示
     */
    private final String message;
    /**
     * 状态码
     */
    private final Integer code;

    ErrorCodeConstants(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取错误提示
     *
     * @return 错误提示文本
     */
    public String getMessage() {
        return LocaleUtil.getMessage(message, message);
    }

    /**
     * 获取错误code
     *
     * @return 错误code
     */
    public Integer getCode() {
        return code;
    }
}
