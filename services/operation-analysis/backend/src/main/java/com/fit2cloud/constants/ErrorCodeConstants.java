package com.fit2cloud.constants;

import com.fit2cloud.common.utils.LocaleUtil;

/**
 * {@code  @Author:张少虎}
 * {@code  @Date: 2022/8/30  2:36 PM}
 * {@code  @Version 1.0}
 * {@code  @注释: 模块-菜单-操作按钮}
 * 运营分析模块 500000000
 * - 总览:             500100000
 * - 资源分析:          500200000
 * - 基础资源分析      500200100
 * - 云主机分析       500200200
 * - 磁盘分析分析      500200300
 * - 资源优化:          500300000
 */
public enum ErrorCodeConstants {
    GET_OPTIMIZE_SUGGEST_STRATEGY_ERROR(500300110, "获取优化策略失败"),
    NOT_EXISTS_OPTIMIZE_SUGGEST_STRATEGY(500300112, "不存在优化策略"),
    UNSUPPORTED_STRATEGY(500300111, "不支持的优化策略{0}"),
    INVALID_PARAMETER(500300113, "无效参数"),
    ;
    /**
     * 提示
     */
    private final String message;
    /**
     * 状态码
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
