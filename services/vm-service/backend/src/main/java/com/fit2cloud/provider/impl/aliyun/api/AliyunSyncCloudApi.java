package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.bssopenapi20171214.models.*;
import com.aliyun.cms20190101.models.DescribeMetricListRequest;
import com.aliyun.cms20190101.models.DescribeMetricListResponse;
import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.CreateInstanceRequest;
import com.aliyun.ecs20140526.models.CreateInstanceResponse;
import com.aliyun.ecs20140526.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fit2cloud.common.constants.Language;
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
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.constants.PriceUnit;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CNetwork;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.aliyun.constants.*;
import com.fit2cloud.provider.impl.aliyun.entity.AliyunDiskTypeDTO;
import com.fit2cloud.provider.impl.aliyun.entity.AliyunInstanceType;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;
import com.fit2cloud.provider.impl.aliyun.util.AliyunMappingUtil;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @Author:张少虎
 * @Date: 2022/9/21  8:43 AM
 * @Version 1.0
 * @注释:
 */
public class AliyunSyncCloudApi {
    private static Logger logger = LoggerFactory.getLogger(AliyunSyncCloudApi.class);

    public static F2CVirtualMachine createVirtualMachine(AliyunVmCreateRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        CreateInstanceRequest createInstanceRequest = req.toCreateInstanceRequest();
        DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance = new DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance();
        try {
            CreateInstanceResponse createInstanceResponse = client.createInstance(createInstanceRequest);
            String instanceId = createInstanceResponse.body.instanceId;
            Optional.ofNullable(instanceId).orElseThrow(() -> new RuntimeException("Instance ID is null."));

            DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
            describeInstanceStatusRequest.setRegionId(req.getRegionId());
            describeInstanceStatusRequest.setInstanceId(Arrays.asList(new String[]{instanceId}));
            checkStatus(client, "Stopped", describeInstanceStatusRequest);

            if (req.getF2CNetwork() != null && req.getF2CNetwork().getNetworkId() != null) {
                if (req.getHasPublicIp()) {
                    try {
                        // 创建 弹性公网IP
                        AllocateEipAddressRequest allocateEipAddressRequest = new AllocateEipAddressRequest();
                        allocateEipAddressRequest.setRegionId(req.getRegionId());
                        allocateEipAddressRequest.setBandwidth(req.getBandwidth());
                        allocateEipAddressRequest.setInternetChargeType(req.getBandwidthChargeType());
                        AllocateEipAddressResponse allocateEipAddressResponse = client.allocateEipAddress(allocateEipAddressRequest);
                        String eipId = allocateEipAddressResponse.body.allocationId;

                        // 弹性公网IP 绑定云主机
                        AssociateEipAddressRequest associateEipAddressRequest = new AssociateEipAddressRequest();
                        associateEipAddressRequest.setAllocationId(eipId);
                        associateEipAddressRequest.setInstanceId(instanceId);
                        client.associateEipAddress(associateEipAddressRequest);
                    } catch (Exception e) {
                        logger.error("Failed to create public ip !" + e.getMessage());
                    }
                }
            } else {
                if (req.getHasPublicIp()) {
                    try {
                        AllocatePublicIpAddressRequest allocatePublicIpAddressRequest = new AllocatePublicIpAddressRequest();
                        allocatePublicIpAddressRequest.setInstanceId(instanceId);
                        AllocatePublicIpAddressResponse allocatePublicIpAddressResponse = client.allocatePublicIpAddress(allocatePublicIpAddressRequest);
                        String publicIp = allocatePublicIpAddressResponse.body.ipAddress;
                        logger.debug("Succeed to allocate public ip. Public ip is :: " + publicIp);
                    } catch (Exception e) {
                        logger.error("Failed to allocate public ip ! Instance : " + createInstanceRequest.getInstanceName() + ", errorMsg : " + e.getMessage());
                    }
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }

            // 启动云主机
            StartInstanceRequest startInstanceRequest = new StartInstanceRequest();
            startInstanceRequest.setInstanceId(instanceId);
            try {
                StartInstanceResponse startInstanceResponse = client.startInstance(startInstanceRequest);
                if (StringUtils.isNotBlank(startInstanceResponse.body.requestId)) {
                    logger.debug("Start instance :: " + instanceId);
                } else {
                    throw new PluginException("Failed to start virtual machine!");
                }
            } catch (Exception e) {
                logger.error("Failed to start virtual machine! instance : " + createInstanceRequest.getInstanceName());
            }

            checkStatus(client, "Running", describeInstanceStatusRequest);
            instance = getInstanceById(instanceId, req.getRegionId(), client);
            Optional.ofNullable(instance).orElseThrow(() -> new RuntimeException("Return result is null."));
            F2CVirtualMachine f2cInstance = AliyunMappingUtil.toF2CVirtualMachine(instance);
            Optional.ofNullable(f2cInstance).ifPresent((result) -> result.setId(req.getId()));
            return f2cInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance." + e.getMessage(), e);
        }
    }

    public static F2CVirtualMachine getSimpleServerByCreateRequest(AliyunVmCreateRequest request) {
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
     * 获取区域
     *
     * @param req
     * @return
     */
    public static List<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion> getRegions(AliyunGetRegionRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
        describeRegionsRequest.setResourceType("instance");
        describeRegionsRequest.setInstanceChargeType(req.getInstanceChargeType());
        if (req.getLanguage() != null) {
            if (Language.en_US.equals(req.getLanguage())) {
                describeRegionsRequest.setAcceptLanguage("en-US");
            }
        }
        try {
            DescribeRegionsResponse describeRegionsResponse = client.describeRegions(describeRegionsRequest);
            List<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion> regions = describeRegionsResponse.body.regions.region;
            return regions;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get region." + e.getMessage(), e);
        }
    }

    /**
     * 获取可用区
     *
     * @param req
     * @return
     */
    public static List<DescribeZonesResponseBody.DescribeZonesResponseBodyZonesZone> getZones(AliyunGetZoneRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeZonesRequest describeZonesRequest = new DescribeZonesRequest();
        describeZonesRequest.setRegionId(req.getRegionId());
        if (req.getLanguage() != null) {
            if (Language.en_US.equals(req.getLanguage())) {
                describeZonesRequest.setAcceptLanguage("en-US");
            }
        }
        try {
            DescribeZonesResponse describeRegionsResponse = client.describeZones(describeZonesRequest);
            List<DescribeZonesResponseBody.DescribeZonesResponseBodyZonesZone> zones = describeRegionsResponse.body.zones.zone;
            return zones;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get zone." + e.getMessage(), e);
        }
    }

    /**
     * 获取区域下的所有网络
     *
     * @param req
     * @return
     */
    public static List<F2CNetwork> getNetworks(AliyunGetVSwitchRequest req) {
        List<DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc> vpcList = getVpcList(req);
        return vpcList.stream().flatMap(vpc -> {
            try {
                req.setVpcId(vpc.getVpcId());
                return getVSwitches(req).stream().map(vSwitch -> toNetwork(vSwitch, vpc));
            } catch (PluginException e) {
                logger.error("Failed to get network." + e.getMessage(), e);
            }
            return new ArrayList<F2CNetwork>().stream();
        }).collect(Collectors.toList());
    }

    /**
     * 获取 vpc
     *
     * @param req
     * @return
     */
    private static List<DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc> getVpcList(AliyunBaseRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
        describeVpcsRequest.setRegionId(req.getRegionId());
        try {
            DescribeVpcsResponse describeVpcsResponse = client.describeVpcs(describeVpcsRequest);
            List<DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc> vpcList = describeVpcsResponse.body.vpcs.vpc;
            List<DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc> results = new ArrayList<>();
            for (DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc vpc : vpcList) {
                String vpcname = "[" + vpc.getVpcId();
                if (vpc.getIsDefault()) {
                    vpcname += ", default";
                }
                vpcname += "]" + vpc.getVpcName();
                vpc.setVpcName(vpcname);
                results.add(vpc);
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get vpc." + e.getMessage(), e);
        }
    }

    /**
     * 获取虚拟机交换机
     *
     * @param req
     * @return
     */
    private static List<DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch> getVSwitches(AliyunGetVSwitchRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeVSwitchesRequest describeVSwitchesRequest = new DescribeVSwitchesRequest();
        try {
            List<DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch> vSwitches = new ArrayList<>();
            if (StringUtils.isNoneEmpty(req.getVpcId())) {
                describeVSwitchesRequest.setVpcId(req.getVpcId());
                if (StringUtils.isNoneEmpty(req.getZoneId())) {
                    describeVSwitchesRequest.setZoneId(req.getZoneId());
                }
                DescribeVSwitchesResponse describeVSwitchesResponse = client.describeVSwitches(describeVSwitchesRequest);
                vSwitches = describeVSwitchesResponse.body.vSwitches.vSwitch;
                vSwitches.stream().filter(vSwitch ->
                        "Available".equalsIgnoreCase(vSwitch.getStatus())).forEach(vSwitch -> {
                    if (StringUtils.isBlank(vSwitch.getVSwitchName())) {
                        vSwitch.setVSwitchName(vSwitch.getVSwitchId());
                    }
                });
            }
            return vSwitches;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static F2CNetwork toNetwork(DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch vSwitch, DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc vpc) {
        F2CNetwork f2CNetwork = new F2CNetwork();
        f2CNetwork.setRegionId(vpc.getRegionId());
        f2CNetwork.setVpcId(vpc.getVpcId());
        f2CNetwork.setVpcName(vpc.getVpcName());
        f2CNetwork.setNetworkName((vSwitch.getVSwitchName()));
        f2CNetwork.setNetworkId(vSwitch.getVSwitchId());
        f2CNetwork.setZoneId(vSwitch.getZoneId());
        f2CNetwork.setIpSegment(vSwitch.cidrBlock);
        return f2CNetwork;
    }

    /**
     * 获取安全组
     *
     * @param req
     * @return
     */
    public static List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> getSecurityGroups(AliyunGetSecurityGroupRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeSecurityGroupsRequest describeSecurityGroupsRequest = new DescribeSecurityGroupsRequest();
        describeSecurityGroupsRequest.setRegionId(req.getRegionId());

        if (req.getF2CNetwork() != null && StringUtils.isNotBlank(req.getF2CNetwork().getVpcId())) {
            describeSecurityGroupsRequest.setVpcId(req.getF2CNetwork().getVpcId());
        }
        try {
            List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> resultSecurityGroups = new ArrayList<>();
            int pageNum = 1;
            int pageSize = 50;
            int totalCount = 0;
            describeSecurityGroupsRequest.setPageSize(pageSize);
            do {
                describeSecurityGroupsRequest.setPageNumber(pageNum);
                DescribeSecurityGroupsResponse describeSecurityGroupsResponse = client.describeSecurityGroups(describeSecurityGroupsRequest);
                totalCount = describeSecurityGroupsResponse.getBody().totalCount;

                List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> securityGroups = describeSecurityGroupsResponse.body.securityGroups.securityGroup;
                for (DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup sg : securityGroups) {
                    String name = sg.getSecurityGroupName();
                    if (name == null || name.trim().length() == 0) {
                        name = sg.getSecurityGroupId();
                    } else {
                        name = sg.getSecurityGroupId() + "[" + name + "]";
                    }
                    sg.setSecurityGroupName(sg.getSecurityGroupId());
                    if (sg.getDescription().contains("System created security group") || sg.getDescription().startsWith("G")) {
                        name += "(System Default)";
                    }
                    sg.setSecurityGroupName(name);
                }

                resultSecurityGroups.addAll(securityGroups);
            } while (totalCount > pageNum++ * pageSize);

            Collections.sort(resultSecurityGroups, new Comparator<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup>() {
                public int compare(DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup sg1, DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup sg2) {
                    if (sg1.getSecurityGroupName().contains("System Default")) {
                        return -1;
                    }
                    if (sg2.getSecurityGroupName().contains("System Default")) {
                        return 1;
                    }
                    return sg1.getSecurityGroupName().compareTo(sg2.getSecurityGroupName());
                }
            });
            return resultSecurityGroups;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get security group." + e.getMessage(), e);
        }
    }

    /**
     * 获取可选实例类型
     *
     * @param req
     * @return
     */
    public static List<AliyunInstanceType> getInstanceTypes(AliyunGetAvailableResourceRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());

        // 获取所有实例
        ListInstanceTypesRequest listInstanceTypesRequest = new ListInstanceTypesRequest();
        BeanUtils.copyProperties(req, listInstanceTypesRequest);
        List<DescribeInstanceTypesResponseBody.DescribeInstanceTypesResponseBodyInstanceTypesInstanceType> allInstanceTypes = listInstanceType(listInstanceTypesRequest);

        // 获取可用区可选实例
        DescribeAvailableResourceRequest describeAvailableResourceRequest = new DescribeAvailableResourceRequest();
        describeAvailableResourceRequest.setRegionId(req.getRegionId());
        describeAvailableResourceRequest.setZoneId(req.getZoneId());
        describeAvailableResourceRequest.setInstanceChargeType(req.getInstanceChargeType());
        describeAvailableResourceRequest.setDestinationResource("InstanceType");
        describeAvailableResourceRequest.setResourceType("instance");

        // 获取可用区可选实例的 ID 集合
        List<String> instanceTypeIds = new ArrayList<>();
        List<DescribeAvailableResourceResponseBody.DescribeAvailableResourceResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> supportedResources = getAvailableResources(describeAvailableResourceRequest, client);
        if (CollectionUtils.isNotEmpty(supportedResources)) {
            instanceTypeIds = supportedResources.stream().map((supportedResource) -> supportedResource.getValue()).toList();
        }

        // 过滤可用实例
        final List<String> instanceTypeIdsFinal = instanceTypeIds;
        List<AliyunInstanceType> result = allInstanceTypes.stream().filter(instanceType -> instanceTypeIdsFinal.contains(instanceType.getInstanceTypeId())).map((instanceType) -> {
            AliyunInstanceType resultType = new AliyunInstanceType();
            // 应要求过滤掉小于1G内存的实例规格
            if (instanceType.getMemorySize() >= 1) {
                resultType.setInstanceType(instanceType.getInstanceTypeId());
                resultType.setCpuMemory(instanceType.getCpuCoreCount() + "vCPU " + instanceType.getMemorySize().intValue() + "GB");
                resultType.setCpu(instanceType.getCpuCoreCount());
                resultType.setMemory(instanceType.getMemorySize().intValue());
                resultType.setInstanceTypeFamily(instanceType.getInstanceTypeFamily());
                resultType.setInstanceTypeFamilyName(instanceType.getInstanceTypeFamily());
            }
            return resultType;
        }).toList();

        return result.stream().filter(instanceType -> instanceType.getInstanceTypeFamily() != null).
                sorted(Comparator.comparing(AliyunInstanceType::getInstanceTypeFamily).thenComparing(AliyunInstanceType::getCpu).thenComparing(AliyunInstanceType::getMemory))
                .collect(Collectors.toList());
    }

    /**
     * 查询实例族名称
     *
     * @param instanceTypes
     * @param req
     * @return
     */
    private static void appendInstanceTypeFamilyName(List<AliyunInstanceType> instanceTypes, AliyunGetAvailableResourceRequest req) {
        // 查询规格族名称列表
        AliyunGetPriceModuleRequest getPriceModuleRequest = new AliyunGetPriceModuleRequest();
        BeanUtils.copyProperties(req, getPriceModuleRequest);
        DescribePricingModuleResponseBody.DescribePricingModuleResponseBodyData pricingModuleResData = getPricingModule(getPriceModuleRequest);
        List<DescribePricingModuleResponseBody.DescribePricingModuleResponseBodyDataAttributeListAttributeValuesAttributeValue> attributeValue = new ArrayList<>();
        if (pricingModuleResData != null) {
            List<DescribePricingModuleResponseBody.DescribePricingModuleResponseBodyDataAttributeListAttribute> attributeList = pricingModuleResData.getAttributeList().attribute.stream().filter(attribute ->
                    "InstanceTypeFamily".equalsIgnoreCase(attribute.getCode())
            ).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(attributeList) && CollectionUtils.isNotEmpty(attributeList.get(0).values.attributeValue)) {
                attributeValue = attributeList.get(0).values.attributeValue;
            }
        }

        // 根据实例类型查找
        if (CollectionUtils.isNotEmpty(attributeValue)) {
            for (int i = 0; i < instanceTypes.size(); i++) {
                for (int j = 0; j < attributeValue.size(); j++) {
                    instanceTypes.get(i).setInstanceTypeFamilyName(instanceTypes.get(i).getInstanceTypeFamily());// 查不到名字则显示实例族
                    if (instanceTypes.get(i).getInstanceTypeFamily() != null && instanceTypes.get(i).getInstanceTypeFamily().equalsIgnoreCase(attributeValue.get(j).value)) {
                        instanceTypes.get(i).setInstanceTypeFamilyName(attributeValue.get(j).name);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 获取可选镜像
     *
     * @param req
     * @return
     */
    public static List<F2CImage> getImages(AliyunGetImageRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());

        DescribeImagesRequest describeImagesRequest = req.toDescribeImagesRequest();
        describeImagesRequest.setPageSize(PageUtil.DefaultPageSize);
        describeImagesRequest.setPageNumber(PageUtil.DefaultCurrentPage);
        List<F2CImage> f2CImages;
        try {
            List<DescribeImagesResponseBody.DescribeImagesResponseBodyImagesImage> images = PageUtil.page(describeImagesRequest, request -> describeImages(client, request), res -> res.getBody().images.image, (request, res) -> res.getBody().getPageSize() <= res.getBody().images.image.size(), request -> request.setPageNumber(request.getPageNumber() + 1));
            String os = req.getOs();
            if (os != null) {
                if (AliyunOSType.WindowsServer.getDisplayValue().equalsIgnoreCase(os)) {
                    images = images.stream().filter(image -> image.getOSType().equalsIgnoreCase("windows")).collect(Collectors.toList());
                } else {
                    images = images.stream().filter(image -> image.getOSName().indexOf(req.getOs()) > -1).collect(Collectors.toList());
                }
            }
            f2CImages = images.stream().map(image -> AliyunMappingUtil.toF2CImage(image, req.getRegionId())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return f2CImages;
    }

    /**
     * 获取镜像
     *
     * @param client 客户端
     * @param req    请求参数
     * @return 镜像数据返回值
     */
    private static DescribeImagesResponse describeImages(Client client, DescribeImagesRequest req) {
        try {
            return client.describeImages(req);
        } catch (Exception e) {
            SkipPageException.throwSkip(e);
            ReTryException.throwReTry(e);
            throw new RuntimeException("Failed to get images." + e.getMessage(), e);
        }
    }

    /**
     * 查询阿里云某个产品对应模块信息
     *
     * @param req
     * @return
     */
    private static DescribePricingModuleResponseBody.DescribePricingModuleResponseBodyData getPricingModule(AliyunGetPriceModuleRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        com.aliyun.bssopenapi20171214.Client client = credential.getBssClient();

        if (req.getInstanceChargeType() == null) {
            return null;
        }
        DescribePricingModuleRequest describePricingModuleRequest = req.toDescribePricingModuleRequest();

        try {
            DescribePricingModuleResponse res = client.describePricingModule(describePricingModuleRequest);
            if ("Success".equalsIgnoreCase(res.body.code)) {
                return res.body.data;
            }
        } catch (Exception e) {
            logger.error("Failed to get pricing module.");
        }
        return null;
    }

    /**
     * 获取某一可用区的可用资源
     *
     * @param req
     * @return
     */
    public static List<DescribeAvailableResourceResponseBody.DescribeAvailableResourceResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> getAvailableResources(DescribeAvailableResourceRequest req, Client client) {
        try {
            DescribeAvailableResourceResponse describeAvailableResourceResponse = client.describeAvailableResource(req);
            List<DescribeAvailableResourceResponseBody.DescribeAvailableResourceResponseBodyAvailableZonesAvailableZone> availableZones = describeAvailableResourceResponse.body.availableZones.availableZone;

            if (CollectionUtils.isNotEmpty(availableZones)
                    && CollectionUtils.isNotEmpty(availableZones.get(0).availableResources.availableResource)
                    && CollectionUtils.isNotEmpty(availableZones.get(0).availableResources.availableResource.get(0).supportedResources.supportedResource)) {
                return availableZones.get(0).availableResources.availableResource.get(0).supportedResources.supportedResource;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get available resource types." + e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public static List<Map<String, String>> getKeyPairs(AliyunBaseRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeKeyPairsRequest describeKeyPairsRequest = new DescribeKeyPairsRequest();
        describeKeyPairsRequest.setRegionId(req.getRegionId());
        try {
            DescribeKeyPairsResponse describeKeyPairsResponse = client.describeKeyPairs(describeKeyPairsRequest);
            List<DescribeKeyPairsResponseBody.DescribeKeyPairsResponseBodyKeyPairsKeyPair> keyPairList = describeKeyPairsResponse.body.keyPairs.keyPair;
            return keyPairList.stream().map(keyPair -> {
                Map<String, String> map = new HashMap();
                map.put("id", keyPair.getKeyPairFingerPrint());
                map.put("name", keyPair.getKeyPairName());
                return map;
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get key pair." + e.getMessage(), e);
        }
    }

    /**
     * 基础配置询价
     *
     * @param req
     * @return
     */
    public static String calculateConfigPrice(AliyunPriceRequest req) {
        return calculatePrice(req);
    }

    /**
     * 公网IP流量配置询价
     *
     * @param req
     * @return
     */
    public static String calculateTrafficPrice(AliyunPriceRequest req) {
        try {
            AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
            com.aliyun.bssopenapi20171214.Client client = credential.getBssClient();

            // 阿里云公网 IP 流量只能通过后付费接口查询
            GetPayAsYouGoPriceRequest getPayAsYouGoPriceRequest = new GetPayAsYouGoPriceRequest()
                    .setProductCode("ecs")
                    .setSubscriptionType("PayAsYouGo")
                    .setRegion(req.getRegionId())
                    .setModuleList(req.toPostPaidModuleList())
                    .setProductType("");
            try {
                GetPayAsYouGoPriceResponse res = client.getPayAsYouGoPrice(getPayAsYouGoPriceRequest);
                if ("Success".equalsIgnoreCase(res.getBody().getCode())) {
                    Float price;
                    List<GetPayAsYouGoPriceResponseBody.GetPayAsYouGoPriceResponseBodyDataModuleDetailsModuleDetail> moduleDetail = res.getBody().getData().moduleDetails.moduleDetail;
                    // 返回公网 IP 流量费用
                    for (int i = 0; i < moduleDetail.size(); i++) {
                        if ("InternetTrafficOut".equalsIgnoreCase(moduleDetail.get(i).getModuleCode())) {
                            price = moduleDetail.get(i).getCostAfterDiscount();
                            return String.format("%.2f", price) + PriceUnit.YUAN + "/GB";
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate traffic price." + e.getMessage(), e);
        }
        return "";
    }

    /**
     * 获取创建云主机预估价格
     *
     * @param req
     * @return
     */
    private static String calculatePrice(AliyunPriceRequest req) {
        String result = "";
        try {
            AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
            com.aliyun.bssopenapi20171214.Client client = credential.getBssClient();

            // 预付费
            if (AliyunChargeType.PREPAID.getId().equalsIgnoreCase(req.getInstanceChargeType())) {
                GetSubscriptionPriceRequest getSubscriptionPriceRequest = new GetSubscriptionPriceRequest()
                        .setProductCode("ecs")
                        .setSubscriptionType("Subscription")
                        .setOrderType(req.getOrderType())
                        .setQuantity(req.getCount())
                        .setRegion(req.getRegionId())
                        .setModuleList(req.toPrePaidModuleList());
                if (AliyunPriceRequest.NEW_ORDER.equalsIgnoreCase(req.getOrderType())) {
                    String periodNum = req.getPeriodNum();
                    String period = periodNum.indexOf("week") > 0 ? periodNum.substring(0, periodNum.indexOf("week")) : periodNum;
                    getSubscriptionPriceRequest.setServicePeriodUnit(periodNum.indexOf("week") > 0 ? "week" : "month");
                    getSubscriptionPriceRequest.setServicePeriodQuantity(Integer.valueOf(period));
                }
                try {
                    GetSubscriptionPriceResponse res = client.getSubscriptionPrice(getSubscriptionPriceRequest);
                    if ("Success".equalsIgnoreCase(res.getBody().getCode())) {
                        result = String.format("%.3f", res.getBody().getData().tradePrice) + PriceUnit.YUAN;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            } else {
                GetPayAsYouGoPriceRequest getPayAsYouGoPriceRequest = new GetPayAsYouGoPriceRequest()
                        .setProductCode("ecs")
                        .setSubscriptionType("PayAsYouGo")
                        .setRegion(req.getRegionId())
                        .setModuleList(req.toPostPaidModuleList())
                        .setProductType("");
                try {
                    GetPayAsYouGoPriceResponse res = client.getPayAsYouGoPrice(getPayAsYouGoPriceRequest);
                    if ("Success".equalsIgnoreCase(res.getBody().getCode())) {
                        Float price = 0.00f;
                        List<GetPayAsYouGoPriceResponseBody.GetPayAsYouGoPriceResponseBodyDataModuleDetailsModuleDetail> moduleDetail = res.getBody().getData().moduleDetails.moduleDetail;
                        // 返回费用总和,排除公网 IP 流量费用，公网 IP 流量单独计费
                        for (int i = 0; i < moduleDetail.size(); i++) {
                            if (!"InternetTrafficOut".equalsIgnoreCase(moduleDetail.get(i).getModuleCode())) {
                                price = price + moduleDetail.get(i).getCostAfterDiscount();
                            }
                        }
                        result = String.format("%.3f", price * req.getCount()) + PriceUnit.YUAN + "/" + PriceUnit.HOUR;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate price." + e.getMessage(), e);
        }
        return result;
    }

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
        AliyunVmCredential credential = JsonUtil.parseObject(listInstanceTypesRequest.getCredential(), AliyunVmCredential.class);
        try {
            DescribeInstanceTypesResponse describeInstanceTypesResponse = credential.getClient().describeInstanceTypes(listInstanceTypesRequest);
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
                DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance = getInstanceById(aliyunInstanceRequest.getUuid(), aliyunInstanceRequest.getRegionId(), client);
                if(F2CInstanceStatus.Stopped.name().equalsIgnoreCase(instance.getStatus())){
                    return true;
                }
                client.stopInstancesWithOptions(stopInstancesRequest, new RuntimeOptions());
                DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
                describeInstanceStatusRequest.setRegionId(aliyunInstanceRequest.getRegionId());
                describeInstanceStatusRequest.setInstanceId(Arrays.asList(aliyunInstanceRequest.getUuid()));
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
                DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance = getInstanceById(aliyunInstanceRequest.getUuid(), aliyunInstanceRequest.getRegionId(), client);
                if(F2CInstanceStatus.Running.name().equalsIgnoreCase(instance.getStatus())){
                    return true;
                }
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
                DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance = getInstanceById(aliyunInstanceRequest.getUuid(), aliyunInstanceRequest.getRegionId(), client);
                if (AliyunChargeType.PREPAID.getId().equalsIgnoreCase(instance.getInstanceChargeType())) {
                    ModifyInstanceChargeTypeRequest request = new ModifyInstanceChargeTypeRequest()
                            .setInstanceIds(JsonUtil.toJSONString(Arrays.asList(aliyunInstanceRequest.getUuid())))
                            .setRegionId(aliyunInstanceRequest.getRegionId())
                            .setInstanceChargeType(AliyunChargeType.POSTPAID.getId());
                    client.modifyInstanceChargeType(request);
                }

                loopExecute((DeleteInstancesRequest req) -> {
                    try {
                        client.deleteInstances(req);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }, deleteInstancesRequest, 5, 5000L);

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

    private static <T> void loopExecute(Function<T, Object> func, T req, int loopTimes, Long waitTime) {
        int count = 0;
        while (true) {
            try {
                func.apply(req);
                break;
            } catch (Exception e) {
                if (count < loopTimes) {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    throw e;
                }
            }
        }
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
     * 获取系统盘类型
     *
     * @param req
     * @return
     */
    public static List<AliyunDiskTypeDTO.AliyunDiskType> getSystemDiskType(AliyunGetDiskTypeRequest req) {
        req.setDiskUsage("SystemDisk");
        return getDiskTypes(req);
    }

    /**
     * 获取数据盘类型
     *
     * @param req
     * @return
     */
    public static List<AliyunDiskTypeDTO.AliyunDiskType> getDataDiskType(AliyunGetDiskTypeRequest req) {
        req.setDiskUsage("DataDisk");
        return getDiskTypes(req);
    }

    private static List<AliyunDiskTypeDTO.AliyunDiskType> getDiskTypes(AliyunGetDiskTypeRequest req) {
        AliyunVmCredential credential = JsonUtil.parseObject(req.getCredential(), AliyunVmCredential.class);
        Client client = credential.getClientByRegion(req.getRegionId());
        DescribeAvailableResourceRequest describeAvailableResourceRequest = req.toDescribeAvailableResourceRequest();
        List<AliyunDiskTypeDTO.AliyunDiskType> diskTypes = new ArrayList<>();

        if (describeAvailableResourceRequest.getInstanceType() == null) {
            return diskTypes;
        }

        List<DescribeAvailableResourceResponseBody.DescribeAvailableResourceResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> supportedResources = getAvailableResources(describeAvailableResourceRequest, client);
        if (CollectionUtils.isNotEmpty(supportedResources)) {
            diskTypes = supportedResources.stream().map((supportedResource) -> {
                AliyunDiskTypeDTO.AliyunDiskType diskType = AliyunDiskTypeDTO.AliyunDiskType
                        .builder()
                        .diskType(supportedResource.value)
                        .diskTypeName(AliyunDiskType.getName(supportedResource.value))
                        .minDiskSize(supportedResource.min.longValue())
                        .maxDiskSize(supportedResource.max.longValue())
                        .build();
                return diskType;
            }).collect(Collectors.toList());
        }
        return diskTypes;
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

                if (isAttached) {
                    AliyunAttachDiskRequest attachDiskRequest = new AliyunAttachDiskRequest();
                    BeanUtils.copyProperties(request, attachDiskRequest);
                    attachDiskRequest.setDiskId(createdDisk.getDiskId());
                    createdDisk = attachDisk(attachDiskRequest);
                }
                return createdDisk;
            } else {
                throw new RuntimeException("RegionId or credential can not be null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
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
        describeDisksRequest.setDiskIds(JsonUtil.toJSONString(new String[]{f2CDisk.getDiskId()}));
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
        //设置时间，根据syncTimeStampStr,默认一个小时
        getMetricsRequest.setStartTime(String.valueOf(DateUtil.beforeOneHourToTimestamp(Long.valueOf(getMetricsRequest.getSyncTimeStampStr()))));
        getMetricsRequest.setEndTime(getMetricsRequest.getSyncTimeStampStr());
        try {
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            result.addAll(getVmPerfMetric(getMetricsRequest));
        } catch (Exception e) {
            e.printStackTrace();
            throw new SkipPageException(100021, "获取监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
        return result;
    }

    /**
     * TODO 未实现
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
            // result.addAll(getDiskPerfMetric(getMetricsRequest));
        } catch (Exception e) {
            SkipPageException.throwSkipPageException(e);
            throw new Fit2cloudException(100021, "获取监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
        return result;
    }

    private static List<F2CPerfMetricMonitorData> getDiskPerfMetric(GetMetricsRequest getMetricsRequest) {
        AliyunVmCredential credential = JsonUtil.parseObject(getMetricsRequest.getCredential(), AliyunVmCredential.class);
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        //查询所有云盘
        ListDisksRequest listDisksRequest = new ListDisksRequest();
        listDisksRequest.setCredential(getMetricsRequest.getCredential());
        listDisksRequest.setRegionId(getMetricsRequest.getRegionId());
        List<F2CDisk> disks = listDisk(listDisksRequest);
        if (disks.size() == 0) {
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
        disks.forEach(disk -> {
            dimension.put("instanceId", disk.getInstanceUuid());
            //监控指标
            Arrays.stream(AliyunPerfMetricConstants.CloudDiskPerfMetricEnum.values()).sorted().collect(Collectors.toList()).forEach(perfMetric -> {
                describeMetricListRequest.setDimensions(JsonUtil.toJSONString(Arrays.asList(dimension)));
                describeMetricListRequest.setMetricName(perfMetric.getMetricName());
                try {
                    //查询监控指标数据
                    DescribeMetricListResponse response = cmsClient.describeMetricListWithOptions(describeMetricListRequest, new RuntimeOptions());
                    if (StringUtils.equalsIgnoreCase(response.getBody().getCode(), "200") && !StringUtils.isBlank(response.getBody().getDatapoints())) {
                        ArrayNode jsonArray = JsonUtil.parseArray(response.getBody().getDatapoints());
                        jsonArray.forEach(v -> {
                            F2CPerfMetricMonitorData f2CEntityPerfMetric = AliyunMappingUtil.toF2CPerfMetricMonitorData(v);
                            f2CEntityPerfMetric.setEntityType(F2CEntityType.DISK.name());
                            f2CEntityPerfMetric.setMetricName(perfMetric.name());
                            f2CEntityPerfMetric.setPeriod(getMetricsRequest.getPeriod());
                            f2CEntityPerfMetric.setInstanceId(disk.getDiskId());
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
                            f2CEntityPerfMetric.setDevice(v.get("device") == null ? null : v.get("device").toString());
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
    public static F2CDisk attachDisk(AliyunAttachDiskRequest request) {
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
                    return checkDiskStatus(aliyunClient, request.toDescribeDisksRequest(), F2CDiskStatus.IN_USE);
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

    /**
     * 虚拟机配置变更
     *
     * @param request
     * @return
     * @throws PluginException
     */
    public static F2CVirtualMachine changeVmConfig(AliyunUpdateConfigRequest request) throws PluginException {
        Client client = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();

        DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance = getInstanceById(request.getInstanceUuid(), request.getRegionId(), client);
        Optional.ofNullable(instance).orElseThrow(() -> new RuntimeException("Instance not exists."));

        // 阿里云变更实例前需要先关机
        boolean isStartInstance = false;
        AliyunInstanceRequest instanceRequest = new AliyunInstanceRequest();
        BeanUtils.copyProperties(request, instanceRequest);
        instanceRequest.setUuid(request.getInstanceUuid());
        if (!F2CInstanceStatus.Stopped.name().equalsIgnoreCase(instance.getStatus())) {
            powerOff(instanceRequest);
            isStartInstance = true;
        }

        // 变配
        if (AliyunChargeType.PREPAID.getId().equalsIgnoreCase(instance.getInstanceChargeType())) {
            changePrePaidInstanceType(request, client);
        } else {
            changePostPaidInstanceType(request, client);
        }

        // 查看实例是否为已关机状态
        DescribeInstanceStatusRequest describeInstanceStatusRequest = new DescribeInstanceStatusRequest();
        describeInstanceStatusRequest.setRegionId(request.getRegionId());
        describeInstanceStatusRequest.setInstanceId(Arrays.asList(new String[]{request.getInstanceUuid()}));
        try {
            checkStatus(client, "Stopped", describeInstanceStatusRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get status!" + e.getMessage(), e);
        }

        if (isStartInstance) {
            powerOn(instanceRequest);
        } else {
            try {
                // 等待关机的实例配置变更属性更新完，否则查询实例返回的实例类型可能是未变更前的
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }

        F2CVirtualMachine f2CVirtualMachine = AliyunMappingUtil.toF2CVirtualMachine(getInstanceById(request.getInstanceUuid(), request.getRegionId(), client));
        if (F2CInstanceStatus.Starting.name().equalsIgnoreCase(f2CVirtualMachine.getInstanceStatus())) {
            f2CVirtualMachine.setInstanceStatus(F2CInstanceStatus.Running.name());
        }
        return f2CVirtualMachine;
    }

    /**
     * 修改预付费实例规格
     *
     * @param request
     * @param client
     */
    private static void changePrePaidInstanceType(AliyunUpdateConfigRequest request, Client client) {
        ModifyPrepayInstanceSpecRequest modifyPrepayInstanceSpecRequest = new ModifyPrepayInstanceSpecRequest()
                .setRegionId(request.getRegionId())
                .setInstanceId(request.getInstanceUuid())
                .setInstanceType(request.getNewInstanceType());
        try {
            client.modifyPrepayInstanceSpec(modifyPrepayInstanceSpecRequest);
        } catch (Exception e) {
            throw new RuntimeException("Fail to modify instance! Error message:" + e.getMessage());
        }
    }

    /**
     * 修改后付费实例规格
     *
     * @param request
     * @param client
     */
    private static void changePostPaidInstanceType(AliyunUpdateConfigRequest request, Client client) {
        ModifyInstanceSpecRequest modifyInstanceSpecRequest = new ModifyInstanceSpecRequest()
                .setInstanceId(request.getInstanceUuid())
                .setInstanceType(request.getNewInstanceType());
        try {
            client.modifyInstanceSpec(modifyInstanceSpecRequest);
        } catch (Exception e) {
            throw new RuntimeException("Fail to modify instance! Error message:" + e.getMessage());
        }
    }

    /**
     * 根据实例 ID 获取实例
     *
     * @param instanceId
     * @param regionId
     * @param client
     * @return
     */
    private static DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance getInstanceById(String instanceId, String regionId, Client client) {
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.setRegionId(regionId);
        Optional.ofNullable(instanceId).orElseThrow(() -> new RuntimeException("Instance ID is null!"));
        List<String> instancesList = new ArrayList<>();
        instancesList.add(instanceId);
        describeInstancesRequest.setInstanceIds(new Gson().toJson(instancesList));
        return getInstanceById(describeInstancesRequest, client);
    }

    /**
     * 根据实例 ID 获取实例
     *
     * @param describeInstancesRequest
     * @param client
     * @return
     */
    private static DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance getInstanceById(DescribeInstancesRequest describeInstancesRequest, Client client) {
        try {
            DescribeInstancesResponse res = client.describeInstances(describeInstancesRequest);
            List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance> instances = res.body.instances.instance;
            if (CollectionUtils.isNotEmpty(instances)) {
                return res.getBody().getInstances().getInstance().get(0);
            } else {
                throw new RuntimeException("Instance not exist!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 根据实例 ID 获取实例磁盘列表
     *
     * @param instanceId
     * @param client
     * @return
     */
    private static List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> getDiskByInstanceId(String instanceId, String regionId, Client client) {
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest()
                .setRegionId(regionId)
                .setInstanceId(instanceId);
        try {
            DescribeDisksResponse response = client.describeDisks(describeDisksRequest);
            return response.getBody().getDisks().getDisk();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get disks of instance.Instance id:" + instanceId + e.getMessage(), e);
        }
    }

    /**
     * 获取配置变更可选实例类型
     *
     * @param request
     * @return
     */
    public static List<AliyunInstanceType> getInstanceTypesForConfigUpdate(AliyunUpdateConfigRequest request) {
        Client client = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();
        List<AliyunInstanceType> result;

        // 获取所有实例
        ListInstanceTypesRequest listInstanceTypesRequest = new ListInstanceTypesRequest();
        BeanUtils.copyProperties(request, listInstanceTypesRequest);
        List<DescribeInstanceTypesResponseBody.DescribeInstanceTypesResponseBodyInstanceTypesInstanceType> allInstanceTypes = listInstanceType(listInstanceTypesRequest);

        // 获取可用区可选实例
        DescribeAvailableResourceRequest describeAvailableResourceRequest = new DescribeAvailableResourceRequest();
        describeAvailableResourceRequest.setRegionId(request.getRegionId());
        describeAvailableResourceRequest.setZoneId(request.getZoneId());
        describeAvailableResourceRequest.setInstanceChargeType(request.getInstanceChargeType());
        describeAvailableResourceRequest.setDestinationResource("InstanceType");
        describeAvailableResourceRequest.setResourceType("instance");
        String systemDiskCategory = "";
        List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disks = getDiskByInstanceId(request.getInstanceUuid(), request.getRegionId(), client);
        for (DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk diskInfo : disks) {
            if ("System".equalsIgnoreCase(diskInfo.getType())) {
                systemDiskCategory = diskInfo.getCategory();
                break;
            }
        }
        describeAvailableResourceRequest.setSystemDiskCategory(systemDiskCategory);

        // 获取可用区可选实例的 ID 集合
        List<String> zoneInstanceTypeIds = new ArrayList<>();
        List<DescribeAvailableResourceResponseBody.DescribeAvailableResourceResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> zoneSupportedResources = getAvailableResources(describeAvailableResourceRequest, client);
        if (CollectionUtils.isNotEmpty(zoneSupportedResources)) {
            zoneInstanceTypeIds = zoneSupportedResources.stream().map((supportedResource) -> supportedResource.getValue()).toList();
        }

        // 获取配置变更可选实例
        List<DescribeResourcesModificationResponseBody.DescribeResourcesModificationResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> supportedResource = new ArrayList<>();
        List<DescribeResourcesModificationResponseBody.DescribeResourcesModificationResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> upgradeSupportedResource = describeResourcesModification(request, AliyunOperationType.Upgrade, client);
        if (CollectionUtils.isNotEmpty(upgradeSupportedResource)) {
            supportedResource.addAll(upgradeSupportedResource);
        }
        if (AliyunChargeType.PREPAID.getId().equalsIgnoreCase(request.getInstanceChargeType())) {
            List<DescribeResourcesModificationResponseBody.DescribeResourcesModificationResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> downgradeSupportedResource = describeResourcesModification(request, AliyunOperationType.Downgrade, client);
            if (CollectionUtils.isNotEmpty(downgradeSupportedResource)) {
                supportedResource.addAll(downgradeSupportedResource);
            }
        }

        // 获取配置变更可选实例 ID 集合
        List<String> instanceTypeIds = supportedResource.stream().map((theSupportedResource) -> theSupportedResource.getValue()).toList();

        // 可用区可选实例和配置变更可选实例取交集
        instanceTypeIds = instanceTypeIds.stream().filter(zoneInstanceTypeIds::contains).collect(Collectors.toList());

        // 过滤可用实例
        final List<String> instanceTypeIdsFinal = instanceTypeIds;
        result = allInstanceTypes.stream()
                .filter(instanceType -> instanceTypeIdsFinal.contains(instanceType.getInstanceTypeId()) &&
                        !instanceType.getInstanceTypeId().equalsIgnoreCase(request.getCurrentInstanceType()))
                .map((instanceType) -> {
                    AliyunInstanceType resultType = new AliyunInstanceType();
                    // 应要求过滤掉小于 1G 内存的实例规格和当前实例的规格
                    if (instanceType.getMemorySize() >= 1) {
                        resultType.setInstanceType(instanceType.getInstanceTypeId());
                        resultType.setCpuMemory(instanceType.getCpuCoreCount() + "vCPU " + instanceType.getMemorySize().intValue() + "GB");
                        resultType.setInstanceTypeDesc(resultType.getInstanceType() + "（" + resultType.getCpuMemory() + "）");
                    }
                    return resultType;
                }).toList();

        return result;
    }

    /**
     * 查询升级和降配实例规格或时，某一可用区的可用资源信息
     *
     * @param request
     * @param operationType
     * @param client
     * @return
     */
    private static List<DescribeResourcesModificationResponseBody.DescribeResourcesModificationResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> describeResourcesModification(AliyunUpdateConfigRequest request, AliyunOperationType operationType, Client client) {
        DescribeResourcesModificationRequest describeResourcesModificationRequest = new DescribeResourcesModificationRequest()
                .setRegionId(request.getRegionId())
                .setResourceId(request.getInstanceUuid())
                .setDestinationResource("InstanceType")
                .setOperationType(operationType.name())
                .setZoneId(request.getZoneId());

        DescribeResourcesModificationResponse response = null;
        List<DescribeResourcesModificationResponseBody.DescribeResourcesModificationResponseBodyAvailableZonesAvailableZoneAvailableResourcesAvailableResourceSupportedResourcesSupportedResource> supportedResource = new ArrayList<>();
        try {
            response = client.describeResourcesModification(describeResourcesModificationRequest);
            List<DescribeResourcesModificationResponseBody.DescribeResourcesModificationResponseBodyAvailableZonesAvailableZone> availableZone = response.body.availableZones.availableZone;
            if (CollectionUtils.isNotEmpty(availableZone) &&
                    CollectionUtils.isNotEmpty(availableZone.get(0).availableResources.availableResource) &&
                    CollectionUtils.isNotEmpty(availableZone.get(0).availableResources.availableResource.get(0).supportedResources.supportedResource)) {
                supportedResource = availableZone.get(0).availableResources.availableResource.get(0).supportedResources.supportedResource.stream()
                        .filter((theSupportedResource) -> "Available".equalsIgnoreCase(theSupportedResource.status) &&
                                !"WithoutStock".equalsIgnoreCase(theSupportedResource.statusCategory)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get instanceType for updating! " + e.getMessage(), e);
        }

        return supportedResource;
    }

    /**
     * 实例配置变更询价（调用BSS接口询价）
     *
     * @param request
     * @return
     */
    public static String calculateConfigUpdatePrice(AliyunUpdateConfigRequest request) {
        Client client = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();

        if (AliyunChargeType.PREPAID.getId().equalsIgnoreCase(request.getInstanceChargeType())) {
            try {
                return calculatePrePaidConfigUpgradePrice(request, client);
            } catch (Exception e) {
                // 包年包月降配直接返回0，目前尚没有包年包月降配询价的接口
                return "--";
            }
        }

        DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance = getInstanceById(request.getInstanceUuid(), request.getRegionId(), client);

        AliyunPriceRequest priceRequest = new AliyunPriceRequest();
        BeanUtils.copyProperties(request, priceRequest);
        priceRequest.setCount(1);
        priceRequest.setInstanceTypeDTO(new AliyunInstanceType(request.getNewInstanceType()));
        priceRequest.setInstanceChargeType(instance.getInstanceChargeType());
        priceRequest.setOs(instance.OSType);

        // 磁盘
        List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disks = getDiskByInstanceId(request.getInstanceUuid(), request.getRegionId(), client);
        List<AliyunCreateDiskForm> diskList = new ArrayList<>();
        disks.stream().forEach((disk) -> {
            if ("System".equalsIgnoreCase(disk.getType())) {
                AliyunCreateDiskForm systemDisk = new AliyunCreateDiskForm();
                systemDisk.setDiskType(disk.getCategory());
                systemDisk.setSize(disk.getSize().longValue());
                systemDisk.setPerformanceLevel(disk.getPerformanceLevel());
                diskList.add(0, systemDisk);
            }
            if ("Data".equalsIgnoreCase(disk.getType())) {
                AliyunCreateDiskForm dataDisk = new AliyunCreateDiskForm();
                dataDisk.setDiskType(disk.getCategory());
                dataDisk.setSize(disk.getSize().longValue());
                dataDisk.setPerformanceLevel(disk.getPerformanceLevel());
                diskList.add(dataDisk);
            }
        });
        priceRequest.setDisks(diskList);
        priceRequest.setOrderType(AliyunPriceRequest.UPGRADE);

        return calculateConfigPrice(priceRequest);
    }

    /**
     * 查询未到期的包年包月 ECS 实例升配时目标实例规格的价格信息 （ECS接口）
     *
     * @param request
     * @return
     */
    private static String calculatePrePaidConfigUpgradePrice(AliyunUpdateConfigRequest request, Client client) {
        DescribeInstanceModificationPriceRequest describeInstanceModificationPriceRequest = new DescribeInstanceModificationPriceRequest()
                .setRegionId(request.getRegionId())
                .setInstanceId(request.getInstanceUuid())
                .setInstanceType(request.getNewInstanceType());
        try {
            DescribeInstanceModificationPriceResponse response = client.describeInstanceModificationPrice(describeInstanceModificationPriceRequest);
            String result = String.format("%.3f", response.getBody().getPriceInfo().price.tradePrice) + PriceUnit.YUAN;
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get price! " + e.getMessage(), e);
        }
    }

    /**
     * 获取云主机关联的磁盘
     *
     * @param request
     * @return
     */
    public static List<F2CDisk> getVmF2CDisks(BaseDiskRequest request) {
        try {
            Client client = JsonUtil.parseObject(request.getCredential(), AliyunVmCredential.class).getClient();
            List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disks = getDiskByInstanceId(request.getInstanceUuid(), request.getRegionId(), client);
            return disks.stream().map((disk) -> AliyunMappingUtil.toF2CDisk(disk)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("GetVmF2CDisks Error!" + e.getMessage(), e);
        }
    }
}
