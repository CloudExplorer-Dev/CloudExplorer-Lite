package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/2 4:15 PM
 */
@Data
public class VsphereCreateDiskForm {
    @Form(inputType = InputType.Radio, label = "磁盘格式", propsInfo = "{\"radioType\":\"radio\"}", defaultValue = "THIN", textField = "name", valueField = "id", method = "getDiskTypesForCreateDisk", clazz = VsphereCloudProvider.class)
    private String diskType;
    @Form(inputType = InputType.Radio, label = "磁盘模式", propsInfo = "{\"radioType\":\"radio\"}", defaultValue = "independent_persistent", textField = "name", valueField = "id", method = "getDiskModes", clazz = VsphereCloudProvider.class)
    private String diskMode;
    @Form(inputType = InputType.IntNumber, label = "磁盘大小", defaultValue = "10", attrs = "{\"min\":10,\"max\":32728,\"step\":10,\"v-number\":{\"min\":10,\"max\":32728}}", propsInfo = "{\"style\":{\"width\":\"120px\"}}", unit = "GB")
    private Long size;
    @Form(inputType = InputType.Radio, label = "存储器", defaultValue = "only-a-flag", textField = "name", valueField = "id", method = "getDatastoreTypes", clazz = VsphereCloudProvider.class)
    private String datastoreType;
    @Form(inputType = InputType.VsphereDatastoreForm, label = "存储器", propsInfo = "{\"showLabel\":false,\"errMsg\":\"请选择存储器\",\"style\":{\"width\":\"100%\"}}", relationShows = "datastoreType", relationShowValues = "customize", method = "getDatastoreListByVm", clazz = VsphereCloudProvider.class)
    private String datastore;

}
