package com.fit2cloud.common.form.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  9:08 AM
 * @Version 1.0
 * @注释: 输入框 文本
 */
@NoArgsConstructor
@Getter
@Setter
public class LabelTextForm extends Form {
    /**
     * 字段名称
     */
    private String field;

    /**
     * 字段值
     */
    private String fieldValue;

}
