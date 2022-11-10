package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.annotaion.FormConfirmInfo;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.provider.impl.vsphere.VsphereBaseCloudProvider;
import com.fit2cloud.provider.ICreateServerRequest;
import com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider;
import com.fit2cloud.provider.impl.vsphere.util.ResourceConstants;
import com.fit2cloud.service.impl.VmCloudImageServiceImpl;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FormStepInfo(step = 1, name = "基础配置")
@FormStepInfo(step = 2, name = "资源配置")
@FormStepInfo(step = 3, name = "网络配置")
@FormStepInfo(step = 4, name = "系统配置")
@FormConfirmInfo(group = 0, name = "云账号")
@FormConfirmInfo(group = 1, name = "基础配置", items = 2)
@FormConfirmInfo(group = 2, name = "资源配置")
@FormConfirmInfo(group = 3, name = "网络配置")
@FormConfirmInfo(group = 4, name = "系统配置")
@FormGroupInfo(group = 1, name = "区域")
@FormGroupInfo(group = 2, name = "操作系统")
@FormGroupInfo(group = 3, name = "实例规格")
@FormGroupInfo(group = 4, name = "磁盘配置")
@FormGroupInfo(group = 5, name = "计算资源")
@FormGroupInfo(group = 6, name = "存储资源", description = "配置该资源池可用的存储资源")
@FormGroupInfo(group = 7, name = "主机存放位置")
@FormGroupInfo(group = 8, name = "网络")
@FormGroupInfo(group = 9, name = "主机命名")
public class VsphereVmCreateRequest extends VsphereVmBaseRequest implements ICreateServerRequest {

    @Form(inputType = InputType.Number,
            label = "购买数量",
            unit = "台",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":10,\"step\":1}",
            confirmGroup = 1

    )
    private int count;

    private int index;

    /**
     * 数据库中ID
     */
    private String id;

    //step 1
    //数据中心datacenter
    @Form(inputType = InputType.Radio,
            label = "数据中心",
            clazz = VsphereBaseCloudProvider.class,
            method = "getRegions",
            textField = "name",
            valueField = "regionId",
            group = 1,
            step = 1,
            confirmGroup = 0
    )
    private String region;

    //集群
    @Form(inputType = InputType.Radio,
            label = "集群",
            clazz = VsphereCloudProvider.class,
            method = "getClusters",
//            textField = "${info} <span\n" +
//                    "        style=\"\n" +
//                    "          float: right;\n" +
//                    "          color: var(--el-text-color-secondary);\n" +
//                    "          font-size: 13px;\n" +
//                    "        \"\n" +
//                    "        >${description}</span>",
            textField = "<div>${info}</div>\n" +
                    "      <div style=\"color: var(--el-text-color-secondary); font-size: smaller\">\n" +
                    "        ${description}\n" +
                    "      </div>",
            formatTextField = true,
            valueField = "name",
            relationTrigger = "region",
            group = 1,
            step = 1,
            confirmGroup = 0
    )
    private String cluster;

    //模版
    @Form(inputType = InputType.SingleSelect,
            label = "模版",
            clazz = VmCloudImageServiceImpl.class,
            serviceMethod = true,
            method = "listVmCloudImage",
            textField = "imageName",
            valueField = "imageName", //由于vc还是拿name作为快速索引，所以不用mor作为查询值
            relationTrigger = "region",
            group = 2,
            step = 1,
            confirmGroup = 1
    )
    private String template;

    //cpu核数
    @Form(inputType = InputType.Number,
            label = "CPU",
            unit = "核",
            group = 3,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":128,\"step\":1}",
            confirmGroup = 1
    )
    private int cpu;

    //内存GB
    @Form(inputType = InputType.Number,
            label = "内存",
            unit = "GB",
            group = 3,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":512,\"step\":1}",
            confirmGroup = 1
    )
    private int ram;

    //磁盘配置
    @Form(inputType = InputType.VsphereDiskConfigForm,
            step = 1,
            group = 4,
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = "template",
            confirmGroup = 1,
            confirmSpecial = true
    )
    private List<DiskConfig> disks;


    //step 2
    @Form(inputType = InputType.VsphereComputeConfigForm,
            step = 2,
            group = 5,
            defaultValue = "{\"location\": \"host\"}",
            defaultJsonValue = true,
            relationTrigger = "cluster",
            confirmGroup = 2,
            confirmSpecial = true
    )
    private ComputeConfig computeConfig;

    //
    @Form(inputType = InputType.Radio,
            label = "磁盘格式",
            clazz = VsphereCloudProvider.class,
            method = "getDiskTypes",
            textField = "info",
            valueField = "value",
            defaultValue = "DEFAULT",
            step = 2,
            group = 6,
            confirmGroup = 2
    )
    private String diskType;

    //存储器
    @Form(inputType = InputType.VsphereDatastoreForm,
            label = "存储器",
            clazz = VsphereCloudProvider.class,
            method = "getDatastoreList",
            step = 2,
            group = 6,
            relationTrigger = "computeConfig",
            confirmGroup = 2,
            confirmSpecial = true
    )
    private String datastore;

    //文件夹
    @Form(inputType = InputType.SingleSelect,
            label = "文件夹",
            clazz = VsphereCloudProvider.class,
            method = "getFolders",
            textField = "name",
            valueField = "name", //由于vc还是拿name作为快速索引，所以不用mor作为查询值
            relationTrigger = "cluster",
            group = 7,
            step = 2,
            confirmGroup = 2
    )
    private String folder;


    //step 3
    //网卡
    @Form(inputType = InputType.VsphereNetworkAdapterForm,
            step = 3,
            group = 8,
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = {"cluster", "computeConfig"},
            confirmGroup = 3,
            confirmSpecial = true,
            confirmPosition = Form.Position.TOP
    )
    private List<NetworkConfig> networkConfigs;


    //step 4
    //云主机名称
    @Form(inputType = InputType.VsphereServerInfoForm,
            step = 4,
            group = 9,
            defaultValue = "[]",
            defaultJsonValue = true,
            confirmGroup = 4,
            confirmSpecial = true,
            confirmPosition = Form.Position.TOP
    )
    private List<ServerInfo> serverInfos;


    @Data
    @Accessors(chain = true)
    public static class DiskConfig {

        private Integer size;

        private boolean deleteWithInstance;

    }

    @Data
    @Accessors(chain = true)
    public static class ComputeConfig {
        //计算资源类型
        private String location;

        //主机/资源池的Mor
        //private String mor; //由于vc还是拿name作为快速索引，所以不用mor作为查询值

        //主机/资源池的名称
        private String name;

    }

    @Data
    @Accessors(chain = true)
    public static class NetworkConfig {
        private List<NetworkAdapter> adapters;

        private String dns1;
        private String dns2;

    }

    @Data
    @Accessors(chain = true)
    public static class NetworkAdapter {

        private String ipType = ResourceConstants.ipv4;

        private String vlan;

        private boolean dhcp;

        private String ipAddr;

        private String gateway;

        private String netmask;

        private String ipAddrV6;

        private String gatewayV6;

        private String netmaskV6;

    }

    @Data
    @Accessors(chain = true)
    public static class ServerInfo {

        private String name;

        private String hostname;

        //username
        //password
        //hostname

        private boolean addDomain;
        private String domain;
        private String domainAdmin;
        private String domainPassword;

    }


}
