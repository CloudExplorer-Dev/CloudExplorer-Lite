package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.aliyun.AliyunCloudProvider;
import com.fit2cloud.vm.entity.request.RenewInstanceRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/11  15:35}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class AliRenewInstanceRequest extends RenewInstanceRequest {

    @Form(inputType = InputType.SingleSelect,
            label = "续费时长",
            defaultValue = "1week",
            required = false,
            method = "getPeriodOption",
            textField = "periodDisplayName",
            valueField = "period",
            clazz = AliyunCloudProvider.class,
            propsInfo = "{\"style\":{\"width\":\"120px\"}}"
    )
    private String periodNum;

}
