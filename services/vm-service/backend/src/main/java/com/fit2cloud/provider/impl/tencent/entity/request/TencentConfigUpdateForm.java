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
    @Form(inputType = InputType.SingleSelect,
            label = "实例规格",
            textField = "instanceTypeDesc",
            valueField = "instanceType",
            method = "getInstanceTypesForConfigUpdate",
            clazz = TencentCloudProvider.class)
    private String newInstanceType;
}
