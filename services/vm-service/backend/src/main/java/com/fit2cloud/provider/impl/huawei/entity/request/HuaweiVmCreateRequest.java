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
import com.fit2cloud.provider.impl.huawei.entity.*;
import com.fit2cloud.provider.impl.tencent.TencentCloudProvider;
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
@FormGroupInfo(group = 4, name = "操作系统")
@FormGroupInfo(group = 5, name = "磁盘配置")
@FormGroupInfo(group = 6, name = "网络与安全")
@FormGroupInfo(group = 7, name = "公网IP",description="可以公网访问的公网IP")
@FormGroupInfo(group = 8, name = "登录凭证")
@FormGroupInfo(group = 9, name = "主机命名")
public class HuaweiVmCreateRequest extends HuaweiBaseRequest implements ICreateServerRequest {

    @Form(inputType = InputType.Radio,
            label = "付费方式",
            clazz = HuaweiCloudProvider.class,
            method = "getBillingMode",
            defaultValue = "postPaid",
            textField = "name",
            valueField = "id",
            step = 1,
            group = 1,
            confirmGroup = 1
    )
    private String billingMode;


    private int index;

    private String id;


    //step 1
    @Form(inputType = InputType.SingleSelect,
            label = "区域",
            defaultValue = "cn-south-1",
            clazz = HuaweiBaseCloudProvider.class,
            method = "getRegions",
            textField = "name",
            valueField = "regionId",
            group = 2,
            step = 1,
            confirmGroup = 0
    )
    private String regionId;

    //集群
    @Form(inputType = InputType.Radio,
            label = "可用区",
            defaultValue = "cn-south-1a",
            clazz = HuaweiCloudProvider.class,
            method = "getAvailabilityZone",
            textField = "displayName",
            valueField = "zoneName",
            relationTrigger = {"regionId", "billingMode"},
            group = 2,
            step = 1,
            confirmGroup = 0
    )
    private String availabilityZone;

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统",
            defaultValue = "CentOS",
            clazz = HuaweiCloudProvider.class,
            method = "listOs",
            textField = "name",
            valueField = "id",
            relationTrigger = "instanceSpecConfig",
            group = 4,
            step = 1,
            confirmGroup = 1
    )
    private String os;

    @Form(inputType = InputType.HuaweiOsSingleSelectForm,
            label = "系统版本",
            clazz = HuaweiCloudProvider.class,
            confirmSpecial = true,
            defaultJsonValue = true,
            method = "listOsVersion",
            relationTrigger = {"os","instanceSpecConfig"},
            textField = "osVersion",
            valueField = "id",
            group = 4,
            step = 1,
            confirmGroup = 1
    )
    private OsConfig osVersion;

    @Form(inputType = InputType.HuaweiInstanceSpecForm,
            description = "购买数量为多台时，实例规格一样，若想要不同实例规格需要分多次申请。",
            defaultValue = "{}",
            confirmSpecial = true,
            defaultJsonValue = true,
            relationTrigger = {"availabilityZone","billingMode"},
            group = 3,
            step = 1,
            confirmGroup = 1,
            confirmPosition = Form.Position.TOP
    )
    private InstanceSpecType instanceSpecConfig;

    //磁盘配置
    @Form(inputType = InputType.HuaweiDiskConfigForm,
            clazz = HuaweiCloudProvider.class,
            label = "",
            step = 1,
            group = 5,
            method = "getAllDiskTypes",
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = {"availabilityZone","billingMode"},
            confirmGroup = 1,
            confirmSpecial = true
    )
    private List<DiskConfig> disks;

    @Form(inputType = InputType.HuaweiNetworkConfigForm,
            step = 2,
            group = 6,
            label = "网络",
            confirmSpecial = true,
            defaultJsonValue = true,
            relationTrigger = "regionId",
            confirmGroup = 2,
            textField = "name",
            valueField = "uuid",
            confirmPosition = Form.Position.TOP
    )
    private NetworkConfig networkConfigs;

    @Form(inputType = InputType.MultiSelect,
            label = "选择安全组",
            clazz = HuaweiCloudProvider.class,
            method = "listSecurityGroups",
            relationTrigger = "regionId",
            textField = "name",
            valueField = "uuid",
            confirmGroup = 2,
            group = 6,
            step = 2
    )
    private List<String> securityGroups;

    @Form(inputType = InputType.SwitchBtn,
            label = "公网IP",
            defaultValue = "false",
            defaultJsonValue = true,
            attrs = "{\"active-text\":\"使用\",\"inactive-text\":\"禁用\",\"inline-prompt\":true}",
            confirmGroup = 2,
            group = 7,
            step = 2,
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
            confirmGroup = 2,
            step = 2,
            group = 7
    )
    private String chargeMode;

    /**
     * 按带宽
     */
    @Form(inputType = InputType.Number,
            label = "带宽大小",
            unit = "Mbit/s",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":2000,\"step\":1}",
            relationShows = {"usePublicIp","chargeMode"},
            relationShowValues = {"true","noTraffic"},
            defaultValue = "1",
            confirmGroup = 2,
            step = 2,
            group = 7
    )
    private int bandwidthSize;

    /**
     * 按流量带宽大小
     */
    @Form(inputType = InputType.Number,
            label = "带宽大小",
            unit = "Mbit/s",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":300,\"step\":1}",
            relationShows = {"usePublicIp","chargeMode"},
            relationShowValues = {"true","traffic"},
            defaultValue = "1",
            confirmGroup = 2,
            step = 2,
            group = 7
    )
    private int trafficBandwidthSize;


    @Form(inputType = InputType.Radio,
            label = "登录方式",
            clazz = HuaweiCloudProvider.class,
            method = "getLoginMethod",
            defaultValue = "pwd",
            confirmGroup = 3,
            textField = "name",
            valueField = "id",
            step = 3,
            group = 8
    )
    private String loginMethod;

    @Form(inputType = InputType.LabelText,
            label = "登录名",
            clazz = HuaweiCloudProvider.class,
            method = "getLoginName",
            attrs = "{\"style\":\"width:35%\"}",
            relationShows = "loginMethod",
            relationShowValues = "pwd",
            required = false,
            relationTrigger = "os",
            confirmGroup = 3,
            step = 3,
            group = 8
    )
    private String loginName;

    @Form(inputType = InputType.Password,
            attrs = "{\"style\":\"width:35%\",\"min\":\"8\"}",
            label = "密码",
            relationShows = "loginMethod",
            relationShowValues = "pwd",
            regexp = "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*root)(?!.*toor)(?!.*[A|a]dministrator)(?!.*rotartsinimd[A|a]).{8,25})$",
            regexpDescription = "必须包含大小写字母和数字的组合，可以使用特殊字符，不能包含用户名或逆向用户名，长度在8-26之间",
            step = 3,
            group = 8
    )
    private String pwd;

    @Form(inputType = InputType.SingleSelect,
            label = "密钥对",
            clazz = HuaweiCloudProvider.class,
            method = "listKeyPairs",
            relationTrigger = "regionId",
            textField = "name",
            valueField = "name",
            relationShows = "loginMethod",
            relationShowValues = "keyPair",
            confirmGroup = 3,
            step = 3,
            group = 8
    )
    private String keyPari;

    //云主机名称
    @Form(inputType = InputType.HuaweiNameConfigForm,
            attrs = "{\"style\":\"width:35%\"}",
            step = 3,
            group = 9,
            defaultValue = "[]",
            defaultJsonValue = true,
            confirmGroup = 3,
            regexp = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$",
            regexpDescription = "允许使用点号(.)分隔字符成多段,每段允许使用大小写字母、数字或连字符(-),但不能连续使用点号(.)或连字符(-),不能以点号(.)或连字符(-)开头或结尾,不能出现(.-)和(-.)",
            confirmSpecial = true,
            confirmPosition = Form.Position.TOP
    )
    private List<HuaweiServerNameInfo> serverNameInfos;

    @Form(inputType = InputType.Number,
            label = "购买数量",
            unit = "台",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":10,\"step\":1}",
            confirmGroup = 1

    )
    private int count;

    @Form(inputType = InputType.SingleSelect,
            label = "购买时长",
            clazz = HuaweiCloudProvider.class,
            method = "getPeriodOption",
            defaultValue = "1",
            textField = "periodDisplayName",
            valueField = "period",
            defaultJsonValue = true,
            confirmGroup = 4,
            relationShows = "billingMode",
            relationShowValues = "prePaid"

    )
    private String periodNum;

    @Form(inputType = InputType.LabelText,
            label = "配置费用",
            clazz = HuaweiCloudProvider.class,
            method = "calculateConfigPrice",
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            relationTrigger = {"billingMode","periodNum","availabilityZone","instanceSpecConfig","disks","count"},
            confirmSpecial = true,
            required = false
    )
    private String totalAmountText;

    @Form(inputType = InputType.LabelText,
            label = "公网IP流量费用",
            clazz = HuaweiCloudProvider.class,
            method = "calculateBandwidthConfigPrice",
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            relationShows = {"chargeMode"},
            relationShowValues = {"traffic"},
            relationTrigger = {"bandwidthSize", "periodNum", "trafficBandwidthSize", "chargeMode"},
            confirmSpecial = true,
            required = false
    )
    private String trafficPrice;


}
