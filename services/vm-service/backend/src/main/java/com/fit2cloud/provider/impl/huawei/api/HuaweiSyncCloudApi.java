package com.fit2cloud.provider.impl.huawei.api;

import com.aliyun.tea.TeaException;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.entity.F2CEntityType;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CChargeType;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.PriceUnit;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.huawei.constants.HuaweiDiskType;
import com.fit2cloud.provider.impl.huawei.constants.HuaweiPerfMetricConstants;
import com.fit2cloud.provider.impl.huawei.entity.*;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiVmCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.*;
import com.fit2cloud.provider.impl.huawei.util.HuaweiMappingUtil;
import com.google.gson.Gson;
import com.huaweicloud.sdk.bss.v2.BssClient;
import com.huaweicloud.sdk.bss.v2.model.*;
import com.huaweicloud.sdk.ces.v1.CesClient;
import com.huaweicloud.sdk.ces.v1.model.Datapoint;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataRequest;
import com.huaweicloud.sdk.ces.v1.model.ShowMetricDataResponse;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author:张少虎
 * @Date: 2022/9/22  2:44 PM
 * @Version 1.0
 * @注释:
 */
public class HuaweiSyncCloudApi {
    private static Logger logger = LoggerFactory.getLogger(HuaweiSyncCloudApi.class);
    private static final int WAIT_COUNT = 50;

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
                return instances.stream().map(server -> HuaweiMappingUtil.toF2CVirtualMachine(server, ports)).map(f2CVirtualMachine -> {
                            f2CVirtualMachine.setRegion(listVirtualMachineRequest.getRegionId());
                            return f2CVirtualMachine;
                        }).map(f2CVirtualMachine -> appendDisk(listVirtualMachineRequest.getCredential(), listVirtualMachineRequest.getRegionId(), f2CVirtualMachine))
                        .toList();
            }
        }
        return new ArrayList<>();
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
            ListVolumesResponse listVolumesResponse = evsClient.listVolumes(request);
            List<VolumeDetail> volumes = listVolumesResponse.getVolumes();
            return volumes.stream().map(HuaweiMappingUtil::toF2CDisk).toList();
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
            // 只查询公共镜像gold
            request.setImagetype(ListImagesRequest.ImagetypeEnum.GOLD);
            ListImagesResponse listImagesResponse = imsClient.listImages(request);
            List<ImageInfo> images = listImagesResponse.getImages();
            return images.stream().map(imageInfo -> HuaweiMappingUtil.toF2CImage(imageInfo, request.getRegionId())).filter(Objects::nonNull).toList();
        }
        return new ArrayList<>();
    }

    /**
     * 给云主机添加磁盘数据
     *
     * @param credential        认证信息
     * @param regionId          区域信息
     * @param f2CVirtualMachine 云主机对象
     * @return 云主机对象
     */
    private static F2CVirtualMachine appendDisk(String credential, String regionId, F2CVirtualMachine f2CVirtualMachine) {
        ListDisksRequest listDisksRequest = new ListDisksRequest();
        listDisksRequest.setCredential(credential);
        listDisksRequest.setRegionId(regionId);
        List<F2CDisk> disks = listDisk(listDisksRequest);
        long sum = disks.stream().mapToLong(F2CDisk::getSize).sum();
        f2CVirtualMachine.setDisk((int) sum);
        return f2CVirtualMachine;
    }


    /**
     * 获取port列表
     *
     * @param credential 认证信息
     * @param regionId   区域id
     * @return port对象
     */
    private static List<Port> listPorts(String credential, String regionId) {
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(credential, HuaweiVmCredential.class);
            VpcClient vpcClient = huaweiVmCredential.getVpcClient(regionId);
            ListPortsRequest listPortsRequest = new ListPortsRequest();
            ListPortsResponse listPortsResponse = vpcClient.listPorts(listPortsRequest);
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
                BatchStopServersResponse batchStopServersResponse = client.batchStopServers(new BatchStopServersRequest()
                        .withBody(new BatchStopServersRequestBody()
                                .withOsStop(
                                        new BatchStopServersOption()
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuid())))
                                                .withType(request.getForce() ? BatchStopServersOption.TypeEnum.HARD : BatchStopServersOption.TypeEnum.SOFT))));

                checkEcsJobStatus(client, batchStopServersResponse.getJobId());
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

    public static boolean powerOn(HuaweiInstanceRequest request) {
        if (StringUtils.isEmpty(request.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            try {
                BatchStartServersResponse batchStartServersResponse = client.batchStartServers(new BatchStartServersRequest()
                        .withBody(new BatchStartServersRequestBody()
                                .withOsStart(
                                        new BatchStartServersOption()
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuid()))))));
                checkEcsJobStatus(client, batchStartServersResponse.getJobId());
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
            } catch (TeaException error) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_REBOOT_FAIL.getCode(), error.getMessage());
            } catch (Exception _error) {
                TeaException error = new TeaException(_error.getMessage(), _error);
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
                DeleteServersResponse batchStartServersResponse = client.deleteServers(new DeleteServersRequest()
                        .withBody(new DeleteServersRequestBody()
                                .withServers(Arrays.asList(new ServerId().withId(request.getUuid())))));
                checkEcsJobStatus(client, batchStartServersResponse.getJobId());
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

    private static void checkEcsJobStatus(EcsClient client, String jobId) throws Exception {
        int count = 0;
        while (true) {
            com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse jobResponse = client.showJob(new com.huaweicloud.sdk.ecs.v2.model.ShowJobRequest().withJobId(jobId));
            com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse.StatusEnum status = jobResponse.getStatus();
            if (ShowJobResponse.StatusEnum.SUCCESS.getValue().equals(status.getValue())) {
                break;
            }
            if (ShowJobResponse.StatusEnum.FAIL.getValue().equals(status.getValue())) {
                throw new RuntimeException(jobResponse.getFailReason());
            }
            if (count < 40) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                break;
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
            throw new RuntimeException(e.getMessage(), e);
        }
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
     *
     * @param request
     * @return
     */
    public static F2CDisk createDisk(HuaweiCreateDiskRequest request) {
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
        try {
            CreateVolumeResponse response = evsClient.createVolume(request.toCreateVolumeRequest());
            String volumeId;
            String status = request.getInstanceUuid() == null ? F2CDiskStatus.AVAILABLE : "in-use"; //华为云的 in-use 是中划线😭
            if (StringUtils.isNotEmpty(response.getOrderId())) {
                volumeId = checkOrderResourceId(response.getOrderId(), huaweiVmCredential.getBssClient());
            } else {
                ShowJobResponse showJobResponse = getJob(response.getJobId(), evsClient);
                volumeId = showJobResponse.getEntities().getVolumeId();
            }
            F2CDisk createdDisk = HuaweiMappingUtil.toF2CDisk(checkVolumeStatus(volumeId, evsClient, status));
            createdDisk.setDeleteWithInstance(request.getDeleteWithInstance());
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
            if (status.equalsIgnoreCase("in-use") || status.equalsIgnoreCase(F2CDiskStatus.AVAILABLE)) {
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
    public static boolean attachDisk(HuaweiAttachDiskRequest request) {
        try {
            HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
            EcsClient ecsClient = huaweiVmCredential.getEcsClient(request.getRegionId());
            ecsClient.attachServerVolume(request.toAttachServerVolumeRequest());
            checkVolumeStatus(request.getDiskId(), evsClient, "in-use");
            return true;
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
    public static void updateServerBlockDevice(EcsClient client, String instanceId, String deviceId, String deleteWithInstance) {
        if (StringUtils.isNotEmpty(instanceId)) {
            UpdateServerBlockDeviceRequest blockDeviceRequest =
                    new UpdateServerBlockDeviceRequest()
                            .withServerId(instanceId)
                            .withVolumeId(deviceId)
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
            ShowJobResponse showJobResponse = null;
            while (true) {
                Thread.sleep(2000);
                count++;
                showJobResponse = evsClient.showJob(showJobRequest);
                if (showJobResponse.getStatus().getValue().equalsIgnoreCase("FAIL")) {
                    throw new RuntimeException(new Gson().toJson(showJobResponse));
                }
                if (showJobResponse != null && showJobResponse.getStatus().getValue().equalsIgnoreCase("SUCCESS")) {
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
        if (StringUtils.isEmpty(getMetricsRequest.getRegionId())) {
            throw new Fit2cloudException(10002, "区域为必填参数");
        }
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        //设置时间，根据interval,默认一个小时
        getMetricsRequest.setStartTime(String.valueOf(DateUtil.getBeforeHourTime(getMetricsRequest.getInterval())));
        getMetricsRequest.setEndTime(String.valueOf(System.currentTimeMillis()));
        System.out.println("开始时间：" + getMetricsRequest.getStartTime());
        System.out.println("结束时间：" + getMetricsRequest.getEndTime());
        System.out.println("区域：" + getMetricsRequest.getRegionId());
        try {
            getMetricsRequest.setRegionId(getMetricsRequest.getRegionId());
            result.addAll(getVmPerfMetric(getMetricsRequest));
        } catch (Exception e) {
            //throw new Fit2cloudException(100021, "获取监控数据失败-" + getMetricsRequest.getRegionId() + "-" + e.getMessage());
            e.printStackTrace();
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
        HuaweiVmCredential credential = JsonUtil.parseObject(getMetricsRequest.getCredential(), HuaweiVmCredential.class);
        List<F2CPerfMetricMonitorData> result = new ArrayList<>();
        List<F2CVirtualMachine> vms = listVirtualMachine(getListVmRequest(getMetricsRequest));
        if (vms.size() == 0) {
            return result;
        }
        //查询监控指标数据参数
        ///TODO 由于我们只查询一个小时内的数据，时间间隔是5m,所以查询每台机器的监控数据的时候最多不过12条数据，所以不需要分页查询
        ShowMetricDataRequest request = getShowMetricDataRequest(getMetricsRequest);
        CesClient cesClient = credential.getCesClient(getMetricsRequest.getRegionId());
        vms.forEach(vm -> {
            request.setDim0("instance_id," + vm.getInstanceUUID());
            //监控指标
            Arrays.stream(HuaweiPerfMetricConstants.CloudServerPerfMetricEnum.values()).sorted().collect(Collectors.toList()).forEach(perfMetric -> {
                request.setMetricName(perfMetric.getMetricName());
                try {
                    //查询监控指标数据
                    ShowMetricDataResponse response = cesClient.showMetricData(request);
                    if (response.getHttpStatusCode() == 200 && CollectionUtils.isNotEmpty(response.getDatapoints())) {
                        List<Datapoint> list = response.getDatapoints();
                        list.forEach(v -> {
                            F2CPerfMetricMonitorData f2CEntityPerfMetric = HuaweiMappingUtil.toF2CPerfMetricMonitorData(v);
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
     * 查询云主机监控数据参数
     *
     * @param getMetricsRequest
     * @return
     */
    @NotNull
    private static ShowMetricDataRequest getShowMetricDataRequest(GetMetricsRequest getMetricsRequest) {
        ShowMetricDataRequest request = new ShowMetricDataRequest();
        request.setNamespace("SYS.ECS");
        request.withFilter(ShowMetricDataRequest.FilterEnum.fromValue("average"));
        request.withPeriod(300);
        request.withFrom(Long.valueOf(getMetricsRequest.getStartTime()));
        request.withTo(Long.valueOf(getMetricsRequest.getEndTime()));
        return request;
    }

    /**
     * 查询所有虚拟机参数
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
        InstanceSpecType instanceType = request.getInstanceSpecConfig();

        virtualMachine
                .setId(request.getId())
                .setName(request.getServerNameInfos().get(index).getName())
                .setIpArray(new ArrayList<>())
                .setInstanceType(instanceType.getSpecName())
                .setCpu(Integer.valueOf(instanceType.getVcpus()))
                .setMemory(instanceType.getRam() / 1024)
                .setInstanceTypeDescription(instanceType.getInstanceSpec());

        return virtualMachine;

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
                extendparamServer.withPeriodType(PrePaidServerExtendParam.PeriodTypeEnum.fromValue(request.getPeriodType()));
                extendparamServer.withPeriodNum(request.getPeriodNum());
            }
            //安全组
            List<PrePaidServerSecurityGroup> listServerSecurityGroups = new ArrayList<>();
            request.getSecurityGroups().forEach(v -> {
                listServerSecurityGroups.add(
                        new PrePaidServerSecurityGroup()
                                .withId(v)
                );
            });

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
                extendparamEip.withChargingMode(PrePaidServerEipExtendParam.ChargingModeEnum.fromValue(request.getBillingMode() == "1" ? "prePaid" : "postPaid"));
                PrePaidServerEipBandwidth bandwidthEip = new PrePaidServerEipBandwidth();
                bandwidthEip.withSize(request.getBandwidthSize())
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
            List<PrePaidServerNic> listServerNics = new ArrayList<>();
            listServerNics.add(
                    new PrePaidServerNic()
                            .withSubnetId(request.getNetworkConfigs().getUuid())
                            .withIpAddress("")
            );
            PrePaidServer serverbody = new PrePaidServer();
            //获取镜像ID，根据规格、操作系统、操作系统版本
            List<F2CImage> images = listCreateImages(request);
            if (CollectionUtils.isEmpty(images)) {
                throw new RuntimeException("No suitable image found!");
            }
            serverbody.withImageRef(images.get(0).getId())
                    .withFlavorRef(request.getInstanceSpecConfig().getSpecName())
                    .withName(request.getServerNameInfos().get(request.getIndex()).getName())
                    .withVpcid(request.getNetworkConfigs().getVpcId())
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
            f2CVirtualMachine = HuaweiMappingUtil.toF2CVirtualMachine(getJobEntities(client, response.getJobId()), ports);
            f2CVirtualMachine.setRegion(request.getRegionId());
            f2CVirtualMachine.setId(request.getId());
            setServerHostName(client, f2CVirtualMachine, request);
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
        request.setFlavorId(createRequest.getInstanceSpecConfig().getSpecName());
        request.setPlatform(ListImagesRequest.PlatformEnum.valueOf(createRequest.getOs()));
        request.setStatus(ListImagesRequest.StatusEnum.ACTIVE);
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            ImsClient imsClient = credential.getImsClient(request.getRegionId());
            request.setImagetype(ListImagesRequest.ImagetypeEnum.GOLD);
            ListImagesResponse listImagesResponse = imsClient.listImages(request);
            List<ImageInfo> images = listImagesResponse.getImages();
            //根据用户输入的操作系统版本过滤
            return images.stream().filter(v -> StringUtils.equalsIgnoreCase(v.getOsVersion(), createRequest.getOsVersion().getOsVersion())).map(imageInfo -> HuaweiMappingUtil.toF2CImage(imageInfo, request.getRegionId())).filter(Objects::nonNull).toList();
        }
        return new ArrayList<>();
    }


    private static ServerDetail getJobEntities(EcsClient client, String jobId) {
        int count = 0;
        ServerDetail result = null;
        while (true) {
            com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse jobResponse = client.showJob(new com.huaweicloud.sdk.ecs.v2.model.ShowJobRequest().withJobId(jobId));
            com.huaweicloud.sdk.ecs.v2.model.ShowJobResponse.StatusEnum status = jobResponse.getStatus();
            if (ShowJobResponse.StatusEnum.SUCCESS.getValue().equals(status.getValue())) {
                String id = jobResponse.getEntities().getSubJobs().get(0).getEntities().getServerId();
                ShowServerRequest request = new ShowServerRequest();
                request.setServerId(id);
                ShowServerResponse response = client.showServer(request);
                result = response.getServer();
                break;
            }
            if (ShowJobResponse.StatusEnum.FAIL.equals(status)) {
                throw new RuntimeException(jobResponse.getFailReason());
            }
            if (count < 40) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                break;
            }
        }
        if (result == null) {
            throw new RuntimeException("getJobEntities fail jobId - " + jobId);
        }
        return result;
    }


    public static InstanceSpecConfig getInstanceSpecTypes(HuaweiVmCreateRequest request) {
        InstanceSpecConfig instanceSpecConfig = new InstanceSpecConfig();
        if (StringUtils.isEmpty(request.getRegionId()) || StringUtils.isEmpty(request.getAvailabilityZone())) {
            return instanceSpecConfig;
        }
        try {
            List<InstanceSpecType> instanceSpecTypes = new ArrayList<>();
            HuaweiVmCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
            EcsClient client = credential.getEcsClient(request.getRegionId());
            ListFlavorsResponse response = client.listFlavors(new ListFlavorsRequest()
                    .withAvailabilityZone(request.getAvailabilityZone()));
            for (Flavor flavor : response.getFlavors()) {
                if (flavor.getOsExtraSpecs().getCondOperationAz().indexOf(request.getAvailabilityZone() + "(normal)") > 0) {
                    InstanceSpecType instanceSpecType = HuaweiMappingUtil.toInstanceSpecType(flavor);
                    instanceSpecTypes.add(instanceSpecType);
                }
            }
            instanceSpecConfig.setTableData(instanceSpecTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instanceSpecConfig;
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
    public static String calculatedPrice(HuaweiVmCreateRequest request) {
        StringBuffer result = new StringBuffer();
        try {
            if (StringUtils.isEmpty(request.getAvailabilityZone())) {
                return result.toString();
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
                //带宽计费方式
                boolean bandwidthTraffic = false;
                if (request.isUsePublicIp() && StringUtils.equalsIgnoreCase(request.getChargeMode(), "traffic")) {
                    bandwidthTraffic = true;
                }
                //按量计费
                if (StringUtils.equalsIgnoreCase(request.getBillingMode(), "postPaid")) {
                    vmAmount = vmInquiryPriceForHour(request, credential, projectId);
                    diskAmount = diskInquiryPriceForHour(request, credential, projectId);
                    if (request.isUsePublicIp()) {
                        bandwidthAmount = bandwidthInquiryPriceForHour(request, credential, projectId);
                    }
                    BigDecimal amountBig = new BigDecimal(vmAmount + diskAmount + (!bandwidthTraffic ? bandwidthAmount : 0));
                    result.append(amountBig.setScale(4, RoundingMode.HALF_UP));
                    result.append("元/小时");
                }
                //包年包月
                if (StringUtils.equalsIgnoreCase(request.getBillingMode(), "prePaid")) {
                    vmAmount = vmInquiryPriceForMonth(request, credential, projectId);
                    diskAmount = diskInquiryPriceForMonth(request, credential, projectId);
                    if (request.isUsePublicIp()) {
                        bandwidthAmount = bandwidthInquiryPriceForMonth(request, credential, projectId);
                    }
                    BigDecimal amountBig = new BigDecimal(vmAmount + diskAmount + (!bandwidthTraffic ? bandwidthAmount : 0));
                    result.append(amountBig.setScale(4, RoundingMode.HALF_UP));
                    //包年包月不显示单位
                    result.append("元");
//                    result.append(StringUtils.equalsIgnoreCase("month",request.getPeriodType())?"月":"年");
                }
                if (bandwidthTraffic) {
                    result.append(" + ");
                    result.append("弹性公网IP流量费用" + bandwidthAmount + "元/GB");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 虚拟机包年包月询价
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
                .withId(createRequest.getInstanceSpecConfig().getSpecName())
                //云主机询价固定
                .withCloudServiceType("hws.service.type.ec2")
                .withResourceType("hws.resource.type.vm")
                //区分linux\win，目前查询结果价格一致，官网这个价格，不根据操作系统的不同而改变价格，所以这里不做区分
                .withResourceSpec(createRequest.getInstanceSpecConfig().getSpecName() + ".linux")
                .withRegion(createRequest.getRegionId())
                //周期类型0:天2:月3:年4:小时
                .withPeriodType(StringUtils.equalsIgnoreCase(createRequest.getPeriodType(), "month") ? 2 : 3)
                //周期数 1个月
                .withPeriodNum(createRequest.getPeriodNum())
                //数量
                .withSubscriptionNum(createRequest.getCount()));
        body.withProductInfos(listPeriodProductInfo);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            ListRateOnPeriodDetailResponse response = credential.getBssClient().listRateOnPeriodDetail(request);
            return response.getOfficialWebsiteRatingResult().getOfficialWebsiteAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }

    /**
     * 虚拟机按需询价
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
        listBodyProductInfos.add(new DemandProductInfo()
                //唯一标识
                .withId(createRequest.getInstanceSpecConfig().getSpecName())
                //云主机询价固定
                .withCloudServiceType("hws.service.type.ec2")
                .withResourceType("hws.resource.type.vm")
                //区分linux\win，目前查询结果价格一致，官网这个价格，不根据操作系统的不同而改变价格，所以这里不做区分
                .withResourceSpec(createRequest.getInstanceSpecConfig().getSpecName() + ".linux")
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
            ListOnDemandResourceRatingsResponse response = credential.getBssClient().listOnDemandResourceRatings(request);
            return response.getOfficialWebsiteAmount();
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
                    .withPeriodType(StringUtils.equalsIgnoreCase(createRequest.getPeriodType(), "month") ? 2 : 3)
                    .withPeriodNum(createRequest.getPeriodNum())
                    .withSubscriptionNum(1));
        }
        body.withProductInfos(listbodyProductInfos);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            ListRateOnPeriodDetailResponse response = credential.getBssClient().listRateOnPeriodDetail(request);
            return response.getOfficialWebsiteRatingResult().getOfficialWebsiteAmount();
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
            ListOnDemandResourceRatingsResponse response = credential.getBssClient().listOnDemandResourceRatings(request);
            return response.getOfficialWebsiteAmount();
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
        //按流量与周期无关
        if (StringUtils.equalsIgnoreCase(createRequest.getChargeMode(), "traffic")) {
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
                        .withResourceSize(createRequest.getBandwidthSize())
                        .withSizeMeasureId(15)
                        .withPeriodType(StringUtils.equalsIgnoreCase(createRequest.getPeriodType(), "month") ? 2 : 3)
                        .withPeriodNum(createRequest.getPeriodNum())
                        .withSubscriptionNum(createRequest.getCount())
        );
        body.withProductInfos(listbodyProductInfos);
        body.withProjectId(projectId);
        request.withBody(body);
        try {
            ListRateOnPeriodDetailResponse response = credential.getBssClient().listRateOnPeriodDetail(request);
            return response.getOfficialWebsiteRatingResult().getOfficialWebsiteAmount();
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
     *
     * @param createRequest
     * @param credential
     * @param projectId
     * @return
     */
    private static Double bandwidthInquiryPriceForHour(HuaweiVmCreateRequest createRequest, HuaweiVmCredential credential, String projectId) {
        if (StringUtils.isEmpty(createRequest.getChargeMode()) && createRequest.getBandwidthSize() == 0) {
            return 0D;
        }
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
                .withResourceSize(createRequest.getBandwidthSize());
        // 按流量
        if (StringUtils.equalsIgnoreCase(createRequest.getChargeMode(), "traffic")) {
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
            ListOnDemandResourceRatingsResponse response = credential.getBssClient().listOnDemandResourceRatings(request);
            return response.getAmount();
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
                && StringUtils.isEmpty(createRequest.getInstanceSpecConfig().getSpecName())) {
            return result;
        }
        ListImageRequest request = new ListImageRequest();
        request.setRegionId(createRequest.getRegionId());
        request.setCredential(createRequest.getCredential());
        request.setFlavorId(createRequest.getInstanceSpecConfig().getSpecName());
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
            osConfig.setImageMinDiskSize(Long.valueOf(String.valueOf(v.getMinDisk())));
            result.add(osConfig);
        });
        return result.stream().sorted(Comparator.comparing(OsConfig::getOsVersion)).collect(Collectors.toList());
    }

    public static List<Map<String, String>> listOs(String request) {
        List<Map<String, String>> result = new ArrayList<>();
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
        InstanceSpecType instanceSpecType = new InstanceSpecType();
        instanceSpecType.setSpecName(request.getNewInstanceType());
        createRequest.setInstanceSpecConfig(instanceSpecType);
        createRequest.setCount(1);
        String projectId = server.getTenantId();

        Double price;
        if (F2CChargeType.PRE_PAID.equalsIgnoreCase(instanceChargeType)) {
            BssClient bssClient = huaweiVmCredential.getBssClient();
            ShowCustomerOrderDetailsResponse response = getOrderDetailsById(server.getMetadata().get("metering.order_id"), bssClient);
            response.getOrderLineItems().stream().forEach((item) -> {
                if ("hws.service.type.ec2".equalsIgnoreCase(item.getServiceTypeCode())) {
                    createRequest.setPeriodType(item.getPeriodType() == 2 ? "month" : "year");
                    createRequest.setPeriodNum(item.getPeriodNum() == null ? 1 : item.getPeriodNum());
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
}
