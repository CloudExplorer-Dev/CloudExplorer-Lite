package com.fit2cloud.common.form.constants;

import com.fit2cloud.common.form.vo.*;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  9:13 AM
 * @Version 1.0
 * @注释:
 */
public enum InputType {
    /**
     * 数字类型
     */
    number(InputNumberForm.class),
    /**
     * 密码输入框
     */
    password(PasswordForm.class),
    /**
     * 文本
     */
    text(InputTextForm.class),
    /**
     * 单选框
     */
    radio(RadioForm.class),
    /**
     * 多选框
     */
    combobox(ComboboxForm.class),
    /**
     * 下拉单选
     */
    single_select(SingleSelectForm.class),
    /**
     * 下拉多选
     */
    multi_select(MultiSelectForm.class),

    /**
     * 开关
     */
    switchBtn(SwitchForm.class);

    /**
     * 表单类型对应的实体类
     */
    private Class<? extends Form> formClass;

    InputType(Class<? extends Form> formClass) {
        this.formClass = formClass;
    }

    public Class<? extends Form> getFormClass() {
        return formClass;
    }
}
