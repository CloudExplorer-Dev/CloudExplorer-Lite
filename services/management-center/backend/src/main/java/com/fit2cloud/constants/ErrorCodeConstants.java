package com.fit2cloud.constants;

import com.fit2cloud.common.utils.LocaleUtil;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  2:36 PM
 * @Version 1.0
 * @注释: 管理中心服务-错误码前缀为 100
 * 菜单错误码规则：服务-模块-菜单-自定义错误码
 * 管理中心-租户与用户 100100 管理中心-云账号管理 100200 模块步长100
 * 管理中心-租户与用户-用户管理 10010010 管理中心-租户与用户-角色管理 10010020  菜单步长10
 * 管理中心-租户与用户-用户管理-查询错误 100100101
 */
public enum ErrorCodeConstants {
    USER_CAN_NOT_DELETE(100100101, "i18n.user.cannot.delete"),
    USER_NOT_EXIST(100100102, "i18n.user.not.exist"),
    USER_NOT_LOCAL_CAN_NOT_EDIT_PASSWORD(100100103, "i18n.user.cannot.edit.pwd"),
    USER_KEEP_ONE_ADMIN(100100104, "i18n.user.at.least.has.admin"),
    SYSTEM_NOT_HAVE_ADMIN(100100105, "i18n.system.not.has.admin"),
    USER_ID_CANNOT_BE_SYSTEM(100100109, "i18n.user.id.cannot.be.system"),

    USER_EMAIL_WARN_DUPLICATED(100100110, "i18n.user.email.warn.duplicated"),

    ORGANIZATION_CANNOT_DELETE(10010, "i18n.organization.cannot.delete.have.parent"),
    ORGANIZATION_ID_AND_NAME_REQUIRED(100011, "i18n.organization.id.or.name.required"),
    ORGANIZATION_NAME_REPEAT(100012, "i18n.organization.name.not.repeat"),
    ORGANIZATION_UPDATE_NOT_THIS_CHILD(100013, "i18n.organization.pid.not.mv.to.this_child"),

    CLOUD_ACCOUNT_NOT_SUPPORT_PLATFORM(100100, "i18n.cloud_account.not_support.platform"),

    CLOUD_ACCOUNT_JOB_IS_NOT_EXISTENT(100101, "i18n.cloud_account.job.is.not.existent"),
    CLOUD_ACCOUNT_INVALID_UNABLE_SYNC(100102,"i18n.cloud_account.invalid.unable.sync"),
    WORKSPACE_ID_AND_NAME_REQUIRED(100100401, "i18n.workspace.id.or.name.required"),

    WORKSPACE_NAME_REPEAT(100100402, "i18n.workspace.name.not.repeat"),

    WORKSPACE_IS_NOT_EXIST(100100403, "i18n.workspace.is.not.empty"),

    BASE_ROLE_CANNOT_DELETE(100100404, "i18n.role.base.warn.cannot.delete"),

    ROLE_ID_CANNOT_BE_NULL(100100405, "i18n.role.id.warn.cannot.null");


    /**
     * 提示
     */
    private final String message;
    /**
     * 状态吗
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
     * 获取错误提示
     *
     * @param args 错误提示参数
     * @return 错误提示文本
     */
    public String getMessage(Object[] args) {
        return LocaleUtil.getMessage(message, args, message);
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
