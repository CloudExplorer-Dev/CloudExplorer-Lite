package com.fit2cloud.provider.impl.tencent.api;

import com.aliyun.tea.TeaException;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.constants.PriceUnit;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CNetwork;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.tencent.constants.TencentChargeType;
import com.fit2cloud.provider.impl.tencent.constants.TencentDiskType;
import com.fit2cloud.provider.impl.tencent.constants.TencentPerfMetricConstants;
import com.fit2cloud.provider.impl.tencent.entity.TencentDiskTypeDTO;
import com.fit2cloud.provider.impl.tencent.entity.TencentInstanceType;
import com.fit2cloud.provider.impl.tencent.entity.credential.TencentVmCredential;
import com.fit2cloud.provider.impl.tencent.entity.request.*;
import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.cbs.v20170312.models.Filter;
import com.tencentcloudapi.cbs.v20170312.models.Placement;
import com.tencentcloudapi.cbs.v20170312.models.*;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.Image;
import com.tencentcloudapi.cvm.v20170312.models.ItemPrice;
import com.tencentcloudapi.cvm.v20170312.models.Price;
import com.tencentcloudapi.cvm.v20170312.models.*;
import com.tencentcloudapi.monitor.v20180724.MonitorClient;
import com.tencentcloudapi.monitor.v20180724.models.DataPoint;
import com.tencentcloudapi.monitor.v20180724.models.Dimension;
import com.tencentcloudapi.monitor.v20180724.models.GetMonitorDataRequest;
import com.tencentcloudapi.monitor.v20180724.models.GetMonitorDataResponse;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import com.tencentcloudapi.vpc.v20170312.models.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TencentSyncCloudApi {
    private static Logger logger = LoggerFactory.getLogger(TencentSyncCloudApi.class);

    public static F2CVirtualMachine createVirtualMachine(TencentVmCreateRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(req.getRegionId());

        RunInstancesRequest runInstancesRequest = req.toRunInstancesRequest();
        RunInstancesResponse runInstancesResponse;
        try {
            runInstancesResponse = cvmClient.RunInstances(runInstancesRequest);
        } catch (TencentCloudSDKException e) {
            logger.error("Failed to create instance", e);
            throw new RuntimeException("Failed to create instance!" + e.getMessage(), e);
        }

        // 等待磁盘初始化结束
        try {
            DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
            com.tencentcloudapi.cbs.v20170312.models.Filter[] filters = new com.tencentcloudapi.cbs.v20170312.models.Filter[1];
            com.tencentcloudapi.cbs.v20170312.models.Filter statusFilter = new com.tencentcloudapi.cbs.v20170312.models.Filter();
            statusFilter.setName("instance-id");
            statusFilter.setValues(runInstancesResponse.getInstanceIdSet());
            filters[0] = statusFilter;
            describeDisksRequest.setFilters(filters);
            CbsClient cbsClient = tencentVmCredential.getCbsClient(req.getRegionId());

            int count = 1;
            while (count < 60) {
                DescribeDisksResponse describeDisksResponse = cbsClient.DescribeDisks(describeDisksRequest);
                Disk[] disks = describeDisksResponse.getDiskSet();
                if (disks != null && disks.length == req.getDisks().size()) {
                    break;
                }
                count++;
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

        try {
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            String[] instanceIds = {runInstancesResponse.getInstanceIdSet()[0]};
            describeInstancesRequest.setInstanceIds(instanceIds);
            DescribeInstancesResponse describeInstancesResponse = null;
            int count = 1;
            while (count < 60) {
                describeInstancesResponse = cvmClient.DescribeInstances(describeInstancesRequest);
                if (describeInstancesResponse.getInstanceSet().length > 0) {
                    if ("RUNNING".equalsIgnoreCase(describeInstancesResponse.getInstanceSet()[0].getInstanceState())) {
                        break;
                    }
                }
                count++;
                Thread.sleep(5000);
            }
            if (count >= 60) {
                throw new RuntimeException("Timeout!");
            }
            F2CVirtualMachine f2CVirtualMachine = TencentMappingUtil.toF2CVirtualMachine(describeInstancesResponse.getInstanceSet()[0]);
            f2CVirtualMachine.setRegion(req.getRegionId());
            if (f2CVirtualMachine != null) {
                f2CVirtualMachine.setId(req.getId());
            }
            return f2CVirtualMachine;
        } catch (InterruptedException e) {
            String errMsg = "Thread hibernation failed!";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg + e.getMessage(), e);
        } catch (TencentCloudSDKException e) {
            String errMsg = "Failed to get instance!";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg + e.getMessage(), e);
        }
    }

    /**
     * 获取区域
     *
     * @param req
     * @return
     */
    public static List<Map<String, String>> getRegions(TencentBaseRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(req.getRegionId());
        try {
            DescribeRegionsRequest request = new DescribeRegionsRequest();
            DescribeRegionsResponse res = cvmClient.DescribeRegions(request);
            RegionInfo[] regionInfos = res.getRegionSet();
            return Arrays.asList(regionInfos).stream()
                    .filter(regionInfo -> "AVAILABLE".equalsIgnoreCase(regionInfo.getRegionState()))
                    .map(regionInfo -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", regionInfo.getRegion());
                        map.put("name", regionInfo.getRegionName());
                        return map;
                    }).toList();
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("Failed to get region." + e.getMessage(), e);
        }
    }

    public static F2CVirtualMachine getSimpleServerByCreateRequest(TencentVmCreateRequest request) {
        F2CVirtualMachine virtualMachine = new F2CVirtualMachine();
        int index = request.getIndex();

        virtualMachine
                .setId(request.getId())
                .setName(request.getServerInfos().get(index).getName())
                .setIpArray(new ArrayList<>())
                .setInstanceType(request.getInstanceTypeDTO() == null ? "" : request.getInstanceTypeDTO().getInstanceType())
                .setInstanceTypeDescription(request.getInstanceTypeDTO() == null ? "" : request.getInstanceTypeDTO().getCpuMemory());

        return virtualMachine;
    }

    /**
     * 获取可用区
     *
     * @param req
     * @return
     */
    public static List<Map<String, String>> getZones(TencentBaseRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(req.getRegionId());
        try {
            DescribeZonesRequest request = new DescribeZonesRequest();
            DescribeZonesResponse res = cvmClient.DescribeZones(request);
            ZoneInfo[] zoneInfos = res.getZoneSet();
            return Arrays.asList(zoneInfos).stream()
                    .filter(zoneInfo -> "AVAILABLE".equalsIgnoreCase(zoneInfo.getZoneState()))
                    .map(zoneInfo -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", zoneInfo.getZone());
                        map.put("name", zoneInfo.getZoneName());
                        return map;
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取实例类型
     *
     * @param req
     * @return
     */
    public static List<TencentInstanceType> getInstanceTypes(TencentGetInstanceTypeRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(req.getRegionId());

        try {
            DescribeZoneInstanceConfigInfosRequest request = req.toDescribeZoneInstanceConfigInfosRequest();
            DescribeZoneInstanceConfigInfosResponse res = cvmClient.DescribeZoneInstanceConfigInfos(request);
            InstanceTypeQuotaItem[] instanceTypeQuotaItems = res.getInstanceTypeQuotaSet();

            List<TencentInstanceType> returnList = new ArrayList<>();
            if (instanceTypeQuotaItems != null && instanceTypeQuotaItems.length > 0) {
                for (InstanceTypeQuotaItem instanceTypeQuotaItem : instanceTypeQuotaItems) {
                    if ("SELL".equalsIgnoreCase(instanceTypeQuotaItem.getStatus())) {
                        String cpuMemory = instanceTypeQuotaItem.getCpu() + "vCPU " + instanceTypeQuotaItem.getMemory() + "GB";
                        TencentInstanceType tencentInstanceType = new TencentInstanceType().builder()
                                .instanceTypeFamily(instanceTypeQuotaItem.getInstanceFamily())
                                .instanceTypeFamilyName(instanceTypeQuotaItem.getTypeName())
                                .instanceType(instanceTypeQuotaItem.getInstanceType())
                                .cpuMemory(cpuMemory)
                                .cpu(instanceTypeQuotaItem.getCpu())
                                .memory(instanceTypeQuotaItem.getMemory())
                                .build();
                        returnList.add(tencentInstanceType);
                    }
                }
            }

            return returnList;
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("Failed to get instance type." + e.getMessage(), e);
        }
    }

    /**
     * 获取可选镜像
     *
     * @param req
     * @return
     */
    public static List<F2CImage> getImages(TencentGetImageRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(req.getRegionId());
        DescribeImagesRequest describeImagesRequest = req.toDescribeImagesRequest();
        describeImagesRequest.setOffset(0l);
        describeImagesRequest.setLimit(100l);

        try {
            List<Image> images = PageUtil.page(describeImagesRequest, request -> describeImages(cvmClient, request), res -> Arrays.stream(res.getImageSet()).toList(),
                    (request, res) -> request.getLimit() <= res.getImageSet().length,
                    request -> request.setOffset(request.getOffset() + request.getLimit()));

            // 此处再根据 OS 过滤一遍的原因是API返回的数据不完全准确
            List<F2CImage> f2CImages = images.stream().filter(image -> "Normal".equalsIgnoreCase(image.getImageState()) && image.getPlatform().equalsIgnoreCase(req.getOs()))
                    .map(image -> TencentMappingUtil.toF2CImage(image)).collect(Collectors.toList());

            return f2CImages;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 根据条件查询镜像列表
     *
     * @param cvmClient
     * @param request
     * @return
     */
    private static DescribeImagesResponse describeImages(CvmClient cvmClient, DescribeImagesRequest request) {
        try {
            return cvmClient.DescribeImages(request);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取系统盘类型
     *
     * @param req 请求数据
     * @return
     */
    public List<TencentDiskTypeDTO.TencentDiskType> getSystemDiskType(TencentGetDiskTypeRequest req) {
        req.setDiskUsage("SYSTEM_DISK");
        return getDiskTypes(req).getSystemDiskTypes();
    }

    /**
     * 获取数据盘类型
     *
     * @param req 请求数据
     * @return
     */
    public static List<TencentDiskTypeDTO.TencentDiskType> getDataDiskType(TencentGetDiskTypeRequest req) {
        req.setDiskUsage("DATA_DISK");

        // 极速型 SSD 云硬盘仅支持随存储增强型云服务器 S5se 一起购买。单独创建磁盘时不展示该选项
        return getDiskTypes(req).getDataDiskTypes().stream().filter((item) ->
                !TencentDiskType.CLOUD_TSSD.getId().equalsIgnoreCase(item.getDiskType())).collect(Collectors.toList());
    }

    /**
     * 获取磁盘类型
     *
     * @param req
     * @return
     */
    public static TencentDiskTypeDTO getDiskTypes(TencentGetDiskTypeRequest req) {
        if (req.getZoneId() == null) {
            return new TencentDiskTypeDTO();
        }
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        CbsClient client = tencentVmCredential.getCbsClient(req.getRegionId());

        DescribeDiskConfigQuotaRequest describeDiskConfigQuotaRequest = req.toDescribeDiskConfigQuotaRequest();
        DescribeDiskConfigQuotaResponse describeDiskConfigQuotaResponse;
        try {
            describeDiskConfigQuotaResponse = client.DescribeDiskConfigQuota(describeDiskConfigQuotaRequest);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("Failed to get disk type." + e.getMessage(), e);
        }

        List<TencentDiskTypeDTO.TencentDiskType> systemDiskTypes = new ArrayList<>();
        List<TencentDiskTypeDTO.TencentDiskType> dataDiskTypes = new ArrayList<>();
        Arrays.stream(describeDiskConfigQuotaResponse.getDiskConfigSet()).filter(DiskConfig::getAvailable).forEach(diskConfig -> {
            if ("SYSTEM_DISK".equalsIgnoreCase(diskConfig.getDiskUsage())) {
                TencentDiskTypeDTO.TencentDiskType type = new TencentDiskTypeDTO().new TencentDiskType();
                type.setDiskType(diskConfig.getDiskType());
                type.setDiskTypeName(TencentDiskType.getName(diskConfig.getDiskType()));
                type.setMinDiskSize(diskConfig.getMinDiskSize());
                type.setMaxDiskSize(diskConfig.getMaxDiskSize());
                systemDiskTypes.add(type);
            }

            if ("DATA_DISK".equalsIgnoreCase(diskConfig.getDiskUsage())) {
                TencentDiskTypeDTO.TencentDiskType type = new TencentDiskTypeDTO().new TencentDiskType();
                type.setDiskType(diskConfig.getDiskType());
                type.setDiskTypeName(TencentDiskType.getName(diskConfig.getDiskType()));
                type.setMinDiskSize(diskConfig.getMinDiskSize());
                type.setMaxDiskSize(diskConfig.getMaxDiskSize());
                dataDiskTypes.add(type);
            }
        });

        return new TencentDiskTypeDTO()
                .builder()
                .systemDiskTypes(systemDiskTypes.stream().distinct().toList())
                .dataDiskTypes(dataDiskTypes.stream().distinct().toList())
                .build();
    }

    /**
     * 获取网络
     *
     * @param req
     * @return
     */
    public static List<F2CNetwork> getNetworks(TencentGetSubnetRequest req) {
        // 获取 VPC
        TencentGetVpcRequest vpcRequest = new TencentGetVpcRequest();
        BeanUtils.copyProperties(req, vpcRequest);
        List<Vpc> vpcList = getVpcList(vpcRequest);

        // 获取子网
        List<Subnet> subnets = getSubnets(req);

        // 组合返回数据
        return subnets.stream().map(subnet -> {
            F2CNetwork f2CNetwork = new F2CNetwork();
            f2CNetwork.setVpcId(subnet.getVpcId());
            String vpcName = vpcList.stream().filter(vpc ->
                    vpc.getVpcId().equals(subnet.getVpcId())
            ).findFirst().map(Vpc::getVpcName).orElse("——");
            f2CNetwork.setVpcName(vpcName);
            f2CNetwork.setNetworkName(subnet.getSubnetName());
            f2CNetwork.setNetworkId(subnet.getSubnetId());
            f2CNetwork.setIpSegment(subnet.getCidrBlock());
            return f2CNetwork;
        }).toList();
    }

    /**
     * 获取 VPC
     *
     * @param req
     * @return
     */
    public static List<Vpc> getVpcList(TencentGetVpcRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        VpcClient client = tencentVmCredential.getVpcClient(req.getRegionId());

        DescribeVpcsRequest describeVpcsRequest = req.toDescribeVpcRequest();
        String offset = "0";
        String limit = "100";
        describeVpcsRequest.setLimit(limit);
        List<Vpc> vpcList = new ArrayList<>();
        while (true) {
            describeVpcsRequest.setOffset(offset);
            try {
                DescribeVpcsResponse describeVpcsResponse = client.DescribeVpcs(describeVpcsRequest);
                if (describeVpcsResponse.getVpcSet() != null) {
                    vpcList.addAll(Arrays.asList(describeVpcsResponse.getVpcSet()));
                }
                if (describeVpcsResponse.getTotalCount() <= Integer.parseInt(offset) + Integer.parseInt(limit)) {
                    break;
                } else {
                    offset = String.valueOf(Integer.parseInt(offset) + Integer.parseInt(limit));
                }
            } catch (TencentCloudSDKException e) {
                logger.error("Failed to get vpc list!" + e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return vpcList;
    }

    /**
     * 获取子网
     *
     * @param req
     * @return
     */
    public static List<Subnet> getSubnets(TencentGetSubnetRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        VpcClient client = tencentVmCredential.getVpcClient(req.getRegionId());

        DescribeSubnetsRequest describeSubnetsRequest = req.toDescribeSubnetsRequest();
        String offset = "0";
        String limit = "100";
        describeSubnetsRequest.setLimit(limit);
        List<Subnet> subnets = new ArrayList<>();
        while (true) {
            describeSubnetsRequest.setOffset(offset);
            try {
                DescribeSubnetsResponse describeSubnetsResponse = client.DescribeSubnets(describeSubnetsRequest);
                subnets.addAll(Arrays.asList(describeSubnetsResponse.getSubnetSet()));
                if (describeSubnetsResponse.getTotalCount() <= Integer.parseInt(offset) + Integer.parseInt(limit)) {
                    break;
                } else {
                    offset = String.valueOf(Integer.parseInt(offset) + Integer.parseInt(limit));
                }
            } catch (TencentCloudSDKException e) {
                logger.error("Failed to get subnet list!" + e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return subnets;
    }

    /**
     * 获取安全组
     *
     * @param req
     * @return
     */
    public static List<Map<String, String>> getSecurityGroups(TencentBaseRequest req) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        VpcClient client = tencentVmCredential.getVpcClient(req.getRegionId());

        DescribeSecurityGroupsRequest describeSecurityGroupsRequest = new DescribeSecurityGroupsRequest();
        String offset = "0";
        String limit = "100";
        describeSecurityGroupsRequest.setLimit(limit);
        List<SecurityGroup> securityGroups = new ArrayList<>();
        while (true) {
            describeSecurityGroupsRequest.setOffset(offset);
            try {
                DescribeSecurityGroupsResponse describeSecurityGroupsResponse = client.DescribeSecurityGroups(describeSecurityGroupsRequest);
                securityGroups.addAll(Arrays.asList(describeSecurityGroupsResponse.getSecurityGroupSet()));
                if (describeSecurityGroupsResponse.getTotalCount() <= Integer.parseInt(offset) + Integer.parseInt(limit)) {
                    break;
                } else {
                    offset = String.valueOf(Integer.parseInt(offset) + Integer.parseInt(limit));
                }
            } catch (TencentCloudSDKException e) {
                logger.error("Failed to get security group!", e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return securityGroups.stream().map(securityGroup -> {
            Map<String, String> map = new HashMap<>();
            String name = securityGroup.getSecurityGroupName() == null ? securityGroup.getSecurityGroupId() : securityGroup.getSecurityGroupId() + " (" + securityGroup.getSecurityGroupName() + ")";
            map.put("id", securityGroup.getSecurityGroupId());
            map.put("name", name);
            return map;
        }).toList();
    }

    /**
     * 基础配置询价
     *
     * @param req
     * @return
     */
    public static String calculateConfigPrice(TencentVmCreateRequest req) {
        return calculatePrice(req, false);
    }

    /**
     * 公网IP流量配置询价
     *
     * @param req
     * @return
     */
    public static String calculateTrafficPrice(TencentVmCreateRequest req) {
        return calculatePrice(req, true);
    }

    /**
     * 询价
     *
     * @param req
     * @return
     */
    private static String calculatePrice(TencentVmCreateRequest req, Boolean trafficPriceOnly) {
        String result = "";

        // 询价镜像 ID 必填
        if (StringUtils.isBlank(req.getZoneId()) || StringUtils.isBlank(req.getOsVersion())) {
            return result;
        }

        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(req.getCredential(), TencentVmCredential.class);
        CvmClient client = tencentVmCredential.getCvmClient(req.getRegionId());

        InquiryPriceRunInstancesRequest request = req.toInquiryPriceRunInstancesRequest();
        try {
            InquiryPriceRunInstancesResponse resp = client.InquiryPriceRunInstances(request);
            Price price = resp.getPrice();

            // 按流量计费的公网IP单独显示价格
            if (trafficPriceOnly) {
                result = price.getBandwidthPrice().getUnitPrice() + PriceUnit.YUAN + "/GB";
                return result;
            }

            // 带宽计费方式
            boolean traffic = false;
            if (req.getHasPublicIp() != null && req.getHasPublicIp() && "traffic".equalsIgnoreCase(req.getBandwidthChargeType())) {
                traffic = true;
            }

            // 按需
            if (TencentChargeType.POSTPAID.getId().equalsIgnoreCase(req.getInstanceChargeType())) {
                Float priceAmount = price.getInstancePrice().getUnitPrice() + (!traffic ? price.getBandwidthPrice().getUnitPrice() : 0);
                result = String.format("%.2f", priceAmount * req.getCount()) + PriceUnit.YUAN + "/" + PriceUnit.HOUR;
            }

            // 包年包月
            if (TencentChargeType.PREPAID.getId().equalsIgnoreCase(req.getInstanceChargeType())) {
                Float priceAmount = price.getInstancePrice().getOriginalPrice() + (!traffic ? price.getBandwidthPrice().getOriginalPrice() : 0);
                result = String.format("%.2f", priceAmount * req.getCount()) + PriceUnit.YUAN;
            }

            return result;
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

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

                    BeanUtils.copyProperties(request, attachDiskRequest);
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

            Instance instance = getInstanceById(request.getUuid(), client);
            if (F2CInstanceStatus.Stopped.name().equalsIgnoreCase(TencentMappingUtil.toF2CStatus(instance.getInstanceState()))) {
                return true;
            }

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
        //设置时间，根据syncTimeStampStr,默认一个小时
        getMetricsRequest.setStartTime(String.valueOf(DateUtil.beforeOneHourToTimestamp(Long.valueOf(getMetricsRequest.getSyncTimeStampStr()))));
        getMetricsRequest.setEndTime(getMetricsRequest.getSyncTimeStampStr());
        System.out.println("开始时间：" + getMetricsRequest.getStartTime());
        System.out.println("结束时间：" + getMetricsRequest.getEndTime());
        try {
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            TencentVmCredential credential = JsonUtil.parseObject(getMetricsRequest.getCredential(), TencentVmCredential.class);
            GetMonitorDataRequest request = getShowMetricDataRequest(getMetricsRequest);
            MonitorClient monitorClient = credential.getMonitorClient(getMetricsRequest.getRegionId());
            ///TODO 由于我们只查询一个小时内的数据，时间间隔是60s,所以查询每台机器的监控数据的时候最多不过60条数据，所以不需要分页查询
            result.addAll(getVmPerfMetric(monitorClient, request, getMetricsRequest));
            result.addAll(getVmDiskPerfMetric(monitorClient, request, getMetricsRequest));
        } catch (Exception e) {
            throw new SkipPageException(100021, "获取监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
        return result;
    }

    /**
     * 暂时不实现
     *
     * @param getMetricsRequest
     * @return
     */
    public static List<F2CPerfMetricMonitorData> getF2CDiskPerfMetricList(GetMetricsRequest getMetricsRequest) {
        if (StringUtils.isEmpty(getMetricsRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        //设置时间，根据syncTimeStampStr,默认一个小时
        getMetricsRequest.setStartTime(String.valueOf(DateUtil.beforeOneHourToTimestamp(Long.valueOf(getMetricsRequest.getSyncTimeStampStr()))));
        getMetricsRequest.setEndTime(getMetricsRequest.getSyncTimeStampStr());
        try {
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            TencentVmCredential credential = JsonUtil.parseObject(getMetricsRequest.getCredential(), TencentVmCredential.class);
            GetMonitorDataRequest request = getShowMetricDataRequest(getMetricsRequest);
            MonitorClient monitorClient = credential.getMonitorClient(getMetricsRequest.getRegionId());
            //result.addAll(getDiskPerfMetric(monitorClient, request, getMetricsRequest));
        } catch (Exception e) {
            SkipPageException.throwSkipPageException(e);
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
    private static List<F2CPerfMetricMonitorData> getVmPerfMetric(MonitorClient monitorClient, GetMonitorDataRequest req, GetMetricsRequest getMetricsRequest) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        ListVirtualMachineRequest listVirtualMachineRequest = new ListVirtualMachineRequest();
        listVirtualMachineRequest.setCredential(getMetricsRequest.getCredential());
        listVirtualMachineRequest.setRegionId(getMetricsRequest.getRegionId());
        List<String> ids = listVirtualMachine(listVirtualMachineRequest).stream().map(vm -> vm.getInstanceUUID()).collect(Collectors.toList());
        if (ids.size() == 0) {
            return result;
        }
        req.setNamespace("QCE/CVM");
        ids.forEach(id -> {
            Arrays.stream(TencentPerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().forEach(perfMetric -> {
                req.setMetricName(perfMetric.getMetricName());
                req.setInstances(getInstance("InstanceId", id));
                Map<Long, BigDecimal> dataMap = getMonitorData(monitorClient, req);
                addMonitorData(null, result, dataMap, F2CEntityType.VIRTUAL_MACHINE.name(), perfMetric.getUnit(), getMetricsRequest.getPeriod(), perfMetric.name(), id);
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
    private static List<F2CPerfMetricMonitorData> getVmDiskPerfMetric(MonitorClient monitorClient, GetMonitorDataRequest req, GetMetricsRequest getMetricsRequest) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        ListDiskRequest listDiskRequest = new ListDiskRequest();
        listDiskRequest.setCredential(getMetricsRequest.getCredential());
        listDiskRequest.setRegionId(getMetricsRequest.getRegionId());
        List<F2CDisk> ids = listDisk(listDiskRequest).stream().filter(disk -> StringUtils.isNotEmpty(disk.getInstanceUuid())).collect(Collectors.toList());
        if (ids.size() == 0) {
            return result;
        }
        req.setNamespace("QCE/BLOCK_STORAGE");
        ids.forEach(disk -> {
            Arrays.stream(TencentPerfMetricConstants.CloudDiskPerfMetricEnum.values()).sorted().forEach(perfMetric -> {
                req.setMetricName(perfMetric.getMetricName());
                //TODO 这个磁盘使用率，查不到数据，一直是namespace或者MetricName无效，这个参数是通过控制台查看到的参数，无法获取到数据，暂时留着先
                if (StringUtils.equalsIgnoreCase("DISK_USED_UTILIZATION", perfMetric.name())) {
                    req.setInstances(getInstance("InstanceId", disk.getInstanceUuid()));
                    Map<String, Map<Long, BigDecimal>> dataMap = getMonitorDataForDiskUsedRate(monitorClient, req);
                    dataMap.forEach((k, v) -> {
                        addMonitorData(k, result, v, F2CEntityType.VIRTUAL_MACHINE.name(), perfMetric.getUnit(), getMetricsRequest.getPeriod(), perfMetric.name(), disk.getInstanceUuid());
                    });
                } else {
                    //这个地方没有云主机所有盘指标，所以只获取系统盘的指标
                    if (disk.isBootable()) {
                        req.setInstances(getInstance("diskId", disk.getDiskId()));
                        Map<Long, BigDecimal> dataMap = getMonitorData(monitorClient, req);
                        addMonitorData(null, result, dataMap, F2CEntityType.VIRTUAL_MACHINE.name(), perfMetric.getUnit(), getMetricsRequest.getPeriod(), perfMetric.name(), disk.getInstanceUuid());
                    }
                }
            });
        });
        return result;
    }

    private static List<F2CPerfMetricMonitorData> getDiskPerfMetric(MonitorClient monitorClient, GetMonitorDataRequest req, GetMetricsRequest getMetricsRequest) {
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        ListDiskRequest listDiskRequest = new ListDiskRequest();
        listDiskRequest.setCredential(getMetricsRequest.getCredential());
        listDiskRequest.setRegionId(getMetricsRequest.getRegionId());
        List<F2CDisk> ids = listDisk(listDiskRequest).stream().collect(Collectors.toList());
        if (ids.size() == 0) {
            return result;
        }
        req.setNamespace("QCE/BLOCK_STORAGE");
        ids.forEach(disk -> {
            Arrays.stream(TencentPerfMetricConstants.CloudDiskPerfMetricEnum.values()).sorted().forEach(perfMetric -> {
                req.setMetricName(perfMetric.getMetricName());
                req.setInstances(getInstance("diskId", disk.getDiskId()));
                Map<Long, BigDecimal> dataMap = getMonitorData(monitorClient, req);
                addMonitorData(null, result, dataMap, F2CEntityType.DISK.name(), perfMetric.getUnit(), getMetricsRequest.getPeriod(), perfMetric.name(), disk.getDiskId());
            });
        });
        return result;
    }

    /**
     * 获取查询对象
     *
     * @param dimensionId
     * @param instanceId
     * @return
     */
    private static com.tencentcloudapi.monitor.v20180724.models.Instance[] getInstance(String dimensionId, String instanceId) {
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

    private static void addMonitorData(String device, List<F2CPerfMetricMonitorData> result, Map<Long, BigDecimal> dataMap, String entityType, String unit, Integer period, String metricName, String instanceId) {
        if (StringUtils.isEmpty(device) && StringUtils.equalsIgnoreCase("DISK_USED_UTILIZATION", metricName)) {
            device = "ALL";
        }
        //最大最小值去时间段内的，因为接口拿不到
        String finalDevice = device;
        dataMap.keySet().forEach(k -> {
            BigDecimal max = dataMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
            BigDecimal min = dataMap.entrySet().stream().min(Map.Entry.comparingByValue()).get().getValue();
            F2CPerfMetricMonitorData f2CEntityPerfMetric = TencentMappingUtil.toF2CPerfMetricMonitorData(dataMap, k, unit);
            f2CEntityPerfMetric.setEntityType(entityType);
            f2CEntityPerfMetric.setMetricName(metricName);
            f2CEntityPerfMetric.setPeriod(period);
            f2CEntityPerfMetric.setInstanceId(instanceId);
            f2CEntityPerfMetric.setUnit(unit);
            f2CEntityPerfMetric.setMaximum(max);
            f2CEntityPerfMetric.setMinimum(min);
            f2CEntityPerfMetric.setDevice(finalDevice);
            result.add(f2CEntityPerfMetric);
        });
    }

    /**
     * 返回的时间戳不足13位，*1000
     *
     * @param monitorClient
     * @param req
     * @return
     */
    private static Map<Long, BigDecimal> getMonitorData(MonitorClient monitorClient, GetMonitorDataRequest req) {
        Map<Long, BigDecimal> map = new LinkedHashMap<>();
        try {
            //查询监控指标数据
            GetMonitorDataResponse response = monitorClient.GetMonitorData(req);
            if (StringUtils.isEmpty(response.getMsg()) && response.getDataPoints().length > 0) {
                DataPoint[] dataPoints = response.getDataPoints();
                Arrays.stream(dataPoints).toList().forEach(dataPoint -> {
                    Long[] timestamps = dataPoint.getTimestamps();
                    Float[] values = dataPoint.getValues();
                    for (int i = 0; i < timestamps.length; i++) {
                        map.put(timestamps[i] * 1000, new BigDecimal(values[i]));
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(req.getMetricName() + "-" + e.getMessage());
            LogUtil.error("{}-{}", req.getMetricName(), e.getMessage());
        }
        return map;
    }

    private static Map<String, Map<Long, BigDecimal>> getMonitorDataForDiskUsedRate(MonitorClient monitorClient, GetMonitorDataRequest req) {
        Map<String, Map<Long, BigDecimal>> result = new LinkedHashMap<>();
        Map<Long, BigDecimal> map = new LinkedHashMap<>();
        try {
            //查询监控指标数据
            GetMonitorDataResponse response = monitorClient.GetMonitorData(req);
            if (StringUtils.isEmpty(response.getMsg()) && response.getDataPoints().length > 0) {
                DataPoint[] dataPoints = response.getDataPoints();
                Arrays.stream(dataPoints).toList().forEach(dataPoint -> {
                    Long[] timestamps = dataPoint.getTimestamps();
                    Float[] values = dataPoint.getValues();
                    for (int i = 0; i < timestamps.length; i++) {
                        map.put(timestamps[i] * 1000, new BigDecimal(values[i]));
                    }
                    result.put(dataPoint.getDimensions()[0].getValue(), map);
                });
            }
        } catch (Exception e) {
            System.out.println(req.getMetricName() + "-" + e.getMessage());
            LogUtil.error("{}-{}", req.getMetricName(), e.getMessage());
        }
        return result;
    }

    /**
     * 查询云主机监控数据参数
     *
     * @param getMetricsRequest
     * @return
     */
    @NotNull
    private static GetMonitorDataRequest getShowMetricDataRequest(GetMetricsRequest getMetricsRequest) {
        GetMonitorDataRequest req = new GetMonitorDataRequest();
        req.setPeriod(60L);
        req.setStartTime(DateUtil.getISO8601TimestampFromDateStr(DateUtil.dateToString(Long.valueOf(getMetricsRequest.getStartTime()), null)));
        req.setEndTime(DateUtil.getISO8601TimestampFromDateStr(DateUtil.dateToString(Long.valueOf(getMetricsRequest.getEndTime()), null)));
        return req;
    }

    /**
     * 云主机配置变更
     *
     * @param request
     * @return
     */
    public static F2CVirtualMachine changeVmConfig(TencentUpdateConfigRequest request) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(request.getRegionId());
        Instance instance = getInstanceById(request.getInstanceUuid(), cvmClient);

        // 只支持系统盘类型是 CLOUD_BASIC、CLOUD_PREMIUM、CLOUD_SSD 类型的实例
        if (!instance.getSystemDisk().getDiskType().startsWith("CLOUD")) {
            throw new RuntimeException("Only instances whose system disk type is cloud_basic, cloud_premium, and cloud_ssd are supported!");
        }

        boolean isStartInstance = false;
        // 先关机
        if (!F2CInstanceStatus.Stopped.name().equalsIgnoreCase(instance.getInstanceState())) {
            powerOff(request.getInstanceUuid(), "SOFT_FIRST", cvmClient);
            isStartInstance = true;
        }

        // InstanceChargeType,对于包年包月实例，使用该接口会涉及扣费,这个得在前端处理
        ResetInstancesTypeRequest resetInstancesTypeRequest = new ResetInstancesTypeRequest();
        resetInstancesTypeRequest.setInstanceType(request.getNewInstanceType());
        resetInstancesTypeRequest.setInstanceIds(new String[]{instance.getInstanceId()});
        resetInstancesTypeRequest.setForceStop(true);
        try {
            ResetInstancesTypeResponse response = cvmClient.ResetInstancesType(resetInstancesTypeRequest);
            checkStatus(cvmClient, response.getRequestId(), request.getInstanceUuid());
        } catch (Exception e) {
            throw new RuntimeException("Failed to change instance's config!" + e.getMessage(), e);
        }

        // 完成调整后开机
        if (isStartInstance) {
            TencentInstanceRequest tencentInstanceRequest = new TencentInstanceRequest();
            BeanUtils.copyProperties(request, tencentInstanceRequest);
            tencentInstanceRequest.setUuid(request.getInstanceUuid());
            powerOn(tencentInstanceRequest);
        }

        return TencentMappingUtil.toF2CVirtualMachine(getInstanceById(request.getInstanceUuid(), cvmClient));
    }

    /**
     * 根据实例 ID 获取实例
     *
     * @param instanceId
     * @param client
     * @return
     */
    private static Instance getInstanceById(String instanceId, CvmClient client) {
        Optional.ofNullable(instanceId).orElseThrow(() -> new RuntimeException("Instance id is null!"));

        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.setInstanceIds(new String[]{instanceId});
        try {
            DescribeInstancesResponse describeInstancesResponse = client.DescribeInstances(describeInstancesRequest);
            if (describeInstancesResponse.getInstanceSet().length == 0) {
                String errMsg = "Instance not exists! Instance id：" + instanceId;
                logger.error(errMsg);
                throw new RuntimeException(errMsg);
            }
            return describeInstancesResponse.getInstanceSet()[0];
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 关机
     *
     * @param instanceId
     * @param stopType
     * @param cvmClient
     * @return
     */
    private static Boolean powerOff(String instanceId, String stopType, CvmClient cvmClient) {
        Optional.ofNullable(instanceId).orElseThrow(() -> new RuntimeException("Instance id is null!"));
        StopInstancesRequest stopInstancesRequest = new StopInstancesRequest();
        stopInstancesRequest.setInstanceIds(new String[]{instanceId});
        stopInstancesRequest.setStopType(stopType);
        try {
            StopInstancesResponse response = cvmClient.StopInstances(stopInstancesRequest);
            checkStatus(cvmClient, response.getRequestId(), instanceId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to stop instance!" + e.getMessage(), e);
        }
        return true;
    }

    /**
     * 获取配置变更可选实例规格
     *
     * @param request
     * @return
     */
    public static List<TencentInstanceType> getInstanceTypesForConfigUpdate(TencentUpdateConfigRequest request) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(request.getRegionId());

        DescribeInstancesModificationRequest req = new DescribeInstancesModificationRequest();
        Optional.ofNullable(request.getInstanceUuid()).orElseThrow(() -> new RuntimeException("Instance id is null!"));
        req.setInstanceIds(new String[]{request.getInstanceUuid()});

        // 获取可用区可选实例
        TencentGetInstanceTypeRequest tencentGetInstanceTypeRequest = new TencentGetInstanceTypeRequest();
        BeanUtils.copyProperties(request, tencentGetInstanceTypeRequest);
        List<TencentInstanceType> zoneResources = getInstanceTypes(tencentGetInstanceTypeRequest);

        List<TencentInstanceType> returnList = new ArrayList<>();
        try {
            // 获取配置变更可选实例
            DescribeInstancesModificationResponse resp = cvmClient.DescribeInstancesModification(req);
            InstanceTypeConfigStatus[] instanceTypeConfigStatusSet = resp.getInstanceTypeConfigStatusSet();

            if (instanceTypeConfigStatusSet != null && instanceTypeConfigStatusSet.length > 0) {
                for (InstanceTypeConfigStatus instanceTypeConfigStatus : instanceTypeConfigStatusSet) {
                    if ("SELL".equalsIgnoreCase(instanceTypeConfigStatus.getStatus()) &&
                            !instanceTypeConfigStatus.getInstanceTypeConfig().getInstanceType().equalsIgnoreCase(request.getCurrentInstanceType())) {
                        InstanceTypeConfig instanceTypeConfig = instanceTypeConfigStatus.getInstanceTypeConfig();
                        String cpuMemory = instanceTypeConfig.getCPU() + "vCPU " + instanceTypeConfig.getMemory() + "GB";
                        TencentInstanceType tencentInstanceType = new TencentInstanceType().builder()
                                .instanceType(instanceTypeConfig.getInstanceType())
                                .instanceTypeDesc(instanceTypeConfig.getInstanceType() + "（" + cpuMemory + "）")
                                .instanceTypeFamily(instanceTypeConfig.getInstanceFamily())
                                .cpuMemory(cpuMemory)
                                .cpu(instanceTypeConfig.getCPU())
                                .memory(instanceTypeConfig.getMemory())
                                .build();
                        returnList.add(tencentInstanceType);
                    }
                }
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("Failed to get instance type for updating!" + e.getMessage(), e);
        }

        // 可用区可选实例与配置变更可选实例取交集
        return returnList.stream().filter(zoneResources::contains).collect(Collectors.toList());
    }

    /**
     * 配置变更询价
     *
     * @param request
     * @return
     */
    public static String calculateConfigUpdatePrice(TencentUpdateConfigRequest request) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
        CvmClient cvmClient = tencentVmCredential.getCvmClient(request.getRegionId());

        InquiryPriceResetInstancesTypeRequest req = new InquiryPriceResetInstancesTypeRequest();
        Optional.ofNullable(request.getInstanceUuid()).orElseThrow(() -> new RuntimeException("Instance id is null."));
        req.setInstanceIds(new String[]{request.getInstanceUuid()});
        req.setInstanceType(request.getNewInstanceType());

        try {
            InquiryPriceResetInstancesTypeResponse resp = cvmClient.InquiryPriceResetInstancesType(req);
            ItemPrice item = resp.getPrice().getInstancePrice();
            Float price;
            String unit;
            if (TencentChargeType.PREPAID.getId().equalsIgnoreCase(request.getInstanceChargeType())) {
                price = StringUtils.isBlank(String.valueOf(item.getDiscountPrice())) ? item.getOriginalPrice() : item.getDiscountPrice();
                unit = PriceUnit.YUAN;
            } else {
                price = StringUtils.isBlank(String.valueOf(item.getUnitPriceDiscount())) ? item.getUnitPrice() : item.getUnitPriceDiscount();
                unit = PriceUnit.YUAN + "/" + PriceUnit.HOUR;
            }
            return String.format("%.2f", price) + unit;
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("Failed to get the price of config update!" + e.getMessage(), e);
        }
    }

    /**
     * 获取云主机关联的磁盘
     *
     * @param request
     * @return
     */
    public static List<F2CDisk> getVmF2CDisks(BaseDiskRequest request) {
        TencentVmCredential tencentVmCredential = JsonUtil.parseObject(request.getCredential(), TencentVmCredential.class);
        CbsClient cbsClient = tencentVmCredential.getCbsClient(request.getRegionId());
        try {
            return getDisksByInstanceId(request.getInstanceUuid(), cbsClient).stream().map((disk) -> {
                        F2CDisk f2cDisk = TencentMappingUtil.toF2CDisk(disk);
                        f2cDisk.setRegion(request.getRegionId());
                        return f2cDisk;
                    }
            ).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("GetVmF2CDisks Error!" + e.getMessage(), e);
        }
    }

    /**
     * 根据实例 ID 获取实例磁盘列表
     *
     * @param instanceId
     * @param cbsClient
     * @return
     */
    private static List<Disk> getDisksByInstanceId(String instanceId, CbsClient cbsClient) {
        try {
            DescribeDisksRequest req = new DescribeDisksRequest();
            Filter[] filters = new Filter[1];
            Filter filter = new Filter();
            filter.setName("instance-id");
            filter.setValues(new String[]{instanceId});
            filters[0] = filter;
            req.setFilters(filters);
            DescribeDisksResponse response = cbsClient.DescribeDisks(req);
            return Arrays.asList(response.getDiskSet());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get disks of instance.Instance id:" + instanceId + e.getMessage(), e);
        }
    }
}
