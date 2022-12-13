package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.annotaion.FormConfirmInfo;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.ICreateServerRequest;
import com.fit2cloud.provider.entity.F2CNetwork;
import com.fit2cloud.provider.impl.tencent.TencentCloudProvider;
import com.fit2cloud.provider.impl.tencent.constants.TencentBandwidthType;
import com.fit2cloud.provider.impl.tencent.constants.TencentChargeType;
import com.fit2cloud.provider.impl.tencent.entity.TencentInstanceType;
import com.tencentcloudapi.cvm.v20170312.models.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
@FormGroupInfo(group = 4, name = "操作系统")
@FormGroupInfo(group = 5, name = "磁盘配置", description = "若需要新增数据盘，申请后需要挂载和初始化后才能正常使用。")
@FormGroupInfo(group = 6, name = "网络与安全")
@FormGroupInfo(group = 7, name = "公网IP")
@FormGroupInfo(group = 8, name = "登录凭证")
@FormGroupInfo(group = 9, name = "主机命名")
public class TencentVmCreateRequest extends TencentBaseRequest implements ICreateServerRequest {
    /**
     * 数据库中预插入数据的主键
     */
    private String id;

    private int index;

    @Form(inputType = InputType.Radio,
            label = "付费方式",
            clazz = TencentCloudProvider.class,
            method = "getChargeType",
            textField = "name",
            valueField = "id",
            defaultValue = "POSTPAID_BY_HOUR",
            step = 1,
            group = 1,
            confirmGroup = 1
    )
    private String instanceChargeType;

    @Form(inputType = InputType.SingleSelect,
            label = "购买时长",
            clazz = TencentCloudProvider.class,
            method = "getPeriodOption",
            attrs = "{\"style\":\"width:120px\"}",
            textField = "periodDisplayName",
            valueField = "period",
            defaultValue = "1",
            defaultJsonValue = true,
            confirmGroup = 1,
            relationShows = "instanceChargeType",
            relationShowValues = "PREPAID"
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
            clazz = TencentCloudProvider.class,
            method = "getRegions",
            textField = "name",
            valueField = "id",
            defaultValue = "ap-guangzhou",
            step = 1,
            group = 2,
            confirmGroup = 0
    )
    private String regionId;

    @Form(inputType = InputType.Radio,
            label = "可用区",
            clazz = TencentCloudProvider.class,
            method = "getZones",
            textField = "name",
            valueField = "id",
            defaultValue = "ap-guangzhou-3",
            relationTrigger = "regionId",
            step = 1,
            group = 2,
            confirmGroup = 0
    )
    private String zoneId;

    @Form(inputType = InputType.TencentInstanceTypeForm,
            clazz = TencentCloudProvider.class,
            method = "getInstanceTypes",
            label = "",
            relationTrigger = {"instanceChargeType", "zoneId"},
            step = 1,
            group = 3,
            confirmGroup = 1,
            confirmSpecial = true
    )
    private TencentInstanceType instanceTypeDTO;

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统",
            clazz = TencentCloudProvider.class,
            method = "getOsTypes",
            textField = "name",
            valueField = "id",
            defaultValue = "TencentOS",
            step = 1,
            group = 4,
            confirmGroup = 1
    )
    private String os;

    @Form(inputType = InputType.SingleSelect,
            label = "操作系统版本",
            clazz = TencentCloudProvider.class,
            method = "getImages",
            textField = "name",
            valueField = "id",
            relationTrigger = {"instanceTypeDTO", "os"},
            step = 1,
            group = 4,
            confirmGroup = 1
    )
    private String osVersion;

    @Form(inputType = InputType.TencentDiskConfigForm,
            clazz = TencentCloudProvider.class,
            method = "getDiskTypesForCreateVm",
            label = "",
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = {"instanceChargeType", "zoneId", "instanceTypeDTO"},
            step = 1,
            group = 5,
            confirmGroup = 1,
            confirmSpecial = true
    )
    private List<TencentCreateDiskForm> disks;

    @Form(inputType = InputType.TencentNetConfigForm,
            clazz = TencentCloudProvider.class,
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

    @Form(inputType = InputType.MultiSelect,
            label = "安全组",
            clazz = TencentCloudProvider.class,
            method = "getSecurityGroups",
            textField = "name",
            valueField = "id",
            relationTrigger = "regionId",
            step = 2,
            group = 6,
            confirmGroup = 2
    )
    private List<String> securityGroupIds;

    @Form(inputType = InputType.SwitchBtn,
            label = "公网IP",
            defaultValue = "false",
            defaultJsonValue = true,
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private Boolean hasPublicIp;

    @Form(inputType = InputType.Radio,
            label = "带宽计费模式",
            clazz = TencentCloudProvider.class,
            method = "getBandwidthChargeTypes",
            defaultValue = "bandwidth",
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
            label = "带宽值",
            clazz = TencentCloudProvider.class,
            defaultValue = "1", attrs = "{\"min\":1,\"max\":100,\"step\":1}",
            relationShows = "hasPublicIp",
            relationShowValues = "true",
            unit = "Mbps",
            step = 2,
            group = 7,
            confirmGroup = 2
    )
    private Long bandwidth;

    @Form(inputType = InputType.Radio,
            label = "登录方式",
            clazz = TencentCloudProvider.class,
            method = "getLoginTypes",
            defaultValue = "password",
            textField = "name",
            valueField = "id",
            step = 3,
            group = 8,
            confirmGroup = 3
    )
    private String loginType;

    @Form(inputType = InputType.LabelText,
            label = "登录名",
            clazz = TencentCloudProvider.class,
            method = "getLoginUser",
            relationShows = "loginType",
            relationShowValues = "password",
            relationTrigger = "os",
            required = false,
            step = 3,
            group = 8,
            confirmGroup = 3
    )
    private String user;

    @Form(inputType = InputType.Password,
            label = "登录密码",
            relationShows = "loginType",
            relationShowValues = "password",
            regexp = "^(?!/)(?![\\da-z]+$)(?![\\dA-Z]+$)(?![\\d\\(\\)`~!@#\\$%\\^&\\*-\\+=_\\|\\{\\}\\[\\]:;'<>,\\.\\?/]+$)(?![a-zA-Z]+$)(?![a-z\\(\\)`~!@#\\$%\\^&\\*-\\+=_\\|\\{\\}\\[\\]:;'<>,\\.\\?/]+$)(?![A-Z\\(\\)`~!@#\\$%\\^&\\*-\\+=_\\|\\{\\}\\[\\]:;'<>,\\.\\?/]+$)[\\da-zA-z\\(\\)`~!@#\\$%\\^&\\*-\\+=_\\|\\{\\}\\[\\]:;'<>,\\.\\?/]{12,30}$",
            regexpDescription = "在 12 ～ 30 位字符数以内，不能以\" / \"开头，至少包含其中三项(小写字母 a ~ z、大写字母 A ～ Z、数字 0 ～ 9、()`~!@#$%^&*-+=_|{}[]:;'<>,.?/)\n",
            step = 3,
            group = 8
    )
    private String password;

    @Form(inputType = InputType.TencentServerInfoForm,
            defaultValue = "[]",
            defaultJsonValue = true,
            step = 3,
            group = 9,
            confirmGroup = 3,
            confirmSpecial = true
    )
    private List<TencentVmCreateRequest.ServerInfo> serverInfos;

    @Form(inputType = InputType.LabelText,
            label = "配置费用",
            clazz = TencentCloudProvider.class,
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
            clazz = TencentCloudProvider.class,
            method = "calculateTrafficPrice",
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            relationShows = {"bandwidthChargeType"},
            relationShowValues = {"traffic"},
            relationTrigger = {"bandwidth", "bandwidthChargeType"},
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

    /**
     * 生产创建实例 request 对象
     *
     * @return
     */
    public RunInstancesRequest toRunInstancesRequest() {
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

        Placement placement = new Placement();
        placement.setZone(this.zoneId);
        runInstancesRequest.setPlacement(placement);
        runInstancesRequest.setImageId(this.osVersion);
        if (this.instanceTypeDTO != null) {
            runInstancesRequest.setInstanceType(this.instanceTypeDTO.getInstanceType());
        }

        // 设置机器收费类型:按需或者包周期
        runInstancesRequest.setInstanceChargeType(this.instanceChargeType);
        if (TencentChargeType.PREPAID.getId().equalsIgnoreCase(this.instanceChargeType)) {
            runInstancesRequest.setInstanceChargePrepaid(generateInstanceChargePrepaid());
        }

        // 磁盘
        if (CollectionUtils.isNotEmpty(this.disks)) {
            runInstancesRequest.setSystemDisk(generateSystemDisk());
            if (CollectionUtils.isNotEmpty(generateDataDisk())) {
                runInstancesRequest.setDataDisks(generateDataDisk().toArray(new DataDisk[generateDataDisk().size()]));
            }
        }

        // 公网IP
        if (this.hasPublicIp) {
            runInstancesRequest.setInternetAccessible(generateInternetAccessible());
        }

        // 设置云主机名称和 hostname
        String instanceName = this.serverInfos.get(index).getName();
        String hostname = this.serverInfos.get(index).getHostname();
        if (StringUtils.isNotBlank(instanceName)) {
            runInstancesRequest.setInstanceName(instanceName.trim());
        }
        if (StringUtils.isNotBlank(hostname)) {
            runInstancesRequest.setHostName(hostname.trim());
        }

        // 设置密码
        LoginSettings loginSettings = new LoginSettings();
        loginSettings.setPassword(this.password);
        runInstancesRequest.setLoginSettings(loginSettings);

        // 设置子网和安全组
        VirtualPrivateCloud virtualPrivateCloud = new VirtualPrivateCloud();
        virtualPrivateCloud.setVpcId(this.f2CNetwork.getVpcId());
        virtualPrivateCloud.setSubnetId(this.f2CNetwork.getNetworkId());
        runInstancesRequest.setVirtualPrivateCloud(virtualPrivateCloud);
        runInstancesRequest.setInstanceCount(1l);
        if (CollectionUtils.isNotEmpty(this.securityGroupIds)) {
            runInstancesRequest.setSecurityGroupIds(securityGroupIds.toArray(new String[securityGroupIds.size()]));
        }

        return runInstancesRequest;
    }

    /**
     * 生成询价 request 对象
     *
     * @return
     */
    public InquiryPriceRunInstancesRequest toInquiryPriceRunInstancesRequest() {
        InquiryPriceRunInstancesRequest req = new InquiryPriceRunInstancesRequest();

        Placement placement = new Placement();
        placement.setZone(this.zoneId);
        req.setPlacement(placement);
        req.setImageId(this.osVersion);
        if (this.instanceTypeDTO != null) {
            req.setInstanceType(this.instanceTypeDTO.getInstanceType());
        }
        req.setInstanceChargeType(this.instanceChargeType);
        if (TencentChargeType.PREPAID.getId().equalsIgnoreCase(this.instanceChargeType)) {
            req.setInstanceChargePrepaid(generateInstanceChargePrepaid());
        }

        // 磁盘
        if (CollectionUtils.isNotEmpty(this.disks)) {
            req.setSystemDisk(generateSystemDisk());
            if (CollectionUtils.isNotEmpty(generateDataDisk())) {
                req.setDataDisks(generateDataDisk().toArray(new DataDisk[generateDataDisk().size()]));
            }
        }

        // 公网 IP
        if (this.hasPublicIp != null && this.hasPublicIp) {
            req.setInternetAccessible(generateInternetAccessible());
        }
        return req;
    }

    /**
     * 转换带宽计费模式
     *
     * @param bandwidthChargeType
     * @return
     */
    public String transferBandwidthChargeType(String bandwidthChargeType) {
        String result;
        if ("BANDWIDTH".equalsIgnoreCase(bandwidthChargeType)) {
            if (TencentChargeType.PREPAID.getId().equalsIgnoreCase(this.instanceChargeType)) {
                result = TencentBandwidthType.BANDWIDTH_PREPAID.getId();
            } else {
                result = TencentBandwidthType.BANDWIDTH_POSTPAID_BY_HOUR.getId();
            }
        } else {
            result = TencentBandwidthType.TRAFFIC.getId();
        }
        return result;
    }

    /**
     * 生成系统盘配置（默认第一块盘为系统盘）
     *
     * @return
     */
    private SystemDisk generateSystemDisk() {
        TencentCreateDiskForm systemDiskConfig = this.disks.get(0);
        SystemDisk systemDisk = new SystemDisk();
        systemDisk.setDiskType(systemDiskConfig.getDiskType());
        systemDisk.setDiskSize(systemDiskConfig.getSize());
        return systemDisk;
    }

    /**
     * 生成数据盘配置（默认第一块盘为系统盘，其余为数据盘）
     *
     * @return
     */
    private List<DataDisk> generateDataDisk() {
        List<DataDisk> dataDisks = new ArrayList<>();
        for (int i = 1; i < this.disks.size(); i++) {
            DataDisk dataDisk = new DataDisk();
            dataDisk.setDeleteWithInstance("TRUE".equalsIgnoreCase(disks.get(i).getDeleteWithInstance()));
            dataDisk.setDiskSize(this.disks.get(i).getSize());
            dataDisk.setDiskType(this.disks.get(i).getDiskType());
            dataDisks.add(dataDisk);
        }
        return dataDisks;
    }

    /**
     * 生成公网IP配置
     *
     * @return
     */
    private InternetAccessible generateInternetAccessible() {
        InternetAccessible internetAccessible = new InternetAccessible();
        internetAccessible.setPublicIpAssigned(true);
        internetAccessible.setInternetChargeType(transferBandwidthChargeType(this.bandwidthChargeType));
        internetAccessible.setInternetMaxBandwidthOut(this.bandwidth);
        return internetAccessible;
    }

    /**
     * 生成包年包月配置
     *
     * @return
     */
    private InstanceChargePrepaid generateInstanceChargePrepaid() {
        InstanceChargePrepaid instanceChargePrepaid = new InstanceChargePrepaid();
        instanceChargePrepaid.setPeriod(Long.valueOf(this.periodNum));
        instanceChargePrepaid.setRenewFlag("NOTIFY_AND_MANUAL_RENEW");
        if (instanceChargePrepaid.getPeriod() == null) {
            instanceChargePrepaid.setPeriod(1L);
        }
        return instanceChargePrepaid;
    }
}
