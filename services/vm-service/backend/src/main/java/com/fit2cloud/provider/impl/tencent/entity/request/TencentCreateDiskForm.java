package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.tencent.TencentCloudProvider;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/2 4:15 PM
 */
@Data
public class TencentCreateDiskForm {

    @Form(inputType = InputType.Text,
            label = "名称",
            propsInfo = "{\"style\":{\"width\":\"100%\"}}")
    private String diskName;

    @Form(inputType = InputType.IntNumber,
            label = "磁盘大小",
            defaultValue = "20",
            defaultJsonValue = true,
            attrs = "{\"min\":20,\"max\":32000,\"step\":10}",
            propsInfo = "{\"style\":{\"width\":\"120px\"}}",
            unit = "GB")
    private Long size;

    @Form(inputType = InputType.Radio,
            label = "磁盘类型",
            propsInfo = "{\"radioType\":\"radio\",\"style\":{\"width\":\"100%\"}}",
            defaultValue = "CLOUD_PREMIUM",
            textField = "diskTypeName",
            valueField = "diskType",
            method = "getDiskTypesForCreateDisk",
            clazz = TencentCloudProvider.class)
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
}
