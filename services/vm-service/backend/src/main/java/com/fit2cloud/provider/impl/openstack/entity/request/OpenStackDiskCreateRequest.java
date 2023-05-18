package com.fit2cloud.provider.impl.openstack.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.provider.impl.openstack.OpenStackCloudProvider;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class OpenStackDiskCreateRequest extends OpenStackBaseRequest {

    // private String zone;

    @Form(inputType = InputType.Text, label = "名称", propsInfo = "{\"style\":{\"width\":\"100%\"}}")
    private String diskName;

    @Form(inputType = InputType.SingleSelect,
            required = false,
            label = "磁盘类型",
            textField = "name",
            valueField = "name",
            propsInfo = "{\"radioType\":\"radio\",\"style\":{\"width\":\"100%\"}}",
            method = "listVolumeType",
            clazz = OpenStackCloudProvider.class)
    private String diskType;

    @Form(inputType = InputType.IntNumber,
            label = "磁盘大小",
            defaultValue = "20",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":32728,\"step\":10}",
            propsInfo = "{\"style\":{\"width\":\"120px\"}}",
            unit = "GB")
    private int size;

    @Form(inputType = InputType.Text, label = "描述", required = false)
    private String description;

    private String instanceUuid;

    //private String deleteWithInstance; //不支持

}
