package com.fit2cloud.common.constants;

/**
 * @Author:张少虎
 * @Date: 2022/8/29  5:54 PM
 * @Version 1.0
 * @注释:
 */
public enum GlobalErrorCodeConstants {
    LOGIN_TIME_OUT(9000,"登录超时"),
    SYSTEM_ERROR(5000,"系统异常"),
    OPERATE_FAILED(9001,"请勿频繁操作"),
    BUSINESS_ERROR(5001,"业务处理异常");

    /**
     * 提示
     */
    private String message;
    /**
     * 状态吗
     */
    private Integer code;

    GlobalErrorCodeConstants(Integer code,String message){
      this.code=code;
      this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
