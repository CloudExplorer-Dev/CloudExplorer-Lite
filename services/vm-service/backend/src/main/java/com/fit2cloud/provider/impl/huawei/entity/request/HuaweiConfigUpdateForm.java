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
    @Form(inputType = InputType.SingleSelect,
            label = "实例规格",
            textField = "instanceTypeDesc",
            valueField = "specName",
            method = "getInstanceTypesForConfigUpdate",
            clazz = HuaweiCloudProvider.class)
    private String newInstanceType;
}
