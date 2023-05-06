package com.fit2cloud.common.form.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/6  17:02}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class TableRadioForm extends Form {
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
