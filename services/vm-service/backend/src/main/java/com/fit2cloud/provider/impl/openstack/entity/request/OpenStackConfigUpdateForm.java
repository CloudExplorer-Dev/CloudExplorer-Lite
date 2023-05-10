package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.openstack.OpenStackCloudProvider;
import lombok.Data;


@Data
public class OpenStackConfigUpdateForm {
    @Form(inputType = InputType.TableRadio,
            label = "实例规格",
            textField = "instanceTypeDesc",
            baseTextField = "instanceTypeDesc",
            formatTextField = true,
            valueField = "id",
            propsInfo = "{\"activeTextEval\":\"`${row['name']}(${row['instanceTypeDesc']})`\",\"rules\":[{\"message\":\"实例规格不能为空\",\"trigger\":\"change\",\"required\":true}],\"style\":{\"width\":\"100%\",\"height\":\"450px\"},\"showLabel\":false,\"activeMsg\":\"已选实例\",\"title\":\"选择变更实例\",\"tableColumns\":[{\"property\":\"name\",\"label\":\"规格类型\"},{\"property\":\"instanceTypeDesc\",\"label\":\"实例规格\"},{\"property\":\"name\",\"innerHtml\":\"`${row['name']}(${row['instanceTypeDesc']})`\",\"label\":\"规格详情\",\"min-width\":\"120px\"}]}",
            method = "getInstanceTypesForConfigUpdate",
            clazz = OpenStackCloudProvider.class)
    private String newInstanceType;
}
