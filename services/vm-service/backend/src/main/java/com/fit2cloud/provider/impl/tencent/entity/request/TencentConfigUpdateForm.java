package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.tencent.TencentCloudProvider;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/23 10:08
 */
@Data
public class TencentConfigUpdateForm {
    @Form(inputType = InputType.TableRadio,
            label = "实例规格",
            textField = "instanceTypeDesc",
            valueField = "instanceType",
            method = "getInstanceTypesForConfigUpdate",
            propsInfo = "{\"style\":{\"width\":\"100%\",\"height\":\"450px\"},\"showLabel\":false,\"activeMsg\":\"已选实例\",\"title\":\"选择变更实例\",\"tableColumns\":[{\"property\":\"instanceType\",\"label\":\"规格类型\"},{\"property\":\"cpuMemory\",\"label\":\"实例规格\"},{\"property\":\"instanceTypeDesc\",\"label\":\"规格详情\",\"min-width\":\"120px\"}]}",
            clazz = TencentCloudProvider.class)
    private String newInstanceType;
}
