package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.aliyun.AliyunCloudProvider;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/2 4:15 PM
 */
@Data
public class AliyunCreateDiskForm {
    @Form(inputType = InputType.Text,
            propsInfo = "{\"style\":{\"width\":\"100%\"}}",
            label = "名称"
    )
    private String diskName;

    @Form(inputType = InputType.Text,
            label = "描述",
            propsInfo = "{\"style\":{\"width\":\"100%\"}}",
            required = false
    )
    private String description;

    @Form(inputType = InputType.IntNumber,
            label = "磁盘大小",
            defaultValue = "20",
            defaultJsonValue = true,
            attrs = "{\"min\":20,\"max\":32768,\"step\":10}",
            propsInfo = "{\"style\":{\"width\":\"120px\"}}",
            unit = "GB"
    )
    private Long size;

    @Form(inputType = InputType.Radio,
            label = "磁盘类型",
            propsInfo = "{\"radioType\":\"radio\",\"style\":{\"width\":\"100%\"}}",
            defaultValue = "cloud_ssd",
            textField = "name",
            valueField = "id",
            method = "getDiskTypesForCreateDisk",
            clazz = AliyunCloudProvider.class
    )
    private String diskType;

    @Form(inputType = InputType.SwitchBtn,
            label = "随实例删除",
            defaultValue = "YES",
            textField = "name",
            valueField = "id",
            attrs = "{\"inactive-value\":\"NO\",\"active-value\":\"YES\"}",
            propsInfo = "{\"style\":{\"width\":\"40px\"}}"
    )
    private String deleteWithInstance;

    /**
     * ESSD 云盘的性能等级,创建时给默认值，前端暂不显示
     */
    private String performanceLevel;
}
