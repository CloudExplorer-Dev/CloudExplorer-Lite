package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.provider.impl.vsphere.VsphereBaseCloudProvider;
import com.fit2cloud.provider.ICreateServerRequest;
import com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider;
import lombok.Data;

import java.util.List;


@Data
public class TencentVmCreateRequest implements ICreateServerRequest {

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
            clazz = VsphereBaseCloudProvider.class,
            method = "getClusters",
            textField = "name",
            valueField = "clusterId",
            relationTrigger = "region",
            group = 1,
            step = 1
    )
    private String cluster;

    //模版
    @Form(inputType = InputType.SingleSelect,
            label = "模版",
            clazz = VsphereCloudProvider.class,
            method = "listImage",
            textField = "name",
            valueField = "id",
            relationTrigger = "cluster",
            group = 3,
            step = 1
    )
    private String template;

    //cpu核数
    @Form(inputType = InputType.Number,
            label = "CPU",
            unit = "核",
            relationTrigger = "template",
            group = 3,
            step = 1
    )
    private int vCpu;

    //内存GB
    @Form(inputType = InputType.Number,
            label = "内存",
            unit = "GB",
            relationTrigger = "template",
            group = 2,
            step = 1
    )
    private int ram;

    //磁盘配置
    @Form(inputType = InputType.VsphereDiskConfigForm
    )
    private List<DiskConfig> disks;


    //step 2
    @Form(inputType = InputType.VsphereComputeConfigForm
    )
    private ComputeConfig computeConfig;

    //
    @Form(inputType = InputType.Radio,
            label = "磁盘格式"
    )
    private String diskType;

    //存储器
    @Form(inputType = InputType.VsphereDatastoreForm,
            label = "存储器"
    )
    private String datastore;


    //step 3
    //网卡
    @Form(inputType = InputType.VsphereNetworkAdapterForm
    )
    private List<NetworkAdapter> networkAdapters;


    //step 4
    //云主机名称
    @Form(inputType = InputType.VsphereServerInfoForm
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
