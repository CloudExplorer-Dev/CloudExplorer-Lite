package com.fit2cloud.constants;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  2:36 PM
 * @Version 1.0
 * @注释:
 * 管理中心服务-错误码前缀为 100
 * 菜单错误码规则：服务-模块-菜单-自定义错误码
 * 管理中心-租户与用户 100100 管理中心-云账号管理 100200 模块步长100
 * 管理中心-租户与用户-用户管理 10010010 管理中心-租户与用户-角色管理 10010020  菜单步长10
 * 管理中心-租户与用户-用户管理-查询错误 100100101
 */
public enum ErrorCodeConstants {
    USER_CAN_NOT_DELETE(100100101,"不能删除当前登录用户"),
    USER_NOT_EXIST(100100102,"用户不存在"),

    ORGANIZATION_CANNOT_DELETE(10010, "i18n.organization.cannot.delete.have.parent"),
    ORGANIZATION_ID_AND_NAME_REQUIRED(100011, "i18n.organization.id.or.name.required"),
    ORGANIZATION_NAME_REPEAT(100012, "i18n.organization.name.not.repeat"),
    ORGANIZATION_UPDATE_NOT_THIS_CHILD(100013,"i18m.organization.pid.not.mv.to.this_child"),

    WORKSPACE_ID_AND_NAME_REQUIRED(100100401, "工作空间ID和名称必须传一个"),
    WORKSPACE_NAME_REPEAT(100100402, "工作空间名称不能重复"),
    WORKSPACE_IS_NOT_EXIST(100100403, "工作空间不存在");
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
