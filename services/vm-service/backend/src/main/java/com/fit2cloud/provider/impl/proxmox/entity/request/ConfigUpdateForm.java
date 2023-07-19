package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/16  22:42}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ConfigUpdateForm {
    //cpu核数
    @Form(inputType = InputType.Number,
            label = "CPU Sockets",
            leftLabel = true,
            unit = "个",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":128,\"step\":1}",
            confirmGroup = 1
    )
    private int cpuSlot;


    //内存GB
    @Form(inputType = InputType.Number,
            label = "CPU Cores",
            leftLabel = true,
            unit = "核",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":512,\"step\":1}",
            confirmGroup = 1
    )
    private int cpu;

    //内存GB
    @Form(inputType = InputType.Number,
            label = "内存",
            leftLabel = true,
            unit = "GB",
            group = 30,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":512,\"step\":1}",
            confirmGroup = 1
    )
    private int mem;
}
