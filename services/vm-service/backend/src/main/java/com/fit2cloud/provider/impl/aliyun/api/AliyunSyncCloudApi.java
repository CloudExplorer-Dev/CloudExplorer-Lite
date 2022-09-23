package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.exception.ReTryException;
import com.fit2cloud.provider.exception.SkipPageException;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListDisksRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListImageRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListInstanceTypesRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListVirtualMachineRequest;
import com.fit2cloud.provider.impl.aliyun.util.AliyunMappingUtil;
import com.fit2cloud.provider.util.PageUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;


/**
 * @Author:张少虎
 * @Date: 2022/9/21  8:43 AM
 * @Version 1.0
 * @注释:
 */
public class AliyunSyncCloudApi {
    /**
     * 获取阿里云虚拟机数据
     *
     * @param listVirtualMachineRequest 获取阿里云虚拟机请求对象
     * @return 虚拟机对象
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ListVirtualMachineRequest listVirtualMachineRequest) {
        if (StringUtils.isEmpty(listVirtualMachineRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(listVirtualMachineRequest.getCredential())) {
            AliyunVmCredential credential = JsonUtil.parseObject(listVirtualMachineRequest.getCredential(), AliyunVmCredential.class);
            listVirtualMachineRequest.setPageNumber(PageUtil.DefaultCurrentPage);
            listVirtualMachineRequest.setPageSize(PageUtil.DefaultPageSize);
            Client client = credential.getClient();
            // 分页查询虚拟机列表
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
            Client client = JsonUtil.parseObject(listDescribeDisksRequest.getCredential(), AliyunVmCredential.class).getClient();
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
            Client client = JsonUtil.parseObject(listImageRequest.getCredential(), AliyunVmCredential.class).getClient();
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
            throw new Fit2cloudException(10002, "获取阿里云虚拟机列表失败" + e.getMessage());
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


}
