package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.cms20190101.models.DescribeMetricListRequest;
import com.aliyun.cms20190101.models.DescribeMetricListResponse;
import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunDiskType;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunPerfMetricConstants;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;
import com.fit2cloud.provider.impl.aliyun.util.AliyunMappingUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.ajax.JSON;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author:张少虎
 * @Date: 2022/9/21  8:43 AM
 * @Version 1.0
 * @注释:
 */
public class AliyunSyncCloudApi {
    /**
     * 获取阿里云云主机数据
     *
     * @param listVirtualMachineRequest 获取阿里云云主机请求对象
     * @return 云主机对象
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ListVirtualMachineRequest listVirtualMachineRequest) {
        if (StringUtils.isEmpty(listVirtualMachineRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(listVirtualMachineRequest.getCredential())) {
            AliyunVmCredential credential = JsonUtil.parseObject(listVirtualMachineRequest.getCredential(), AliyunVmCredential.class);
            listVirtualMachineRequest.setPageNumber(PageUtil.DefaultCurrentPage);
            listVirtualMachineRequest.setPageSize(PageUtil.DefaultPageSize);
            Client client = credential.getClientByRegion(listVirtualMachineRequest.getRegionId());
            // 分页查询云主机列表
            List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance> instance = PageUtil.page(listVirtualMachineRequest, req -> describeInstancesWithOptions(client, req), res -> res.getBody().getInstances().instance, (req, res) -> res.getBody().getPageSize() <= res.getBody().getInstances().instance.size(), req -> req.setPageNumber(req.getPageNumber() + 1));
            return instance.stream().map(AliyunMappingUtil::toF2CVirtualMachine).map(f2CVirtualMachine -> appendDisk(listVirtualMachineRequest.getCredential(), f2CVirtualMachine)).map(f2CVirtualMachine -> appendInstanceType(listVirtualMachineRequest.getCredential(), f2CVirtualMachine)).toList();
        }
        return new ArrayList<>();
    }

    /**
     * 获取阿里云磁盘数据
     *
     * @param listDescribeDisksRequest 阿里云磁盘请求对象
     * @return 磁盘数据
     */
    public static List<F2CDisk> listDisk(ListDisksRequest listDescribeDisksRequest) {
        if (StringUtils.isEmpty(listDescribeDisksRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(listDescribeDisksRequest.getCredential())) {
            Client client = JsonUtil.parseObject(listDescribeDisksRequest.getCredential(), AliyunVmCredential.class).getClientByRegion(listDescribeDisksRequest.getRegionId());
            listDescribeDisksRequest.setPageSize(PageUtil.DefaultPageSize);
            listDescribeDisksRequest.setPageNumber(PageUtil.DefaultCurrentPage);
            List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disk = PageUtil.page(listDescribeDisksRequest, req -> describeDisksWithOptions(client, req), res -> res.getBody().getDisks().disk, (req, res) -> res.getBody().getPageSize() <= res.getBody().disks.disk.size(), req -> req.setPageNumber(req.getPageNumber() + 1));
            return disk.stream().map(AliyunMappingUtil::toF2CDisk).toList();
        }
        throw new Fit2cloudException(10001, "认证信息不存在");
    }

    /**
     * 获取镜像数据
     *
     * @param listImageRequest 请求对象
     * @return 镜像数据
     */
    public static List<F2CImage> listImage(ListImageRequest listImageRequest) {
        if (StringUtils.isEmpty(listImageRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(listImageRequest.getCredential())) {
            Client client = JsonUtil.parseObject(listImageRequest.getCredential(), AliyunVmCredential.class).getClientByRegion(listImageRequest.getRegionId());
            listImageRequest.setPageSize(PageUtil.DefaultPageSize);
            listImageRequest.setPageNumber(PageUtil.DefaultCurrentPage);
            List<DescribeImagesResponseBody.DescribeImagesResponseBodyImagesImage> images = PageUtil.page(listImageRequest, req -> describeImagesWithOptions(client, req), res -> res.getBody().images.image, (req, res) -> res.getBody().getPageSize() <= res.getBody().images.image.size(), req -> req.setPageNumber(req.getPageNumber() + 1));
            return images.stream().map(image -> AliyunMappingUtil.toF2CImage(image, listImageRequest.regionId)).toList();
        }
        throw new Fit2cloudException(10001, "认证信息不存在");
    }


    /**
     * 获取实例类型
     *
     * @param listInstanceTypesRequest 实例类型请求参数
     * @return 返回值
     */
    public static List<DescribeInstanceTypesResponseBody.DescribeInstanceTypesResponseBodyInstanceTypesInstanceType> listInstanceType(ListInstanceTypesRequest listInstanceTypesRequest) {
        AliyunVmCredential credential1 = JsonUtil.parseObject(listInstanceTypesRequest.getCredential(), AliyunVmCredential.class);
        try {
            DescribeInstanceTypesResponse describeInstanceTypesResponse = credential1.getClient().describeInstanceTypesWithOptions(listInstanceTypesRequest, new RuntimeOptions());
            return describeInstanceTypesResponse.getBody().instanceTypes.instanceType;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取磁盘数据
     *
     * @param client 客户端
     * @param req    请求参数
     * @return 磁盘返回值
     */
    public static DescribeDisksResponse describeDisksWithOptions(Client client, ListDisksRequest req) {
        try {
            return client.describeDisksWithOptions(req, new RuntimeOptions());
        } catch (Exception e) {
            SkipPageException.throwSkip(e);
            ReTryException.throwReTry(e);
            throw new Fit2cloudException(1002, "获取磁盘异常" + e.getMessage());
        }
    }

    /**
     * 获取镜像数据
     *
     * @param client 客户端
     * @param req    请求参数
     * @return 镜像数据返回值
     */
    public static DescribeImagesResponse describeImagesWithOptions(Client client, ListImageRequest req) {
        try {
            return client.describeImagesWithOptions(req, new RuntimeOptions());
        } catch (Exception e) {
            SkipPageException.throwSkip(e);
            ReTryException.throwReTry(e);
            throw new Fit2cloudException(1002, "获取磁盘异常" + e.getMessage());
        }
    }

    /**
     * 获取阿里云实例函数
     *
     * @param client  阿里云客户端
     * @param request 请求参数
     * @return 阿里云
     */
    private static DescribeInstancesResponse describeInstancesWithOptions(Client client, ListVirtualMachineRequest request) {
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            return client.describeInstancesWithOptions(request, runtime);
        } catch (Exception e) {
            ReTryException.throwReTry(e);
            SkipPageException.throwSkip(e);
            throw new Fit2cloudException(10002, "获取阿里云云主机列表失败" + e.getMessage());
        }
    }

    /**
     * 给F2CVirtualMachine 添加磁盘相关信息
     *
     * @param credential        认证信息
     * @param f2CVirtualMachine 实例对象
     * @return 添加后的实例对象
     */
    private static F2CVirtualMachine appendDisk(String credential, F2CVirtualMachine f2CVirtualMachine) {
        ListDisksRequest listDescribeDisksRequest = new ListDisksRequest();
        listDescribeDisksRequest.setCredential(credential);
        listDescribeDisksRequest.setInstanceId(f2CVirtualMachine.getInstanceId());
        listDescribeDisksRequest.setRegionId(f2CVirtualMachine.getRegion());
        List<F2CDisk> f2CDisks = listDisk(listDescribeDisksRequest);
        LongSummaryStatistics collect = f2CDisks.stream().collect(Collectors.summarizingLong(F2CDisk::getSize));
        f2CVirtualMachine.setDisk((int) collect.getSum());
        return f2CVirtualMachine;
    }

    /**
     * 给F2CVirtualMachine 添加实例类型相关参数
     *
     * @param credential        认证信息
     * @param f2CVirtualMachine 实例对象
     * @return 添加后的实例对象
     */
    private static F2CVirtualMachine appendInstanceType(String credential, F2CVirtualMachine f2CVirtualMachine) {
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

    public static boolean powerOff(AliyunInstanceRequest aliyunInstanceRequest) {
        if (StringUtils.isEmpty(aliyunInstanceRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(aliyunInstanceRequest.getCredential())) {
            AliyunVmCredential credential = JsonUtil.parseObject(aliyunInstanceRequest.getCredential(), AliyunVmCredential.class);
            Client client = credential.getClientByRegion(aliyunInstanceRequest.getRegionId());
            com.aliyun.ecs20140526.models.StopInstancesRequest stopInstancesRequest = new com.aliyun.ecs20140526.models.StopInstancesRequest();
            stopInstancesRequest.setRegionId(aliyunInstanceRequest.getRegionId());
            stopInstancesRequest.setForceStop(aliyunInstanceRequest.getForce());
            stopInstancesRequest.setInstanceId(Arrays.asList(aliyunInstanceRequest.getUuid()));
            try {
                client.stopInstancesWithOptions(stopInstancesRequest, new RuntimeOptions());
                DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
                describeInstanceStatusRequest.setRegionId(aliyunInstanceRequest.getRegionId());
                checkStatus(client, "Stopped", describeInstanceStatusRequest);
                return true;
            } catch (TeaException error) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_OFF_FAIL.getCode(), error.getMessage());
            } catch (Exception _error) {
                TeaException error = new TeaException(_error.getMessage(), _error);
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_OFF_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    public static boolean powerOn(AliyunInstanceRequest aliyunInstanceRequest) {
        if (StringUtils.isEmpty(aliyunInstanceRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(aliyunInstanceRequest.getCredential())) {
            AliyunVmCredential credential = JsonUtil.parseObject(aliyunInstanceRequest.getCredential(), AliyunVmCredential.class);
            Client client = credential.getClientByRegion(aliyunInstanceRequest.getRegionId());
            StartInstancesRequest startInstancesRequest = new StartInstancesRequest();
            startInstancesRequest.setRegionId(aliyunInstanceRequest.getRegionId());
            startInstancesRequest.setInstanceId(Arrays.asList(aliyunInstanceRequest.getUuid()));
            try {
                client.startInstances(startInstancesRequest);
                DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
                describeInstanceStatusRequest.setRegionId(aliyunInstanceRequest.getRegionId());
                checkStatus(client, "Running", describeInstanceStatusRequest);
                return true;
            } catch (TeaException error) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_ON_FAIL.getCode(), error.getMessage());
            } catch (Exception _error) {
                TeaException error = new TeaException(_error.getMessage(), _error);
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_ON_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    public static boolean rebootInstance(AliyunInstanceRequest aliyunInstanceRequest) {
        if (StringUtils.isEmpty(aliyunInstanceRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(aliyunInstanceRequest.getCredential())) {
            AliyunVmCredential credential = JsonUtil.parseObject(aliyunInstanceRequest.getCredential(), AliyunVmCredential.class);
            Client client = credential.getClientByRegion(aliyunInstanceRequest.getRegionId());
            RebootInstancesRequest rebootInstancesRequest = new RebootInstancesRequest();
            rebootInstancesRequest.setRegionId(aliyunInstanceRequest.getRegionId());
            // TODO 强制重启
            rebootInstancesRequest.setForceReboot(aliyunInstanceRequest.getForce());
            rebootInstancesRequest.setInstanceId(Arrays.asList(aliyunInstanceRequest.getUuid()));
            try {
                client.rebootInstances(rebootInstancesRequest);
                DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
                describeInstanceStatusRequest.setRegionId(aliyunInstanceRequest.getRegionId());
                checkStatus(client, "Running", describeInstanceStatusRequest);
                return true;
            } catch (TeaException error) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_REBOOT_FAIL.getCode(), error.getMessage());
            } catch (Exception _error) {
                TeaException error = new TeaException(_error.getMessage(), _error);
                throw new Fit2cloudException(ErrorCodeConstants.VM_REBOOT_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    public static boolean deleteInstance(AliyunInstanceRequest aliyunInstanceRequest) {
        if (StringUtils.isEmpty(aliyunInstanceRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(aliyunInstanceRequest.getCredential())) {
            AliyunVmCredential credential = JsonUtil.parseObject(aliyunInstanceRequest.getCredential(), AliyunVmCredential.class);
            Client client = credential.getClientByRegion(aliyunInstanceRequest.getRegionId());
            DeleteInstancesRequest deleteInstancesRequest = new DeleteInstancesRequest();
            deleteInstancesRequest.setRegionId(aliyunInstanceRequest.getRegionId());
            // TODO 强制删除
            deleteInstancesRequest.setForce(aliyunInstanceRequest.getForce());
            deleteInstancesRequest.setInstanceId(Arrays.asList(aliyunInstanceRequest.getUuid()));
            try {
                client.deleteInstances(deleteInstancesRequest);
                return true;
            } catch (TeaException error) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_DELETE_FAIL.getCode(), error.getMessage());
            } catch (Exception _error) {
                TeaException error = new TeaException(_error.getMessage(), _error);
                throw new Fit2cloudException(ErrorCodeConstants.VM_DELETE_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    private static void checkStatus(Client client, String status, DescribeInstanceStatusRequest describeInstanceStatusRequest) throws Exception {
        int count = 0;
        while (true) {
            DescribeInstanceStatusResponse response = client.describeInstanceStatus(describeInstanceStatusRequest);
            if (StringUtils.equalsIgnoreCase(response.getBody().getInstanceStatuses().getInstanceStatus().get(0).getStatus(), status)) {
                break;
            }
            if (count < 40) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    /**
     * 查询可用区支持的磁盘类型
     *
     * @param req
     * @return
     */
    public static List<Map<String, String>> getDiskTypes(AliyunGetDiskTypeRequest req) {
        List<Map<String, String>> diskTypes = new ArrayList<>();
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeZonesRequest describeZonesRequest = new DescribeZonesRequest();
        describeZonesRequest.setRegionId(req.getRegionId());
        describeZonesRequest.setVerbose(false); // 不展示详细信息

        try {
            List<DescribeZonesResponseBody.DescribeZonesResponseBodyZonesZone> zones = client.describeZones(describeZonesRequest).getBody().zones.zone;
            for (DescribeZonesResponseBody.DescribeZonesResponseBodyZonesZone zone : zones) {
                if (zone.getZoneId().equalsIgnoreCase(req.getZone())) {
                    for (String availableDiskCategory : zone.availableDiskCategories.diskCategories) {
                        Map<String, String> diskType = new HashMap<>();
                        diskType.put("id", availableDiskCategory);
                        diskType.put("name", AliyunDiskType.getName(availableDiskCategory));
                        diskTypes.add(diskType);
                    }
                }
            }
            return diskTypes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get disk type! " + e.getMessage(), e);
        }
    }

    /**
     * 创建单块磁盘
     * 如果不挂载默认创建出来的都是后付费的，如果挂载到机器则该机型需要时预付费的，对应创建出来的磁盘也是预付费的，
     * 后付费的机器，可以分为两步。1.先创建出来一块空盘 2.调用挂载接口
     * 自动挂载(只有预付费的才能自动挂载，)
     * https://next.api.aliyun.com/api/Ecs/2014-05-26/CreateDisk
     *
     * @param request 请求对象
     * @return 镜像数据
     */
    public static F2CDisk createDisk(AliyunCreateDiskRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                Client client = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();
                String chargeType = null;
                Boolean isAttached = request.getIsAttached();
                if (isAttached) {
                    DescribeInstancesResponse describeInstancesResponse = client.describeInstances(request.toDescribeInstancesRequest());
                    if (describeInstancesResponse.getBody().getInstances().getInstance().size() == 0) {
                        throw new RuntimeException("Instance not found！");
                    }
                    chargeType = describeInstancesResponse.getBody().getInstances().getInstance().get(0).getInstanceChargeType();
                }
                CreateDiskRequest createDiskRequest = request.toCreateDiskRequest(chargeType);
                CreateDiskResponse createDiskResponse = client.createDisk(createDiskRequest);
                F2CDisk createdDisk = checkDiskStatus(client, request.toDescribeDisksRequest(createDiskResponse.getBody().diskId), F2CDiskStatus.AVAILABLE);
                if (isAttached && "PostPaid".equals(chargeType)) {
                    AliyunAttachDiskRequest attachDiskRequest = new AliyunAttachDiskRequest();
                    BeanUtils.copyProperties(request, attachDiskRequest);
                    attachDiskRequest.setDiskId(createdDisk.getDiskId());
                    attachDisk(attachDiskRequest);
                }
                return createdDisk;
            } else {
                throw new RuntimeException("RegionId or credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 创建磁盘
     * 如果不挂载默认创建出来的都是后付费的，如果挂载到机器则该机型需要时预付费的，对应创建出来的磁盘也是预付费的，
     * 后付费的机器，可以分为两步。1.先创建出来一块空盘 2.调用挂载接口
     * 自动挂载(只有预付费的才能自动挂载，)
     * https://next.api.aliyun.com/api/Ecs/2014-05-26/CreateDisk
     *
     * @param request 请求对象
     * @return 镜像数据
     */
    public static List<F2CDisk> createDisks(AliyunCreateDisksRequest request) {
        List<F2CDisk> f2CDisks = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                Client client = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();
                CreateDiskRequest createDiskRequest = request.toCreateDiskRequest();
                for (F2CDisk f2CDisk : request.getDisks()) {
                    createDiskRequest.setDiskName(f2CDisk.getDiskName());
                    createDiskRequest.setSize((int) f2CDisk.getSize());
                    createDiskRequest.setDiskCategory(f2CDisk.getDiskType());
                    //自动挂载 (zoneId 和instanceId不可同时存在)
                    if (f2CDisk.isBootable()) {
                        createDiskRequest.setInstanceId(f2CDisk.getInstanceUuid());
                    } else {
                        createDiskRequest.setZoneId(f2CDisk.getZone());
                    }
                    CreateDiskResponse createDiskResponse = client.createDisk(createDiskRequest);
                    f2CDisk.setDiskId(createDiskResponse.getBody().diskId);
                    f2CDisk.setRegion(request.getRegionId());
                    F2CDisk createdDisk = checkDiskStatus(client, toDescribeDisksRequest(f2CDisk), F2CDiskStatus.AVAILABLE);
                    f2CDisks.add(createdDisk);
                }
                return f2CDisks;
            } else {
                throw new RuntimeException("RegionId or credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static DescribeDisksRequest toDescribeDisksRequest(F2CDisk f2CDisk) {
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
        describeDisksRequest.setRegionId(f2CDisk.getRegion());
        describeDisksRequest.setZoneId(f2CDisk.getZone());
        describeDisksRequest.setDiskIds(JSON.toString(new String[]{f2CDisk.getDiskId()}));
        return describeDisksRequest;
    }

    private static F2CDisk checkDiskStatus(Client client, DescribeDisksRequest describeDisksRequest, String expectStatus) {
        try {
            int count = 0;
            while (true) {
                Thread.sleep(5000);
                DescribeDisksResponse describeDisksResponse = client.describeDisks(describeDisksRequest);
                List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disks = describeDisksResponse.getBody().getDisks().getDisk();
                if (disks.size() > 0 && disks.get(0).getStatus().equalsIgnoreCase(expectStatus)) {
                    return AliyunMappingUtil.toF2CDisk(disks.get(0));
                }
                if (count >= 40) {
                    throw new Exception("Check cloud disk status timeout！");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricList(GetMetricsRequest getMetricsRequest) {
        if (StringUtils.isEmpty(getMetricsRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        //设置时间，根据interval,默认一个小时
        getMetricsRequest.setStartTime(String.valueOf(DateUtil.getBeforeHourTime(getMetricsRequest.getInterval())));
        getMetricsRequest.setEndTime(String.valueOf(System.currentTimeMillis()));
        System.out.println("开始时间：" + getMetricsRequest.getStartTime());
        System.out.println("结束时间：" + getMetricsRequest.getEndTime());
        try {
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            result.addAll(getVmPerfMetric(getMetricsRequest));
        } catch (Exception e) {
            throw new Fit2cloudException(100021, "获取监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取虚拟机监控指标数据
     *
     * @param getMetricsRequest
     * @return
     */
    private static List<F2CPerfMetricMonitorData> getVmPerfMetric(GetMetricsRequest getMetricsRequest) {
        AliyunVmCredential credential = JsonUtil.parseObject(getMetricsRequest.getCredential(), AliyunVmCredential.class);
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        //查询所有虚拟机
        ListVirtualMachineRequest listVirtualMachineRequest = new ListVirtualMachineRequest();
        listVirtualMachineRequest.setCredential(getMetricsRequest.getCredential());
        listVirtualMachineRequest.setRegion(getMetricsRequest.getRegionId());
        List<F2CVirtualMachine> vms = listVirtualMachine(listVirtualMachineRequest);
        if (vms.size() == 0) {
            return result;
        }
        //查询监控指标数据参数
        ///TODO 由于我们只查询一个小时内的数据，时间间隔是60s,所以查询每台机器的监控数据的时候最多不过60条数据，所以不需要分页查询
        Map<String, String> dimension = new HashMap<>();
        DescribeMetricListRequest describeMetricListRequest = new DescribeMetricListRequest()
                .setNamespace("acs_ecs_dashboard")
                .setPeriod(String.valueOf(getMetricsRequest.getPeriod()))
                .setEndTime(getMetricsRequest.getEndTime())
                .setStartTime(getMetricsRequest.getStartTime())
                .setRegionId(getMetricsRequest.getRegionId());
        com.aliyun.cms20190101.Client cmsClient = credential.getCmsClientByRegion(getMetricsRequest.getRegionId());
        vms.forEach(vm -> {
            dimension.put("instanceId", vm.getInstanceUUID());
            //监控指标
            Arrays.stream(AliyunPerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().collect(Collectors.toList()).forEach(perfMetric -> {
                describeMetricListRequest.setDimensions(JsonUtil.toJSONString(Arrays.asList(dimension)));
                describeMetricListRequest.setMetricName(perfMetric.getMetricName());
                try {
                    //查询监控指标数据
                    DescribeMetricListResponse response = cmsClient.describeMetricListWithOptions(describeMetricListRequest, new RuntimeOptions());
                    if (StringUtils.equalsIgnoreCase(response.getBody().getCode(), "200") && !StringUtils.isBlank(response.getBody().getDatapoints())) {
                        ArrayNode jsonArray = JsonUtil.parseArray(response.getBody().getDatapoints());
                        jsonArray.forEach(v -> {
                            F2CPerfMetricMonitorData f2CEntityPerfMetric = AliyunMappingUtil.toF2CPerfMetricMonitorData(v);
                            f2CEntityPerfMetric.setEntityType(F2CEntityType.VIRTUAL_MACHINE.name());
                            f2CEntityPerfMetric.setMetricName(perfMetric.name());
                            f2CEntityPerfMetric.setPeriod(getMetricsRequest.getPeriod());
                            f2CEntityPerfMetric.setInstanceId(vm.getInstanceUUID());
                            f2CEntityPerfMetric.setUnit(perfMetric.getUnit());
                            result.add(f2CEntityPerfMetric);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });
        return result;
    }

    /**
     * 扩容磁盘
     *
     * @return
     */
    public static boolean enlargeDisk(AliyunResizeDiskRequest request) {
        try {
            Client aliyunClient = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();
            DescribeDisksResponse describeDisksResponse = aliyunClient.describeDisks(request.toDescribeDisksRequest());
            if (describeDisksResponse.getBody().getDisks().getDisk().size() == 0) {
                throw new RuntimeException("Disk not found！");
            }
            String diskStatus = describeDisksResponse.getBody().getDisks().getDisk().get(0).getStatus();
            // 磁盘类型：系统盘（system） or 数据盘（data）
            String diskType = describeDisksResponse.getBody().getDisks().getDisk().get(0).getType();
            ResizeDiskRequest resizeDiskRequest = request.toResizeDiskRequest();
            if (StringUtils.isNotEmpty(request.getInstanceUuid())) {
                DescribeInstancesResponse describeInstancesResponse = aliyunClient.describeInstances(request.toDescribeInstancesRequest());
                if (describeInstancesResponse.getBody().getInstances().getInstance().size() == 0) {
                    throw new RuntimeException("Instance not found！");
                }
                String instanceStatus = describeInstancesResponse.getBody().getInstances().getInstance().get(0).getStatus();
                if (!("running").equalsIgnoreCase(instanceStatus) && !("stopped").equalsIgnoreCase(instanceStatus)) {
                    throw new RuntimeException("The state of the instance must be running or stopped.");
                }

                if (("running").equalsIgnoreCase(instanceStatus)) {
                    if ("system".equalsIgnoreCase(diskType)) {
                        resizeDiskRequest.setType("online");
                    }
                    if (("data").equalsIgnoreCase(diskType) && diskStatus.equalsIgnoreCase(F2CDiskStatus.IN_USE)) {
                        resizeDiskRequest.setType("online");
                    }
                }
            }
            ResizeDiskResponse resizeDiskResponse = aliyunClient.resizeDisk(resizeDiskRequest);
            checkDiskStatus(aliyunClient, request.toDescribeDisksRequest(), diskStatus);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 挂载磁盘
     *
     * @return
     */
    public static boolean attachDisk(AliyunAttachDiskRequest request) {
        try {
            Client aliyunClient = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();
            DescribeDisksResponse describeDisksResponse = aliyunClient.describeDisks(request.toDescribeDisksRequest());
            if (describeDisksResponse.getBody().getDisks().getDisk().size() == 0) {
                throw new RuntimeException("Disk not found！");
            }
            String diskStatus = describeDisksResponse.getBody().getDisks().getDisk().get(0).getStatus();
            if (!diskStatus.equalsIgnoreCase(F2CDiskStatus.AVAILABLE)) {
                throw new RuntimeException("The status of the cloud disk must be available！");
            }
            DescribeInstancesResponse describeInstancesResponse = aliyunClient.describeInstances(request.toDescribeInstancesRequest());
            if (describeInstancesResponse.getBody().getInstances().getInstance().size() == 0) {
                throw new RuntimeException("Instance not found！");
            }

            // 阿里云无法在一台机器上并发挂载多块盘
            synchronized (request.getInstanceUuid().intern()) {
                String instanceStatus = describeInstancesResponse.getBody().getInstances().getInstance().get(0).getStatus();
                if (instanceStatus.equalsIgnoreCase("running") || instanceStatus.equalsIgnoreCase("stopped")) {
                    aliyunClient.attachDisk(request.toAttachDiskRequest());
                    checkDiskStatus(aliyunClient, request.toDescribeDisksRequest(), F2CDiskStatus.IN_USE);
                    return true;
                } else {
                    throw new RuntimeException("The state of the instance must be running or stopped.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 卸载磁盘
     *
     * @return
     */
    public static boolean detachDisk(AliyunDetachDiskRequest request) {
        try {
            Client aliyunClient = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();

            String instanceUuid = request.getInstanceUuid();
            if (StringUtils.isEmpty(instanceUuid)) {
                throw new RuntimeException("The instanceUuid can not be null！");
            }

            // 阿里云无法在一台机器上并发卸载多块盘
            synchronized (instanceUuid.intern()) {
                DescribeDisksResponse describeDisksResponse = aliyunClient.describeDisks(request.toDescribeDisksRequest());
                if (describeDisksResponse.getBody().getDisks().getDisk().size() == 0) {
                    throw new RuntimeException("Disk not found！");
                }
                String diskStatus = describeDisksResponse.getBody().getDisks().getDisk().get(0).getStatus();
                if (!diskStatus.equalsIgnoreCase(F2CDiskStatus.IN_USE)) {
                    throw new RuntimeException("The status of the cloud disk must be In_Use！");
                }
                DescribeInstancesResponse describeInstancesResponse = aliyunClient.describeInstances(request.toDescribeInstancesRequest());
                if (describeInstancesResponse.getBody().getInstances().getInstance().size() == 0) {
                    throw new RuntimeException("Instance not found！");
                }
                String instanceStatus = describeInstancesResponse.getBody().getInstances().getInstance().get(0).getStatus();
                if (instanceStatus.equalsIgnoreCase("running") || instanceStatus.equalsIgnoreCase("stopped")) {
                    aliyunClient.detachDisk(request.toDetachDisksRequest());
                    checkDiskStatus(aliyunClient, request.toDescribeDisksRequest(), F2CDiskStatus.AVAILABLE);
                    return true;
                } else {
                    throw new RuntimeException("The state of the instance must be running or stopped.");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 删除磁盘
     *
     * @return
     */
    public static boolean deleteDisk(AliyunDeleteDiskRequest request) {
        try {
            Client client = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();
            DescribeDisksResponse describeDisksResponse = client.describeDisks(request.toDescribeDisksRequest());
            if (describeDisksResponse.getBody().getDisks().getDisk().size() == 0) {
                return true;
            }
            String diskStatus = describeDisksResponse.getBody().getDisks().getDisk().get(0).getStatus();
            if (!diskStatus.equalsIgnoreCase(F2CDiskStatus.AVAILABLE)) {
                throw new RuntimeException("The status of the cloud disk must be Available！");
            }
            client.deleteDisk(request.toDeleteDiskRequest());
            int count = 0;
            while (true) {
                Thread.sleep(5000);
                describeDisksResponse = client.describeDisks(request.toDescribeDisksRequest());
                if (describeDisksResponse.getBody().getDisks().getDisk().size() == 0) {
                    return true;
                }
                if (count >= 40) {
                    throw new Exception("Check cloud disk status timeout！");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
