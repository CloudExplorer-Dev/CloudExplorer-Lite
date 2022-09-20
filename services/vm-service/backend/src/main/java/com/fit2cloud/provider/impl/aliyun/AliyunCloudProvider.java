package com.fit2cloud.provider.impl.aliyun;

import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListDisksRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListInstanceTypesRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListVirtualMachineRequest;
import com.fit2cloud.provider.impl.constants.DeleteWithInstance;
import com.fit2cloud.provider.impl.constants.F2CDiskStatus;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:55 AM
 * @Version 1.0
 * @注释:
 */
public class AliyunCloudProvider extends AbstractCloudProvider<AliyunVmCredential> implements ICloudProvider {


    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    public List<F2CVirtualMachine> listVirtualMachine(ListVirtualMachineRequest listVirtualMachineRequest) {

        String credential = listVirtualMachineRequest.getCredential();
        AliyunVmCredential credential1 = getCredential(credential);
        RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            DescribeInstancesResponse describeInstancesResponse = credential1.getClient().describeInstancesWithOptions(listVirtualMachineRequest, runtime);
            List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance> instance = describeInstancesResponse.getBody().instances.instance;
            List<F2CVirtualMachine> f2CVirtualMachines = instance.stream().map(this::toF2CVirtualMachine).map(f2CVirtualMachine -> this.appendDisk(credential, f2CVirtualMachine)).map(f2CVirtualMachine -> this.appendInstanceType(credential, f2CVirtualMachine)).toList();
            return f2CVirtualMachines;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private F2CVirtualMachine appendDisk(String credential, F2CVirtualMachine f2CVirtualMachine) {
        ListDisksRequest listDescribeDisksRequest = new ListDisksRequest();
        listDescribeDisksRequest.setCredential(credential);
        listDescribeDisksRequest.setInstanceId(f2CVirtualMachine.getInstanceId());
        listDescribeDisksRequest.setRegionId(f2CVirtualMachine.getRegion());
        List<F2CDisk> f2CDisks = listDisk(listDescribeDisksRequest);
        LongSummaryStatistics collect = f2CDisks.stream().collect(Collectors.summarizingLong(F2CDisk::getSize));
        f2CVirtualMachine.setDisk(collect.getSum());
        return f2CVirtualMachine;
    }

    private F2CVirtualMachine appendInstanceType(String credential, F2CVirtualMachine f2CVirtualMachine) {
        ListInstanceTypesRequest listInstanceTypesRequest = new ListInstanceTypesRequest();
        listInstanceTypesRequest.setCredential(credential);
        listInstanceTypesRequest.setInstanceTypes(new ArrayList<>() {{
            add(f2CVirtualMachine.getInstanceType());
        }});
        List<DescribeInstanceTypesResponseBody.DescribeInstanceTypesResponseBodyInstanceTypesInstanceType> describeInstanceTypesResponseBodyInstanceTypesInstanceTypes = listInstanceType(listInstanceTypesRequest);
        describeInstanceTypesResponseBodyInstanceTypesInstanceTypes.stream().findFirst().ifPresent(instanceType -> {
            f2CVirtualMachine.setCpu(instanceType.getCpuCoreCount());
            int memory = Math.round(instanceType.getMemorySize());
            f2CVirtualMachine.setMemory(memory);
            f2CVirtualMachine.setInstanceTypeDescription(instanceType.getCpuCoreCount() + "vCPU " + memory + "GB");
        });
        return f2CVirtualMachine;
    }

    private List<DescribeInstanceTypesResponseBody.DescribeInstanceTypesResponseBodyInstanceTypesInstanceType> listInstanceType(ListInstanceTypesRequest listInstanceTypesRequest) {
        AliyunVmCredential credential1 = getCredential(listInstanceTypesRequest.getCredential());
        try {
            DescribeInstanceTypesResponse describeInstanceTypesResponse = credential1.getClient().describeInstanceTypesWithOptions(listInstanceTypesRequest, new RuntimeOptions());
            List<DescribeInstanceTypesResponseBody.DescribeInstanceTypesResponseBodyInstanceTypesInstanceType> instanceType = describeInstanceTypesResponse.getBody().instanceTypes.instanceType;
            return instanceType;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将DescribeInstancesResponseBodyInstancesInstance对象转换为F2CVirtualMachine对象
     *
     * @param instance 阿里云实例对象
     * @return 云管公共对象
     */
    private F2CVirtualMachine toF2CVirtualMachine(DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance) {
        F2CVirtualMachine f2CVirtualMachine = new F2CVirtualMachine();
        f2CVirtualMachine.setCpu(instance.getCpu());
        f2CVirtualMachine.setRegion(instance.getRegionId());
        f2CVirtualMachine.setHostname(instance.getHostName());
        f2CVirtualMachine.setImageId(instance.getImageId());
        f2CVirtualMachine.setInstanceId(instance.getInstanceId());
        f2CVirtualMachine.setProjectId(instance.getResourceGroupId());
        f2CVirtualMachine.setInstanceUUID(instance.getInstanceId());
        f2CVirtualMachine.setInstanceStatus(instance.getStatus());
        String instanceType = instance.getInstanceType();
        f2CVirtualMachine.setInstanceType(instanceType);
        f2CVirtualMachine.setInstanceChargeType(instance.getInstanceChargeType());
        List<String> ipArray = new ArrayList<>();
        DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstancePublicIpAddress publicIpAddress = instance.getPublicIpAddress();
        List<String> ipAddress = publicIpAddress.getIpAddress();
        if (ipAddress.size() > 0) {
            f2CVirtualMachine.setRemoteIP(ipAddress.get(0));
            addIpToArray(ipArray, ipAddress);
        }
        if (instance.getEipAddress() != null) {
            if (instance.getEipAddress().getIpAddress() != null && instance.getEipAddress().getIpAddress().trim().length() > 0) {
                f2CVirtualMachine.setRemoteIP(instance.getEipAddress().getIpAddress());
                ipArray.add(instance.getEipAddress().getIpAddress());
            }
        }
        DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstanceInnerIpAddress innerIpAddress = instance.getInnerIpAddress();
        List<String> innerIpList = innerIpAddress.getIpAddress();
        if (innerIpList.size() > 0) {
            f2CVirtualMachine.setLocalIP(innerIpList.get(0));
            addIpToArray(ipArray, innerIpList);
        } else {
            DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstanceVpcAttributes vpcAttributes = instance.getVpcAttributes();
            DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstanceVpcAttributesPrivateIpAddress privateIpAddress = vpcAttributes.getPrivateIpAddress();
            if (CollectionUtils.isNotEmpty(privateIpAddress.ipAddress)) {
                f2CVirtualMachine.setLocalIP(privateIpAddress.ipAddress.get(0));
                addIpToArray(ipArray, privateIpAddress.ipAddress);
            }
        }
        f2CVirtualMachine.setIpArray(ipArray);

        f2CVirtualMachine.setName(instance.getInstanceName());
        if (StringUtils.isEmpty(instance.getInstanceName())) {
            f2CVirtualMachine.setName(instance.getInstanceId());
        }
        f2CVirtualMachine.setOs(instance.getOSName());
        f2CVirtualMachine.setZone(instance.getZoneId());

        instance.setCreationTime(null);
        instance.setDescription(null);
        instance.setInstanceId(null);
        instance.setInstanceName(null);
        instance.setHostName(null);
        instance.setStatus(null);
        instance.setInnerIpAddress(null);
        instance.setImageId(null);
        f2CVirtualMachine.setCustomData(new Gson().toJson(instance));
        f2CVirtualMachine.setVpcId(instance.getVpcAttributes().getVpcId());
        f2CVirtualMachine.setSubnetId(instance.getVpcAttributes().getVSwitchId());
        return f2CVirtualMachine;
    }

    private static void addIpToArray(List<String> ipArray, List<String> ipList) {
        for (String ip : ipList) {
            if (ip.contains(".")) {
                ipArray.add(ip);
            }
        }
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return null;
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return listDisk(JsonUtil.parseObject(req, ListDisksRequest.class));
    }

    private List<F2CDisk> listDisk(ListDisksRequest listDescribeDisksRequest) {

        if (StringUtils.isNotEmpty(listDescribeDisksRequest.getCredential())) {
            Client client = getCredential(listDescribeDisksRequest.getCredential()).getClient();
            try {
                DescribeDisksResponse describeDisksResponse = client.describeDisksWithOptions(listDescribeDisksRequest, new RuntimeOptions());
                List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disk = describeDisksResponse.getBody().getDisks().disk;
                return disk.stream().map(this::toF2CDisk).toList();
            } catch (Exception e) {
                throw new Fit2cloudException(10003, "获取磁盘数据错误");
            }
        }
        throw new Fit2cloudException(10001, "认证信息不存在");
    }


    private F2CDisk toF2CDisk(DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk disk) {
        F2CDisk f2cDisk = new F2CDisk();
        if (disk.getCategory() != null) {
            f2cDisk.setCategory(disk.getCategory());
            f2cDisk.setDiskType(disk.getCategory());
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(disk.getType()) && disk.getType().equalsIgnoreCase("system")) {
            f2cDisk.setBootable(true);
        } else {
            f2cDisk.setBootable(false);
        }
        f2cDisk.setCreateTime(getUTCTime(disk.getCreationTime()));
        f2cDisk.setDescription(disk.getDescription());
        f2cDisk.setDevice(disk.getDevice());
        f2cDisk.setDiskChargeType(disk.getDiskChargeType());
        f2cDisk.setDiskId(disk.getDiskId());
        f2cDisk.setDiskName(disk.getDiskName());
        f2cDisk.setInstanceUuid(disk.getInstanceId());
        f2cDisk.setRegion(disk.getRegionId());
        f2cDisk.setZone(disk.getZoneId());
        f2cDisk.setSize(disk.getSize());
        f2cDisk.setStatus(toF2CDiskStatus(disk.getStatus()));
        if (org.apache.commons.lang.StringUtils.isBlank(disk.getDiskName())) {
            f2cDisk.setDiskName(disk.getDiskId());
        }
        if (disk.getDeleteWithInstance()) {
            f2cDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
        } else {
            f2cDisk.setDeleteWithInstance(DeleteWithInstance.NO.name());
        }
        return f2cDisk;
    }

    public static String toF2CDiskStatus(String status) {
        if (F2CDiskStatus.ATTACHING.equalsIgnoreCase(status)) {
            return F2CDiskStatus.ATTACHING;
        } else if (F2CDiskStatus.AVAILABLE.equalsIgnoreCase(status)) {
            return F2CDiskStatus.AVAILABLE;
        } else if (F2CDiskStatus.CREATING.equalsIgnoreCase(status)) {
            return F2CDiskStatus.CREATING;
        } else if (F2CDiskStatus.DETACHING.equalsIgnoreCase(status)) {
            return F2CDiskStatus.DETACHING;
        } else if (F2CDiskStatus.IN_USE.equalsIgnoreCase(status)) {
            return F2CDiskStatus.IN_USE;
        } else if (F2CDiskStatus.REINITING.equalsIgnoreCase(status)) {
            return F2CDiskStatus.REINITING;
        } else {
            return F2CDiskStatus.UNKNOWN;
        }
    }

    private static long getUTCTime(String dateStr) {
        try {
            Calendar cal = Calendar.getInstance();
            int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
            int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = sdf.parse(dateStr);
            long time = date.getTime() + (zoneOffset + dstOffset);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
