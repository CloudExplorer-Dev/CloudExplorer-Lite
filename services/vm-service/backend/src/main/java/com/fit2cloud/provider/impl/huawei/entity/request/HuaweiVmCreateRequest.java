package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.annotaion.FormConfirmInfo;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.provider.impl.huawei.HuaweiBaseCloudProvider;
import com.fit2cloud.provider.ICreateServerRequest;
import com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider;
import com.fit2cloud.provider.impl.huawei.entity.DiskConfig;
import com.fit2cloud.service.impl.VmCloudImageServiceImpl;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FormStepInfo(step = 1, name = "基础配置")
@FormStepInfo(step = 2, name = "网络配置")
@FormStepInfo(step = 3, name = "系统配置")
@FormConfirmInfo(group = 0, name = "云账号")
@FormConfirmInfo(group = 1, name = "基础配置")
@FormConfirmInfo(group = 2, name = "网络配置")
@FormConfirmInfo(group = 3, name = "系统配置")
@FormGroupInfo(group = 1, name = "付费方式")
@FormGroupInfo(group = 2, name = "区域")
@FormGroupInfo(group = 3, name = "操作系统")
@FormGroupInfo(group = 4, name = "实例规格")
@FormGroupInfo(group = 5, name = "磁盘配置")
@FormGroupInfo(group = 6, name = "网络与安全")
@FormGroupInfo(group = 7, name = "公网IP",description="可以公网访问的公网IP")
@FormGroupInfo(group = 8, name = "登录凭证")
@FormGroupInfo(group = 9, name = "主机命名")
public class HuaweiVmCreateRequest extends HuaweiBaseRequest implements ICreateServerRequest {

    @Form(inputType = InputType.Number,
            label = "购买数量",
            unit = "台",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":10,\"step\":1}",
            confirmGroup = 1
    )
    private int count;

    @Form(inputType = InputType.Number,
            label = "购买时长",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":9,\"step\":1}",
            confirmGroup = 1

    )
    private String periodNum;

    @Form(inputType = InputType.SingleSelect,
            label = "周期类型",
            clazz = HuaweiCloudProvider.class,
            method = "getPeriodType",
            defaultValue = "month",
            attrs = "{\"style\":\"width:120px\"}",
            textField = "name",
            valueField = "id",
            confirmGroup = 1

    )
    private String periodType;

    private int index;

    private String id;

    @Form(inputType = InputType.Radio,
            label = "计费模式",
            clazz = HuaweiCloudProvider.class,
            method = "getBillingMode",
            defaultValue = "0",
            textField = "name",
            valueField = "id",
            step = 1,
            group = 1
    )
    private String billingMode;

    //step 1
    @Form(inputType = InputType.SingleSelect,
            label = "区域",
            defaultValue = "cn-south-1",
            clazz = HuaweiBaseCloudProvider.class,
            method = "getRegions",
            textField = "name",
            valueField = "regionId",
            group = 2,
            step = 1
    )
    private String region;

    //集群
    @Form(inputType = InputType.Radio,
            label = "可用区",
            clazz = HuaweiCloudProvider.class,
            defaultValue = "random",
            method = "getAvailabilityZone",
            textField = "displayName",
            valueField = "zoneName",
            relationTrigger = {"region", "billingMode"},
            group = 2,
            step = 1
    )
    private String availabilityZone;

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统",
            clazz = VmCloudImageServiceImpl.class,
            serviceMethod = true,
            method = "listOs",
            textField = "name",
            valueField = "id",
            relationTrigger = "availabilityZone",
            group = 3,
            step = 1,
            confirmGroup = 1
    )
    private String os;

    //镜像
    @Form(inputType = InputType.SingleSelect,
            label = "镜像名称",
            clazz = VmCloudImageServiceImpl.class,
            serviceMethod = true,
            method = "listVmCloudImageByOs",
            textField = "imageName",
            valueField = "imageName", //由于vc还是拿name作为快速索引，所以不用mor作为查询值
            relationTrigger = "os",
            group = 3,
            step = 1,
            confirmGroup = 1
    )
    private String template;

    @Form(inputType = InputType.HuaweiInstanceSpecForm,
            description = "购买数量为多台时，实例规格一样，若想要不同实例规格需要分多次申请。",
            defaultJsonValue = true,
            confirmSpecial = true,
            relationTrigger = {"availabilityZone","billingMode"},
            group = 4,
            step = 1,
            confirmGroup = 1
    )
    private String instanceSpecConfig;

    //磁盘配置
    @Form(inputType = InputType.HuaweiDiskConfigForm,
            step = 1,
            group = 5,
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = {"availabilityZone","billingMode"},
            confirmGroup = 1,
            confirmSpecial = true
    )
    private List<DiskConfig> disks;

    public void setRegion(String region) {
        setRegionId(region);
    }

}
