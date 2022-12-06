package com.fit2cloud.provider.impl.tencent.api;

import com.aliyun.tea.TeaException;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.tencent.constants.TencentPerfMetricConstants;
import com.fit2cloud.provider.impl.tencent.entity.credential.TencentVmCredential;
import com.fit2cloud.provider.impl.tencent.entity.request.*;
import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.google.gson.Gson;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.cbs.v20170312.models.Placement;
import com.tencentcloudapi.cbs.v20170312.models.*;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.Image;
import com.tencentcloudapi.cvm.v20170312.models.*;
import com.tencentcloudapi.monitor.v20180724.MonitorClient;
import com.tencentcloudapi.monitor.v20180724.models.DataPoint;
import com.tencentcloudapi.monitor.v20180724.models.Dimension;
import com.tencentcloudapi.monitor.v20180724.models.GetMonitorDataRequest;
import com.tencentcloudapi.monitor.v20180724.models.GetMonitorDataResponse;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil.toF2cDiskStatus;

/**
 * @Author:张少虎
 * @Date: 2022/9/23  3:52 PM
 * @Version 1.0
 * @注释:
 */
public class TencetSyncCloudApi {

    /**
     * 获取云主机数据
     *
     * @param listVirtualMachineRequest 云主机请求参数
     * @return 云主机数据
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ListVirtualMachineRequest listVirtualMachineRequest) {
        if (StringUtils.isNotEmpty(listVirtualMachineRequest.getRegionId()) && StringUtils.isNotEmpty(listVirtualMachineRequest.getCredential())) {
            TencentVmCredential tencentVmCredential = JsonUtil.parseObject(listVirtualMachineRequest.getCredential(), TencentVmCredential.class);
            CvmClient cvmClient = tencentVmCredential.getCvmClient(listVirtualMachineRequest.getRegionId());
            listVirtualMachineRequest.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
            listVirtualMachineRequest.setLimit(PageUtil.DefaultPageSize.longValue());
            List<Instance> page = PageUtil.page(listVirtualMachineRequest,
                    req -> describeInstances(cvmClient, req),
                    res -> Arrays.stream(res.getInstanceSet()).toList(),
                    (req, res) -> req.getLimit() <= res.getInstanceSet().length,
                    req -> req.setOffset(req.getOffset() + req.getLimit()));
            List<F2CVirtualMachine> f2CVirtualMachines = page.stream().map(TencentMappingUtil::toF2CVirtualMachine).map(f2CVirtualMachine -> {
                f2CVirtualMachine.setRegion(listVirtualMachineRequest.getRegionId());
                return f2CVirtualMachine;
            }).toList();
            return f2CVirtualMachines;
        }
        return new ArrayList<>();
    }

    /**
     * 获取磁盘数据
     *
     * @param request 磁盘请求参数
     * @return 磁盘数据
     */
    public static List<F2CDisk> listDisk(ListDiskRequest request) {
        if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
            TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
            CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());
            request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
            request.setLimit(PageUtil.DefaultPageSize.longValue());
            List<Disk> disks = PageUtil.page(request,
                    req -> describeDisks(cbsClient, req),
                    res -> Arrays.stream(res.getDiskSet()).toList(),
                    (req, res) -> req.getLimit() <= res.getDiskSet().length,
                    req -> req.setOffset(req.getOffset() + req.getLimit()));
            return disks.stream().map(TencentMappingUtil::toF2CDisk).map(disk -> {
                disk.setRegion(request.getRegionId());
                return disk;
            }).toList();
        }
        return new ArrayList<>();
    }

    /**
     * 获取腾讯镜像数据
     *
     * @param request 请求对象
     * @return 镜像数据
     */
    public static List<F2CImage> listImage(ListImageRequest request) {
        if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
            TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
            CvmClient cvmClient = tencentVmCredential.getCvmClient(request.getRegionId());
            request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
            request.setLimit(PageUtil.DefaultPageSize.longValue());
            List<Image> imagess = PageUtil.page(request,
                    req -> describeImages(cvmClient, req),
                    res -> Arrays.stream(res.getImageSet()).toList(),
                    (req, res) -> req.getLimit() <= res.getImageSet().length,
                    req -> req.setOffset(req.getOffset() + req.getLimit()));

            return imagess.stream().map(TencentMappingUtil::toF2CImage).map(image -> {
                image.setRegion(request.getRegionId());
                return image;
            }).toList();
        }
        return new ArrayList<>();
    }


    /**
     * 调用腾讯云api 获取磁盘数据
     *
     * @param cbsClient 客户端
     * @param request   请求对象
     * @return 磁盘数据
     */
    private static DescribeDisksResponse describeDisks(CbsClient cbsClient, ListDiskRequest request) {
        try {
            DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
            BeanUtils.copyProperties(request, describeDisksRequest);
            return cbsClient.DescribeDisks(describeDisksRequest);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用腾讯云api 获取镜像数据
     *
     * @param cvmClient 客户端
     * @param request   请求对象
     * @return 磁盘数据
     */
    private static DescribeImagesResponse describeImages(CvmClient cvmClient, ListImageRequest request) {
        try {
            DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
            BeanUtils.copyProperties(request, describeImagesRequest);
            return cvmClient.DescribeImages(describeImagesRequest);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用腾讯云api 获取实例相关数据
     *
     * @param cvmClient                 腾讯云客户端
     * @param listVirtualMachineRequest 请求参数
     * @return 实例数据
     */
    private static DescribeInstancesResponse describeInstances(CvmClient cvmClient, ListVirtualMachineRequest listVirtualMachineRequest) {
        try {
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            BeanUtils.copyProperties(listVirtualMachineRequest, describeInstancesRequest);
            return cvmClient.DescribeInstances(describeInstancesRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建磁盘
     * api https://console.cloud.tencent.com/api/explorer?Product=cbs&Version=2017-03-12&Action=CreateDisks
     *
     * @param request 创建磁盘请求参数
     */
    public static List<F2CDisk> createDisks(TencentCreateDisksRequest request) {
        List<F2CDisk> result = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
                CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());
                for (F2CDisk f2CDisk : request.getDisks()) {
                    CreateDisksRequest createDisksRequest = toCreateDisksRequest(f2CDisk);
                    CreateDisksResponse createDisksResponse = cbsClient.CreateDisks(createDisksRequest);
                    DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
                    describeDisksRequest.setDiskIds(new String[]{createDisksResponse.getDiskIdSet()[0]});
                    //检查云盘状态
                    DescribeDisksResponse response = checkDiskState(cbsClient, describeDisksRequest, Arrays.asList("UNATTACHED", "ATTACHED"));
                    if (response != null) {
                        F2CDisk disk = trans2F2CDisk(request.getRegionId(), response.getDiskSet()[0]);
                        disk.setInstanceUuid(f2CDisk.getInstanceUuid());
                        disk.setDeleteWithInstance(f2CDisk.getDeleteWithInstance());
                        result.add(disk);
                    }
                }
                return result;
            } else {
                throw new RuntimeException("regionId or credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建磁盘
     *
     * @param request 创建磁盘请求参数
     */
    public static F2CDisk createDisk(TencentCreateDiskRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
                CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());

                // 查询实例,获取 projectId
                if (request.getInstanceUuid() != null && request.getProjectId() == null) {
                    CvmClient cvmClient = tencentVmCredential.getCvmClient(request.getRegionId());
                    ListVirtualMachineRequest req = new ListVirtualMachineRequest();
                    BeanUtils.copyProperties(request, req);
                    req.setInstanceIds(new String[]{request.getInstanceUuid()});
                    DescribeInstancesResponse res = describeInstances(cvmClient, req);

                    if (res.getInstanceSet().length == 0) {
                        throw new RuntimeException("Not found instance - " + request.getInstanceUuid());
                    }

                    Instance instance = Arrays.stream(res.getInstanceSet()).toList().get(0);
                    if (Optional.ofNullable(instance).isPresent()) {
                        // 磁盘的项目由云主机定
                        request.setProjectId(instance.getPlacement().getProjectId().toString());

                        // 如果未设置付费类型，则使用云主机的付费类型
                        if (request.getDiskChargeType() == null) {
                            request.setDiskChargeType(instance.getInstanceChargeType());
                        }

                        // Windows 的机器不支持创建磁盘时初始化文件系统
                        if (instance.getOsName().toUpperCase().indexOf("WIN") > 0) {
                            request.setFileSystemType(null);
                            request.setMountPoint(null);
                        }

                    }
                }

                CreateDisksRequest createDisksRequest = request.toCreateDisksRequest();
                CreateDisksResponse createDisksResponse = cbsClient.CreateDisks(createDisksRequest);
                DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
                describeDisksRequest.setDiskIds(new String[]{createDisksResponse.getDiskIdSet()[0]});

                // 检查云盘状态
                DescribeDisksResponse response = checkDiskState(cbsClient, describeDisksRequest, Arrays.asList("UNATTACHED", "ATTACHED"));
                F2CDisk disk = new F2CDisk();
                if (response != null) {
                    disk = trans2F2CDisk(request.getRegionId(), response.getDiskSet()[0]);
                    disk.setInstanceUuid(request.getInstanceUuid());
                    disk.setDeleteWithInstance(request.getDeleteWithInstance());
                }

                // Windows 机器或者没填写文件系统的 Linux 机器，创建完磁盘手动挂载。
                if (request.getIsAttached() && createDisksRequest.getAutoMountConfiguration() == null) {
                    TencentAttachDiskRequest attachDiskRequest = new TencentAttachDiskRequest();

                    BeanUtils.copyProperties(request,attachDiskRequest);
                    attachDiskRequest.setDiskId(disk.getDiskId());
                    attachDiskRequest.setInstanceUuid(disk.getInstanceUuid());
                    attachDiskRequest.setDeleteWithInstance(disk.getDeleteWithInstance());
                    attachDisk(attachDiskRequest);
                }
                return disk;
            } else {
                throw new RuntimeException("RegionId or credential can not be null.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 扩容磁盘
     * https://console.cloud.tencent.com/api/explorer?Product=cbs&Version=2017-03-12&Action=ResizeDisk
     *
     * @param request 扩容磁盘请求参数
     */
    public static boolean enlargeDisk(TencentResizeDiskRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
                CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());
                cbsClient.ResizeDisk(request.toResizeDiskRequest());
                return true;
            } else {
                throw new RuntimeException("regionId or credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 挂载磁盘
     * https://console.cloud.tencent.com/api/explorer?Product=cbs&Version=2017-03-12&Action=AttachDisks
     *
     * @param request 挂载磁盘请求参数
     */
    public static boolean attachDisk(TencentAttachDiskRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
                CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());

                String instanceUuid = request.getInstanceUuid();
                // 防止批量操作时失败
                synchronized (instanceUuid.intern()) {
                    cbsClient.AttachDisks(request.toAttachDisksRequest());
                    // 检查云盘状态
                    DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
                    describeDisksRequest.setDiskIds(new String[]{request.getDiskId()});
                    DescribeDisksResponse describeDisksResponse = checkDiskState(cbsClient, describeDisksRequest, Arrays.asList("ATTACHED"));
                    F2CDisk disk = trans2F2CDisk(request.getRegionId(), describeDisksResponse.getDiskSet()[0]);
                    disk.setInstanceUuid(request.getInstanceUuid());
                }
                return true;
            } else {
                throw new RuntimeException("RegionId or credential can not be null.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 卸载磁盘
     * https://console.cloud.tencent.com/api/explorer?Product=cbs&Version=2017-03-12&Action=DetachDisks
     *
     * @param request 卸载磁盘请求参数
     */
    public static boolean detachDisk(TencentDetachDiskRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
                CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());

                String instanceUuid = request.getInstanceUuid();
                // 防止批量操作时失败
                synchronized (instanceUuid.intern()) {
                    cbsClient.DetachDisks(request.toDetachDisksRequest());

                    //查看磁盘状态
                    DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
                    describeDisksRequest.setDiskIds(new String[]{request.getDiskId()});
                    checkDiskState(cbsClient, describeDisksRequest, Arrays.asList("UNATTACHED"));
                }
                return true;
            } else {
                throw new RuntimeException("RegionId and credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 删除磁盘
     * https://console.cloud.tencent.com/api/explorer?Product=cbs&Version=2017-03-12&Action=TerminateDisks
     *
     * @param request 删除磁盘请求参数
     */
    public static boolean deleteDisk(TencentDeleteDiskRequest request) {
        try {
            if (StringUtils.isNotEmpty(request.getRegionId()) && StringUtils.isNotEmpty(request.getCredential())) {
                TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
                CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());
                cbsClient.TerminateDisks(request.toTerminateDisksRequest());
                return true;
            } else {
                throw new RuntimeException("regionId or credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建创建磁盘的参数
     *
     * @param f2CDisk
     * @return
     */
    private static CreateDisksRequest toCreateDisksRequest(F2CDisk f2CDisk) {
        CreateDisksRequest createDisksRequest = new CreateDisksRequest();
        createDisksRequest.setDiskType(f2CDisk.getDiskType());
        createDisksRequest.setDiskName(f2CDisk.getDiskName());
        createDisksRequest.setDiskSize(f2CDisk.getSize());
        createDisksRequest.setDiskCount(1l);
        createDisksRequest.setDiskChargeType(TencentMappingUtil.toTencentChargeType(f2CDisk.getDiskChargeType()));
        // 放置位置
        Placement placement = new Placement();
        placement.setZone(f2CDisk.getZone());
        if (StringUtils.isNotEmpty(f2CDisk.getProjectId())) {
            placement.setProjectId(Long.valueOf(f2CDisk.getProjectId()));
        }
        createDisksRequest.setPlacement(placement);
        // 自动挂载
        if (f2CDisk.isBootable()) {
            AutoMountConfiguration autoMountConfiguration = new AutoMountConfiguration();
            autoMountConfiguration.setInstanceId(new String[]{f2CDisk.getInstanceUuid()});
            autoMountConfiguration.setMountPoint(new String[]{f2CDisk.getMountPoint()});
            autoMountConfiguration.setFileSystemType(f2CDisk.getFileSystemType());
            createDisksRequest.setAutoMountConfiguration(autoMountConfiguration);
        }
        return createDisksRequest;
    }

    /**
     * 检查磁盘状态
     *
     * @param client
     * @param describeDisksRequest
     * @param checkDiskStates
     * @return
     * @throws TencentCloudSDKException
     * @throws InterruptedException
     */
    private static DescribeDisksResponse checkDiskState(CbsClient client, DescribeDisksRequest describeDisksRequest, List<String> checkDiskStates) throws TencentCloudSDKException, InterruptedException {
        DescribeDisksResponse describeDisksResponse;
        int count = 1;
        while (true) {
            describeDisksResponse = client.DescribeDisks(describeDisksRequest);
            if (describeDisksResponse.getDiskSet().length == 0) {
                if (count >= 60) {
                    break;
                }
            } else {
                if (checkDiskStates.contains(describeDisksResponse.getDiskSet()[0].getDiskState())) {
                    break;
                }
            }
            count++;
            Thread.sleep(5000);
        }
        return describeDisksResponse;
    }

    private static F2CDisk trans2F2CDisk(String region, Disk disk) {
        F2CDisk f2CDisk = new F2CDisk();
        f2CDisk.setSize(disk.getDiskSize());
        if (StringUtils.isNotEmpty(disk.getCreateTime())) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date cTime = sdf.parse(disk.getCreateTime());
                f2CDisk.setCreateTime(cTime.getTime());
            } catch (Exception e) {
            }
        }
        f2CDisk.setDiskName(disk.getDiskName());
        f2CDisk.setStatus(toF2cDiskStatus(disk.getDiskState()));
        f2CDisk.setDiskId(disk.getDiskId());
        f2CDisk.setInstanceUuid(disk.getInstanceId());
        f2CDisk.setDiskType(disk.getDiskType());
        f2CDisk.setDiskChargeType(disk.getDiskChargeType());
        f2CDisk.setRegion(region);
        f2CDisk.setCategory(disk.getDiskUsage());
        if (disk.getDeleteWithInstance()) {
            f2CDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
        } else {
            f2CDisk.setDeleteWithInstance(DeleteWithInstance.NO.name());
        }
        if ("SYSTEM_DISK".equalsIgnoreCase(disk.getDiskUsage())) {
            f2CDisk.setBootable(true);
        }
        com.tencentcloudapi.cbs.v20170312.models.Placement placement = disk.getPlacement();
        if (placement != null) {
            f2CDisk.setZone(placement.getZone());
        }
        return f2CDisk;
    }

    public static boolean powerOff(TencentInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            TencentVmCredential credential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
            CvmClient client = credential.getCvmClient(request.getRegionId());
            try {
                StopInstancesRequest stopInstancesRequest = new StopInstancesRequest();
                stopInstancesRequest.setInstanceIds(new String[]{request.getUuid()});
                stopInstancesRequest.setStopType(request.getForce() ? "HARD" : "SOFT");
                StopInstancesResponse response = client.StopInstances(stopInstancesRequest);
                checkStatus(client, response.getRequestId(), request.getUuid());
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

    public static boolean powerOn(TencentInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            TencentVmCredential credential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
            CvmClient client = credential.getCvmClient(request.getRegionId());
            try {
                StartInstancesRequest startInstancesRequest = new StartInstancesRequest();
                startInstancesRequest.setInstanceIds(new String[]{request.getUuid()});
                StartInstancesResponse response = client.StartInstances(startInstancesRequest);
                checkStatus(client, response.getRequestId(), request.getUuid());
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

    public static boolean rebootInstance(TencentInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            TencentVmCredential credential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
            CvmClient client = credential.getCvmClient(request.getRegionId());
            try {
                RebootInstancesRequest rebootInstancesRequest = new RebootInstancesRequest();
                rebootInstancesRequest.setInstanceIds(new String[]{request.getUuid()});
                rebootInstancesRequest.setStopType(request.getForce() ? "HARD" : "SOFT");
                RebootInstancesResponse response = client.RebootInstances(rebootInstancesRequest);
                checkStatus(client, response.getRequestId(), request.getUuid());
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

    public static boolean deleteInstance(TencentInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            TencentVmCredential credential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
            CvmClient client = credential.getCvmClient(request.getRegionId());
            try {
                TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest();
                terminateInstancesRequest.setInstanceIds(new String[]{request.getUuid()});
                client.TerminateInstances(terminateInstancesRequest);
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

    private static void checkStatus(CvmClient client, String requestId, String uuid) throws Exception {
        DescribeInstancesRequest req = new DescribeInstancesRequest();
        int count = 0;
        while (true) {
            req.setInstanceIds(new String[]{uuid});
            DescribeInstancesResponse resp = client.DescribeInstances(req);
            if (resp.getInstanceSet().length == 0) {
                throw new RuntimeException("Not found instance - " + uuid);
            }
            Instance instance = Arrays.stream(resp.getInstanceSet()).toList().get(0);
            if (Optional.ofNullable(instance).isPresent()) {
                if (StringUtils.equalsIgnoreCase(instance.getLatestOperationRequestId(), requestId) && StringUtils.equalsIgnoreCase(instance.getLatestOperationState(), "SUCCESS")) {
                    break;
                }
            }
            if (count < 40) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                break;
            }
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
            TencentVmCredential credential = JsonUtil.parseObject(getMetricsRequest.getCredential(), TencentVmCredential.class);
            GetMonitorDataRequest request = getShowMetricDataRequest(getMetricsRequest);
            MonitorClient monitorClient = credential.getMonitorClient(getMetricsRequest.getRegionId());
            ///TODO 由于我们只查询一个小时内的数据，时间间隔是60s,所以查询每台机器的监控数据的时候最多不过60条数据，所以不需要分页查询
            result.addAll(getVmPerfMetric(monitorClient,request,getMetricsRequest));
            result.addAll(getDiskPerfMetric(monitorClient,request,getMetricsRequest));
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
    private static List<F2CPerfMetricMonitorData> getVmPerfMetric(MonitorClient monitorClient,GetMonitorDataRequest req,GetMetricsRequest getMetricsRequest) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        ListVirtualMachineRequest listVirtualMachineRequest = new ListVirtualMachineRequest();
        listVirtualMachineRequest.setCredential(getMetricsRequest.getCredential());
        listVirtualMachineRequest.setRegionId(getMetricsRequest.getRegionId());
        List<String> ids = listVirtualMachine(listVirtualMachineRequest).stream().map(vm->vm.getInstanceUUID()).collect(Collectors.toList());
        if (ids.size() == 0) {
            return result;
        }
        req.setNamespace("QCE/CVM");
        ids.forEach(id->{
            Arrays.stream(TencentPerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().forEach(perfMetric -> {
                req.setMetricName(perfMetric.getMetricName());
                req.setInstances(getInstance("InstanceId",id));
                Map<Long, BigDecimal> dataMap = getMonitorData(monitorClient,req);
                addMonitorData(result,dataMap,F2CEntityType.VIRTUAL_MACHINE.name(),perfMetric.getUnit(),getMetricsRequest.getPeriod(),perfMetric.name(),id);
            });
        });
        return result;
    }

    /**
     * 获取系统磁盘监控指标数据
     *
     * @param getMetricsRequest
     * @return
     */
    private static List<F2CPerfMetricMonitorData> getDiskPerfMetric(MonitorClient monitorClient,GetMonitorDataRequest req,GetMetricsRequest getMetricsRequest) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        ListDiskRequest listDiskRequest = new ListDiskRequest();
        listDiskRequest.setCredential(getMetricsRequest.getCredential());
        listDiskRequest.setRegionId(getMetricsRequest.getRegionId());
        List<F2CDisk> ids = listDisk(listDiskRequest).stream().filter(disk->disk.isBootable() && StringUtils.isNotEmpty(disk.getInstanceUuid())).collect(Collectors.toList());
        if (ids.size() == 0) {
            return result;
        }
        req.setNamespace("QCE/BLOCK_STORAGE");
        ids.forEach(disk->{
            Arrays.stream(TencentPerfMetricConstants.CloudDiskPerfMetricEnum.values()).sorted().forEach(perfMetric -> {
                req.setMetricName(perfMetric.getMetricName());
                req.setInstances(getInstance("diskId",disk.getDiskId()));
                Map<Long, BigDecimal> dataMap = getMonitorData(monitorClient,req);
                addMonitorData(result,dataMap,F2CEntityType.VIRTUAL_MACHINE.name(),perfMetric.getUnit(),getMetricsRequest.getPeriod(),perfMetric.name(),disk.getInstanceUuid());
            });
        });
        return result;
    }

    /**
     * 获取查询对象
     * @param dimensionId
     * @param instanceId
     * @return
     */
    private static com.tencentcloudapi.monitor.v20180724.models.Instance[] getInstance(String dimensionId,String instanceId){
        com.tencentcloudapi.monitor.v20180724.models.Instance[] instances1 = new com.tencentcloudapi.monitor.v20180724.models.Instance[1];
        com.tencentcloudapi.monitor.v20180724.models.Instance instance1 = new com.tencentcloudapi.monitor.v20180724.models.Instance();
        Dimension[] dimensions1 = new Dimension[1];
        Dimension dimension1 = new Dimension();
        dimension1.setName(dimensionId);
        dimension1.setValue(instanceId);
        dimensions1[0] = dimension1;
        instance1.setDimensions(dimensions1);
        instances1[0] = instance1;
        return instances1;
    }

    private static void addMonitorData(List<F2CPerfMetricMonitorData> result,Map<Long, BigDecimal> dataMap,String entityType,String unit,Integer period,String metricName,String instanceId){
        dataMap.keySet().forEach(k->{
                    F2CPerfMetricMonitorData f2CEntityPerfMetric = TencentMappingUtil.toF2CPerfMetricMonitorData(dataMap,k,unit);
                    f2CEntityPerfMetric.setEntityType(entityType);
                    f2CEntityPerfMetric.setMetricName(metricName);
                    f2CEntityPerfMetric.setPeriod(period);
                    f2CEntityPerfMetric.setInstanceId(instanceId);
                    f2CEntityPerfMetric.setUnit(unit);
                    result.add(f2CEntityPerfMetric);
        });
    }

    /**
     * 返回的时间戳不足13位，*1000
     * @param monitorClient
     * @param req
     * @return
     */
    private static Map<Long, BigDecimal> getMonitorData(MonitorClient monitorClient,GetMonitorDataRequest req) {
        Map<Long, BigDecimal> map = new LinkedHashMap<>();
        try {
            //查询监控指标数据
            GetMonitorDataResponse response = monitorClient.GetMonitorData(req);
            if (StringUtils.isEmpty(response.getMsg()) && response.getDataPoints().length>0) {
                DataPoint[] dataPoints = response.getDataPoints();
                Arrays.stream(dataPoints).toList().forEach(dataPoint->{
                    Long[] timestamps = dataPoint.getTimestamps();
                    Float[] values = dataPoint.getValues();
                    for(int i=0;i<timestamps.length;i++){
                        map.put(timestamps[i]*1000,new BigDecimal(values[i]));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 查询云主机监控数据参数
     * @param getMetricsRequest
     * @return
     */
    @NotNull
    private static GetMonitorDataRequest getShowMetricDataRequest(GetMetricsRequest getMetricsRequest) {
        GetMonitorDataRequest req = new GetMonitorDataRequest();
        req.setPeriod(60L);
        req.setStartTime(DateUtil.getISO8601TimestampFromDateStr(DateUtil.dateToString(Long.valueOf(getMetricsRequest.getStartTime()),null)));
        req.setEndTime(DateUtil.getISO8601TimestampFromDateStr(DateUtil.dateToString(Long.valueOf(getMetricsRequest.getEndTime()),null)));
        return req;
    }

}
