package com.fit2cloud.common.form.vo;

import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  9:31 AM
 * @Version 1.0
 * @注释: 多选
 */
@Data
public class ComboboxForm extends Form {
    /**
     * 值
     */
    private String valueField;
    /**
     * label 提示
     */
    private String textField;
    /**
     * 获取options数据
     */
    private String method;
}
