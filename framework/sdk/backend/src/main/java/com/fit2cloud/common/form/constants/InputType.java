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
    Number(InputNumberForm.class),
    /**
     * 密码输入框
     */
    Password(PasswordForm.class),
    /**
     * 文本
     */
    Text(InputTextForm.class),
    /**
     * 单选框
     */
    Radio(RadioForm.class),
    /**
     * 多选框
     */
    Combobox(ComboboxForm.class),
    /**
     * 下拉单选
     */
    SingleSelect(SingleSelectForm.class),
    /**
     * 下拉多选
     */
    MultiSelect(MultiSelectForm.class),

    /**
     * 开关
     */
    SwitchBtn(SwitchForm.class),


    VsphereDiskConfigForm(com.fit2cloud.common.form.vo.vsphere.DiskConfigForm.class),
    VsphereComputeConfigForm(com.fit2cloud.common.form.vo.vsphere.ComputeConfigForm.class),
    VsphereDatastoreForm(com.fit2cloud.common.form.vo.vsphere.DatastoreForm.class),
    VsphereNetworkAdapterForm(com.fit2cloud.common.form.vo.vsphere.NetworkAdapterForm.class),
    VsphereServerInfoForm(com.fit2cloud.common.form.vo.vsphere.ServerInfoForm.class);

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
