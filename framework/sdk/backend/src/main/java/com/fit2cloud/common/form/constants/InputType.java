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

    IntNumber(InputNumberForm.class),
    /**
     * 密码输入框
     */
    Password(PasswordForm.class),
    /**
     * 文本
     */
    Text(InputTextForm.class),
    /**
     * 单选框 (按钮)
     */
    Radio(RadioForm.class),
    /**
     * 单选框（原生）
     */
    RadioRaw(RadioForm.class),
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
     * 描述
     */
    LabelText(LabelTextForm.class),

    /**
     * 开关
     */
    SwitchBtn(SwitchForm.class),
    /**
     * 表格单选
     */
    TableRadio(TableRadioForm.class),
    /**
     * 实时验证正则组密码输入框
     */
    RegexInput(RegexInputForm.class),

    /**
     * label与单位同一行的数字框
     */
    LineNumber(LineInputNumberForm.class),

    /**
     * 表格单选
     */
    TableCheckbox(TableCheckboxForm.class),
    /**
     * 磁盘
     */
    DiskConfig(DiskConfigForm.class),
    /**
     * 用于收集 Object类型数据
     */
    BaseObjectContainer(BaseContainerForm.class),
    /**
     * 用于收集List<Object>类型数据
     */
    BaseArrayContainer(BaseArrayContainerForm.class),

    /**
     * 用于收集 表单随创建数量增加而增加的Tab数据
     */
    BaseCountTabContainer(BaseCountTabContainerForm.class),
    /**
     * 用于收集 表单随创建数量增加而增加的数据
     */
    BaseCountContainer(BaseCountContainerForm.class),

    LabelTextInput(InputTextForm.class),

    AliyunInstanceTypeForm(com.fit2cloud.common.form.vo.aliyun.AliyunInstanceType.class),

    AliyunDiskConfigForm(com.fit2cloud.common.form.vo.aliyun.AliyunDiskConfig.class),

    AliyunNetConfigForm(com.fit2cloud.common.form.vo.aliyun.AliyunNetConfig.class),

    AliyunServerInfoForm(com.fit2cloud.common.form.vo.aliyun.AliyunServerInfoForm.class),

    TencentInstanceTypeForm(com.fit2cloud.common.form.vo.tencent.TencentInstanceType.class),

    TencentDiskConfigForm(com.fit2cloud.common.form.vo.tencent.TencentDiskConfig.class),

    TencentNetConfigForm(com.fit2cloud.common.form.vo.tencent.TencentNetConfig.class),

    TencentServerInfoForm(com.fit2cloud.common.form.vo.tencent.TencentServerInfoForm.class),

    HuaweiOsSingleSelectForm(com.fit2cloud.common.form.vo.huawei.OsConfigForm.class),

    HuaweiInstanceSpecForm(com.fit2cloud.common.form.vo.huawei.InstanceSpecForm.class),

    HuaweiDiskConfigForm(com.fit2cloud.common.form.vo.huawei.DiskConfigForm.class),

    HuaweiNetworkConfigForm(com.fit2cloud.common.form.vo.huawei.NetworkConfigForm.class),

    HuaweiNameConfigForm(com.fit2cloud.common.form.vo.huawei.NameConfigForm.class),

    HuaweiPasswordInfoForm(com.fit2cloud.common.form.vo.huawei.HuaweiPasswordInfoForm.class),

    VsphereClusterForm(com.fit2cloud.common.form.vo.vsphere.ClusterForm.class),

    VsphereTemplateSelectForm(com.fit2cloud.common.form.vo.vsphere.TemplateForm.class),

    VsphereDiskConfigForm(com.fit2cloud.common.form.vo.vsphere.DiskConfigForm.class),

    VsphereComputeConfigForm(com.fit2cloud.common.form.vo.vsphere.ComputeConfigForm.class),

    VsphereDatastoreForm(com.fit2cloud.common.form.vo.vsphere.DatastoreForm.class),

    VsphereNetworkAdapterForm(com.fit2cloud.common.form.vo.vsphere.NetworkAdapterForm.class),

    VspherePasswordInfoForm(com.fit2cloud.common.form.vo.vsphere.PasswordInfoForm.class),

    VsphereServerInfoForm(com.fit2cloud.common.form.vo.vsphere.ServerInfoForm.class),

    OpenStackFlavorForm(com.fit2cloud.common.form.vo.openstack.FlavorForm.class),

    OpenStackDiskConfigForm(com.fit2cloud.common.form.vo.openstack.DiskConfigForm.class),

    OpenStackServerInfoForm(com.fit2cloud.common.form.vo.openstack.ServerInfoForm.class),

    OpenStackNetworkConfigForm(com.fit2cloud.common.form.vo.openstack.NetworkConfigForm.class),

    OpenStackSecurityGroupConfigForm(com.fit2cloud.common.form.vo.openstack.SecurityGroupConfigForm.class);

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
