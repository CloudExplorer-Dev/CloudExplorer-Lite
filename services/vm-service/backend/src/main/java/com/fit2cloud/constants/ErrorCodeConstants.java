package com.fit2cloud.constants;

import com.fit2cloud.common.utils.LocaleUtil;
import org.springframework.lang.Nullable;

import java.util.List;

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
    USER_CAN_NOT_DELETE(100100101, "不能删除当前登录用户"),
    USER_NOT_EXIST(100100102, "用户不存在"),
    USER_NOT_LOCAL_CAN_NOT_EDIT_PASSWORD(100100103, "非本地用户不能修改密码"),
    USER_KEEP_ONE_ADMIN(100100104, "至少保留一个系统管理员用户"),
    SYSTEM_NOT_HAVE_ADMIN(100100105, "更改此用户角色，系统将无默认系统管理员"),
    USER_ID_CANNOT_BE_NULL(100100106, "用户ID不能为空"),
    USER_NAME_CANNOT_BE_NULL(100100107, "用户姓名不能为空"),
    USER_PWD_CANNOT_BE_NULL(100100108, "密码不能为空"),
    USER_EMAIL_CANNOT_BE_NULL(100100109, "邮箱不能为空"),
    USER_ID_CANNOT_BE_SYSTEM(100100109, "用户ID不能为system"),

    ORGANIZATION_CANNOT_DELETE(10010, "i18n.organization.cannot.delete.have.parent"),
    ORGANIZATION_ID_AND_NAME_REQUIRED(100011, "i18n.organization.id.or.name.required"),
    ORGANIZATION_NAME_REPEAT(100012, "i18n.organization.name.not.repeat"),
    ORGANIZATION_UPDATE_NOT_THIS_CHILD(100013, "i18n.organization.pid.not.mv.to.this_child"),

    CLOUD_ACCOUNT_NOT_SUPPORT_PLATFORM(100100, "i18n.cloud_account.not_support.platform"),

    CLOUD_ACCOUNT_JOB_IS_NOT_EXISTENT(100101, "i18n.cloud_account.job.is.not.existent"),

    WORKSPACE_ID_AND_NAME_REQUIRED(100100401, "i18n.workspace.id.or.name.required"),

    WORKSPACE_NAME_REPEAT(100100402, "i18n.workspace.name.not.repeat"),

    WORKSPACE_IS_NOT_EXIST(100100403, "i18n.workspace.is.not.empty"),

    BASE_ROLE_CANNOT_DELETE(100100404, "i18n.role.base.warn.cannot.delete"),

    ROLE_ID_CANNOT_BE_NULL(100100405, "i18n.role.id.warn.cannot.null"),

    VM_NOT_EXIST(200100101, "云主机不存在"),
    VM_OPERATE_FAIL(200100102, "云主机操作失败"),
    VM_BATCH_OPERATE_FAIL(200100103, "云主机批量操作失败"),
    SELECT_AT_LEAST_ONE_VM(200100104,"至少选择一个云主机"),
    NOT_SUPPORTED_TEMPORARILY(200100105, "暂不支持该操作"),;



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
