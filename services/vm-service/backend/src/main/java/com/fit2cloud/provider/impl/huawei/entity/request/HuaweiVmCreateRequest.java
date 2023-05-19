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
import com.fit2cloud.provider.impl.huawei.entity.HuaweiServerNameInfo;
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
@FormGroupInfo(group = 3, name = "实例规格")
@FormGroupInfo(group = 4, name = "操作系统", inline = true)
@FormGroupInfo(group = 5, name = "磁盘配置")
@FormGroupInfo(group = 6, name = "网络与安全")
@FormGroupInfo(group = 7, name = "公网IP", description = "可以公网访问的公网IP")
@FormGroupInfo(group = 8, name = "登录凭证")
@FormGroupInfo(group = 9, name = "主机命名")
public class HuaweiVmCreateRequest extends HuaweiBaseRequest implements ICreateServerRequest {

    private int index;

    private String id;

    @Form(inputType = InputType.Radio,
            label = "付费方式",
            clazz = HuaweiCloudProvider.class,
            method = "getBillingMode",
            textField = "name",
            valueField = "id",
            defaultValue = "postPaid",
            step = 1,
            group = 1,
            confirmGroup = 1
    )
    private String billingMode;

    @Form(inputType = InputType.LineNumber,
            label = "购买数量",
            unit = "台",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":10,\"step\":1}",
            confirmGroup = 1
    )
    private int count;

    @Form(inputType = InputType.SingleSelect,
            label = "时长",
            clazz = HuaweiCloudProvider.class,
            method = "getPeriodOption",
            textField = "periodDisplayName",
            valueField = "period",
            defaultValue = "1",
            defaultJsonValue = true,
            relationShows = "billingMode",
            relationShowValues = "prePaid",
            confirmGroup = 1
    )
    private String periodNum;

    @Form(inputType = InputType.SingleSelect,
            label = "区域",
            clazz = HuaweiBaseCloudProvider.class,
            method = "getRegions",
            textField = "name",
            valueField = "regionId",
            defaultValue = "cn-south-1",
            propsInfo = "{\"style\":{\"width\":\"100%\",\"height\":\"32px\"}}",
            step = 1,
            group = 2,
            confirmGroup = 0
    )
    private String regionId;

    @Form(inputType = InputType.Radio,
            label = "可用区",
            clazz = HuaweiCloudProvider.class,
            method = "getAvailabilityZone",
            textField = "displayName",
            valueField = "zoneName",
            defaultValue = "cn-south-1a",
            relationTrigger = {"regionId", "billingMode"},
            step = 1,
            group = 2,
            confirmGroup = 0
    )
    private String availabilityZone;

    @Form(inputType = InputType.TableRadio,
            label = "实例规格",
            clazz = HuaweiCloudProvider.class,
            method = "getInstanceSpecTypes",
            textField = "instanceSpec",
            valueField = "specName",
            relationTrigger = {"availabilityZone", "billingMode"},
            propsInfo = "{\"rules\":[{\"message\":\"实例规格不能为空\",\"trigger\":\"change\",\"required\":true}],\"style\":{\"width\":\"100%\",\"height\":\"400px\"},\"showLabel\":false,\"activeMsg\":\"已选实例\",\"title\":\"选择实例规格\",\"tableColumns\":[{\"property\":\"specType\",\"label\":\"规格类型\",\"min-width\":\"120px\"},{\"property\":\"specName\",\"label\":\"规格名称\"},{\"property\":\"instanceSpec\",\"label\":\"实例规格\"}]}",
            step = 1,
            group = 3,
            confirmGroup = 1
    )
    private String instanceType;

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统",
            clazz = HuaweiCloudProvider.class,
            method = "listOs",
            textField = "name",
            valueField = "id",
            defaultValue = "CentOS",
            propsInfo = "{\"style\":{\"width\":\"100%\"}}",
            step = 1,
            group = 4,
            confirmGroup = 1
    )
    private String os;

    @Form(inputType = InputType.SingleSelect,
            label = "系统版本",
            clazz = HuaweiCloudProvider.class,
            method = "listOsVersion",
            textField = "imageName",
            valueField = "imageId",
            propsInfo = "{\"style\":{\"width\":\"100%\"}}",
            relationTrigger = {"os", "instanceType"},
            step = 1,
            group = 4,
            confirmGroup = 1

    )
    private String osVersion;

    /**
     * 磁盘
     */
    @Form(inputType = InputType.HuaweiDiskConfigForm,
            clazz = HuaweiCloudProvider.class,
            method = "getAllDiskTypes",
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = {"availabilityZone"},
            step = 1,
            group = 5,
            confirmGroup = 1,
            confirmSpecial = true
    )
    private List<DiskConfig> disks;

    @Form(inputType = InputType.TableRadio,
            label = "网络",
            clazz = HuaweiCloudProvider.class,
            method = "listSubnet",
            textField = "name",
            valueField = "uuid",
            relationTrigger = {"regionId","availabilityZone"},
            propsInfo = "{\"rules\":[{\"message\":\"网络不能为空\",\"trigger\":\"change\",\"required\":true}],\"style\":{\"width\":\"100%\",\"height\":\"400px\"},\"showLabel\":false,\"activeMsg\":\"已选网络\",\"title\":\"选择网络\",\"tableColumns\":[{\"property\":\"name\",\"label\":\"子网名称\",\"min-width\":\"120px\"},{\"property\":\"vpcName\",\"label\":\"所属VPC\"},{\"property\":\"cidr\",\"label\":\"IPV4网段\"}]}",
            step = 2,
            group = 6,
            confirmGroup = 2
    )
    private String networkId;

    @Form(inputType = InputType.TableCheckbox,
            label = "选择安全组",
            clazz = HuaweiCloudProvider.class,
            method = "listSecurityGroups",
            textField = "name",
            valueField = "uuid",
            relationTrigger = "regionId",
            propsInfo = "{\"style\":{\"width\":\"100%\",\"height\":\"400px\"},\"showLabel\":false,\"activeMsg\":\"已选安全组\",\"title\":\"选择安全组\",\"tableColumns\":[{\"property\":\"name\",\"label\":\"安全组名称\",\"min-width\":\"120px\"},{\"property\":\"description\",\"label\":\"描述\"}]}",
            step = 2,
            group = 6,
            confirmGroup = 2
    )
    private List<String> securityGroups;

    @Form(inputType = InputType.SwitchBtn,
            label = "公网IP",
            defaultValue = "false",
            defaultJsonValue = true,
            attrs = "{\"active-text\":\"使用\",\"inactive-text\":\"禁用\",\"inline-prompt\":true}",
            step = 2,
            group = 7,
            confirmGroup = 2,
            required = false
    )
    private boolean usePublicIp;

    @Form(inputType = InputType.Radio,
            label = "计费类型",
            clazz = HuaweiCloudProvider.class,
            method = "getChargeMode",
            textField = "name",
            valueField = "id",
            defaultValue = "noTraffic",
            relationShows = "usePublicIp",
            relationShowValues = "true",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private String chargeMode;

    /**
     * 按带宽
     */
    @Form(inputType = InputType.LineNumber,
            label = "带宽大小",
            unit = "Mbit/s",
            defaultValue = "1",
            defaultJsonValue = true,
            relationShows = {"usePublicIp", "chargeMode"},
            relationShowValues = {"true", "noTraffic"},
            attrs = "{\"min\":1,\"max\":2000,\"step\":1,\"style\":\"width:223px\"}",

            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private int bandwidthSize;

    /**
     * 按流量带宽大小
     */
    @Form(inputType = InputType.LineNumber,
            label = "带宽大小",
            unit = "Mbit/s",
            defaultValue = "1",
            defaultJsonValue = true,
            relationShows = {"usePublicIp", "chargeMode"},
            relationShowValues = {"true", "traffic"},
            attrs = "{\"min\":1,\"max\":300,\"step\":1,\"style\":\"width:223px\"}",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private int trafficBandwidthSize;

    @Form(inputType = InputType.Radio,
            label = "登录方式",
            clazz = HuaweiCloudProvider.class,
            method = "getLoginMethod",
            textField = "name",
            valueField = "id",
            defaultValue = "pwd",
            step = 3,
            group = 8,
            confirmGroup = 3
    )
    private String loginMethod;

    @Form(inputType = InputType.LabelText,
            label = "登录名",
            clazz = HuaweiCloudProvider.class,
            method = "getLoginName",
            relationTrigger = "os",
            relationShows = "loginMethod",
            relationShowValues = "pwd",
            attrs = "{\"style\":\"width:35%\"}",
            step = 3,
            group = 8,
            confirmGroup = 3,
            required = false
    )
    private String loginName;

    @Form(inputType = InputType.RegexInput,
            label = "登录密码",
            description = "密码须同时符合以下规则",
            relationShows = "loginMethod",
            relationShowValues = "pwd",
            regexList = "[" +
                    "{\"regex\":\"^((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*root)(?!.*toor)(?!.*[A|a]dministrator)(?!.*rotartsinimd[A|a]).{8,25})$\"" +
                    ",\"message\":\"必须包含大小写字母和数字的组合，可以使用特殊字符，不能包含用户名或逆向用户名，长度在8-26之间\"}]",
            propsInfo = "{\"style\":{\"width\":\"100%\"}}",
            step = 3,
            group = 8,
            encrypted = true
    )
    private String pwd;

    @Form(inputType = InputType.SingleSelect,
            label = "密钥对",
            clazz = HuaweiCloudProvider.class,
            method = "listKeyPairs",
            textField = "name",
            valueField = "name",
            relationTrigger = "regionId",
            relationShows = "loginMethod",
            relationShowValues = "keyPair",
            step = 3,
            group = 8,
            confirmGroup = 3
    )
    private String keyPari;

    /**
     * 云主机信息
     */
    @Form(inputType = InputType.HuaweiNameConfigForm,
            defaultValue = "[]",
            defaultJsonValue = true,
            step = 3,
            group = 9,
            confirmGroup = 3,
            confirmSpecial = true
    )
    private List<HuaweiServerNameInfo> serverNameInfos;

    @Form(inputType = InputType.LabelText,
            label = "配置费用",
            clazz = HuaweiCloudProvider.class,
            method = "calculateConfigPrice",
            relationTrigger = {"billingMode", "periodNum", "availabilityZone", "instanceType", "disks", "count","trafficBandwidthSize", "bandwidthSize","chargeMode","usePublicIp"},
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            required = false,
            confirmSpecial = true,
            footerLocation = 1
    )
    private String totalAmountText;

    @Form(inputType = InputType.LabelText,
            label = "公网IP流量费用",
            clazz = HuaweiCloudProvider.class,
            method = "calculateBandwidthConfigPrice",
            relationShows = {"chargeMode"},
            relationShowValues = {"traffic"},
            relationTrigger = {"bandwidthSize", "periodNum", "trafficBandwidthSize", "chargeMode"},
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            required = false,
            confirmSpecial = true,
            footerLocation = 1
    )
    private String trafficPrice;


}
