package com.fit2cloud.common.es.constants;

import com.fit2cloud.common.utils.LocaleUtil;

/**
 * @author jianneng
 * @date 2022/9/22 14:02
 **/
public enum EsErrorCodeConstants {

    INDEX_NOT_EXIST(300100, "索引不存在"),

    SEARCH_FAILED(300101, "搜索失败"),

    SEARCH_TOTAL_FAILED(300102, "搜索数据总数失败");
    /**
     * 提示
     */
    private final String message;
    /**
     * 状态吗
     */
    private final Integer code;

    EsErrorCodeConstants(Integer code, String message) {
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
