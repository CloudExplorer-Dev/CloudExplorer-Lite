package com.fit2cloud.constants;

import com.fit2cloud.common.utils.LocaleUtil;

/**
 * 管理中心服务-错误码前缀为 100
 * 菜单错误码规则：服务-模块-菜单-自定义错误码
 * 管理中心-租户与用户 100100 管理中心-云账号管理 100200 模块步长100
 * 管理中心-租户与用户-用户管理 10010010 管理中心-租户与用户-角色管理 10010020  菜单步长10
 * 管理中心-租户与用户-用户管理-查询错误 100100101
 */
public enum ErrorCodeConstants {

    DEMO_ERROR(99999, "DEMO测试错误");

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
