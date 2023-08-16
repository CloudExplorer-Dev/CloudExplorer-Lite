package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.tencent.TencentCloudProvider;
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
public class TencentRenewInstanceRequest extends RenewInstanceRequest {

    @Form(inputType = InputType.SingleSelect,
            label = "续费时长",
            required = false,
            method = "getPeriodOption",
            textField = "periodDisplayName",
            valueField = "period",
            defaultValue = "1",
            propsInfo = "{\"style\":{\"width\":\"120px\"}}",
            defaultJsonValue = true,
            clazz = TencentCloudProvider.class
    )
    private String periodNum;


}
