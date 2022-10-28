package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.provider.impl.vsphere.VsphereBaseCloudProvider;
import com.fit2cloud.provider.ICreateServerRequest;
import com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider;
import com.fit2cloud.service.IVmCloudImageService;
import com.fit2cloud.service.impl.VmCloudImageServiceImpl;
import lombok.Data;

import java.util.List;


@Data
@FormStepInfo(step = 1, name = "基础配置")
@FormStepInfo(step = 2, name = "选择资源")
@FormStepInfo(step = 3, name = "网络配置")
@FormStepInfo(step = 4, name = "系统配置")
@FormGroupInfo(group = 1, name = "区域")
@FormGroupInfo(group = 2, name = "操作系统")
@FormGroupInfo(group = 3, name = "实例规格")
@FormGroupInfo(group = 4, name = "磁盘配置")
@FormGroupInfo(group = 5, name = "计算资源")
@FormGroupInfo(group = 6, name = "存储资源", description = "配置该资源池可用的存储资源")
@FormGroupInfo(group = 7, name = "网络")
@FormGroupInfo(group = 8, name = "主机命名")
public class VsphereVmCreateRequest extends VsphereVmBaseRequest implements ICreateServerRequest {

    @Form(inputType = InputType.Number,
            label = "购买数量",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":10,\"step\":1}"
    )
    private String count;

    //step 1
    //数据中心datacenter
    @Form(inputType = InputType.SingleSelect,
            label = "数据中心",
            clazz = VsphereBaseCloudProvider.class,
            method = "getRegions",
            textField = "name",
            valueField = "regionId",
            group = 1,
            step = 1
    )
    private String region;

    //集群
    @Form(inputType = InputType.SingleSelect,
            label = "集群",
            clazz = VsphereCloudProvider.class,
            method = "getClusters",
            textField = "${info} <span\n" +
                    "        style=\"\n" +
                    "          float: right;\n" +
                    "          color: var(--el-text-color-secondary);\n" +
                    "          font-size: 13px;\n" +
                    "        \"\n" +
                    "        >${description}</span>",
            formatTextField = true,
            valueField = "name",
            relationTrigger = "region",
            group = 1,
            step = 1
    )
    private String cluster;

    //模版
    @Form(inputType = InputType.SingleSelect,
            label = "模版",
            clazz = VmCloudImageServiceImpl.class,
            serviceMethod = true,
            method = "listVmCloudImage",
            textField = "imageName",
            valueField = "imageId",
            relationTrigger = "region",
            group = 2,
            step = 1
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
            attrs = "{\"min\":1,\"max\":128,\"step\":1}"
    )
    private int vCpu;

    //内存GB
    @Form(inputType = InputType.Number,
            label = "内存",
            unit = "GB",
            group = 3,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":512,\"step\":1}"
    )
    private int ram;

    //磁盘配置
    @Form(inputType = InputType.VsphereDiskConfigForm,
            step = 1,
            group = 4,
            relationTrigger = "template"
    )
    private List<DiskConfig> disks;


    //step 2
    @Form(inputType = InputType.VsphereComputeConfigForm,
            step = 2,
            group = 5
    )
    private ComputeConfig computeConfig;

    //
    @Form(inputType = InputType.Radio,
            label = "磁盘格式",
            step = 2,
            group = 6
    )
    private String diskType;

    //存储器
    @Form(inputType = InputType.VsphereDatastoreForm,
            label = "存储器",
            step = 2,
            group = 6
    )
    private String datastore;


    //step 3
    //网卡
    @Form(inputType = InputType.VsphereNetworkAdapterForm,
            step = 3,
            group = 7
    )
    private List<NetworkAdapter> networkAdapters;


    //step 4
    //云主机名称
    @Form(inputType = InputType.VsphereServerInfoForm,
            step = 4,
            group = 8
    )
    private String name;
    //username
    //password
    //hostname


    @Data
    private static class DiskConfig {

        private Integer size;

        private boolean deleteWithInstance;

    }

    @Data
    private static class ComputeConfig {
        //计算资源类型
        private String location;

        //主机
        private List<String> hosts;

    }

    @Data
    private static class NetworkAdapter {

        private String vlan;

        private boolean dhcp;

        private String ipAddr;

        private String gateway;

        private String netmask;

    }


}
