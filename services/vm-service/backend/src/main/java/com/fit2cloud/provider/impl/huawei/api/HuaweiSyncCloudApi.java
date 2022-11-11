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
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.huawei.constants.HuaweiPerfMetricConstants;
import com.fit2cloud.provider.impl.huawei.constants.HuaweiDiskType;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiVmCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.*;
import com.fit2cloud.provider.impl.huawei.util.HuaweiMappingUtil;
import com.google.gson.Gson;
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
import com.huaweicloud.sdk.ims.v2.ImsClient;
import com.huaweicloud.sdk.ims.v2.model.ImageInfo;
import com.huaweicloud.sdk.ims.v2.model.ListImagesResponse;
import com.huaweicloud.sdk.vpc.v2.VpcClient;
import com.huaweicloud.sdk.vpc.v2.model.ListPortsRequest;
import com.huaweicloud.sdk.vpc.v2.model.ListPortsResponse;
import com.huaweicloud.sdk.vpc.v2.model.Port;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.*;


/**
 * @Author:张少虎
 * @Date: 2022/9/22  2:44 PM
 * @Version 1.0
 * @注释:
 */
public class HuaweiSyncCloudApi {
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
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuId())))
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
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuId()))))));
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
                                                .withServers(Arrays.asList(new ServerId().withId(request.getUuId())))
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
                                .withServers(Arrays.asList(new ServerId().withId(request.getUuId())))));
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
            if (ShowJobResponse.StatusEnum.SUCCESS.equals(status)) {
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
        F2CDisk f2CDisk = new F2CDisk();
        HuaweiVmCredential huaweiVmCredential = JsonUtil.parseObject(request.getCredential(), HuaweiVmCredential.class);
        EvsClient evsClient = huaweiVmCredential.getEvsClient(request.getRegionId());
        try {
            CreateVolumeResponse response = evsClient.createVolume(request.toCreateVolumeRequest());
            ShowJobResponse showJobResponse = getJob(response.getJobId(), evsClient);
            String status = request.getInstanceUuid() == null ? F2CDiskStatus.AVAILABLE : "in-use"; //华为云的 in-use 是中划线😭
            F2CDisk createdDisk = HuaweiMappingUtil.toF2CDisk(checkVolumeStatus(showJobResponse.getEntities().getVolumeId(), evsClient, status));
            createdDisk.setDeleteWithInstance(request.getDeleteWithInstance());
            return f2CDisk;
        } catch (Exception e) {
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
            Optional.ofNullable(volume).orElseThrow(() -> new RuntimeException("Disk can not find!"));

            if (Boolean.valueOf(volume.getBootable())) {
                // 判断实例是否是关机状态
                ShowServerResponse showServerResponse = ecsClient.showServer(new ShowServerRequest().withServerId(request.getInstanceUuid()));
                ServerDetail server = showServerResponse.getServer();
                Optional.ofNullable(server).orElseThrow(() -> new RuntimeException("Can not find server!"));

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
     * @param getMetricsRequest
     * @return
     */
    public static ListVirtualMachineRequest getListVmRequest(GetMetricsRequest getMetricsRequest){
        ListVirtualMachineRequest listVirtualMachineRequest = new ListVirtualMachineRequest();
        listVirtualMachineRequest.setCredential(getMetricsRequest.getCredential());
        listVirtualMachineRequest.setRegionId(getMetricsRequest.getRegionId());
        return listVirtualMachineRequest;
    }

}
