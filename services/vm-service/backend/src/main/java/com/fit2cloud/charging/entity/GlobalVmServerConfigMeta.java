package com.fit2cloud.charging.entity;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/7  10:42}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalVmServerConfigMeta {

    /**
     * 关机不计费
     */
    @Form(label = "关机不计费", inputType = InputType.SwitchBtn, defaultJsonValue = true, defaultValue = "true", propsInfo = "{\"style\":{\"width\":\"40px\"},\"elFormItemStyle\":{\"width\":\"120px\"}}")
    private boolean shutdownNotBilling;
}
