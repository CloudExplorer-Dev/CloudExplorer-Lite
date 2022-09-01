package com.fit2cloud.constants;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  2:36 PM
 * @Version 1.0
 * @注释:
 */
public enum ErrorCodeConstants {
    ORGANIZATION_CANNOT_DELETE(10010, "i18n.organization.cannot.delete.have.parent"),
    ORGANIZATION_ID_AND_NAME_REQUIRED(100011, "i18n.organization.id.or.name.required"),
    ORGANIZATION_NAME_REPEAT(100012, "i18n.organization.name.not.repeat"),
    ORGANIZATION_UPDATE_NOT_THIS_CHILD(100013,"i18m.organization.pid.not.mv.to.this_child");

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
