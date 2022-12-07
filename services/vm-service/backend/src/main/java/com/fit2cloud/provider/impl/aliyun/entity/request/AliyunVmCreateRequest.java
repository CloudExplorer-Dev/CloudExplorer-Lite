package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.bssopenapi20171214.models.GetPayAsYouGoPriceRequest;
import com.aliyun.bssopenapi20171214.models.GetSubscriptionPriceRequest;
import com.aliyun.ecs20140526.models.CreateInstanceRequest;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.annotaion.FormConfirmInfo;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.ICreateServerRequest;
import com.fit2cloud.provider.entity.F2CNetwork;
import com.fit2cloud.provider.impl.aliyun.AliyunCloudProvider;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunBandwidthType;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunChargeType;
import com.fit2cloud.provider.impl.aliyun.entity.AliyunInstanceType;
import com.fit2cloud.provider.impl.aliyun.entity.AliyunPriceModuleConfig;
import com.fit2cloud.service.impl.VmCloudImageServiceImpl;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

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
@FormGroupInfo(group = 3, name = "操作系统")
@FormGroupInfo(group = 4, name = "实例规格", description = "购买数量为多台时，实例规格一样，若想要不同实例规格需要分多次申请。")
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

    @Form(inputType = InputType.SingleSelect,
            label = "购买时长",
            clazz = AliyunCloudProvider.class,
            method = "getPeriodOption",
            attrs = "{\"style\":\"width:120px\"}",
            textField = "periodDisplayName",
            valueField = "period",
            defaultValue = "1",
            confirmGroup = 1,
            relationShows = "instanceChargeType",
            relationShowValues = "PrePaid"
    )
    private String periodNum;

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
            label = "区域",
            clazz = AliyunCloudProvider.class,
            method = "getRegions",
            textField = "localName",
            valueField = "regionId",
            defaultValue = "cn-qingdao",
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

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统",
            clazz = AliyunCloudProvider.class,
            method = "getOsTypes",
            textField = "name",
            valueField = "id",
            defaultValue = "CentOS",
            step = 1,
            group = 3,
            confirmGroup = 1
    )
    private String os;

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统版本",
            clazz = VmCloudImageServiceImpl.class,
            method = "listVmCloudImage",
            serviceMethod = true,
            textField = "os",
            valueField = "imageId",
            relationTrigger = "os",
            step = 1,
            group = 3,
            confirmGroup = 1
    )
    private String osVersion;

    @Form(inputType = InputType.AliyunInstanceTypeForm,
            clazz = AliyunCloudProvider.class,
            method = "getInstanceTypes",
            label = "",
            relationTrigger = "zoneId",
            step = 1,
            group = 4,
            confirmGroup = 1,
            confirmSpecial = true
    )
    private AliyunInstanceType instanceTypeDTO;

    @Form(inputType = InputType.AliyunDiskConfigForm,
            clazz = AliyunCloudProvider.class,
            method = "getDiskTypes",
            label = "",
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = "zoneId",
            step = 1,
            group = 5,
            confirmGroup = 1,
            confirmSpecial = true
    )
    private List<AliyunCreateDiskForm> disks;

    @Form(inputType = InputType.AliyunNetConfigForm,
            clazz = AliyunCloudProvider.class,
            method = "getNetworks",
            label = "网络",
            textField = "name",
            valueField = "networkId",
            relationTrigger = "zoneId",
            step = 2,
            group = 6,
            confirmGroup = 2,
            confirmSpecial = true
    )
    private F2CNetwork f2CNetwork;

    @Form(inputType = InputType.SingleSelect,
            label = "安全组",
            clazz = AliyunCloudProvider.class,
            method = "getSecurityGroups",
            textField = "securityGroupName",
            valueField = "securityGroupId",
            relationTrigger = {"regionId", "f2CNetwork"},
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
            defaultValue = "PayByBandwidth",
            relationShows = "hasPublicIp",
            relationShowValues = "true",
            textField = "name",
            valueField = "id",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private String bandwidthChargeType;

    @Form(inputType = InputType.Number,
            label = "带宽(峰)值",
            clazz = AliyunCloudProvider.class,
            defaultValue = "1", attrs = "{\"min\":1,\"max\":100,\"step\":1}",
            relationShows = "hasPublicIp",
            relationShowValues = "true",
            unit = "Mbps",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private String bandwidth;

    @Form(inputType = InputType.Radio,
            label = "登录方式",
            clazz = AliyunCloudProvider.class,
            method = "getLoginTypes",
            defaultValue = "password",
            textField = "name",
            valueField = "id",
            step = 3,
            group = 8,
            confirmGroup = 3
    )
    private String loginType;

    @Form(inputType = InputType.Text,
            label = "登录名",
            relationShows = "loginType",
            relationShowValues = "password",
            step = 3,
            group = 8,
            confirmGroup = 3
    )
    private String user;

    @Form(inputType = InputType.Password,
            label = "登录密码",
            relationShows = "loginType",
            relationShowValues = "password",
            step = 3,
            group = 8,
            confirmGroup = 3
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

    @Form(inputType = InputType.VsphereServerInfoForm,
            defaultValue = "[]",
            defaultJsonValue = true,
            step = 3,
            group = 9,
            confirmGroup = 3,
            confirmSpecial = true
    )
    private List<AliyunVmCreateRequest.ServerInfo> serverInfos;

    @Form(inputType = InputType.LabelText,
            label = "配置费用",
            clazz = AliyunCloudProvider.class,
            method = "calculateConfigPrice",
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            relationTrigger = {"hasPublicIp", "bandwidth", "bandwidthChargeType", "count", "instanceChargeType", "periodNum", "instanceTypeDTO", "osVersion", "disks"},
            confirmSpecial = true,
            required = false
    )
    private String configPrice;

    @Form(inputType = InputType.LabelText,
            label = "公网IP流量费用",
            clazz = AliyunCloudProvider.class,
            method = "calculateTrafficPrice",
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            relationShows = "bandwidthChargeType",
            relationShowValues = "PayByTraffic",
            relationTrigger = {"regionId","bandwidthChargeType","hasPublicIp"},
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
        createInstanceRequest.setInstanceType(this.instanceTypeDTO == null ? "" : this.instanceTypeDTO.getInstanceType());
        createInstanceRequest.setImageId(this.osVersion);
        createInstanceRequest.setVSwitchId(this.f2CNetwork == null ? "" : f2CNetwork.getNetworkId());
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
                systemDisk.setCategory(otherDisk.getDiskType());
                systemDisk.setSize(otherDisk.getSize().intValue());
                systemDisk.setDiskName(otherDisk.getDiskName());
                systemDisk.setDescription(otherDisk.getDescription());
                dataDisks.add(dataDisk);
            }
            createInstanceRequest.setDataDisk(dataDisks);
        }

        return createInstanceRequest;
    }

    /**
     * 按量付费实例计费
     *
     * @return
     */
    public List<GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList> toPostPaidModuleList() {
        List<GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList> moduleList = new ArrayList<>();
        AliyunPriceModuleConfig config = generateModuleConfig();

        GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList instanceTypeModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                .setModuleCode("InstanceType")
                .setPriceType("Hour")
                .setConfig(config.getInstanceTypeConfig());
        moduleList.add(instanceTypeModule);

        // 系统盘
        if (StringUtils.isNotBlank(config.getSystemDiskConfig())) {
            GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList systemDiskModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                    .setModuleCode("SystemDisk")
                    .setPriceType("Hour")
                    .setConfig(config.getSystemDiskConfig());
            moduleList.add(systemDiskModule);
        }

        // 数据盘
        List<String> dataConfigList = config.getDataDiskConfigList();
        if (CollectionUtils.isNotEmpty(dataConfigList)) {
            for (int i = 1; i < dataConfigList.size(); i++) {
                GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList dataDiskModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                        .setModuleCode("DataDisk")
                        .setPriceType("Hour")
                        .setConfig(dataConfigList.get(i));
                moduleList.add(dataDiskModule);
            }
        }

        // 公网IP
        if (hasPublicIp != null && hasPublicIp) {
            if ("PayByTraffic".equalsIgnoreCase(bandwidthChargeType)) {
                GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList internetTrafficOutModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                        .setModuleCode("InternetTrafficOut")
                        .setPriceType("Usage")
                        .setConfig(config.getPublicIpConfig());
                moduleList.add(internetTrafficOutModule);
            } else {
                GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList internetMaxBandwidthOutModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                        .setModuleCode("InternetMaxBandwidthOut")
                        .setPriceType("Hour")
                        .setConfig(config.getPublicIpConfig());
                moduleList.add(internetMaxBandwidthOutModule);
            }
        }
        return moduleList;
    }

    /**
     * 包年包月实例计费
     *
     * @return
     */
    public List<GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList> toPrePaidModuleList() {
        List<GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList> moduleList = new ArrayList<>();
        AliyunPriceModuleConfig config = generateModuleConfig();

        // 实例类型
        GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList instanceTypeModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                .setModuleCode("InstanceType")
                .setConfig(config.getInstanceTypeConfig());
        moduleList.add(instanceTypeModule);

        // 系统盘
        if (StringUtils.isNotBlank(config.getSystemDiskConfig())) {
            GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList systemDiskModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                    .setModuleCode("SystemDisk")
                    .setConfig(config.getSystemDiskConfig());
            moduleList.add(systemDiskModule);
        }

        // 数据盘
        List<String> dataConfigList = config.getDataDiskConfigList();
        if (CollectionUtils.isNotEmpty(dataConfigList)) {
            for (int i = 0; i < dataConfigList.size(); i++) {
                GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList dataDiskModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                        .setModuleCode("DataDisk")
                        .setConfig(dataConfigList.get(i));
                moduleList.add(dataDiskModule);
            }
        }

        // 公网IP
        if (hasPublicIp != null && hasPublicIp) {
            // 按流量计费只能通过后付费询价，此处查按带宽计费的费用
            if (AliyunBandwidthType.PayByBandwidth.getId().equalsIgnoreCase(bandwidthChargeType)) {
                GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList internetMaxBandwidthOutModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                        .setModuleCode("InternetMaxBandwidthOut")
                        .setConfig(config.getPublicIpConfig());
                moduleList.add(internetMaxBandwidthOutModule);
            }
        }

        return moduleList;
    }

    private AliyunPriceModuleConfig generateModuleConfig() {
        AliyunPriceModuleConfig config = new AliyunPriceModuleConfig();

        // 实例类型
        String imageOs = os.indexOf("win") > 0 ? "windows" : "linux";
        if (instanceTypeDTO != null) {
            String instanceTypeConfig = "ImageOs:" + imageOs + "," +
                    "IoOptimized:none" + "," +
                    "Region:" + regionId + "," +
                    "InstanceTypeFamily:" + instanceTypeDTO.getInstanceTypeFamily() + "," +
                    "InstanceType:" + instanceTypeDTO.getInstanceType();
            config.setInstanceTypeConfig(instanceTypeConfig);
        }

        // 磁盘
        if (CollectionUtils.isNotEmpty(disks)) {
            // 系统盘
            AliyunCreateDiskForm systemDisk = disks.get(0);
            String systemDiskConfig = "SystemDisk.PerformanceLevel:PL1" + "," +
                    "Region:" + regionId + "," +
                    "SystemDisk.Category:" + systemDisk.getDiskType() + "," +
                    "SystemDisk.Size:" + systemDisk.getSize();
            config.setSystemDiskConfig(systemDiskConfig);

            // 数据盘
            List<String> dataDiskConfigList = new ArrayList();
            for (int i = 1; i < disks.size(); i++) {
                String dataDiskConfig = "DataDisk.PerformanceLevel:PL1" + "," +
                        "Region:" + regionId + "," +
                        "DataDisk.Category:" + disks.get(i).getDiskType() + "," +
                        "DataDisk.Size:" + disks.get(i).getSize();
                dataDiskConfigList.add(dataDiskConfig);
            }
            config.setDataDiskConfigList(dataDiskConfigList);
        }

        // 公网IP
        if (hasPublicIp != null && hasPublicIp) {
            // 按流量计费只能通过后付费询价
            if (AliyunBandwidthType.PayByTraffic.getId().equalsIgnoreCase(bandwidthChargeType)) {
                // 公网流量：按流量计费 XX元/GB
                String internetTrafficOutConfig = "Region:" + regionId + "," +
                        "InternetTrafficOut:1";
                config.setPublicIpConfig(internetTrafficOutConfig);
            } else {
                // 公网带宽：InternetMaxBandwidthOut 单位 KBPS;bandwidth 单位 MBPS
                String internetMaxBandwidthOutConfig = "Region:" + regionId + "," +
                        "InternetMaxBandwidthOut:" + Integer.valueOf(bandwidth) * 1024;
                config.setPublicIpConfig(internetMaxBandwidthOutConfig);
            }
        }
        return config;
    }
}
