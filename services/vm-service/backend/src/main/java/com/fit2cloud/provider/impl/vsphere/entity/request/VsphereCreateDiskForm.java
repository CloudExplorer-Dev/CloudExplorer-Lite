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
    @Form(inputType = InputType.Radio, label = "磁盘格式", defaultValue = "THIN", textField = "name", valueField = "id", method = "getDiskTypesForCreateDisk", clazz = VsphereCloudProvider.class)
    private String diskType;
    @Form(inputType = InputType.Radio, label = "磁盘模式", defaultValue = "independent_persistent", textField = "name", valueField = "id", method = "getDiskModes", clazz = VsphereCloudProvider.class)
    private String diskMode;
    @Form(inputType = InputType.Number, label = "磁盘大小", defaultValue = "10", attrs = "{\"min\":10,\"max\":32728,\"step\":10}",unit = "GB")
    private Long size;
    @Form(inputType = InputType.Radio, label = "存储器", defaultValue = "only-a-flag", textField = "name", valueField = "id", method = "getDatastoreTypes", clazz = VsphereCloudProvider.class)
    private String datastoreType;
    @Form(inputType = InputType.VsphereDatastoreForm, label = "", relationShows = "datastoreType", relationShowValues = "customize", method = "getDatastoreListByVm", clazz = VsphereCloudProvider.class)
    private String datastore;
}
