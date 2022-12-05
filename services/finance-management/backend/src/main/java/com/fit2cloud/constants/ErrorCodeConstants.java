package com.fit2cloud.constants;

import com.fit2cloud.common.utils.LocaleUtil;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * {@code  @Author:张少虎}
 * {@code  @Date: 2022/8/30  2:36 PM}
 * {@code  @Version 1.0}
 * {@code  @注释: 模块-菜单-操作按钮}
 * 账单模块 3000000
 * - 账单总览:      300100000
 * - 账单明细:      300200000
 * - 自定义账单:    300300000
 * - 分账设置:      300400000
 * - provider异常  300000000
 * - 系统其他异常    300500000
 */
public enum ErrorCodeConstants {
    BILL_VIEW_DATE_FORMAT(300100001, "时间格式不正确"),

    BILL_VIEW_UNSUPPORTED_GROUP_FIELD(300100002, "不支持的分组字段{0}"),

    BILL_RULE_UNSUPPORTED_GROUP_KEY(300300001, "不支持{0}分组key"),

    BILL_RULE_GRT_CHILD_GROUP_KEY(300300001, "获取账单子字段发生异常{0}"),

    BILL_DIMENSION_SETTING_NOT_SUPPORT_AUTHORIZE_FIELD(300400001, "不支持的授权字段{0}"),

    BILL_DIMENSION_SETTING_AUTHORIZE_ID_NOT_EXIST(300400002, "授权id不存在"),

    BILL_DIMENSION_SETTING_AUTHORIZE_ERROR(300400002, "账单授权失败"),

    BILL_SYNC_REQUIRED_PARAMS_NOT_EXIST(300500001, "缺少必要参数{0}"),

    BILL_CSV_READ_FILE_FORMAT_UTF8(300500002, "错误的UTF-8格式文件"),

    BILL_PROVIDER_ALI_CLOUD_BUCKET_NOT_EXIST(300000001, "不存在的桶"),

    BILL_PROVIDER_TENCENT_CLOUD_KEY_NOT_EXIST(300000101, "不存在的字段");

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
