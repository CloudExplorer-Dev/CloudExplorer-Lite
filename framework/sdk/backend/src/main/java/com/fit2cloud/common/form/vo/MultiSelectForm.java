package com.fit2cloud.common.form.vo;

import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  9:18 AM
 * @Version 1.0
 * @注释: 下拉多选
 */
@Data
public class MultiSelectForm extends Form {
    /**
     * 值
     */
    private String valueField;
    /**
     * label 提示
     */
    private String textField;

    private String baseTextField;

    private boolean formatTextField;
    /**
     * 获取options数据
     */
    private String method;

}
