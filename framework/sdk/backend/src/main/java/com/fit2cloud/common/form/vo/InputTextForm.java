package com.fit2cloud.common.form.vo;

import com.fit2cloud.common.form.constants.InputType;
import lombok.*;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  9:08 AM
 * @Version 1.0
 * @注释: 输入框 文本
 */
@NoArgsConstructor
@Getter
@Setter
public class InputTextForm extends Form {
    /**
     * 字段名称
     */
    private String field;

}
