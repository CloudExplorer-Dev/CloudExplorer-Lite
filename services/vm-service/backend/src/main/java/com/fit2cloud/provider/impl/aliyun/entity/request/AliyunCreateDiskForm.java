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
    @Form(inputType = InputType.Text, label = "名称")
    private String diskName;
    @Form(inputType = InputType.Text, label = "描述", required = false)
    private String description;
    @Form(inputType = InputType.Number, label = "磁盘大小", defaultValue = "50", attrs = "{\"min\":40,\"max\":32768,\"step\":1}",unit = "GB")
    private Long size;
    @Form(inputType = InputType.Radio, label = "磁盘类型", defaultValue = "cloud_ssd", textField = "name", valueField = "id", method = "getDiskTypes", clazz = AliyunCloudProvider.class)
    private String diskType;
    @Form(inputType = InputType.Radio, label = "随实例删除", defaultValue = "YES", textField = "name", valueField = "id", method = "getDeleteWithInstance", clazz = AliyunCloudProvider.class)
    private String deleteWithInstance;
    /**
     * ESSD 云盘的性能等级,创建时给默认值，前端暂不显示
     */
    private String performanceLevel;
}
