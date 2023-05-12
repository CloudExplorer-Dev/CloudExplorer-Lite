package com.fit2cloud.common.job.result.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fit2cloud.common.job.result.Result;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/11  9:40}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleResult implements Result {
    private int code;

    private String message;

    private boolean termination;


    private Object data;

    private SimpleResult(int code, String message, boolean termination, Object data) {
        this.code = code;
        this.message = message;
        this.termination = termination;
        this.data = data;
    }

    public static SimpleResult of(int code, String message, boolean termination) {
        return new SimpleResult(code, message, termination, null);
    }

    public static SimpleResult of(int code, String message, boolean termination, Object data) {
        return new SimpleResult(code, message, termination, data);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Object getData() {
        return data;
    }

    @JsonIgnore
    @Override
    public boolean getTermination() {
        return termination;
    }


}
