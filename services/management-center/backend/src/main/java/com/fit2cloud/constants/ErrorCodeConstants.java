package com.fit2cloud.constants;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  2:36 PM
 * @Version 1.0
 * @注释:
 */
public enum ErrorCodeConstants {
    ORGANIZATION_CANNOT_DELETE(10010, "不能删除一个父级组织,请先删除子组织"),
    ORGANIZATION_ID_AND_NAME_REQUIRED(100011, "组织和名称必须传一个"),
    ORGANIZATION_NAME_REPEAT(100012, "组织名称不能重复"),

    ORGANIZATION_UPDATE_NOT_THIS_CHILD(100013,"组织修改不能修改到自己到子组织");

    /**
     * 提示
     */
    private String message;
    /**
     * 状态吗
     */
    private Integer code;

    ErrorCodeConstants(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
