package com.fit2cloud.common.constants;

/**
 * @Author:张少虎
 * @Date: 2022/8/29  5:54 PM
 * @Version 1.0
 * @注释:
 */
public enum GlobalErrorCodeConstants {
    LOGIN_TIME_OUT(9000,"登录超时");

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
