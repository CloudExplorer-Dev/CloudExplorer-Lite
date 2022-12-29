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
    @Form(inputType = InputType.SingleSelect,
            label = "实例规格",
            textField = "instanceTypeDesc",
            valueField = "instanceType",
            method = "getInstanceTypesForConfigUpdate",
            clazz = AliyunCloudProvider.class)
    private String newInstanceType;
}
