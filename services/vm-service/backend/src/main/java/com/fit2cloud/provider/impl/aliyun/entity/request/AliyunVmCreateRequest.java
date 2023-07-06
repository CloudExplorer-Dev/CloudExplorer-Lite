package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.ecs20140526.models.CreateInstanceRequest;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.annotaion.FormConfirmInfo;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.aliyun.AliyunCloudProvider;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunChargeType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.fit2cloud.vm.ICreateServerRequest;

import java.util.ArrayList;
import java.util.List;

@Data
@FormStepInfo(step = 1, name = "基础配置")
@FormStepInfo(step = 2, name = "网络配置")
@FormStepInfo(step = 3, name = "系统配置")
@FormConfirmInfo(group = 0, name = "云账号")
@FormConfirmInfo(group = 1, name = "基础配置")
@FormConfirmInfo(group = 2, name = "网络配置")
@FormConfirmInfo(group = 3, name = "系统配置")
@FormGroupInfo(group = 1, name = "付费方式")
@FormGroupInfo(group = 2, name = "区域")
@FormGroupInfo(group = 3, name = "实例规格", description = "购买数量为多台时，实例规格一样，若想要不同实例规格需要分多次申请。")
@FormGroupInfo(group = 4, name = "操作系统", inline = true)
@FormGroupInfo(group = 5, name = "磁盘配置", description = "若需要新增数据盘，申请后需要挂载和初始化后才能正常使用。")
@FormGroupInfo(group = 6, name = "网络与安全")
@FormGroupInfo(group = 7, name = "公网IP")
@FormGroupInfo(group = 8, name = "登录凭证")
@FormGroupInfo(group = 9, name = "主机命名")
public class AliyunVmCreateRequest extends AliyunBaseRequest implements ICreateServerRequest {
    private int index;

    /**
     * 数据库中预插入数据的主键
     */
    private String id;

    @Form(inputType = InputType.Radio,
            label = "付费方式",
            clazz = AliyunCloudProvider.class,
            method = "getChargeType",
            textField = "name",
            valueField = "id",
            defaultValue = "PostPaid",
            step = 1,
            group = 1,
            confirmGroup = 1
    )
    private String instanceChargeType;

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
            clazz = AliyunCloudProvider.class,
            method = "getPeriodOption",
            textField = "periodDisplayName",
            valueField = "period",
            defaultValue = "1",
            relationShowValues = "PrePaid",
            relationShows = "instanceChargeType",
            propsInfo = "{\"style\":{\"height\":\"30px\"}}",
            confirmGroup = 1
    )
    private String periodNum;

    @Form(inputType = InputType.SingleSelect,
            label = "区域",
            clazz = AliyunCloudProvider.class,
            method = "getRegions",
            textField = "localName",
            valueField = "regionId",
            defaultValue = "cn-qingdao",
            propsInfo = "{\"style\":{\"width\":\"100%\",\"height\":\"32px\"}}",
            step = 1,
            group = 2,
            confirmGroup = 0
    )
    private String regionId;

    @Form(inputType = InputType.Radio,
            label = "可用区",
            clazz = AliyunCloudProvider.class,
            method = "getZones",
            textField = "localName",
            valueField = "zoneId",
            defaultValue = "cn-qingdao-c",
            relationTrigger = "regionId",
            step = 1,
            group = 2,
            confirmGroup = 0
    )
    private String zoneId;

    @Form(inputType = InputType.TableRadio,
            label = "实例规格",
            clazz = AliyunCloudProvider.class,
            method = "getInstanceTypes",
            textField = "instanceType",
            valueField = "instanceType",
            relationTrigger = "zoneId",
            propsInfo = "{\"style\":{\"width\":\"100%\",\"height\":\"400px\"},\"showLabel\":false,\"activeMsg\":\"已选实例\",\"title\":\"选择实例规格\",\"tableColumns\":[{\"property\":\"instanceTypeFamilyName\",\"label\":\"规格类型\",\"min-width\":\"120px\"},{\"property\":\"instanceType\",\"label\":\"规格名称\"},{\"property\":\"cpuMemory\",\"label\":\"实例规格\"}]}",
            step = 1,
            group = 3,
            confirmGroup = 1
    )
    private String instanceType;

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统",
            clazz = AliyunCloudProvider.class,
            method = "getOsTypes",
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
            label = "操作系统版本",
            clazz = AliyunCloudProvider.class,
            method = "getImages",
            textField = "os",
            valueField = "id",
            relationTrigger = {"instanceType", "os"},
            propsInfo = "{\"style\":{\"width\":\"100%\"}}",
            step = 1,
            group = 4,
            confirmGroup = 1
    )
    private String osVersion;

    /**
     * 磁盘
     */
    @Form(inputType = InputType.AliyunDiskConfigForm,
            clazz = AliyunCloudProvider.class,
            method = "getDiskTypesForCreateVm",
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = {"instanceChargeType", "zoneId", "instanceType"},
            step = 1,
            group = 5,
            confirmGroup = 1,
            confirmSpecial = true
    )
    private List<AliyunCreateDiskForm> disks;

    @Form(inputType = InputType.TableRadio,
            label = "网络",
            clazz = AliyunCloudProvider.class,
            method = "getNetworks",
            textField = "networkName",
            valueField = "networkId",
            relationTrigger = "zoneId",
            propsInfo = "{\"rules\":[{\"message\":\"网络不能为空\",\"trigger\":\"change\",\"required\":true}],\"style\":{\"width\":\"100%\",\"height\":\"400px\"},\"showLabel\":false,\"activeMsg\":\"已选网络\",\"title\":\"选择网络\",\"tableColumns\":[{\"property\":\"networkName\",\"label\":\"网络名称\",\"min-width\":\"120px\"},{\"property\":\"vpcName\",\"label\":\"所属VPC\"},{\"property\":\"ipSegment\",\"label\":\"IPV4网段\"}]}",
            step = 2,
            group = 6,
            confirmGroup = 2
    )
    private String networkId;

    @Form(inputType = InputType.TableRadio,
            label = "安全组",
            clazz = AliyunCloudProvider.class,
            method = "getSecurityGroups",
            textField = "securityGroupName",
            valueField = "securityGroupId",
            relationTrigger = {"regionId", "networkId"},
            propsInfo = "{\"rules\":[{\"message\":\"安全组不能为空\",\"trigger\":\"change\",\"required\":true}],\"style\":{\"width\":\"100%\",\"height\":\"400px\"},\"showLabel\":false,\"activeMsg\":\"已选安全组\",\"title\":\"选择安全组\",\"tableColumns\":[{\"property\":\"securityGroupName\",\"label\":\"安全组名称\",\"min-width\":\"120px\"},{\"property\":\"Description\",\"label\":\"描述\"}]}",
            step = 2,
            group = 6,
            confirmGroup = 2
    )
    private String securityGroupId;

    @Form(inputType = InputType.SwitchBtn,
            label = "公网IP",
            defaultValue = "false",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private Boolean hasPublicIp;

    @Form(inputType = InputType.Radio,
            label = "带宽计费模式",
            clazz = AliyunCloudProvider.class,
            method = "getBandwidthChargeTypes",
            textField = "name",
            valueField = "id",
            defaultValue = "PayByBandwidth",
            relationShows = "hasPublicIp",
            relationShowValues = "true",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private String bandwidthChargeType;

    @Form(inputType = InputType.LineNumber,
            label = "带宽(峰)值",
            unit = "Mbps",
            defaultValue = "1",
            attrs = "{\"min\":1,\"max\":100,\"step\":1,\"style\":\"width:223px\"}",
            relationShows = "hasPublicIp",
            relationShowValues = "true",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private String bandwidth;

    @Form(inputType = InputType.Radio,
            label = "登录方式",
            clazz = AliyunCloudProvider.class,
            method = "getLoginTypes",
            textField = "name",
            valueField = "id",
            defaultValue = "password",
            step = 3,
            group = 8,
            confirmGroup = 3
    )
    private String loginType;

    @Form(inputType = InputType.LabelText,
            label = "登录名",
            clazz = AliyunCloudProvider.class,
            method = "getLoginUser",
            relationTrigger = "os",
            relationShows = "loginType",
            relationShowValues = "password",
            step = 3,
            group = 8,
            confirmGroup = 3,
            required = false
    )
    private String user;

    @Form(inputType = InputType.RegexInput,
            label = "登录密码",
            description = "密码须同时符合以下规则",
            relationShows = "loginType",
            relationShowValues = "password",
            propsInfo = "{\"rules\":[{\"message\":\"登录密码不符合规则\",\"trigger\":\"blur\",\"required\":true,\"pattern\":\"^(?!/)(?![\\\\da-z]+$)(?![\\\\dA-Z]+$)(?![\\\\d\\\\(\\\\)`~!@#\\\\$%\\\\^&\\\\*\\\\-\\\\+=_\\\\|\\\\{\\\\}\\\\[\\\\]:;'<>,\\\\.\\\\?/]+$)(?![a-zA-Z]+$)(?![a-z\\\\(\\\\)`~!@#\\\\$%\\\\^&\\\\*\\\\-\\\\+=_\\\\|\\\\{\\\\}\\\\[\\\\]:;'<>,\\\\.\\\\?/]+$)(?![A-Z\\\\(\\\\)`~!@#\\\\$%\\\\^&\\\\*\\\\-\\\\+=_\\\\|\\\\{\\\\}\\\\[\\\\]:;'<>,\\\\.\\\\?/]+$)[\\\\da-zA-z\\\\(\\\\)`~!@#\\\\$%\\\\^&\\\\*\\\\-\\\\+=_\\\\|\\\\{\\\\}\\\\[\\\\]:;'<>,\\\\.\\\\?/]{8,30}$\",\"regexMessage\":\"在 8 ～ 30 位字符数以内，不能以\\\" / \\\"开头，至少包含其中三项(小写字母 a ~ z、大写字母 A ～ Z、数字 0 ～ 9、()`~!@#$%^&*-+=_|{}[]:;'<>,.?/)\"}],\"style\":{\"width\":\"100%\"}}",
            step = 3,
            group = 8,
            confirmGroup = 3,
            encrypted = true,
            confirmSpecial = true
    )
    private String password;

// 暂不支持密钥对，先屏蔽
//    @Form(inputType = InputType.SingleSelect,
//            label = "秘钥对",
//            clazz = AliyunCloudProvider.class,
//            method = "getKeyPairs",
//            relationShows = "loginType",
//            relationShowValues = "keyPair",
//            textField = "name",
//            valueField = "name",
//            step = 3,
//            group = 8,
//            confirmGroup = 3
//    )
//    private String keyPairName;

    @Form(inputType = InputType.AliyunServerInfoForm,
            defaultValue = "[]",
            defaultJsonValue = true,
            step = 3,
            group = 9,
            confirmGroup = 3,
            confirmSpecial = true
    )
    private List<ServerInfo> serverInfos;

    @Form(inputType = InputType.LabelText,
            label = "配置费用",
            clazz = AliyunCloudProvider.class,
            method = "calculateConfigPrice",
            relationTrigger = {"hasPublicIp", "bandwidth", "bandwidthChargeType", "count", "instanceChargeType", "periodNum", "instanceType", "osVersion", "disks"},
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            confirmSpecial = true,
            required = false
    )
    private String configPrice;

    @Form(inputType = InputType.LabelText,
            label = "公网IP流量费用",
            clazz = AliyunCloudProvider.class,
            method = "calculateTrafficPrice",
            relationShows = "bandwidthChargeType",
            relationShowValues = "PayByTraffic",
            relationTrigger = {"regionId", "bandwidthChargeType", "hasPublicIp"},
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            confirmSpecial = true,
            required = false
    )
    private String trafficPrice;

    @Data
    @Accessors(chain = true)
    public static class ServerInfo {
        /**
         * 云主机名称
         */
        private String name;
        private String hostname;
    }

    public CreateInstanceRequest toCreateInstanceRequest() {
        CreateInstanceRequest createInstanceRequest = new CreateInstanceRequest();
        createInstanceRequest.setInstanceChargeType(this.instanceChargeType);
        if (AliyunChargeType.PREPAID.getId().equalsIgnoreCase(instanceChargeType)) {
            String period = periodNum.indexOf("week") > 0 ? periodNum.substring(0, periodNum.indexOf("week")) : periodNum;
            createInstanceRequest.setPeriod(Integer.valueOf(period));
            createInstanceRequest.setPeriodUnit(periodNum.indexOf("week") > 0 ? "week" : "month");
            createInstanceRequest.setAutoRenew(true);
            createInstanceRequest.setAutoRenewPeriod(1);
        }
        createInstanceRequest.setRegionId(this.regionId);
        createInstanceRequest.setZoneId(zoneId);
        createInstanceRequest.setInstanceType(this.instanceType);
        createInstanceRequest.setImageId(this.osVersion);
        createInstanceRequest.setVSwitchId(getNetworkId());
        createInstanceRequest.setSecurityGroupId(this.securityGroupId);
        createInstanceRequest.setPassword(this.password);

// 暂不支持密钥对，先屏蔽
//        if (AliyunLoginType.KeyPair.getId().equalsIgnoreCase(this.loginType)) {
//            createInstanceRequest.setKeyPairName(this.keyPairName);
//        }

        // 设置云主机名称和 hostname
        String instanceName = this.serverInfos.get(index).getName();
        String hostname = this.serverInfos.get(index).getHostname();
        if (StringUtils.isNotBlank(instanceName)) {
            createInstanceRequest.setInstanceName(instanceName.trim());
        }
        if (StringUtils.isNotBlank(hostname)) {
            createInstanceRequest.setHostName(hostname.trim());
        }

        // 设置系统盘和数据盘：默认第一块盘为系统盘
        if (CollectionUtils.isNotEmpty(disks)) {
            CreateInstanceRequest.CreateInstanceRequestSystemDisk systemDisk = new CreateInstanceRequest.CreateInstanceRequestSystemDisk();
            AliyunCreateDiskForm firstDisk = disks.get(0);
            systemDisk.setCategory(firstDisk.getDiskType());
            systemDisk.setSize(firstDisk.getSize().intValue());
            systemDisk.setDiskName(firstDisk.getDiskName());
            systemDisk.setDescription(firstDisk.getDescription());
            createInstanceRequest.setSystemDisk(systemDisk);
            List<CreateInstanceRequest.CreateInstanceRequestDataDisk> dataDisks = new ArrayList<>();
            for (int i = 1; i < disks.size(); i++) {
                AliyunCreateDiskForm otherDisk = disks.get(i);
                CreateInstanceRequest.CreateInstanceRequestDataDisk dataDisk = new CreateInstanceRequest.CreateInstanceRequestDataDisk();
                dataDisk.setCategory(otherDisk.getDiskType());
                dataDisk.setSize(otherDisk.getSize().intValue());
                dataDisk.setDiskName(otherDisk.getDiskName());
                dataDisk.setDescription(otherDisk.getDescription());
                dataDisks.add(dataDisk);
            }
            createInstanceRequest.setDataDisk(dataDisks);
        }

        return createInstanceRequest;
    }
}
