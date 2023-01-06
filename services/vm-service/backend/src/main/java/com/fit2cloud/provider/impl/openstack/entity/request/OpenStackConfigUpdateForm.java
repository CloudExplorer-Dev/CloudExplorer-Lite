package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.openstack.OpenStackCloudProvider;
import lombok.Data;


@Data
public class OpenStackConfigUpdateForm {
    @Form(inputType = InputType.SingleSelect,
            label = "实例规格",
            //textField = "instanceTypeDesc",
            baseTextField = "instanceTypeDesc",
            textField = "${name} <span style=\"float: right; color: var(--el-text-color-secondary); font-size: smaller\">[${instanceTypeDesc}]</span>",
            formatTextField = true,
            valueField = "id",
            method = "getInstanceTypesForConfigUpdate",
            clazz = OpenStackCloudProvider.class)
    private String newInstanceType;
}
