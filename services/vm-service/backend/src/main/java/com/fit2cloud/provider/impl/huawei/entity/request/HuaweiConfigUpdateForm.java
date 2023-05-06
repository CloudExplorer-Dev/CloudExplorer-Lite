package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/23 10:40
 */
@Data
public class HuaweiConfigUpdateForm {
    @Form(inputType = InputType.TableRadio,
            label = "实例规格",
            textField = "instanceTypeDesc",
            valueField = "specName",
            method = "getInstanceTypesForConfigUpdate",
            propsInfo = "{\"rules\":[{\"message\":\"实例规格不能为空\",\"trigger\":\"change\",\"required\":true}],\"style\":{\"width\":\"100%\",\"height\":\"450px\"},\"showLabel\":false,\"activeMsg\":\"已选实例\",\"title\":\"选择变更实例\",\"tableColumns\":[{\"property\":\"specName\",\"label\":\"规格类型\"},{\"property\":\"instanceSpec\",\"label\":\"实例规格\"},{\"property\":\"instanceTypeDesc\",\"label\":\"规格详情\",\"min-width\":\"120px\"}]}",
            clazz = HuaweiCloudProvider.class)
    private String newInstanceType;
}
