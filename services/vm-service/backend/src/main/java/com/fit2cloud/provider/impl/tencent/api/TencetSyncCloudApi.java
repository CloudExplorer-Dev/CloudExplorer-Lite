package com.fit2cloud.provider.impl.tencent.api;

import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.tencent.entity.credential.TencentVmCredential;
import com.fit2cloud.provider.impl.tencent.entity.request.ListDiskRequest;
import com.fit2cloud.provider.impl.tencent.entity.request.ListImageRequest;
import com.fit2cloud.provider.impl.tencent.entity.request.ListVirtualMachineRequest;
import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.cbs.v20170312.models.DescribeDisksRequest;
import com.tencentcloudapi.cbs.v20170312.models.DescribeDisksResponse;
import com.tencentcloudapi.cbs.v20170312.models.Disk;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    req -> req.setOffset(req.getOffset() * req.getLimit()));
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
            request.setOffset(PageUtil.DefaultCurrentPage.longValue());
            request.setLimit(PageUtil.DefaultPageSize.longValue());
            List<Disk> disks = PageUtil.page(request,
                    req -> describeDisks(cbsClient, req),
                    res -> Arrays.stream(res.getDiskSet()).toList(),
                    (req, res) -> req.getLimit() <= res.getDiskSet().length,
                    req -> req.setOffset(req.getOffset() * req.getLimit()));
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
            request.setOffset(PageUtil.DefaultCurrentPage.longValue());
            request.setLimit(PageUtil.DefaultPageSize.longValue());
            List<Image> imagess = PageUtil.page(request,
                    req -> describeImages(cvmClient, req),
                    res -> Arrays.stream(res.getImageSet()).toList(),
                    (req, res) -> req.getLimit() <= res.getImageSet().length,
                    req -> req.setOffset(req.getOffset() * req.getLimit()));

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
}
