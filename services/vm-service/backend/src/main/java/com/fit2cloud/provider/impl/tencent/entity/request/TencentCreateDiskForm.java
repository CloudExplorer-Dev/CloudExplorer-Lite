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
    @Form(inputType = InputType.Text, label = "名称")
    private String diskName;
    @Form(inputType = InputType.Number, label = "磁盘大小", defaultValue = "50", attrs = "{\"min\":20,\"max\":32000,\"step\":1}")
    private Long size;
    @Form(inputType = InputType.Radio, label = "磁盘类型", defaultValue = "CLOUD_PREMIUM", textField = "name", valueField = "id", method = "getDiskTypes", clazz = TencentCloudProvider.class)
    private String diskType;
    @Form(inputType = InputType.Radio, label = "文件系统", required = false, textField = "name", valueField = "id", method = "getFileSystemType", clazz = TencentCloudProvider.class)
    private String fileSystemType;
    @Form(inputType = InputType.Text, label = "目录", required = false)
    private String mountPoint;
    @Form(inputType = InputType.Radio, label = "随实例删除", defaultValue = "NO", textField = "name", valueField = "id", method = "getDeleteWithInstance", clazz = TencentCloudProvider.class)
    private String deleteWithInstance;
}
