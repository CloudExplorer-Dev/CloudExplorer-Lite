package com.fit2cloud.common.form.vo;

import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  9:28 AM
 * @Version 1.0
 * @注释: 输入框number
 */
@Data
public class InputNumberForm extends Form {
    /**
     * 字段名称
     */
    private String field;

    private String unit;

}
