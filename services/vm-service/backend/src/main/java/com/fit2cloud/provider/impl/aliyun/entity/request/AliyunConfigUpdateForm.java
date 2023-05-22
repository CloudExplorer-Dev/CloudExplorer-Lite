package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.aliyun.AliyunCloudProvider;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/22 15:58
 */
@Data
public class AliyunConfigUpdateForm {
    @Form(inputType = InputType.TableRadio,
            label = "实例规格",
            textField = "instanceTypeDesc",
            valueField = "instanceType",
            method = "getInstanceTypesForConfigUpdate",
            clazz = AliyunCloudProvider.class,
            propsInfo = "{\"style\":{\"width\":\"100%\",\"height\":\"450px\"},\"showLabel\":false,\"activeMsg\":\"已选实例\",\"title\":\"选择变更实例\",\"tableColumns\":[{\"property\":\"instanceType\",\"label\":\"规格类型\"},{\"property\":\"cpuMemory\",\"label\":\"实例规格\"},{\"property\":\"instanceTypeDesc\",\"label\":\"规格详情\",\"min-width\":\"120px\"}]}"
    )
    private String newInstanceType;
}
