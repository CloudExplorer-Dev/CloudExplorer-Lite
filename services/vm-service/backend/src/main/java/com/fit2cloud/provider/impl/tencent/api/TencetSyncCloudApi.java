package com.fit2cloud.provider.impl.tencent.api;

import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.tencent.entity.credential.TencentVmCredential;
import com.fit2cloud.provider.impl.tencent.entity.request.*;
import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.cbs.v20170312.models.Placement;
import com.tencentcloudapi.cbs.v20170312.models.*;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.Image;
import com.tencentcloudapi.cvm.v20170312.models.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil.toF2cDiskStatus;

/**
 * @Author:张少虎
 * @Date: 2022/9/23  3:52 PM
 * @Version 1.0
 * @注释:
 */
public class TencetSyncCloudApi {

    /**
     * 获取虚拟机数据
     *
     * @param listVirtualMachineRequest 虚拟机请求参数
     * @return 虚拟机数据
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
                cbsClient.DetachDisks(request.toDetachDisksRequest());
                return true;
            } else {
                throw new RuntimeException("RegionId and credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
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
                cbsClient.AttachDisks(request.toAttachDisksRequest());

                //检查云盘状态
                List<F2CDisk> result = new ArrayList<>();
                DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
                describeDisksRequest.setDiskIds(new String[]{request.getDiskId()});
                DescribeDisksResponse describeDisksResponse = checkDiskState(cbsClient, describeDisksRequest, Arrays.asList("ATTACHED"));
                F2CDisk disk = trans2F2CDisk(request.getRegionId(), describeDisksResponse.getDiskSet()[0]);
                disk.setInstanceUuid(request.getInstanceUuid());
                return true;
            } else {
                throw new RuntimeException("RegionId or credential can not be null.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 创建磁盘
     * api https://console.cloud.tencent.com/api/explorer?Product=cbs&Version=2017-03-12&Action=CreateDisks
     *
     * @param request 创建磁盘请求参数
     */
    public static List<F2CDisk> createDisks(TencentCreateDiskRequest request) {
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
        //放置位置
        Placement placement = new Placement();
        placement.setZone(f2CDisk.getZone());
        if (StringUtils.isNotEmpty(f2CDisk.getProjectId())) {
            placement.setProjectId(Long.valueOf(f2CDisk.getProjectId()));
        }
        createDisksRequest.setPlacement(placement);
        //自动挂载
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
}
