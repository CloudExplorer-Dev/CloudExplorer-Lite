package com.fit2cloud.provider.impl.huawei.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.provider.impl.huawei.entity.F2CHuaweiSecurityGroups;
import com.fit2cloud.provider.impl.huawei.entity.F2CHuaweiSubnet;
import com.fit2cloud.provider.impl.huawei.entity.F2CHuaweiVpc;
import com.fit2cloud.provider.impl.huawei.entity.InstanceSpecType;
import com.huaweicloud.sdk.ces.v1.model.DatapointForBatchMetric;
import com.huaweicloud.sdk.ecs.v2.model.*;
import com.huaweicloud.sdk.evs.v2.model.VolumeDetail;
import com.huaweicloud.sdk.ims.v2.model.ImageInfo;
import com.huaweicloud.sdk.vpc.v2.model.Port;
import com.huaweicloud.sdk.vpc.v2.model.SecurityGroup;
import com.huaweicloud.sdk.vpc.v2.model.Subnet;
import com.huaweicloud.sdk.vpc.v2.model.Vpc;
import org.apache.commons.lang3.StringUtils;
import com.fit2cloud.vm.constants.DeleteWithInstance;
import com.fit2cloud.vm.constants.F2CChargeType;
import com.fit2cloud.vm.entity.F2CDisk;
import com.fit2cloud.vm.entity.F2CImage;
import com.fit2cloud.vm.entity.F2CVirtualMachine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:张少虎
 * @Date: 2022/9/22  3:01 PM
 * @Version 1.0
 * @注释:
 */
public class HuaweiMappingUtil {

    public static F2CVirtualMachine toF2CVirtualMachine(ServerDetail server) {
        return toF2CVirtualMachine(server, null);
    }

    public static F2CVirtualMachine toF2CVirtualMachine(ServerDetail server, List<Port> ports) {
        F2CVirtualMachine f2CVirtualMachine = new F2CVirtualMachine();
        if (StringUtils.isNotEmpty(server.getName())) {
            f2CVirtualMachine.setName(server.getName());
        }
        ServerFlavor flavor = server.getFlavor();
        f2CVirtualMachine.setCreated(new Date(CommonUtil.getUTCTime(server.getCreated(), "yyyy-MM-dd'T'HH:mm:ss'Z'")));
        f2CVirtualMachine.setCreateTime(f2CVirtualMachine.getCreated().getTime());
        f2CVirtualMachine.setImageId(server.getImage().getId());
        f2CVirtualMachine.setInstanceId(server.getId());
        f2CVirtualMachine.setInstanceUUID(server.getId());
        f2CVirtualMachine.setInstanceType(flavor.getName());
        f2CVirtualMachine.setInstanceTypeDescription(transInstanceDescription(flavor));
        f2CVirtualMachine.setOsInfo(server.getMetadata().get("image_name"));
        f2CVirtualMachine.setVpcId(server.getMetadata().get("vpc_id"));
        f2CVirtualMachine.setProjectId(server.getEnterpriseProjectId());
        f2CVirtualMachine.setInstanceStatus(toF2CInstanceStatus(server.getStatus()));
        String cpus = server.getFlavor().getVcpus();
        f2CVirtualMachine.setCpu(StringUtils.isEmpty(cpus) ? 0 : Integer.parseInt(cpus));
        String mem = server.getFlavor().getRam();
        f2CVirtualMachine.setMemory(StringUtils.isEmpty(mem) ? 0 : Integer.parseInt(mem) / 1024);
        f2CVirtualMachine.setZone(server.getOsEXTAZAvailabilityZone());
        f2CVirtualMachine.setProjectId(server.getEnterpriseProjectId());
        f2CVirtualMachine.setHostname(server.getOsEXTSRVATTRHostname());
        // 设置网络信息
        Map<String, List<ServerAddress>> addresses = server.getAddresses();
        List<ServerAddress> serverAddresses = addresses.values().stream().flatMap(Collection::stream).toList();
        String remoteIP = getRemoteIP(serverAddresses);
        String localIP = getLocalIP(serverAddresses);
        List<String> ipArray = getIpArray(serverAddresses);
        if (CollectionUtils.isNotEmpty(ports)) {
            String subnetId = getSubnetId(serverAddresses, ports);
            f2CVirtualMachine.setSubnetId(subnetId);
        }
        f2CVirtualMachine.setRemoteIP(remoteIP);
        f2CVirtualMachine.setLocalIP(localIP);
        f2CVirtualMachine.setIpArray(ipArray);
        String instanceChargeType = null;
        // 竞价
        if (StringUtils.equalsIgnoreCase(server.getMetadata().get("charging_mode"), "2")) {
            instanceChargeType = F2CChargeType.SPOT_PAID;
        } else {
            instanceChargeType = StringUtils.equalsIgnoreCase(server.getMetadata().get("charging_mode"), "0") ? F2CChargeType.POST_PAID : F2CChargeType.PRE_PAID;
        }
        // 付费方式
        f2CVirtualMachine.setInstanceChargeType(instanceChargeType);
        if (!CollectionUtils.isEmpty(server.getSecurityGroups())) {
            // 安全组
            List<String> sgIds = server.getSecurityGroups().stream().map(ServerSecurityGroup::getId).collect(Collectors.toList());
            f2CVirtualMachine.setSecurityGroupIds(sgIds);
        }
        return f2CVirtualMachine;
    }

    /**
     * 将远端实例状态转换为系统实例状态
     *
     * @param openstackStatus 华为云实例状态
     * @return 系统实例状态
     */
    public static String toF2CInstanceStatus(String openstackStatus) {
        openstackStatus = openstackStatus.toLowerCase();
        if (openstackStatus.equalsIgnoreCase("active")) {
            return "Running";
        } else if (openstackStatus.equalsIgnoreCase("stopped")) {
            return "Stopped";
        } else if (openstackStatus.equalsIgnoreCase("paused")) {
            return "Stopped";
        } else if (openstackStatus.equalsIgnoreCase("suspended")) {
            return "Stopped";
        } else if (openstackStatus.equalsIgnoreCase("Shutoff")) {
            return "Stopped";
        } else if (openstackStatus.equalsIgnoreCase("delete")) {
            return "Deleted";
        } else if (openstackStatus.equalsIgnoreCase("build")) {
            return "Starting";
        }
        return "Unknown";
    }

    /**
     * 获取公网ip
     *
     * @return 公网ip
     */
    private static String getRemoteIP(List<ServerAddress> addressList) {
        return addressList.stream().filter(address -> ServerAddress.OsEXTIPSTypeEnum.FLOATING.equals(address.getOsEXTIPSType())).findFirst().map(ServerAddress::getAddr).orElse("");
    }

    /**
     * 获取私网ip
     *
     * @param addressList ip地址相关数据
     * @return 私网ip
     */
    private static String getLocalIP(List<ServerAddress> addressList) {
        return addressList.stream().filter(address -> ServerAddress.OsEXTIPSTypeEnum.FIXED.equals(address.getOsEXTIPSType()) && address.getVersion().startsWith("4")).map(ServerAddress::getAddr).findFirst().orElse("");
    }

    /**
     * 获取所有ip
     *
     * @param addressList ip地址相关数据
     * @return 所有的ip
     */
    private static List<String> getIpArray(List<ServerAddress> addressList) {
        return addressList.stream().map(ServerAddress::getAddr).collect(Collectors.collectingAndThen(Collectors.toCollection(HashSet::new), ArrayList::new));
    }

    /**
     * 获取子网id
     *
     * @param addressList ip地址相关数据
     * @param ports       port相关数据
     * @return 子网id
     */
    private static String getSubnetId(List<ServerAddress> addressList, List<Port> ports) {
        return String.join(",", ports.stream().filter(port -> addressList.stream().anyMatch(address -> port.getId().equals(address.getOsEXTIPSPortId()))).map(Port::getNetworkId).collect(Collectors.toSet()));
    }

    /**
     * 将 VolumeDetail对象转换为系统磁盘对象
     *
     * @param disk 华为云磁盘对象
     * @return 系统磁盘对象
     */
    public static F2CDisk toF2CDisk(VolumeDetail disk) {
        F2CDisk f2cDisk = new F2CDisk();
        if (disk.getVolumeType() != null) {
            f2cDisk.setCategory(disk.getVolumeType());
            f2cDisk.setDiskType(disk.getVolumeType());
        }
        f2cDisk.setBootable(Boolean.parseBoolean(disk.getBootable()));
        if (f2cDisk.isBootable()) {
            f2cDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
        }
        String createAt = disk.getCreatedAt();
        if (StringUtils.isNotEmpty(createAt)) {
            f2cDisk.setCreateTime(CommonUtil.getUTCTime(createAt.substring(0, 19), "yyyy-MM-dd'T'hh:mm:ss"));
        }
        f2cDisk.setDescription(disk.getDescription());
        if (!CollectionUtils.isEmpty(disk.getAttachments())) {
            f2cDisk.setDevice(disk.getAttachments().get(0).getDevice());
            f2cDisk.setInstanceUuid(disk.getAttachments().get(0).getServerId());
        }
        f2cDisk.setDiskId(disk.getId());
        f2cDisk.setDiskName(disk.getName());
        f2cDisk.setRegion(disk.getAvailabilityZone().substring(0, disk.getAvailabilityZone().length() - 1));
        f2cDisk.setZone(disk.getAvailabilityZone());
        f2cDisk.setSize(disk.getSize());
        f2cDisk.setProjectId(disk.getOsVolTenantAttrTenantId());
        String status = disk.getStatus();
        if (StringUtils.equals("in-use", status) || StringUtils.equals("creating", status)) {
            status = "in_use";
        }
        f2cDisk.setStatus(status);
        return f2cDisk;
    }

    /**
     * 转换实例描述
     *
     * @param flavor 华为云实例flavor
     * @return 实例描述
     */
    public static String transInstanceDescription(ServerFlavor flavor) {
        String ramStr = flavor.getRam();
        int ram = Integer.parseInt(ramStr);
        float mbPerGb = 1024;
        return flavor.getVcpus() + "vCPU " + (int) Math.ceil(ram / mbPerGb) + "GB";
    }

    /**
     * 转换镜像对象
     *
     * @param image  镜像对象
     * @param region 区域对象
     * @return 系统镜像对象
     */
    public static F2CImage toF2CImage(ImageInfo image, String region) {
        F2CImage result = null;
        if (image.getStatus() == ImageInfo.StatusEnum.ACTIVE) {
            result = new F2CImage();
            result.setImageType(image.getImagetype().getValue());
            result.setId(image.getId());
            result.setName(image.getName());
            result.setRegion(region);
            long utcTime = CommonUtil.getUTCTime(image.getCreatedAt(), "yyyy-MM-dd'T'HH:mm:ss'Z'");
            result.setCreated(utcTime / 1000);
            // TODO 华为的size字段暂无用处，这里存储镜像所需要的最小磁盘大小
            result.setDiskSize(new BigDecimal(image.getMinDisk()).longValue());
            result.setOs(image.getOsVersion());
            result.setDescription(image.getDescription());
        }
        return result;
    }

    /**
     * 监控指标数据
     *
     * @param v
     * @return
     */
    public static F2CPerfMetricMonitorData toF2CPerfMetricMonitorData(DatapointForBatchMetric v) {
        F2CPerfMetricMonitorData f2CEntityPerfMetric = new F2CPerfMetricMonitorData();
        f2CEntityPerfMetric.setTimestamp(v.getTimestamp());
        if (Objects.nonNull(v.getAverage())) {
            f2CEntityPerfMetric.setAverage(new BigDecimal(v.getAverage()).setScale(3, RoundingMode.HALF_UP));
        }
        if (Objects.nonNull(v.getMax())) {
            f2CEntityPerfMetric.setMaximum(new BigDecimal(v.getMax()).setScale(3, RoundingMode.HALF_UP));
        }
        if (Objects.nonNull(v.getMin())) {
            f2CEntityPerfMetric.setMinimum(new BigDecimal(v.getMin()).setScale(3, RoundingMode.HALF_UP));
        }
        return f2CEntityPerfMetric;
    }

    public static InstanceSpecType toInstanceSpecType(Flavor flavor) {
        InstanceSpecType instanceSpecType = new InstanceSpecType();
        try {
            //s6.small.1
            instanceSpecType.setSpecType(flavor.getName().split("\\.")[0]);
            instanceSpecType.setSpecName(flavor.getName());
            instanceSpecType.setVcpus(flavor.getVcpus());
            instanceSpecType.setRam(flavor.getRam());
            instanceSpecType.setDisk(flavor.getDisk());
            instanceSpecType.setInstanceSpec(transInstanceSpecTypeDescription(flavor));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instanceSpecType;
    }

    public static String transInstanceSpecTypeDescription(Flavor flavor) {
        int ram = flavor.getRam();
        float mbPerGb = 1024;
        return flavor.getVcpus() + "vCPU " + (int) Math.ceil(ram / mbPerGb) + "GB";
    }

    public static String transInstanceSpecTypeDescription(ListResizeFlavorsResult flavor) {
        int ram = flavor.getRam();
        float mbPerGb = 1024;
        return flavor.getVcpus() + "vCPU " + (int) Math.ceil(ram / mbPerGb) + "GB";
    }

    public static F2CHuaweiSubnet toF2CHuaweiSubnet(Subnet subnet) {
        F2CHuaweiSubnet f2CHuaweiSubnet = new F2CHuaweiSubnet();
        f2CHuaweiSubnet.setUuid(subnet.getId());
        f2CHuaweiSubnet.setCidr(subnet.getCidr());
        f2CHuaweiSubnet.setCidrV6(subnet.getCidrV6());
        f2CHuaweiSubnet.setName(subnet.getName());
        f2CHuaweiSubnet.setVpcId(subnet.getVpcId());
        f2CHuaweiSubnet.setDescription(subnet.getDescription());
        f2CHuaweiSubnet.setAvailabilityZone(subnet.getAvailabilityZone());
        return f2CHuaweiSubnet;
    }

    public static F2CHuaweiVpc toF2CHuaweiVpc(Vpc vpc) {
        F2CHuaweiVpc f2CHuaweiVpc = new F2CHuaweiVpc();
        f2CHuaweiVpc.setUuid(vpc.getId());
        f2CHuaweiVpc.setCidr(vpc.getCidr());
        f2CHuaweiVpc.setName(vpc.getName());
        f2CHuaweiVpc.setStatus(vpc.getStatus().getValue());
        f2CHuaweiVpc.setDescription(vpc.getDescription());
        return f2CHuaweiVpc;
    }

    public static F2CHuaweiSecurityGroups toF2CHuaweiSecurityGroups(SecurityGroup securityGroup) {
        F2CHuaweiSecurityGroups f2CHuaweiSecurityGroups = new F2CHuaweiSecurityGroups();
        f2CHuaweiSecurityGroups.setUuid(securityGroup.getId());
        f2CHuaweiSecurityGroups.setName(securityGroup.getName());
        f2CHuaweiSecurityGroups.setDescription(securityGroup.getDescription());
        f2CHuaweiSecurityGroups.setVpc_id(securityGroup.getVpcId());
        return f2CHuaweiSecurityGroups;
    }
}
