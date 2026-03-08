package com.fit2cloud.provider.impl.huawei.api;

import com.aliyun.tea.TeaException;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.provider.impl.huawei.constants.HuaweiDiskType;
import com.fit2cloud.provider.impl.huawei.constants.HuaweiPerfMetricConstants;
import com.fit2cloud.provider.impl.huawei.entity.*;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiVmCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.*;
import com.fit2cloud.provider.impl.huawei.util.HuaweiMappingUtil;
import com.fit2cloud.vm.constants.*;
import com.fit2cloud.vm.entity.F2CDisk;
import com.fit2cloud.vm.entity.F2CImage;
import com.fit2cloud.vm.entity.F2CVirtualMachine;
import com.fit2cloud.vm.entity.request.BaseDiskRequest;
import com.fit2cloud.vm.entity.request.GetMetricsRequest;
import com.fit2cloud.vm.entity.request.RenewInstanceRequest;
import com.google.gson.Gson;
import com.huaweicloud.sdk.bss.v2.BssClient;
import com.huaweicloud.sdk.bss.v2.model.*;
import com.huaweicloud.sdk.ces.v1.CesClient;
import com.huaweicloud.sdk.ces.v1.model.*;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.*;
import com.huaweicloud.sdk.evs.v2.EvsClient;
import com.huaweicloud.sdk.evs.v2.model.ShowJobRequest;
import com.huaweicloud.sdk.evs.v2.model.ShowJobResponse;
import com.huaweicloud.sdk.evs.v2.model.*;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.AuthProjectResult;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListAuthProjectsRequest;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListAuthProjectsResponse;
import com.huaweicloud.sdk.ims.v2.ImsClient;
import com.huaweicloud.sdk.ims.v2.model.ImageInfo;
import com.huaweicloud.sdk.ims.v2.model.ListImagesRequest;
import com.huaweicloud.sdk.ims.v2.model.ListImagesResponse;
import com.huaweicloud.sdk.vpc.v2.VpcClient;
import com.huaweicloud.sdk.vpc.v2.model.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author:张少虎
 * @Date: 2022/9/22  2:44 PM
 * @Version 1.0
 * @注释:
 */
public class HuaweiSyncCloudApi {
    private static final Logger logger = LoggerFactory.getLogger(HuaweiSyncCloudApi.class);
    private static final int WAIT_COUNT = 50;
    private static final int SLEEP_TIME = 10000;

    /**
     * 获取华为云云主机数据
     *
     * @param listVirtualMachineRequest 获取华为云云主机请求对象
     * @return 云主机对象
     */
    public static List<F2CVirtualMachine> listVirtualMachine(ListVirtualMachineRequest listVirtualMachineRequest) {
        if (StringUtils.isEmpty(listVirtualMachineRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(listVirtualMachineRequest.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(listVirtualMachineRequest.getCredential(), HuaweiVmCredential.class);
            listVirtualMachineRequest.setLimit(PageUtil.DefaultPageSize);
            listVirtualMachineRequest.setOffset(PageUtil.DefaultCurrentPage);
            EcsClient ecsClient = credential.getEcsClient(listVirtualMachineRequest.getRegionId());
            // 分页查询云主机列表
            List<ServerDetail> instances = PageUtil.page(listVirtualMachineRequest, req -> listServersDetails(ecsClient, req),
                    ListServersDetailsResponse::getServers,
                    (req, res) -> req.getLimit() <= res.getServers().size(),
                    req -> req.setOffset(req.getOffset() + 1));
            if (CollectionUtils.isNotEmpty(instances)) {
                List<Port> ports = listPorts(listVirtualMachineRequest.getCredential(), listVirtualMachineRequest.getRegionId());
                List<OrderInstanceV2> orderInstanceV2s = listPayPerUseCustomerResources(credential, null);
                return instances.stream()
                        .map(server -> HuaweiMappingUtil.toF2CVirtualMachine(server, ports, orderInstanceV2s))
                        .map(virtualMachine -> {
                            virtualMachine.setRegion(listVirtualMachineRequest.getRegionId());
                            return virtualMachine;
                        }).map(virtualMachine -> appendDisk(listVirtualMachineRequest.getCredential(), listVirtualMachineRequest.getRegionId(), virtualMachine))
                        .toList();
            }
        }
        return new ArrayList<>();
    }


    /**
     * 查询流水账单列表
     *
     * @param credential 认证对象
     * @param resourceId 资源id集合 传null代表查所有资源
     * @return 流水账单列表
     */
    public static List<OrderInstanceV2> listPayPerUseCustomerResources(HuaweiVmCredential credential, List<String> resourceId) {
        BssClient bssClient = credential.getBssClient();
        ListPayPerUseCustomerResourcesRequest request = new ListPayPerUseCustomerResourcesRequest();
        QueryResourcesReq queryResourcesReq = new QueryResourcesReq();
        queryResourcesReq.setLimit(PageUtil.DefaultPageSize);
        queryResourcesReq.setOffset(PageUtil.DefaultCurrentPage - 1);
        queryResourcesReq.setResourceIds(resourceId);
        request.setBody(queryResourcesReq);
        return PageUtil.page(request, req -> bssClient.listPayPerUseCustomerResources(request),
                ListPayPerUseCustomerResourcesResponse::getData,
                (req, res) -> req.getBody().getLimit() <= res.getData().size(),
                req -> req.getBody().setOffset(req.getBody().getOffset() + 1));
    }


    /**
     * 获取磁盘
     *
     * @param request 请求磁盘参数
     * @return 响应对象
     */
    public static List<F2CDisk> listDisk(ListDisksRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EvsClient evsClient = credential.getEvsClient(request.getRegionId());
            try {
                ListVolumesResponse listVolumesResponse = evsClient.listVolumes(request);
                List<VolumeDetail> volumes = listVolumesResponse.getVolumes();
                return volumes.stream().map(HuaweiMappingUtil::toF2CDisk).toList();
            } catch (Exception e) {
                ReTryException.throwHuaweiReTry(e);
                SkipPageException.throwHuaweiSkip(e);
                throw new Fit2cloudException(10000, "获取数据失败" + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * 同步镜像
     *
     * @param request 请求对象
     * @return 响应对象
     */
    public static List<F2CImage> lisImages(ListImageRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            ImsClient imsClient = credential.getImsClient(request.getRegionId());
            try {
                // 只查询公共镜像gold
                request.setImagetype(ListImagesRequest.ImagetypeEnum.GOLD);
                ListImagesResponse listImagesResponse = imsClient.listImages(request);
                List<ImageInfo> images = listImagesResponse.getImages();
                return images.stream().map(imageInfo -> HuaweiMappingUtil.toF2CImage(imageInfo, request.getRegionId())).filter(Objects::nonNull).toList();
            } catch (Exception e) {
                ReTryException.throwHuaweiReTry(e);
                SkipPageException.throwHuaweiSkip(e);
                throw new Fit2cloudException(10000, "获取数据失败" + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * 给云主机添加磁盘数据
     *
     * @param credential     认证信息
     * @param regionId       区域信息
     * @param virtualMachine 云主机对象
     * @return 云主机对象
     */
    private static F2CVirtualMachine appendDisk(String credential, String regionId, F2CVirtualMachine virtualMachine) {
        ListDisksRequest listDisksRequest = new ListDisksRequest();
        listDisksRequest.setCredential(credential);
        listDisksRequest.setRegionId(regionId);
        List<F2CDisk> disks = listDisk(listDisksRequest);
        long sum = disks.stream().mapToLong(F2CDisk::getSize).sum();
        virtualMachine.setDisk((int) sum);
        return virtualMachine;
    }

    /**
     * 给云主机增加到期时间
     *
     * @param credential
     * @param serverDetail
     * @param virtualMachine
     */
    private static void appendExpiredTime(HuaweiVmCredential credential, ServerDetail serverDetail, F2CVirtualMachine virtualMachine) {
        String orderId = serverDetail.getMetadata().get("metering.order_id");
        String productId = serverDetail.getMetadata().get("metering.product_id");
        ShowCustomerOrderDetailsResponse response = getOrderDetailsById(orderId, credential.getBssClient());
        if (CollectionUtils.isNotEmpty(response.getOrderLineItems())) {
            String expireTime = response.getOrderLineItems().stream().filter(orderLineItemEntityV2 ->
                    orderLineItemEntityV2.getProductId().equalsIgnoreCase(productId)
            ).toList().get(0).getExpireTime();
            virtualMachine.setExpiredTime(new Date(CommonUtil.getUTCTime(expireTime, "yyyy-MM-dd'T'HH:mm:ss'Z'")).getTime());
        }
    }

    /**
     * 获取port列表
     *
     * @param credential 认证信息
     * @param regionId   区域id
     * @return port对象
     */
    private static List<Port> listPorts(String credential, String regionId) {
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(credential, HuaweiVmCredential.class);
        return listPorts(huaweiVmCredential, regionId);
    }

    /**
     * 获取网络端口
     *
     * @param credential 认证参数
     * @param regionId   区域id
     * @return 网络端口列表
     */
    private static List<Port> listPorts(HuaweiVmCredential credential, String regionId) {
        try {
            VpcClient vpcClient = credential.getVpcClient(regionId);
            ListPortsRequest request = new ListPortsRequest();
            request.setLimit(2000);
            ListPortsResponse listPortsResponse = vpcClient.listPorts(request);
            return listPortsResponse.getPorts();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * @param ecsClient ecs客户端
     * @param req       请求对象
     * @return ListServersDetailsResponse
     */
    private static ListServersDetailsResponse listServersDetails(EcsClient ecsClient, ListServersDetailsRequest req) {
        try {
            return ecsClient.listServersDetails(req);
        } catch (Exception e) {
            ReTryException.throwHuaweiReTry(e);
            SkipPageException.throwHuaweiSkip(e);
            throw new Fit2cloudException(10000, "获取数据失败" + e.getMessage());
        }
    }

    public static boolean powerOff(HuaweiInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());

            try {
                ServerDetail serverDetail = getInstanceById(request.getUuid(), client);
                if (F2CInstanceStatus.Stopped.name().equalsIgnoreCase(HuaweiMappingUtil.toF2CInstanceStatus(serverDetail.getStatus()))) {
                    return true;
                }
                BatchStopServersResponse batchStopServersResponse = client.batchStopServers(new BatchStopServersRequest()
                        .withBody(new BatchStopServersRequestBody()
                                .withOsStop(
                                        new BatchStopServersOption()
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuid())))
                                                .withType(request.getForce() ? BatchStopServersOption.TypeEnum.HARD : BatchStopServersOption.TypeEnum.SOFT))));

                checkEcsJobStatus(client, batchStopServersResponse.getJobId());
                return true;
            } catch (TeaException teaException) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_OFF_FAIL.getCode(), teaException.getMessage());
            } catch (Exception e) {
                TeaException error = new TeaException(e.getMessage(), e);
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_OFF_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    public static boolean powerOn(HuaweiInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            try {
                ServerDetail serverDetail = getInstanceById(request.getUuid(), client);
                if (F2CInstanceStatus.Running.name().equalsIgnoreCase(HuaweiMappingUtil.toF2CInstanceStatus(serverDetail.getStatus()))) {
                    return true;
                }
                BatchStartServersResponse batchStartServersResponse = client.batchStartServers(new BatchStartServersRequest()
                        .withBody(new BatchStartServersRequestBody()
                                .withOsStart(
                                        new BatchStartServersOption()
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuid()))))));
                checkEcsJobStatus(client, batchStartServersResponse.getJobId());
                return true;
            } catch (TeaException teaError) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_ON_FAIL.getCode(), teaError.getMessage());
            } catch (Exception e) {
                TeaException error = new TeaException(e.getMessage(), e);
                throw new Fit2cloudException(ErrorCodeConstants.VM_POWER_ON_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    public static boolean rebootInstance(HuaweiInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            try {
                BatchRebootServersResponse batchRebootServersResponse = client.batchRebootServers(new BatchRebootServersRequest()
                        .withBody(new BatchRebootServersRequestBody()
                                .withReboot(
                                        new BatchRebootSeversOption()
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuid())))
                                                .withType(request.getForce() ? BatchRebootSeversOption.TypeEnum.HARD : BatchRebootSeversOption.TypeEnum.SOFT))));
                checkEcsJobStatus(client, batchRebootServersResponse.getJobId());
                return true;
            } catch (TeaException teaException) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_REBOOT_FAIL.getCode(), teaException.getMessage());
            } catch (Exception e) {
                TeaException error = new TeaException(e.getMessage(), e);
                throw new Fit2cloudException(ErrorCodeConstants.VM_REBOOT_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    public static boolean deleteInstance(HuaweiInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            try {
                ShowServerResponse showServerResponse = client.showServer(new ShowServerRequest().withServerId(request.getUuid()));
                ServerDetail instance = showServerResponse.getServer();
                // 如果是包年包月 调用退订方法 否则直接删除
                Map<String, String> metadata = instance.getMetadata();
                if (ObjectUtils.isNotEmpty(metadata) && metadata.containsKey("charging_mode") && StringUtils.equals(metadata.get("charging_mode"), "1")) {
                    BssClient bssClient = credential.getBssClient();
                    CancelResourcesSubscriptionRequest cancelRequest = new CancelResourcesSubscriptionRequest();
                    UnsubscribeResourcesReq body = new UnsubscribeResourcesReq();
                    body.setResourceIds(new ArrayList<String>() {{
                        add(request.getUuid());
                    }});
                    body.setUnsubscribeType(1);
                    cancelRequest.setBody(body);
                    CancelResourcesSubscriptionResponse cancelResponse = bssClient.cancelResourcesSubscription(cancelRequest);
                    return cancelResponse.getHttpStatusCode() == 200;
                } else {
                    DeleteServersResponse batchStartServersResponse = client.deleteServers(new DeleteServersRequest()
                            .withBody(new DeleteServersRequestBody()
                                    .withServers(Arrays.asList(new ServerId().withId(request.getUuid())))));
                    checkEcsJobStatus(client, batchStartServersResponse.getJobId());
                    return true;
                }
            } catch (TeaException teaException) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_DELETE_FAIL.getCode(), teaException.getMessage());
            } catch (Exception e) {
                TeaException error = new TeaException(e.getMessage(), e);
                throw new Fit2cloudException(ErrorCodeConstants.VM_DELETE_FAIL.getCode(), error.getMessage());
            }
        }
        return false;
    }

    private static void checkEcsJobStatus(EcsClient client, String jobId) {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(SLEEP_TIME);
                com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse jobResponse = client.showJob(new com.huaweicloud.sdk.ecs.v2.model.ShowJobRequest().withJobId(jobId));
                com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse.StatusEnum status = jobResponse.getStatus();
                if (ShowJobResponse.StatusEnum.SUCCESS.getValue().equals(status.getValue())) {
                    break;
                }
                if (ShowJobResponse.StatusEnum.FAIL.getValue().equals(status.getValue())) {
                    throw new RuntimeException(jobResponse.getFailReason());
                }
                if (count >= WAIT_COUNT) {
                    throw new RuntimeException("Check cloud server status timeout！");
                }
                count++;
            } catch (Exception e) {
                throw new RuntimeException("Check cloud server status error: " + e.getMessage());
            }
        }
    }

    /**
     * 根据可用区过滤磁盘种类
     *
     * @param request
     * @return
     */
    public static List<Map<String, String>> getDiskTypes(HuaweiGetDiskTypeRequest request) {
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());

        CinderListVolumeTypesRequest cinderListVolumeTypesRequest = new CinderListVolumeTypesRequest();
        try {
            CinderListVolumeTypesResponse response = evsClient.cinderListVolumeTypes(cinderListVolumeTypesRequest);
            List<Map<String, String>> mapList = new ArrayList<>();
            response.getVolumeTypes().forEach(volumeType -> {
                if (StringUtils.isNoneEmpty(request.getZone())
                        //这个名称的磁盘类型有问题，云上显示没有，但是接口会返回来，在这里特殊处理去掉
                        && !StringUtils.equalsIgnoreCase("uh-l1", volumeType.getName())
                        && StringUtils.isNoneEmpty(volumeType.getExtraSpecs().getReSKEYAvailabilityZones())
                        && volumeType.getExtraSpecs().getReSKEYAvailabilityZones().contains(request.getZone())
                        && (StringUtils.isEmpty(volumeType.getExtraSpecs().getOsVendorExtendedSoldOutAvailabilityZones())
                        || !volumeType.getExtraSpecs().getOsVendorExtendedSoldOutAvailabilityZones().contains(request.getZone())) && !volumeType.getName().startsWith("DESS_")) {
                    Map<String, String> vol = new HashMap<>();
                    vol.put("id", volumeType.getName());
                    vol.put("name", HuaweiDiskType.getName(volumeType.getName()));
                    mapList.add(vol);
                }
            });
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 创建磁盘
     *
     * @param request
     * @return
     */
    public static List<F2CDisk> createDisks(HuaweiCreateDisksRequest request) {
        List<F2CDisk> f2CDisks = new ArrayList<>();
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
        try {
            for (F2CDisk disk : request.getDisks()) {
                CreateVolumeResponse response = evsClient.createVolume(request.toCreateVolumeRequest(disk));
                ShowJobResponse showJobResponse = getJob(response.getJobId(), evsClient);
                String status = request.getInstanceUuid() == null ? F2CDiskStatus.AVAILABLE : "in-use";
                F2CDisk createdDisk = HuaweiMappingUtil.toF2CDisk(checkVolumeStatus(showJobResponse.getEntities().getVolumeId(), evsClient, status));
                createdDisk.setDeleteWithInstance(disk.getDeleteWithInstance());
                f2CDisks.add(createdDisk);
            }
            return f2CDisks;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 创建磁盘
     */
    public static F2CDisk createDisk(HuaweiCreateDiskRequest request) {
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
        try {
            CreateVolumeResponse response = evsClient.createVolume(request.toCreateVolumeRequest());
            String volumeId;
            // 华为云的 in-use 是中划线😭
            String status = request.getInstanceUuid() == null ? F2CDiskStatus.AVAILABLE : "in-use";
            if (StringUtils.isNotEmpty(response.getOrderId())) {
                volumeId = checkOrderResourceId(response.getOrderId(), huaweiVmCredential.getBssClient());
            } else {
                ShowJobResponse showJobResponse = getJob(response.getJobId(), evsClient);
                volumeId = showJobResponse.getEntities().getVolumeId();
            }
            F2CDisk createdDisk = HuaweiMappingUtil.toF2CDisk(checkVolumeStatus(volumeId, evsClient, status));

            // 单独调用接口设置磁盘是否随实例删除属性，不抛出异常
            if (DeleteWithInstance.YES.name().equalsIgnoreCase(request.getDeleteWithInstance())) {
                try {
                    EcsClient ecsClient = huaweiVmCredential.getEcsClient(request.getRegionId());
                    updateServerBlockDevice(ecsClient, request.getInstanceUuid(), createdDisk.getDiskId(), request.getDeleteWithInstance());
                    createdDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
                } catch (Exception e) {
                    createdDisk.setDeleteWithInstance(DeleteWithInstance.NO.name());
                    LogUtil.error("Failed to modify disk." + e.getMessage(), e);
                }
            }

            return createdDisk;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String checkOrderResourceId(String orderId, BssClient bssClient) {
        ListPayPerUseCustomerResourcesRequest resourceRequest = new ListPayPerUseCustomerResourcesRequest();
        QueryResourcesReq body = new QueryResourcesReq();
        body.setOrderId(orderId);
        resourceRequest.withBody(body);
        String resourceId = null;
        try {
            int count = 0;
            boolean b = true;
            while (b) {
                Thread.sleep(5000);
                count++;
                ListPayPerUseCustomerResourcesResponse resourcesResponse = bssClient.listPayPerUseCustomerResources(resourceRequest);
                List<OrderInstanceV2> disksInfo = resourcesResponse.getData();
                if (CollectionUtils.isNotEmpty(disksInfo)) {
                    b = false;
                    resourceId = disksInfo.get(0).getResourceId();
                }
                if (count >= WAIT_COUNT) {
                    throw new RuntimeException("Check order resource info timeout！");
                }
            }
            return resourceId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 扩容磁盘
     *
     * @param request
     * @return
     */
    public static boolean enlargeDisk(HuaweiResizeDiskRequest request) {
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());

            String diskId = request.getDiskId();
            ShowVolumeRequest showVolumeRequest = new ShowVolumeRequest();
            showVolumeRequest.setVolumeId(diskId);
            VolumeDetail volume = evsClient.showVolume(showVolumeRequest).getVolume();
            if (volume == null) {
                throw new RuntimeException("Can not find disk: " + request.getDiskId());
            }
            String status = volume.getStatus();
            evsClient.resizeVolume(request.toResizeVolumeRequest());
            if ("in-use".equalsIgnoreCase(status) || status.equalsIgnoreCase(F2CDiskStatus.AVAILABLE)) {
                checkVolumeStatus(diskId, evsClient, status);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 挂载磁盘
     *
     * @param request
     * @return
     */
    public static F2CDisk attachDisk(HuaweiAttachDiskRequest request) {
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
            EcsClient ecsClient = huaweiVmCredential.getEcsClient(request.getRegionId());
            ecsClient.attachServerVolume(request.toAttachServerVolumeRequest());
            F2CDisk f2CDisk = HuaweiMappingUtil.toF2CDisk(checkVolumeStatus(request.getDiskId(), evsClient, "in-use"));

            // 单独调用接口设置磁盘是否随实例删除属性，不抛出异常
            if (DeleteWithInstance.YES.name().equalsIgnoreCase(request.getDeleteWithInstance())) {
                try {
                    updateServerBlockDevice(ecsClient, request.getInstanceUuid(), request.getDiskId(), request.getDeleteWithInstance());
                    f2CDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
                } catch (Exception e) {
                    f2CDisk.setDeleteWithInstance(DeleteWithInstance.NO.name());
                    LogUtil.error("Failed to modify disk." + e.getMessage(), e);
                }
            }

            return f2CDisk;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 修改云服务器挂载的单个磁盘信息（是否随实例删除属性）
     *
     * @param client
     * @return
     */
    public static void updateServerBlockDevice(EcsClient client, String instanceId, String diskId, String deleteWithInstance) {
        if (StringUtils.isNotEmpty(instanceId)) {
            UpdateServerBlockDeviceRequest blockDeviceRequest =
                    new UpdateServerBlockDeviceRequest()
                            .withServerId(instanceId)
                            .withVolumeId(diskId)
                            .withBody(new UpdateServerBlockDeviceReq()
                                    .withBlockDevice(new UpdateServerBlockDeviceOption()
                                            .withDeleteOnTermination(DeleteWithInstance.YES.name().equals(deleteWithInstance))));
            client.updateServerBlockDevice(blockDeviceRequest);
        }
    }

    /**
     * 卸载磁盘
     *
     * @param request
     * @return
     */
    public static boolean detachDisk(HuaweiDetachDiskRequest request) {
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
            EcsClient ecsClient = huaweiVmCredential.getEcsClient(request.getRegionId());

            // 判断磁盘是否是系统盘
            ShowVolumeRequest showVolumeRequest = new ShowVolumeRequest();
            showVolumeRequest.setVolumeId(request.getDiskId());
            VolumeDetail volume = evsClient.showVolume(showVolumeRequest).getVolume();
            Optional.ofNullable(volume).orElseThrow(() -> new RuntimeException("Can not find the disk!"));

            if (Boolean.valueOf(volume.getBootable())) {
                // 判断实例是否是关机状态
                ShowServerResponse showServerResponse = ecsClient.showServer(new ShowServerRequest().withServerId(request.getInstanceUuid()));
                ServerDetail server = showServerResponse.getServer();
                Optional.ofNullable(server).orElseThrow(() -> new RuntimeException("Can not find the server!"));

                String serverStatus = server.getStatus();
                if (!"stopped".equalsIgnoreCase(serverStatus)) {
                    // 系统盘需要实例关机方可卸载
                    throw new RuntimeException("Server status must be stopped!");
                }
            }

            ecsClient.detachServerVolume(request.toDetachServerVolumeRequest());
            checkVolumeStatus(request.getDiskId(), evsClient, F2CDiskStatus.AVAILABLE);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to detach data disk!" + e.getMessage(), e);
        }
    }

    /**
     * 删除磁盘
     *
     * @param request
     * @return
     */
    public static boolean deleteDisk(HuaweiDeleteDiskRequest request) {
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
            evsClient.deleteVolume(request.toDeleteVolumeRequest());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static VolumeDetail checkVolumeStatus(String volumeId, EvsClient evsClient, String status) throws Exception {
        try {
            int count = 0;
            VolumeDetail volume = null;
            boolean b = true;
            while (b) {
                Thread.sleep(5000);
                count++;
                ShowVolumeRequest showVolumeRequest = new ShowVolumeRequest();
                showVolumeRequest.setVolumeId(volumeId);
                volume = evsClient.showVolume(showVolumeRequest).getVolume();

                if (volume != null && status.equalsIgnoreCase(volume.getStatus())) {
                    b = false;
                }
                if (count >= WAIT_COUNT) {
                    throw new RuntimeException("Check cloud disk status timeout！");
                }
            }
            return volume;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static ShowJobResponse getJob(String jobId, EvsClient evsClient) {
        com.huaweicloud.sdk.evs.v2.model.ShowJobRequest showJobRequest = new ShowJobRequest();
        showJobRequest.setJobId(jobId);
        try {
            int count = 0;
            while (true) {
                Thread.sleep(2000);
                count++;
                ShowJobResponse showJobResponse = evsClient.showJob(showJobRequest);
                if ("FAIL".equalsIgnoreCase(showJobResponse.getStatus().getValue())) {
                    throw new RuntimeException(new Gson().toJson(showJobResponse));
                }
                if ("SUCCESS".equalsIgnoreCase(showJobResponse.getStatus().getValue())) {
                    return showJobResponse;
                }
                if (count >= WAIT_COUNT) {
                    throw new RuntimeException("Check job status timeout！");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<F2CPerfMetricMonitorData> getF2CPerfMetricList(GetMetricsRequest getMetricsRequest) {
        existRegion(getMetricsRequest);
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        //设置时间，根据syncTimeStampStr,默认一个小时
        Long startTime = DateUtil.beforeOneHourToTimestamp(Long.valueOf(getMetricsRequest.getSyncTimeStampStr()));
        //多获取过去30分钟的数据，防止同步线程时间不固定，导致数据不全的问题
        getMetricsRequest.setStartTime((startTime - 1800000L));
        getMetricsRequest.setEndTime(Long.parseLong(getMetricsRequest.getSyncTimeStampStr()));
        try {
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            getMetricsRequest.setPeriod(300);
            getMetricsRequest.setPeriod(300);
            result.addAll(getVmPerfMetric(getMetricsRequest));
        } catch (Exception e) {
            throw new SkipPageException(100021, "获取监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
        }
        return result;
    }

    private static void existRegion(GetMetricsRequest getMetricsRequest) {
        if (StringUtils.isEmpty(getMetricsRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
    }

    /**
     * 获取云主机监控指标数据
     * 除了CPU,内存，磁盘与网络都是基础指标的数据，因为API无法获取
     *
     * @param getMetricsRequest 监控查询参数
     * @return 监控数据
     */
    private static List<F2CPerfMetricMonitorData> getVmPerfMetric(GetMetricsRequest getMetricsRequest) {
        HuaweiVmCredential credential = JsonUtil.parseObject(getMetricsRequest.getCredential(), HuaweiVmCredential.class);
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        List<F2CVirtualMachine> vms = listVirtualMachine(getListVmRequest(getMetricsRequest));
        if (vms.size() == 0) {
            return result;
        }
        CesClient cesClient = credential.getCesClient(getMetricsRequest.getRegionId());
        // 循环云主机
        vms.forEach(vm -> {
            try {
                // 存储接口返回的数据
                Map<String, List<BatchMetricData>> apiResult = new HashMap<>(3);
                // 数据聚合查询接口
                List.of("average", "max", "min").forEach(filter -> {
                    try {
                        BatchListMetricDataRequest request = batchListMetricRequest(getMetricsRequest, vm.getInstanceUUID(), filter);
                        BatchListMetricDataResponse response = cesClient.batchListMetricData(request);
                        apiResult.put(filter, response.getMetrics());
                    } catch (Exception e) {
                        LogUtil.error("华为云查询云主机 " + vm.getName() + "聚合值-" + filter + "- 监控数据失败:" + e.getMessage());
                    }
                });
                // 处理结果，映射数据到平台需要查询的指标
                Arrays.stream(HuaweiPerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().toList().forEach(perfMetric -> {
                    // 存储时间节点数据，5分钟间隔，1个半小时，最多18条数据
                    Map<Long, DatapointForBatchMetric> datapointMap = new HashMap<>(18);
                    // 处理结果数据
                    apiResult.keySet().forEach(key -> {
                        // 过滤监控数据
                        List<DatapointForBatchMetric> datapointList = new ArrayList<>();
                        datapointList = getDatapointForBaseOrAgent(apiResult.get(key), perfMetric);
                        datapointList.forEach(v -> {
                            if (!datapointMap.containsKey(v.getTimestamp())) {
                                datapointMap.put(v.getTimestamp(), v);
                            }
                            if (StringUtils.equalsIgnoreCase(key, "average")) {
                                datapointMap.get(v.getTimestamp()).setAverage(v.getAverage());
                            }
                            if (StringUtils.equalsIgnoreCase(key, "max")) {
                                datapointMap.get(v.getTimestamp()).setMax(v.getMax());
                            }
                            if (StringUtils.equalsIgnoreCase(key, "min")) {
                                datapointMap.get(v.getTimestamp()).setMin(v.getMin());
                            }
                        });
                    });
                    datapointMap.forEach((k, v) -> {
                        F2CPerfMetricMonitorData f2CEntityPerfMetric = HuaweiMappingUtil.toF2CPerfMetricMonitorData(v);
                        f2CEntityPerfMetric.setEntityType(F2CEntityType.VIRTUAL_MACHINE.name());
                        f2CEntityPerfMetric.setMetricName(perfMetric.name());
                        f2CEntityPerfMetric.setPeriod(getMetricsRequest.getPeriod());
                        f2CEntityPerfMetric.setInstanceId(vm.getInstanceUUID());
                        f2CEntityPerfMetric.setUnit(perfMetric.getUnit());
                        result.add(f2CEntityPerfMetric);
                    });
                });
            } catch (Exception e) {
                LogUtil.error("同步 华为 云主机 " + vm.getName() + " 监控失败:" + e.getMessage());
            }
        });
        return result;
    }

    /**
     * 过滤监控数据
     *
     * @param response   结果
     * @param perfMetric 指标
     * @return 监控数据
     */
    private static List<DatapointForBatchMetric> getDatapointForBaseOrAgent(List<BatchMetricData> response, HuaweiPerfMetricConstants.CloudServerPerfMetricEnum perfMetric) {
        List<DatapointForBatchMetric> agentData = getDatapointForBaseOrAgent(response, perfMetric, true);
        return CollectionUtils.isNotEmpty(agentData) ? agentData : getDatapointForBaseOrAgent(response, perfMetric, false);
    }

    /**
     * 获取 基础监控数据活着agent的监控数据datapoint
     *
     * @param response   数据结果
     * @param perfMetric 指标
     * @param agent      是否是agent
     * @return 数据
     */
    private static List<DatapointForBatchMetric> getDatapointForBaseOrAgent(List<BatchMetricData> response, HuaweiPerfMetricConstants.CloudServerPerfMetricEnum perfMetric, boolean agent) {
        List<BatchMetricData> agentMetricData = response.stream()
                .filter(v -> StringUtils.equalsIgnoreCase(agent ? "AGT.ECS" : "SYS.ECS", v.getNamespace()))
                .filter(v -> StringUtils.equalsIgnoreCase(agent ? perfMetric.getAgentMetricName() : perfMetric.getBaseMetricName(), v.getMetricName()))
                .filter(v -> v.getDatapoints().size() > 0).toList();
        return CollectionUtils.isNotEmpty(agentMetricData) ? agentMetricData.get(0).getDatapoints() : new ArrayList<>();
    }


    /**
     * 批量指标获取监控数据
     *
     * @param getMetricsRequest 参数来源
     * @param instanceId        云主机ID
     * @param filter            数据聚合方式,max为最大值,min为最小值,average为平均值
     * @return 批量查询参数
     */
    private static BatchListMetricDataRequest batchListMetricRequest(GetMetricsRequest getMetricsRequest, String instanceId, String filter) {
        BatchListMetricDataRequest request = new BatchListMetricDataRequest();
        BatchListMetricDataRequestBody body = new BatchListMetricDataRequestBody();
        List<MetricsDimension> listMetricsDimensions = new ArrayList<>();
        listMetricsDimensions.add(
                new MetricsDimension()
                        .withName("instance_id")
                        .withValue(instanceId)
        );
        List<MetricInfo> listBodyMetrics = listMetricInfo(listMetricsDimensions);
        body.withTo(Long.valueOf(getMetricsRequest.getEndTime()));
        body.withFrom(Long.valueOf(getMetricsRequest.getStartTime()));
        body.withFilter(filter);
        body.withPeriod("300");
        body.withMetrics(listBodyMetrics);
        request.withBody(body);
        return request;
    }

    /**
     * 获取所有指标两个namespace的数据（基础、agent）
     * listBodyMetrics最大支持500个
     * 目前10*2
     *
     * @param listMetricsDimensions 对象参数
     * @return 所有指标参数
     */
    private static List<MetricInfo> listMetricInfo(List<MetricsDimension> listMetricsDimensions) {
        List<MetricInfo> listBodyMetrics = new ArrayList<>();
        Arrays.stream(HuaweiPerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().toList().forEach(perfMetric -> {
            listBodyMetrics.add(
                    new MetricInfo()
                            .withNamespace("AGT.ECS")
                            .withMetricName(perfMetric.getAgentMetricName())
                            .withDimensions(listMetricsDimensions)
            );
            listBodyMetrics.add(
                    new MetricInfo()
                            .withNamespace("SYS.ECS")
                            .withMetricName(perfMetric.getBaseMetricName())
                            .withDimensions(listMetricsDimensions)
            );
        });
        return listBodyMetrics;
    }

    /**
     * 根据指标查询指定值的监控数据
     *
     * @param cesClient  查询客户端
     * @param vm         要查询监控数据的云主机
     * @param perfMetric 监控指标
     * @param request    API参数
     * @return 监控数据时间戳
     */
    private static Map<Long, Datapoint> getVmMonitoringData(CesClient cesClient, F2CVirtualMachine vm, HuaweiPerfMetricConstants.CloudServerPerfMetricEnum perfMetric, ShowMetricDataRequest request) {
        Map<Long, Datapoint> datapointMap = new HashMap<>();
        List.of("average", "max", "min").forEach(filter -> {
            try {
                request.withFilter(ShowMetricDataRequest.FilterEnum.fromValue(filter));
                //查询监控指标数据
                ShowMetricDataResponse response = cesClient.showMetricData(request);
                if (response.getHttpStatusCode() == 200 && CollectionUtils.isNotEmpty(response.getDatapoints())) {
                    List<Datapoint> list = response.getDatapoints();
                    list.forEach(v -> {
                        if (!datapointMap.containsKey(v.getTimestamp())) {
                            datapointMap.put(v.getTimestamp(), v);
                        }
                        if (StringUtils.equalsIgnoreCase(filter, "average")) {
                            datapointMap.get(v.getTimestamp()).setAverage(v.getAverage());
                        }
                        if (StringUtils.equalsIgnoreCase(filter, "max")) {
                            datapointMap.get(v.getTimestamp()).setMax(v.getMax());
                        }
                        if (StringUtils.equalsIgnoreCase(filter, "min")) {
                            datapointMap.get(v.getTimestamp()).setMin(v.getMin());
                        }
                    });
                }
            } catch (Exception e) {
                LogUtil.error("查询 华为 云主机 " + vm.getName() + " 监控指标 " + perfMetric.getDescription() + " " + filter + "值失败:" + e.getMessage());
            }
        });
        return datapointMap;
    }

    /**
     * 查询云主机监控数据参数
     *
     * @param getMetricsRequest 全部参数
     * @return API所需基本参数
     */
    @NotNull
    private static ShowMetricDataRequest getShowMetricDataRequest(GetMetricsRequest getMetricsRequest) {
        ShowMetricDataRequest request = new ShowMetricDataRequest();
        request.withPeriod(300);
        getMetricsRequest.setPeriod(request.getPeriod());
        request.withFrom(Long.valueOf(getMetricsRequest.getStartTime()));
        request.withTo(Long.valueOf(getMetricsRequest.getEndTime()));
        return request;
    }

    /**
     * 查询所有云主机参数
     *
     * @param getMetricsRequest
     * @return
     */
    public static ListVirtualMachineRequest getListVmRequest(GetMetricsRequest getMetricsRequest) {
        ListVirtualMachineRequest listVirtualMachineRequest = new ListVirtualMachineRequest();
        listVirtualMachineRequest.setCredential(getMetricsRequest.getCredential());
        listVirtualMachineRequest.setRegionId(getMetricsRequest.getRegionId());
        return listVirtualMachineRequest;
    }

    public static List<NovaAvailabilityZoneDTO> getAvailabilityZone(HuaweiVmCreateRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            return new ArrayList<>();
        }
        List<NovaAvailabilityZoneDTO> result = new ArrayList<>();
        try {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            NovaListAvailabilityZonesRequest getAz = new NovaListAvailabilityZonesRequest();
            NovaListAvailabilityZonesResponse response = client.novaListAvailabilityZones(getAz);
            for (NovaAvailabilityZone novaAvailabilityZone : response.getAvailabilityZoneInfo()) {
                NovaAvailabilityZoneDTO dto = new NovaAvailabilityZoneDTO(novaAvailabilityZone);
                String name = dto.getZoneName();
                int index = StringUtils.lowerCase(name).charAt(name.length() - 1) - 96;
                dto.setDisplayName("可用区" + index);
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static F2CVirtualMachine getSimpleServerByCreateRequest(HuaweiVmCreateRequest request) {
        F2CVirtualMachine virtualMachine = new F2CVirtualMachine();

        int index = request.getIndex();
        virtualMachine
                .setId(request.getId())
                .setName(request.getServerNameInfos().get(index).getName())
                .setIpArray(new ArrayList<>())
                .setInstanceType(request.getInstanceType())
                .setInstanceChargeType(request.getChargeMode());

        return virtualMachine;

    }

    public static Integer getPeriodNumber(String periodNumber) {
        if (Integer.valueOf(periodNumber) >= 12) {
            return Integer.valueOf(periodNumber) / 12;
        }
        return Integer.valueOf(periodNumber);
    }

    public static String getPeriodType(String periodNumber) {
        return Integer.valueOf(periodNumber) < 12 ? "month" : "year";
    }

    public static F2CVirtualMachine createServer(HuaweiVmCreateRequest request) {
        F2CVirtualMachine f2CVirtualMachine = new F2CVirtualMachine();
        try {
            request.setRegion(request.getRegionId());
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            //创建云主机参数
            CreateServersRequest createServersRequest = new CreateServersRequest();
            //参数实体
            CreateServersRequestBody body = new CreateServersRequestBody();

            //计费类类型
            PrePaidServerExtendParam extendparamServer = new PrePaidServerExtendParam();
            extendparamServer.withChargingMode(PrePaidServerExtendParam.ChargingModeEnum.fromValue(request.getBillingMode()))
                    .withRegionID(request.getRegionId());
            if (StringUtils.equalsIgnoreCase(request.getBillingMode(), PrePaidServerExtendParam.ChargingModeEnum.PREPAID.getValue())) {
                extendparamServer.withPeriodType(PrePaidServerExtendParam.PeriodTypeEnum.fromValue(getPeriodType(request.getPeriodNum())));
                extendparamServer.withPeriodNum(getPeriodNumber(request.getPeriodNum()));
                extendparamServer.withIsAutoPay(PrePaidServerExtendParam.IsAutoPayEnum.TRUE);
            }
            //安全组
            List<PrePaidServerSecurityGroup> listServerSecurityGroups = new ArrayList<>();
            request.getSecurityGroups().forEach(v -> listServerSecurityGroups.add(new PrePaidServerSecurityGroup().withId(v)));

            //系统盘
            DiskConfig systemDisk = request.getDisks().get(0);
            PrePaidServerRootVolume rootVolumeServer = new PrePaidServerRootVolume();
            rootVolumeServer.withVolumetype(PrePaidServerRootVolume.VolumetypeEnum.fromValue(systemDisk.getDiskType()))
                    .withSize(systemDisk.getSize())
                    .withHwPassthrough(true);
            //数据盘
            List<PrePaidServerDataVolume> listServerDataVolumes = new ArrayList<>();
            for (int i = 0; i < request.getDisks().size(); i++) {
                if (i == 0) {
                    continue;
                }
                DiskConfig dataDisk = request.getDisks().get(i);
                listServerDataVolumes.add(
                        new PrePaidServerDataVolume()
                                .withVolumetype(PrePaidServerDataVolume.VolumetypeEnum.fromValue(dataDisk.getDiskType()))
                                .withSize(dataDisk.getSize())
                                .withShareable(false)
                                .withMultiattach(false)
                                .withHwPassthrough(true)
                                .withDataImageId("")
                );
            }

            //公网IP
            PrePaidServerPublicip publicipServer = null;
            if (request.isUsePublicIp()) {
                PrePaidServerEipExtendParam extendparamEip = new PrePaidServerEipExtendParam();
                extendparamEip.withChargingMode(PrePaidServerEipExtendParam.ChargingModeEnum.fromValue(Objects.equals(request.getBillingMode(), "1") ? "prePaid" : "postPaid"));
                PrePaidServerEipBandwidth bandwidthEip = new PrePaidServerEipBandwidth();
                boolean isTraffic = StringUtils.equalsIgnoreCase("traffic", request.getChargeMode());
                bandwidthEip.withSize(isTraffic ? request.getTrafficBandwidthSize() : request.getBandwidthSize())
                        //PER,表示独享。WHOLE,表示共享
                        .withSharetype(PrePaidServerEipBandwidth.SharetypeEnum.fromValue("PER"))
                        //traffic表示按流量计费，空或者不传为按带宽计费
                        .withChargemode(StringUtils.equalsIgnoreCase(request.getChargeMode(), "traffic") ? "traffic" : "");

                PrePaidServerEip eipPublicip = new PrePaidServerEip();
                //固定
                eipPublicip.withIptype("5_bgp")
                        .withBandwidth(bandwidthEip)
                        .withExtendparam(extendparamEip);
                publicipServer = new PrePaidServerPublicip();
                publicipServer.withEip(eipPublicip);
                //默认随实例删除
                publicipServer.setDeleteOnTermination(true);
            }

            // TODO 网卡 目前仅支持一个网卡，官方支持最多两个
            List<F2CHuaweiSubnet> networks = listSubnet(request);
            if (CollectionUtils.isEmpty(networks)) {
                throw new RuntimeException("No suitable network found!");
            }
            F2CHuaweiSubnet network = networks.get(0);
            List<PrePaidServerNic> listServerNics = new ArrayList<>();
            listServerNics.add(
                    new PrePaidServerNic()
                            .withSubnetId(network.getUuid())
                            .withIpAddress("")
            );
            PrePaidServer serverbody = new PrePaidServer();
            //获取镜像ID，根据规格、操作系统、操作系统版本
            List<F2CImage> images = listCreateImages(request);
            if (CollectionUtils.isEmpty(images)) {
                throw new RuntimeException("No suitable image found!");
            }
            serverbody.withImageRef(images.get(0).getId())
                    .withFlavorRef(request.getInstanceType())
                    .withName(request.getServerNameInfos().get(request.getIndex()).getName())
                    .withVpcid(network.getVpcId())
                    .withNics(listServerNics)
                    .withCount(1)
                    .withIsAutoRename(false)
                    .withRootVolume(rootVolumeServer)
                    .withDataVolumes(listServerDataVolumes)
                    .withSecurityGroups(listServerSecurityGroups)
                    .withAvailabilityZone(request.getAvailabilityZone())
                    .withExtendparam(extendparamServer)
                    //.withMetadata(listServerMetadata)
                    .withDescription("");
            if (publicipServer != null) {
                serverbody.withPublicip(publicipServer);
            }
            if (StringUtils.equalsIgnoreCase("pwd", request.getLoginMethod())) {
                serverbody.withAdminPass(request.getPwd());
            } else {
                serverbody.withKeyName(request.getKeyPari());
            }
            body.withServer(serverbody);
            createServersRequest.withBody(body);
            CreateServersResponse response = client.createServers(createServersRequest);
            List<Port> ports = listPorts(request.getCredential(), request.getRegionId());
            ServerDetail serverDetail = getJobEntities(client, response.getJobId());
            List<OrderInstanceV2> orderInstanceV2s = listPayPerUseCustomerResources(credential, List.of(serverDetail.getId()));
            f2CVirtualMachine = HuaweiMappingUtil.toF2CVirtualMachine(serverDetail, ports, orderInstanceV2s);
            f2CVirtualMachine.setRegion(request.getRegionId());
            f2CVirtualMachine.setId(request.getId());
            setServerHostName(client, f2CVirtualMachine, request);
            // 获取包年包月机器的到期时间
            if (F2CChargeType.PRE_PAID.equalsIgnoreCase(f2CVirtualMachine.getInstanceChargeType())) {
                appendExpiredTime(credential, serverDetail, f2CVirtualMachine);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Fit2cloudException(5000, "Huawei create vm fail - " + e.getMessage());
        }
        return f2CVirtualMachine;
    }

    private static void setServerHostName(EcsClient client, F2CVirtualMachine f2CVirtualMachine, HuaweiVmCreateRequest createRequest) {
        try {
            // 设置hostname
            UpdateServerRequest request = new UpdateServerRequest();
            request.withServerId(f2CVirtualMachine.getInstanceUUID());
            UpdateServerRequestBody body = new UpdateServerRequestBody();
            UpdateServerOption serverbody = new UpdateServerOption();
            serverbody.withHostname(createRequest.getServerNameInfos().get(createRequest.getIndex()).getHostName());
            body.withServer(serverbody);
            request.withBody(body);
            UpdateServerResponse response = client.updateServer(request);
            if (response.getHttpStatusCode() == 200) {
                if (createRequest.getServerNameInfos().get(createRequest.getIndex()).isAuthReboot()) {
                    // 重启
                    HuaweiInstanceRequest instanceRequest = new HuaweiInstanceRequest();
                    instanceRequest.setCredential(createRequest.getCredential());
                    instanceRequest.setRegionId(createRequest.getRegionId());
                    instanceRequest.setUuid(f2CVirtualMachine.getInstanceUUID());
                    rebootInstance(instanceRequest);
                    f2CVirtualMachine.setHostname(response.getServer().getOsEXTSRVATTRHostname());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}-set hostname fail：{}", f2CVirtualMachine.getName(), e.getMessage());
        }
    }

    /**
     * 获取创建主机镜像
     * 根据规格、操作系统、操作系统版本、状态
     *
     * @param createRequest 请求对象
     * @return 响应对象
     */
    public static List<F2CImage> listCreateImages(HuaweiVmCreateRequest createRequest) {
        ListImageRequest request = new ListImageRequest();
        request.setRegionId(createRequest.getRegionId());
        request.setCredential(createRequest.getCredential());
        request.setFlavorId(createRequest.getInstanceType());
        request.setPlatform(ListImagesRequest.PlatformEnum.valueOf(createRequest.getOs()));
        request.setStatus(ListImagesRequest.StatusEnum.ACTIVE);
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            ImsClient imsClient = credential.getImsClient(request.getRegionId());
            request.setImagetype(ListImagesRequest.ImagetypeEnum.GOLD);
            ListImagesResponse listImagesResponse = imsClient.listImages(request);
            List<ImageInfo> images = listImagesResponse.getImages();
            //根据用户输入的操作系统版本过滤
            return images.stream().filter(v -> filterImageByOsAndOsVersion(v, createRequest)).map(imageInfo -> HuaweiMappingUtil.toF2CImage(imageInfo, request.getRegionId())).filter(Objects::nonNull).toList();
        }
        return new ArrayList<>();
    }

    private static boolean filterImageByOsAndOsVersion(ImageInfo imageInfo, HuaweiVmCreateRequest createRequest) {
        return StringUtils.equalsIgnoreCase(imageInfo.getPlatform().getValue(), createRequest.getOs()) && StringUtils.equalsIgnoreCase(imageInfo.getId(), createRequest.getOsVersion());
    }


    private static ServerDetail getJobEntities(EcsClient client, String jobId) {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(SLEEP_TIME);
                com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse jobResponse = client.showJob(new com.huaweicloud.sdk.ecs.v2.model.ShowJobRequest().withJobId(jobId));
                com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse.StatusEnum status = jobResponse.getStatus();
                if (ShowJobResponse.StatusEnum.SUCCESS.getValue().equals(status.getValue())) {
                    String id = jobResponse.getEntities().getSubJobs().get(0).getEntities().getServerId();
                    ShowServerRequest request = new ShowServerRequest();
                    request.setServerId(id);
                    ShowServerResponse response = client.showServer(request);
                    return response.getServer();
                }
                if (ShowJobResponse.StatusEnum.FAIL.getValue().equalsIgnoreCase(status.getValue())) {
                    String errorMsg = jobResponse.getFailReason();
                    try {
                        errorMsg = jobResponse.getEntities().getSubJobs().get(0).getEntities().getErrorcodeMessage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException(errorMsg);
                }
                if (count >= WAIT_COUNT) {
                    throw new RuntimeException("check create server status timeout！");
                }
                count++;
            } catch (Exception e) {
                throw new RuntimeException("getJobEntities fail jobId - " + jobId + "-" + e.getMessage());
            }
        }
    }


    public static List<InstanceSpecType> getInstanceSpecTypes(HuaweiVmCreateRequest request) {
        List<InstanceSpecType> instanceSpecTypes = new ArrayList<>();
        if (StringUtils.isEmpty(request.getRegionId()) || StringUtils.isEmpty(request.getAvailabilityZone())) {
            return instanceSpecTypes;
        }
        try {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            ListFlavorsResponse response = client.listFlavors(new ListFlavorsRequest()
                    .withAvailabilityZone(request.getAvailabilityZone()));
            for (Flavor flavor : response.getFlavors()) {
                if (flavor.getOsExtraSpecs().getCondOperationAz().contains((request.getAvailabilityZone() + "(normal)"))) {
                    InstanceSpecType instanceSpecType = HuaweiMappingUtil.toInstanceSpecType(flavor);
                    instanceSpecTypes.add(instanceSpecType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instanceSpecTypes;
    }

    public static List<Map<String, String>> getAllDiskTypes(HuaweiVmCreateRequest request) {
        if (StringUtils.isEmpty(request.getRegionId()) && StringUtils.isEmpty(request.getAvailabilityZone())) {
            return new ArrayList<>();
        }
        HuaweiGetDiskTypeRequest getDiskTypeRequest = new HuaweiGetDiskTypeRequest();
        getDiskTypeRequest.setZone(request.getAvailabilityZone());
        getDiskTypeRequest.setCredential(request.getCredential());
        getDiskTypeRequest.setRegion(request.getRegionId());
        getDiskTypeRequest.setLanguage(request.getLanguage());
        return getDiskTypes(getDiskTypeRequest);
    }

    public static List<F2CHuaweiSubnet> listSubnet(HuaweiVmCreateRequest request) {
        List<F2CHuaweiSubnet> result = new ArrayList<>();
        Map<String, F2CHuaweiVpc> vpcMap = listVpc(request).stream().collect(Collectors.toMap(F2CHuaweiVpc::getUuid, v -> v, (k1, k2) -> k1));
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            VpcClient vpcClient = huaweiVmCredential.getVpcClient(request.getRegionId());
            ListSubnetsRequest listSubnetsRequest = new ListSubnetsRequest();
            listSubnetsRequest.setLimit(1000);
            ListSubnetsResponse response = vpcClient.listSubnets(listSubnetsRequest);
            if (CollectionUtils.isNotEmpty(response.getSubnets())) {
                response.getSubnets().stream()
                        .collect(Collectors.toList())
                        .forEach(subnet -> {
                            F2CHuaweiSubnet f2CHuaweiSubnet = HuaweiMappingUtil.toF2CHuaweiSubnet(subnet);
                            if (vpcMap.containsKey(f2CHuaweiSubnet.getVpcId())) {
                                f2CHuaweiSubnet.setVpcName(vpcMap.get(f2CHuaweiSubnet.getVpcId()).getName());
                            }
                            result.add(f2CHuaweiSubnet);
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.equalsIgnoreCase("random", request.getAvailabilityZone())) {
            return result.stream().filter(v -> StringUtils.equalsIgnoreCase(request.getAvailabilityZone(), v.getAvailabilityZone())).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(request.getNetworkId())) {
            return result.stream().filter(v -> StringUtils.equalsIgnoreCase(v.getUuid(), request.getNetworkId())).toList();
        }
        return result;
    }

    public static List<F2CHuaweiVpc> listVpc(HuaweiVmCreateRequest request) {
        List<F2CHuaweiVpc> result = new ArrayList<>();
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            VpcClient vpcClient = huaweiVmCredential.getVpcClient(request.getRegionId());
            ListVpcsRequest listVpcsRequest = new ListVpcsRequest();
            listVpcsRequest.setLimit(1000);
            ListVpcsResponse response = vpcClient.listVpcs(listVpcsRequest);
            if (CollectionUtils.isNotEmpty(response.getVpcs())) {
                response.getVpcs().forEach(vpc -> {
                    result.add(HuaweiMappingUtil.toF2CHuaweiVpc(vpc));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static List<F2CHuaweiSecurityGroups> listSecurityGroups(HuaweiVmCreateRequest request) {
        List<F2CHuaweiSecurityGroups> result = new ArrayList<>();
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            VpcClient vpcClient = huaweiVmCredential.getVpcClient(request.getRegionId());
            ListSecurityGroupsRequest listSecurityGroupsRequest = new ListSecurityGroupsRequest();
            listSecurityGroupsRequest.setLimit(1000);
            ListSecurityGroupsResponse response = vpcClient.listSecurityGroups(listSecurityGroupsRequest);
            if (CollectionUtils.isNotEmpty(response.getSecurityGroups())) {
                response.getSecurityGroups().forEach(securityGroup -> {
                    result.add(HuaweiMappingUtil.toF2CHuaweiSecurityGroups(securityGroup));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<NovaSimpleKeypair> listKeyPairs(HuaweiVmCreateRequest request) {
        List<NovaSimpleKeypair> result = new ArrayList<>();
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = huaweiVmCredential.getEcsClient(request.getRegionId());
            NovaListKeypairsRequest listKeypairsRequest = new NovaListKeypairsRequest();
            listKeypairsRequest.setLimit(1000);
            NovaListKeypairsResponse response = client.novaListKeypairs(listKeypairsRequest);
            if (CollectionUtils.isNotEmpty(response.getKeypairs())) {
                response.getKeypairs().forEach(keypair -> {
                    result.add(keypair.getKeypair());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 询价
     *
     * @param request
     * @return
     */
    public static String calculatedPrice(Boolean trafficPriceOnly, HuaweiVmCreateRequest request) {
        StringBuilder result = new StringBuilder();
        try {
            if (StringUtils.isEmpty(request.getAvailabilityZone())) {
                return result.toString();
            }
            if (request.getCount() == 0) {
                request.setCount(1);
            }
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            //查询项目
            KeystoneListAuthProjectsRequest projectsRequest = new KeystoneListAuthProjectsRequest();
            IamClient client = credential.getIamClient(request.getRegionId());
            KeystoneListAuthProjectsResponse projectsResponse = client.keystoneListAuthProjects(projectsRequest);
            List<AuthProjectResult> projectResults = projectsResponse.getProjects().stream()
                    .filter(v -> StringUtils.equalsIgnoreCase(v.getName(), request.getRegionId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(projectResults)) {
                String projectId = projectResults.get(0).getId();
                Double vmAmount = 0D;
                Double diskAmount = 0D;
                Double bandwidthAmount = 0D;
                //开启公网IP
                boolean isUsePublicIp = request.isUsePublicIp();
                //公网IP计费价格
                if (isUsePublicIp) {
                    bandwidthAmount = bandwidthInquiryPriceForHour(request, credential, projectId);
                }
                if (trafficPriceOnly) {
                    result.append(bandwidthAmount).append("元/GB");
                    return result.toString();
                }
                boolean isTraffic = StringUtils.equalsIgnoreCase("traffic", request.getChargeMode());
                //按量计费
                if (StringUtils.equalsIgnoreCase(request.getBillingMode(), "postPaid")) {
                    vmAmount = vmInquiryPriceForHour(request, credential, projectId);
                    diskAmount = diskInquiryPriceForHour(request, credential, projectId);
                    BigDecimal amountBig = new BigDecimal(vmAmount + diskAmount + (isTraffic ? 0 : bandwidthAmount));
                    result.append(amountBig.setScale(4, RoundingMode.HALF_UP));
                    result.append("元/小时");
                }
                //包年包月
                if (StringUtils.equalsIgnoreCase(request.getBillingMode(), "prePaid")) {
                    vmAmount = vmInquiryPriceForMonth(request, credential, projectId);
                    diskAmount = diskInquiryPriceForMonth(request, credential, projectId);
                    if (isUsePublicIp) {
                        bandwidthAmount = bandwidthInquiryPriceForMonth(request, credential, projectId);
                    }
                    BigDecimal amountBig = new BigDecimal(vmAmount + diskAmount + (isTraffic ? 0 : bandwidthAmount));
                    result.append(amountBig.setScale(4, RoundingMode.HALF_UP));
                    result.append("元");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 云主机包年包月询价
     *
     * @param createRequest
     * @param credential
     * @param projectId
     * @return
     */
    private static Double vmInquiryPriceForMonth(HuaweiVmCreateRequest createRequest, HuaweiVmCredential credential, String projectId) {
        ListRateOnPeriodDetailRequest request = new ListRateOnPeriodDetailRequest();
        RateOnPeriodReq body = new RateOnPeriodReq();
        List<PeriodProductInfo> listPeriodProductInfo = new ArrayList<>();
        listPeriodProductInfo.add(new PeriodProductInfo()
                //唯一标识
                .withId(createRequest.getInstanceType())
                //云主机询价固定
                .withCloudServiceType("hws.service.type.ec2")
                .withResourceType("hws.resource.type.vm")
                //区分linux\win，目前查询结果价格一致，官网这个价格，不根据操作系统的不同而改变价格，所以这里不做区分
                .withResourceSpec(createRequest.getInstanceType() + ".linux")
                .withRegion(createRequest.getRegionId())
                //周期类型0:天2:月3:年4:小时
                .withPeriodType(StringUtils.equalsIgnoreCase(getPeriodType(createRequest.getPeriodNum()), "month") ? 2 : 3)
                //周期数 1个月
                .withPeriodNum(getPeriodNumber(createRequest.getPeriodNum()))
                //数量
                .withSubscriptionNum(createRequest.getCount()));
        body.withProductInfos(listPeriodProductInfo);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            if (CollectionUtils.isNotEmpty(body.getProductInfos())) {
                ListRateOnPeriodDetailResponse response = credential.getBssClient().listRateOnPeriodDetail(request);
                return response.getOfficialWebsiteRatingResult().getOfficialWebsiteAmount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }

    /**
     * 云主机按需询价
     *
     * @param createRequest
     * @param credential
     * @param projectId
     * @return
     */
    private static Double vmInquiryPriceForHour(HuaweiVmCreateRequest createRequest, HuaweiVmCredential credential, String projectId) {
        ListOnDemandResourceRatingsRequest request = new ListOnDemandResourceRatingsRequest();
        RateOnDemandReq body = new RateOnDemandReq();
        List<DemandProductInfo> listBodyProductInfos = new ArrayList<>();
        if (Objects.isNull(createRequest.getInstanceType())) {
            return 0D;
        }
        listBodyProductInfos.add(new DemandProductInfo()
                //唯一标识
                .withId(createRequest.getInstanceType())
                //云主机询价固定
                .withCloudServiceType("hws.service.type.ec2")
                .withResourceType("hws.resource.type.vm")
                //区分linux\win，目前查询结果价格一致，官网这个价格，不根据操作系统的不同而改变价格，所以这里不做区分
                .withResourceSpec(createRequest.getInstanceType() + ".linux")
                .withRegion(createRequest.getRegionId())
                //云服务器：Duration
                //云硬盘：Duration
                //弹性IP：Duration
                .withUsageFactor("Duration")
                //按小时询价，使用量值为1，使用量单位为小时。
                .withUsageValue((double) 1)
                //调度单位小时为4
                .withUsageMeasureId(4)
                .withSubscriptionNum(createRequest.getCount()));
        body.withProductInfos(listBodyProductInfos);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            if (CollectionUtils.isNotEmpty(body.getProductInfos())) {
                ListOnDemandResourceRatingsResponse response = credential.getBssClient().listOnDemandResourceRatings(request);
                return response.getOfficialWebsiteAmount().doubleValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }


    /**
     * 磁盘包年包月询价
     *
     * @param createRequest
     * @param credential
     * @param projectId
     * @return
     */
    private static Double diskInquiryPriceForMonth(HuaweiVmCreateRequest createRequest, HuaweiVmCredential credential, String projectId) {
        ListRateOnPeriodDetailRequest request = new ListRateOnPeriodDetailRequest();
        RateOnPeriodReq body = new RateOnPeriodReq();
        List<PeriodProductInfo> listbodyProductInfos = new ArrayList<>();
        for (int i = 0; i < createRequest.getDisks().size(); i++) {
            DiskConfig diskConfig = createRequest.getDisks().get(i);
            listbodyProductInfos.add(new PeriodProductInfo()
                    .withId(String.valueOf(i))
                    .withCloudServiceType("hws.service.type.ebs")
                    .withResourceType("hws.resource.type.volume")
                    .withResourceSpec(diskConfig.getDiskType())
                    .withRegion(createRequest.getRegionId())
                    .withResourceSize(diskConfig.getSize())
                    //资源容量度量标识云盘GB17、15Mbps
                    .withSizeMeasureId(17)
                    //周期类型0:天2:月3:年4:小时
                    .withPeriodType(StringUtils.equalsIgnoreCase(getPeriodType(createRequest.getPeriodNum()), "month") ? 2 : 3)
                    //周期数 1个月
                    .withPeriodNum(getPeriodNumber(createRequest.getPeriodNum()))
                    .withSubscriptionNum(1));
        }
        body.withProductInfos(listbodyProductInfos);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            if (CollectionUtils.isNotEmpty(body.getProductInfos())) {
                ListRateOnPeriodDetailResponse response = credential.getBssClient().listRateOnPeriodDetail(request);
                return response.getOfficialWebsiteRatingResult().getOfficialWebsiteAmount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }

    /**
     * 磁盘按需询价
     *
     * @param createRequest
     * @param credential
     * @param projectId
     * @return
     */
    private static Double diskInquiryPriceForHour(HuaweiVmCreateRequest createRequest, HuaweiVmCredential credential, String projectId) {
        ListOnDemandResourceRatingsRequest request = new ListOnDemandResourceRatingsRequest();
        RateOnDemandReq body = new RateOnDemandReq();
        List<DemandProductInfo> listbodyProductInfos = new ArrayList<>();
        for (int i = 0; i < createRequest.getDisks().size(); i++) {
            DiskConfig diskConfig = createRequest.getDisks().get(i);
            listbodyProductInfos.add(new DemandProductInfo()
                    .withId(String.valueOf(i))
                    .withCloudServiceType("hws.service.type.ebs")
                    .withResourceType("hws.resource.type.volume")
                    .withResourceSpec(diskConfig.getDiskType())
                    .withRegion(createRequest.getRegionId())
                    //大小
                    .withResourceSize(diskConfig.getSize())
                    //资源容量度量标识云盘GB17、15Mbps
                    .withSizeMeasureId(17)
                    .withUsageFactor("Duration")
                    //按小时询价，使用量值为1，使用量单位为小时。
                    .withUsageValue((double) 1)
                    //调度单位小时为4
                    .withUsageMeasureId(4)
                    .withSubscriptionNum(createRequest.getCount()));
        }
        body.withProductInfos(listbodyProductInfos);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            if (CollectionUtils.isNotEmpty(body.getProductInfos())) {
                ListOnDemandResourceRatingsResponse response = credential.getBssClient().listOnDemandResourceRatings(request);
                return response.getOfficialWebsiteAmount().doubleValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }


    /**
     * 公网带宽包年包月询价
     *
     * @param createRequest
     * @param credential
     * @param projectId
     * @return
     */
    private static Double bandwidthInquiryPriceForMonth(HuaweiVmCreateRequest createRequest, HuaweiVmCredential credential, String projectId) {
        boolean isTraffic = StringUtils.equalsIgnoreCase("traffic", createRequest.getChargeMode());
        //按流量与周期无关
        if (isTraffic) {
            return bandwidthInquiryPriceForHour(createRequest, credential, projectId);
        }
        ListRateOnPeriodDetailRequest request = new ListRateOnPeriodDetailRequest();
        RateOnPeriodReq body = new RateOnPeriodReq();
        List<PeriodProductInfo> listbodyProductInfos = new ArrayList<>();
        listbodyProductInfos.add(
                new PeriodProductInfo()
                        .withId("1")
                        .withCloudServiceType("hws.service.type.vpc")
                        .withResourceType("hws.resource.type.bandwidth")
                        .withResourceSpec("19_bgp")
                        .withRegion(createRequest.getRegionId())
                        .withResourceSize(isTraffic ? createRequest.getTrafficBandwidthSize() : createRequest.getBandwidthSize())
                        .withSizeMeasureId(15)
                        //周期类型0:天2:月3:年4:小时
                        .withPeriodType(StringUtils.equalsIgnoreCase(getPeriodType(createRequest.getPeriodNum()), "month") ? 2 : 3)
                        //周期数 1个月
                        .withPeriodNum(getPeriodNumber(createRequest.getPeriodNum()))
                        .withSubscriptionNum(createRequest.getCount())
        );
        body.withProductInfos(listbodyProductInfos);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            if (CollectionUtils.isNotEmpty(body.getProductInfos())) {
                ListRateOnPeriodDetailResponse response = credential.getBssClient().listRateOnPeriodDetail(request);
                return response.getOfficialWebsiteRatingResult().getOfficialWebsiteAmount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }

    /**
     * 带宽按需询价
     * 12_bgp:动态BGP按流量计费带宽
     * 12_sbgp:静态BGP按流量计费带宽
     * 19_bgp:动态BGP按带宽计费带宽
     * 19_sbgp:静态BGP按带宽计费带宽
     * 19_share:按带宽计费共享带宽
     * IP:5_bgp:动态BGP公网
     * IP5_sbgp:静态BGP公网IP
     */
    private static Double bandwidthInquiryPriceForHour(HuaweiVmCreateRequest createRequest, HuaweiVmCredential credential, String projectId) {
        if (StringUtils.isEmpty(createRequest.getChargeMode())) {
            return 0D;
        }
        boolean isTraffic = StringUtils.equalsIgnoreCase("traffic", createRequest.getChargeMode());
        ListOnDemandResourceRatingsRequest request = new ListOnDemandResourceRatingsRequest();
        RateOnDemandReq body = new RateOnDemandReq();
        List<DemandProductInfo> listbodyProductInfos = new ArrayList<>();
        DemandProductInfo demandProductInfo = new DemandProductInfo();
        demandProductInfo.withId("1")
                .withCloudServiceType("hws.service.type.vpc")
                .withResourceType("hws.resource.type.bandwidth")
                .withUsageValue((double) 1)
                .withSizeMeasureId(15)
                .withSubscriptionNum(createRequest.getCount())
                .withRegion(createRequest.getRegionId())
                .withResourceSize(isTraffic ? createRequest.getTrafficBandwidthSize() : createRequest.getBandwidthSize());
        // 按流量
        if (isTraffic) {
            demandProductInfo.withUsageFactor("upflow").withResourceSpec("12_bgp").withUsageMeasureId(10);
        } else {
            // 按带宽
            demandProductInfo.withUsageFactor("Duration").withResourceSpec("19_bgp").withUsageMeasureId(4);
        }
        listbodyProductInfos.add(demandProductInfo);
        body.withProductInfos(listbodyProductInfos);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            if (CollectionUtils.isNotEmpty(body.getProductInfos())) {
                ListOnDemandResourceRatingsResponse response = credential.getBssClient().listOnDemandResourceRatings(request);
                return response.getAmount().doubleValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }

    /**
     * 返回操作系统版本
     *
     * @param createRequest
     * @return
     */
    public static List<OsConfig> listOsVersion(HuaweiVmCreateRequest createRequest) {
        List<OsConfig> result = new ArrayList<>();
        if (StringUtils.isEmpty(createRequest.getOs())
                || (Objects.isNull(createRequest.getInstanceType()))) {
            return result;
        }
        try {
            ListImageRequest request = new ListImageRequest();
            request.setRegionId(createRequest.getRegionId());
            request.setCredential(createRequest.getCredential());
            request.setFlavorId(createRequest.getInstanceType());
            request.setPlatform(ListImagesRequest.PlatformEnum.valueOf(createRequest.getOs()));
            request.setStatus(ListImagesRequest.StatusEnum.ACTIVE);
            List<ImageInfo> osImages = new ArrayList<>();
            if (StringUtils.isNotEmpty(request.getCredential())) {
                HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
                ImsClient imsClient = credential.getImsClient(request.getRegionId());
                request.setImagetype(ListImagesRequest.ImagetypeEnum.GOLD);
                ListImagesResponse listImagesResponse = imsClient.listImages(request);
                List<ImageInfo> imagesAll = listImagesResponse.getImages();
                osImages = imagesAll.stream().filter(v -> StringUtils.equalsIgnoreCase(v.getPlatform().getValue(), createRequest.getOs())).collect(Collectors.toList());
            }
            osImages.forEach(v -> {
                OsConfig osConfig = new OsConfig();
                osConfig.setOs(v.getPlatform().getValue());
                osConfig.setOsVersion(v.getOsVersion());
                osConfig.setImageName(v.getName());
                osConfig.setImageId(v.getId());
                osConfig.setImageDiskMinSize(Long.valueOf(String.valueOf(v.getMinDisk())));
                result.add(osConfig);
            });
            return result.stream().sorted(Comparator.comparing(OsConfig::getOsVersion)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map<String, String>> listOs(String request) {
        List<Map<String, String>> result = new ArrayList<>(14);
        List<String> osList = Arrays.asList("Windows", "RedHat", "CentOS", "SUSE", "Debian", "OpenSUSE", "Oracle Linux", "Fedora", "Ubuntu", "EulerOS", "CoreOS", "ESXi", "Other", "openEuler");
        osList.stream().sorted().forEach(v -> {
            Map<String, String> m = new HashMap<>();
            m.put("id", v);
            m.put("name", v);
            result.add(m);
        });
        return result;
    }

    /**
     * 云主机配置变更
     *
     * @param request
     * @return
     */
    public static F2CVirtualMachine changeVmConfig(HuaweiUpdateConfigRequest request) {
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EcsClient ecsClient = huaweiVmCredential.getEcsClient(request.getRegionId());

        String instanceId = request.getInstanceUuid();
        String instanceType = request.getNewInstanceType();
        ServerDetail server = getInstanceById(instanceId, ecsClient);
        Optional.ofNullable(server).orElseThrow(() -> new RuntimeException("Can not find the server!"));

        ResizeServerRequest resizeServerRequest = new ResizeServerRequest();
        resizeServerRequest.withServerId(instanceId);
        ResizeServerRequestBody body = new ResizeServerRequestBody();

        ResizeServerExtendParam extendParamResize = new ResizeServerExtendParam();
        extendParamResize.withIsAutoPay("true");
        ResizePrePaidServerOption resizeBody = new ResizePrePaidServerOption();
        resizeBody.withFlavorRef(instanceType)
                .withMode("withStopServer")
                .withExtendparam(extendParamResize);

        body.withResize(resizeBody);
        resizeServerRequest.withBody(body);

        ResizeServerResponse resizeResponse = ecsClient.resizeServer(resizeServerRequest);
        if (null == resizeResponse || StringUtils.isEmpty(resizeResponse.getJobId())) {
            throw new RuntimeException("Failed to change instance config.");
        }
        try {
            checkEcsJobStatus(ecsClient, resizeResponse.getJobId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to check ecs job status." + e.getMessage(), e);
        }

        return HuaweiMappingUtil.toF2CVirtualMachine(getInstanceById(instanceId, ecsClient));
    }

    public static List<InstanceSpecType> getInstanceTypesForConfigUpdate(HuaweiUpdateConfigRequest request) {
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EcsClient ecsClient = huaweiVmCredential.getEcsClient(request.getRegionId());

        ListResizeFlavorsRequest listResizeFlavorsRequest = new ListResizeFlavorsRequest()
                .withInstanceUuid(request.getInstanceUuid())
                .withSourceFlavorId(request.getCurrentInstanceType());

        ListResizeFlavorsResponse response = ecsClient.listResizeFlavors(listResizeFlavorsRequest);
        List<InstanceSpecType> result = response.getFlavors().stream()
                .filter(listResizeFlavorsResult -> !listResizeFlavorsResult.getName().equalsIgnoreCase(request.getCurrentInstanceType()))
                .map(flavor -> {
                    InstanceSpecType instanceSpecType = new InstanceSpecType();
                    instanceSpecType.setSpecName(flavor.getName());
                    instanceSpecType.setInstanceSpec(HuaweiMappingUtil.transInstanceSpecTypeDescription(flavor));
                    instanceSpecType.setInstanceTypeDesc(instanceSpecType.getSpecName() + "（" + instanceSpecType.getInstanceSpec() + "）");
                    return instanceSpecType;
                }).collect(Collectors.toList());
        return result;
    }

    /**
     * 云主机配置变更询价
     *
     * @param request
     * @return
     */
    public static String calculateConfigUpdatePrice(HuaweiUpdateConfigRequest request) {
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EcsClient ecsClient = huaweiVmCredential.getEcsClient(request.getRegionId());

        ServerDetail server = getInstanceById(request.getInstanceUuid(), ecsClient);
        Optional.ofNullable(server).orElseThrow(() -> new RuntimeException("Can not find the server!"));

        String instanceChargeType;
        if (StringUtils.equalsIgnoreCase(server.getMetadata().get("charging_mode"), "2")) {
            instanceChargeType = F2CChargeType.SPOT_PAID;
        } else {
            instanceChargeType = StringUtils.equalsIgnoreCase(server.getMetadata().get("charging_mode"), "0") ? F2CChargeType.POST_PAID : F2CChargeType.PRE_PAID;
        }

        HuaweiVmCreateRequest createRequest = new HuaweiVmCreateRequest();
        BeanUtils.copyProperties(request, createRequest);
        createRequest.setInstanceType(request.getNewInstanceType());
        createRequest.setCount(1);
        String projectId = server.getTenantId();

        Double price;
        if (F2CChargeType.PRE_PAID.equalsIgnoreCase(instanceChargeType)) {
            BssClient bssClient = huaweiVmCredential.getBssClient();
            ShowCustomerOrderDetailsResponse response = getOrderDetailsById(server.getMetadata().get("metering.order_id"), bssClient);
            response.getOrderLineItems().stream().forEach((item) -> {
                if ("hws.service.type.ec2".equalsIgnoreCase(item.getServiceTypeCode())) {
                    if (item.getPeriodType() == 2) {
                        createRequest.setPeriodNum(String.valueOf(item.getPeriodNum() == null ? 1 : item.getPeriodNum()));
                    } else {
                        createRequest.setPeriodNum(String.valueOf(item.getPeriodNum() * 12));
                    }
                }
            });
            price = vmInquiryPriceForMonth(createRequest, huaweiVmCredential, projectId);
        } else {
            price = vmInquiryPriceForHour(createRequest, huaweiVmCredential, projectId);
        }
        return String.format("%.2f", price) + PriceUnit.YUAN;
    }

    /**
     * 根据实例 ID 获取实例
     *
     * @param instanceId
     * @param ecsClient
     * @return
     */
    private static ServerDetail getInstanceById(String instanceId, EcsClient ecsClient) {
        ShowServerResponse showServerResponse = ecsClient.showServer(new ShowServerRequest().withServerId(instanceId));
        ServerDetail server = showServerResponse.getServer();
        return server;
    }

    /**
     * 查询订单详情
     *
     * @param orderId
     * @param bssClient
     * @return
     */
    private static ShowCustomerOrderDetailsResponse getOrderDetailsById(String orderId, BssClient bssClient) {
        ShowCustomerOrderDetailsRequest request = new ShowCustomerOrderDetailsRequest();
        request.setOrderId(orderId);
        bssClient.showCustomerOrderDetails(request);
        return bssClient.showCustomerOrderDetails(request);
    }

    /**
     * 获取云主机关联的磁盘
     *
     * @param request
     * @return
     */
    public static List<F2CDisk> getVmF2CDisks(BaseDiskRequest request) {
        HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EcsClient ecsClient = credential.getEcsClient(request.getRegionId());
        EvsClient evsClient = credential.getEvsClient(request.getRegionId());
        try {
            ListServerBlockDevicesResponse listServerBlockDevicesResponse = ecsClient.listServerBlockDevices(new ListServerBlockDevicesRequest().withServerId(request.getInstanceUuid()));
            List<ServerBlockDevice> volumeAttachments = listServerBlockDevicesResponse.getVolumeAttachments();
            return volumeAttachments.stream().map(attachment -> {
                ShowVolumeResponse showVolumeResponse = evsClient.showVolume(new ShowVolumeRequest().withVolumeId(attachment.getVolumeId()));
                VolumeDetail volume = showVolumeResponse.getVolume();
                F2CDisk f2CDisk = HuaweiMappingUtil.toF2CDisk(volume);
                return f2CDisk;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("GetVmF2CDisks Error!" + e.getMessage(), e);
        }
    }

    public static F2CVirtualMachine renewInstance(RenewInstanceRequest request) {
        HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        BssClient bssClient = credential.getBssClient();
        if (StringUtils.isNotEmpty(request.getPeriodNum())) {

            RenewalResourcesRequest renewalResourcesRequest = new RenewalResourcesRequest();
            RenewalResourcesReq renewalResourcesReq = new RenewalResourcesReq();
            int period = Integer.parseInt(request.getPeriodNum());
            Integer periodType = period >= 12 && period % 12 == 0 ? 3 : 2;
            renewalResourcesReq.setResourceIds(List.of(request.getInstanceUuid()));
            renewalResourcesReq.setPeriodNum(period >= 12 ? period / 12 : period);
            renewalResourcesReq.setPeriodType(periodType);
            renewalResourcesReq.setIsAutoPay(1);
            /**
             * 到期策略（字段已废弃，请勿使用该字段。此字段必填，需携带，但携带的枚举实际并不生效）：
             * 0：进入宽限期/保留期
             * 1：转按需
             * 2：自动退订
             * 3：自动续订
             */
            renewalResourcesReq.setExpirePolicy(3);
            renewalResourcesRequest.setBody(renewalResourcesReq);
            bssClient.renewalResources(renewalResourcesRequest);
        }
        if (Objects.nonNull(request.getExpirePolicy())) {
            List<OrderInstanceV2> orderInstanceV2s = listPayPerUseCustomerResources(credential, List.of(request.getInstanceUuid()));
            boolean autoRenew = HuaweiMappingUtil.getAutoRenew(request.getInstanceUuid(), orderInstanceV2s);
            if (Objects.equals(request.getExpirePolicy(), ExpirePolicyConstants.YES) != autoRenew) {
                if (Objects.equals(request.getExpirePolicy(), ExpirePolicyConstants.YES)) {
                    AutoRenewalResourcesRequest autoRenewalResourcesRequest = new AutoRenewalResourcesRequest();
                    autoRenewalResourcesRequest.setResourceId(request.getInstanceUuid());
                    bssClient.autoRenewalResources(autoRenewalResourcesRequest);
                } else {
                    CancelAutoRenewalResourcesRequest cancelAutoRenewalResourcesRequest = new CancelAutoRenewalResourcesRequest();
                    cancelAutoRenewalResourcesRequest.setResourceId(request.getInstanceUuid());
                    bssClient.cancelAutoRenewalResources(cancelAutoRenewalResourcesRequest);
                }

            }
        }
        return getF2CVirtualMachine(credential, request.getRegionId(), request.getInstanceUuid());
    }


    public static F2CVirtualMachine getF2CVirtualMachine(HuaweiVmCredential credential, String regionId, String serverId) {
        EcsClient ecsClient = credential.getEcsClient(regionId);
        ServerDetail instance = getInstanceById(serverId, ecsClient);
        List<Port> ports = listPorts(credential, regionId);
        List<OrderInstanceV2> orderInstanceV2s = listPayPerUseCustomerResources(credential, List.of(instance.getId()));
        return HuaweiMappingUtil.toF2CVirtualMachine(instance, ports, orderInstanceV2s);
    }

    public static String renewInstanceExpiresTime(HuaweiRenewInstanceExpiresTimeRequest request) {
        LocalDateTime expiredTime = request.getExpiredTime();
        if (StringUtils.isEmpty(request.getPeriodNum())) {
            return expiredTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        String periodNum = request.getPeriodNum();
        return expiredTime.plusMonths(Long.parseLong(periodNum)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static BigDecimal renewInstancePrice(HuaweiRenewInstanceRequest request) {
        if (StringUtils.isEmpty(request.getPeriodNum())) {
            return BigDecimal.ZERO;
        }
        HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        BssClient bssClient = credential.getBssClient();
        ListRenewRateOnPeriodRequest req = new ListRenewRateOnPeriodRequest();
        ListRenewRateOnPeriodReq body = new ListRenewRateOnPeriodReq();
        int period = Integer.parseInt(request.getPeriodNum());
        Integer periodType = period >= 12 && period % 12 == 0 ? 3 : 2;
        body.setPeriodNum(period >= 12 ? period / 12 : period);
        body.setPeriodType(periodType);
        body.setResourceIds(List.of(request.getInstanceUuid()));
        req.withBody(body);

        ListRenewRateOnPeriodResponse listRenewRateOnPeriodResponse = bssClient.listRenewRateOnPeriod(req);
        return listRenewRateOnPeriodResponse.getRenewInquiryResults().stream().map(RenewInquiryResultInfo::getAmount).map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
