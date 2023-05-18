package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/23 14:22
 */
@Data
public class VsphereConfigUpdateForm {
    // cpu核数
    @Form(inputType = InputType.IntNumber,
            label = "CPU",
            unit = "核",
            group = 3,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":128,\"step\":1}",
            propsInfo = "{\"style\":{\"width\":\"140px\"}}",
            confirmGroup = 1
    )
    private int cpu;

    // 内存GB
    @Form(inputType = InputType.IntNumber,
            label = "内存",
            unit = "GB",
            group = 3,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":512,\"step\":1}",
            propsInfo = "{\"style\":{\"width\":\"140px\"}}",
            confirmGroup = 1
    )
    private int memory;
}
