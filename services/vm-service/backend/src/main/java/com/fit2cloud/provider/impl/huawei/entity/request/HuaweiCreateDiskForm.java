package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/2 4:15 PM
 */
@Data
public class HuaweiCreateDiskForm {
    @Form(inputType = InputType.Text, label = "名称")
    private String diskName;
    @Form(inputType = InputType.Number, label = "磁盘大小", defaultValue = "10", attrs = "{\"min\":10,\"max\":32728,\"step\":10}")
    private Long size;
    @Form(inputType = InputType.Radio, label = "磁盘类型", defaultValue = "GPSSD", textField = "name", valueField = "id", method = "getDiskTypes", clazz = HuaweiCloudProvider.class)
    private String diskType;
    @Form(inputType = InputType.Radio, label = "随实例删除", defaultValue = "YES", textField = "name", valueField = "id", method = "getDeleteWithInstance", clazz = HuaweiCloudProvider.class)
    private String deleteWithInstance;
}
