package com.fit2cloud.provider.exception;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  9:48 PM
 * @Version 1.0
 * @注释: 重试异常
 */
public class ReTryException extends RuntimeException {
    private String message;
    private Integer code;

    public ReTryException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
