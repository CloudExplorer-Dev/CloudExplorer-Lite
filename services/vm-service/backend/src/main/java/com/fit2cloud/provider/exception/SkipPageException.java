package com.fit2cloud.provider.exception;

import com.aliyun.tea.TeaException;

/**
 * @Author:张少虎
 * @Date: 2022/9/21  4:27 PM
 * @Version 1.0
 * @注释:
 */
public class SkipPageException extends RuntimeException {
    private String message;
    private Integer code;

    public SkipPageException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    /**
     * 当遇到region不可用的情况需要抛出SkipPageException的异常从而跳过同步
     *
     * @param e 异常信息
     */
    public static void throwReTry(Exception e) {
        if (e instanceof TeaException teaException) {
            if (teaException.getCode().equals("InvalidOperation.NotSupportedEndpoint")) {
                throw new SkipPageException(1001, "跳过");
            }
        }
    }
}
