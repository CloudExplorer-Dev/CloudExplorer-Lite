package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fit2cloud.base.entity.*;
import com.fit2cloud.base.service.IBaseJobRecordResourceMappingService;
import com.fit2cloud.base.service.IBaseVmCloudDatastoreService;
import com.fit2cloud.base.service.IBaseVmCloudHostService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.DateUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.es.repository.PerfMetricMonitorDataRepository;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.F2CImageStatus;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.service.*;
import com.google.common.base.Joiner;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author:张少虎
 * @Date: 2022/9/21  11:37 AM
 * @Version 1.0
 * @注释:
 */
@Service
public class SyncProviderServiceImpl extends BaseSyncService implements ISyncProviderService {
    @Resource
    private IVmCloudServerService vmCloudServerService;
    @Resource
    private IVmCloudImageService vmCloudImageService;
    @Resource
    private IVmCloudDiskService vmCloudDiskService;
    @Resource
    private IBaseVmCloudHostService vmCloudHostService;
    @Resource
    private IBaseVmCloudDatastoreService vmCloudDatastoreService;
    @Resource
    private IBaseJobRecordResourceMappingService jobRecordResourceMappingService;
    @Resource
    private PerfMetricMonitorDataRepository perfMetricMonitorDataRepository;
    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void syncCloudServer(String cloudAccountId) {
        List<Credential.Region> regions = getRegions(cloudAccountId);
        syncCloudServer(cloudAccountId, regions);
    }

    @Override
    public void syncCloudServer(String cloudAccountId, List<Credential.Region> regions) {
        proxy(cloudAccountId, regions, com.fit2cloud.constants.JobConstants.JobSyncResourceType.VIRTUAL_MACHINE, ICloudProvider::listVirtualMachine, this::cloudServerSaveOrUpdate, this::writeJobRecord, () -> vmCloudServerService.remove(new LambdaUpdateWrapper<VmCloudServer>().eq(VmCloudServer::getAccountId, cloudAccountId)));
    }

    @Override
    public void syncCloudImage(String cloudAccountId, List<Credential.Region> regions) {
        proxy(cloudAccountId, regions, com.fit2cloud.constants.JobConstants.JobSyncResourceType.IMAGE, ICloudProvider::listImage, this::imageSaveOrUpdate, this::writeJobRecord, () -> vmCloudImageService.remove(new LambdaUpdateWrapper<VmCloudImage>().eq(VmCloudImage::getAccountId, cloudAccountId)));
    }

    @Override
    public void syncCloudDisk(String cloudAccountId, List<Credential.Region> regions) {
        proxy(cloudAccountId, regions, com.fit2cloud.constants.JobConstants.JobSyncResourceType.DISK, ICloudProvider::listDisk, this::diskSaveOrUpdate, this::writeJobRecord, () -> vmCloudDiskService.remove(new LambdaUpdateWrapper<VmCloudDisk>().eq(VmCloudDisk::getAccountId, cloudAccountId)));
    }

    @Override
    public void syncCloudHost(String cloudAccountId, List<Credential.Region> regions) {
        proxy(cloudAccountId, regions, com.fit2cloud.constants.JobConstants.JobSyncResourceType.HOST, ICloudProvider::listHost, this::hostSaveOrUpdate, this::writeJobRecord, () -> vmCloudHostService.remove(new LambdaUpdateWrapper<VmCloudHost>().eq(VmCloudHost::getAccountId, cloudAccountId)));
    }

    @Override
    public void syncCloudDatastore(String cloudAccountId, List<Credential.Region> regions) {
        proxy(cloudAccountId, regions, com.fit2cloud.constants.JobConstants.JobSyncResourceType.DATASTORE, ICloudProvider::listDataStore, this::dataStoreSaveOrUpdate, this::writeJobRecord, () -> vmCloudDatastoreService.remove(new LambdaUpdateWrapper<VmCloudDatastore>().eq(VmCloudDatastore::getAccountId, cloudAccountId)));
    }

    /**
     * 云主机插入并且更新数据
     *
     * @param saveBatchOrUpdateParams 插入更新数据所需要的参数
     */
    private void cloudServerSaveOrUpdate(SaveBatchOrUpdateParams<F2CVirtualMachine> saveBatchOrUpdateParams) {
        List<VmCloudServer> vmCloudServers = saveBatchOrUpdateParams.getSyncRecord().stream().map(f2CVirtualMachine -> toVmCloudServer(f2CVirtualMachine, saveBatchOrUpdateParams.getCloudAccountId(), saveBatchOrUpdateParams.getSyncTime())).toList();
        LambdaUpdateWrapper<VmCloudServer> updateWrapper = new LambdaUpdateWrapper<VmCloudServer>()
                .eq(VmCloudServer::getAccountId, saveBatchOrUpdateParams.getCloudAccountId())
                .eq(VmCloudServer::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())
                .lt(VmCloudServer::getUpdateTime, saveBatchOrUpdateParams.getSyncTime())
                .notIn(VmCloudServer::getInstanceStatus, Arrays.asList(F2CInstanceStatus.Creating.name(), F2CInstanceStatus.Failed.name(), F2CInstanceStatus.WaitCreating.name()))
                .set(VmCloudServer::getInstanceStatus, F2CInstanceStatus.Deleted.name());
        //todo 处理创建中的同步？
        saveBatchOrUpdate(vmCloudServerService, vmCloudServers, vmCloudServer -> new LambdaQueryWrapper<VmCloudServer>()
                        .eq(VmCloudServer::getAccountId, vmCloudServer.getAccountId())
                        .eq(VmCloudServer::getInstanceUuid, vmCloudServer.getInstanceUuid())
                /*.eq(VmCloudServer::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())*/, updateWrapper);
    }

    /**
     * 镜像插入并且更新数据
     *
     * @param saveBatchOrUpdateParams 插入更新数据所需要的参数
     */
    private void imageSaveOrUpdate(SaveBatchOrUpdateParams<F2CImage> saveBatchOrUpdateParams) {
        List<VmCloudImage> vmCloudImages = saveBatchOrUpdateParams.getSyncRecord().stream().map(img -> toVmImage(img, saveBatchOrUpdateParams.getRegion(), saveBatchOrUpdateParams.getCloudAccountId(), saveBatchOrUpdateParams.getSyncTime())).toList();
        LambdaUpdateWrapper<VmCloudImage> updateWrapper = new LambdaUpdateWrapper<VmCloudImage>()
                .eq(VmCloudImage::getAccountId, saveBatchOrUpdateParams.getCloudAccountId())
                .eq(VmCloudImage::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())
                .lt(VmCloudImage::getUpdateTime, saveBatchOrUpdateParams.getSyncTime())
                .set(VmCloudImage::getStatus, F2CImageStatus.deleted);
        saveBatchOrUpdate(vmCloudImageService, vmCloudImages, vmCloudImage -> new LambdaQueryWrapper<VmCloudImage>()
                        .eq(VmCloudImage::getAccountId, vmCloudImage.getAccountId())
                        .eq(VmCloudImage::getImageId, vmCloudImage.getImageId())
                        .eq(VmCloudImage::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId()),
                updateWrapper);
    }

    /**
     * 磁盘插入并且更新数
     *
     * @param saveBatchOrUpdateParams 插入更新数据所需要的参数
     */
    private void diskSaveOrUpdate(SaveBatchOrUpdateParams<F2CDisk> saveBatchOrUpdateParams) {
        List<VmCloudDisk> vmCloudDisks = saveBatchOrUpdateParams.getSyncRecord().stream().map(img -> toVmDisk(img, saveBatchOrUpdateParams.getRegion(), saveBatchOrUpdateParams.getCloudAccountId(), saveBatchOrUpdateParams.getSyncTime())).toList();
        LambdaUpdateWrapper<VmCloudDisk> updateWrapper = new LambdaUpdateWrapper<VmCloudDisk>()
                .eq(VmCloudDisk::getAccountId, saveBatchOrUpdateParams.getCloudAccountId())
                .eq(VmCloudDisk::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())
                .lt(VmCloudDisk::getUpdateTime, saveBatchOrUpdateParams.getSyncTime())
                .set(VmCloudDisk::getStatus, F2CDiskStatus.DELETED);
        saveBatchOrUpdate(vmCloudDiskService, vmCloudDisks, vmCloudDisk -> new LambdaQueryWrapper<VmCloudDisk>()
                        .eq(VmCloudDisk::getAccountId, vmCloudDisk.getAccountId())
                        .eq(VmCloudDisk::getDiskId, vmCloudDisk.getDiskId())
                /*.eq(VmCloudDisk::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())*/,
                updateWrapper);
    }

    /**
     * 宿主机插入并且更新数据
     *
     * @param saveBatchOrUpdateParams 插入更新数据所需要的参数
     */
    private void hostSaveOrUpdate(SaveBatchOrUpdateParams<F2CHost> saveBatchOrUpdateParams) {
        List<VmCloudHost> vmCloudHosts = saveBatchOrUpdateParams.getSyncRecord().stream().map(host -> toVmHost(host, saveBatchOrUpdateParams.getCloudAccountId(), saveBatchOrUpdateParams.getSyncTime())).toList();
        LambdaUpdateWrapper<VmCloudHost> updateWrapper = new LambdaUpdateWrapper<VmCloudHost>()
                .eq(VmCloudHost::getAccountId, saveBatchOrUpdateParams.getCloudAccountId())
                .eq(VmCloudHost::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())
                .lt(VmCloudHost::getUpdateTime, saveBatchOrUpdateParams.getSyncTime())
                .set(VmCloudHost::getStatus, "Deleted");
        saveBatchOrUpdate(vmCloudHostService, vmCloudHosts, vmCloudHost -> new LambdaQueryWrapper<VmCloudHost>()
                        .eq(VmCloudHost::getAccountId, vmCloudHost.getAccountId())
                        .eq(VmCloudHost::getHostId, vmCloudHost.getHostId())
                /*.eq(VmCloudHost::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())*/,
                updateWrapper);
    }

    /**
     * 存储器插入并且更新数据
     *
     * @param saveBatchOrUpdateParams 插入更新数据所需要的参数
     */
    private void dataStoreSaveOrUpdate(SaveBatchOrUpdateParams<F2CDatastore> saveBatchOrUpdateParams) {
        List<VmCloudDatastore> vmCloudDatastores = saveBatchOrUpdateParams.getSyncRecord().stream().map(datastore -> toVmDatastore(datastore, saveBatchOrUpdateParams.getCloudAccountId(), saveBatchOrUpdateParams.getSyncTime())).toList();
        LambdaUpdateWrapper<VmCloudDatastore> updateWrapper = new LambdaUpdateWrapper<VmCloudDatastore>()
                .eq(VmCloudDatastore::getAccountId, saveBatchOrUpdateParams.getCloudAccountId())
                .eq(VmCloudDatastore::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())
                .lt(VmCloudDatastore::getUpdateTime, saveBatchOrUpdateParams.getSyncTime())
                .set(VmCloudDatastore::getStatus, "Deleted");
        saveBatchOrUpdate(vmCloudDatastoreService, vmCloudDatastores, vmCloudDatastore -> new LambdaQueryWrapper<VmCloudDatastore>()
                        .eq(VmCloudDatastore::getAccountId, vmCloudDatastore.getAccountId())
                        .eq(VmCloudDatastore::getDatastoreId, vmCloudDatastore.getDatastoreId())
                /*.eq(VmCloudDatastore::getRegion, saveBatchOrUpdateParams.getRegion().getRegionId())*/,
                updateWrapper);
    }

    /**
     * 写入任务记录
     *
     * @param saveBatchOrUpdateParams 写入任务记录所需要的参数
     */
    private void writeJobRecord(SaveBatchOrUpdateParams<?> saveBatchOrUpdateParams) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("region", saveBatchOrUpdateParams.getRegion());
        params.put("size", saveBatchOrUpdateParams.getSyncRecord().size());
        Map<String, Object> jobParams = saveBatchOrUpdateParams.getJobRecord().getParams();
        if (Objects.isNull(jobParams.get(JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB.name()))) {
            List<HashMap<String, Object>> resourceJobParams = new ArrayList<>();
            resourceJobParams.add(params);
            jobParams.put(JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB.name(), resourceJobParams);
        } else {
            List<HashMap<String, Object>> resourceJobParams = (List<HashMap<String, Object>>) jobParams.get(JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB.name());
            resourceJobParams.add(params);
        }
        baseJobRecordService.updateById(saveBatchOrUpdateParams.getJobRecord());
    }


    @Override
    public void syncCloudServer(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudServer(cloudAccountId, regions);
        }
    }

    @Override
    public void syncCloudImage(String cloudAccountId) {
        List<Credential.Region> regions = getRegions(cloudAccountId);
        syncCloudImage(cloudAccountId, regions);
    }


    @Override
    public void syncCloudImage(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudImage(cloudAccountId, regions);
        }
    }

    @Override
    public void syncCloudDisk(String cloudAccountId) {
        List<Credential.Region> regions = getRegions(cloudAccountId);
        syncCloudDisk(cloudAccountId, regions);
    }


    @Override
    public void syncCloudDisk(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudDisk(cloudAccountId, regions);
        }
    }

    @Override
    public void syncCloudHost(String cloudAccountId) {
        List<Credential.Region> regions = getRegions(cloudAccountId);
        syncCloudHost(cloudAccountId, regions);
    }


    @Override
    public void syncCloudHost(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudHost(cloudAccountId, regions);
        }
    }

    @Override
    public void syncCloudDatastore(String cloudAccountId) {
        List<Credential.Region> regions = getRegions(cloudAccountId);
        syncCloudDatastore(cloudAccountId, regions);
    }


    @Override
    public void syncCloudDatastore(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudDatastore(cloudAccountId, regions);
        }
    }

    /**
     * 同步云主机监控数据
     *
     * @param params 同步云主机所需要的参数
     */
    @Override
    public void syncCloudServerPerfMetricMonitor(Map<String, Object> params) {
        try {
            String cloudAccountId = getCloudAccountId(params);
            List<Credential.Region> regions = getRegions(cloudAccountId);
            if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && CollectionUtils.isNotEmpty(regions)) {
                proxy(cloudAccountId, regions, com.fit2cloud.constants.JobConstants.JobSyncResourceType.VIRTUAL_MACHINE_PERF_METRIC_MONITOR, ICloudProvider::getF2CPerfMetricMonitorData, this::perfMetricMonitorSaveOrUpdate, this::writeJobRecord, () -> {
                });
            }
        } catch (Exception e) {
            LogUtil.error("同步云主机监控失败:" + e.getMessage());
        }
    }

    @Override
    public void syncCloudHostPerfMetricMonitor(Map<String, Object> params) {
        try {
            String cloudAccountId = getCloudAccountId(params);
            List<Credential.Region> regions = getRegions(cloudAccountId);
            if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && CollectionUtils.isNotEmpty(regions)) {
                proxy(
                        cloudAccountId,
                        regions,
                        com.fit2cloud.constants.JobConstants.JobSyncResourceType.HOST_PERF_METRIC_MONITOR,
                        ICloudProvider::getF2CHostPerfMetricMonitorData,
                        this::perfMetricMonitorSaveOrUpdate,
                        this::writeJobRecord,
                        () -> {
                        });
            }
        } catch (Exception e) {
            LogUtil.error("同步宿主机监控失败:" + e.getMessage());
        }
    }

    @Override
    public void syncCloudDiskPerfMetricMonitor(Map<String, Object> params) {
        try {
            String cloudAccountId = getCloudAccountId(params);
            List<Credential.Region> regions = getRegions(cloudAccountId);
            if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && CollectionUtils.isNotEmpty(regions)) {
                proxy(
                        cloudAccountId,
                        regions,
                        com.fit2cloud.constants.JobConstants.JobSyncResourceType.DISK_PERF_METRIC_MONITOR,
                        ICloudProvider::getF2CDiskPerfMetricMonitorData,
                        this::perfMetricMonitorSaveOrUpdate,
                        this::writeJobRecord,
                        () -> {
                        });
            }
        } catch (Exception e) {
            LogUtil.error("同步云磁盘监控失败:" + e.getMessage());
        }
    }

    @Override
    public void syncCloudDatastorePerfMetricMonitor(Map<String, Object> params) {
        try {
            String cloudAccountId = getCloudAccountId(params);
            List<Credential.Region> regions = getRegions(cloudAccountId);
            if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && CollectionUtils.isNotEmpty(regions)) {
                proxy(
                        cloudAccountId,
                        regions,
                        com.fit2cloud.constants.JobConstants.JobSyncResourceType.DATASTORE_PERF_METRIC_MONITOR,
                        ICloudProvider::getF2CDatastorePerfMetricMonitorData,
                        this::perfMetricMonitorSaveOrUpdate,
                        this::writeJobRecord,
                        () -> {
                        });
            }
        } catch (Exception e) {
            LogUtil.error("同步存储器监控失败:" + e.getMessage());
        }
    }

    @Override
    public void syncCloudDatastorePerfMetricMonitor(String cloudAccountId) {
        syncCloudDatastorePerfMetricMonitor(Map.of(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccountId));
    }

    @Override
    public void syncCloudDiskPerfMetricMonitor(String cloudAccountId) {
        syncCloudDiskPerfMetricMonitor(Map.of(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccountId));
    }

    @Override
    public void syncCloudHostPerfMetricMonitor(String cloudAccountId) {
        syncCloudHostPerfMetricMonitor(Map.of(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccountId));
    }

    @Override
    public void syncCloudServerPerfMetricMonitor(String cloudAccountId) {
        syncCloudServerPerfMetricMonitor(Map.of(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccountId));
    }

    @Override
    public void deleteDataSource(List<String> cloudAccountIds) {
        // 删除虚拟机数据
        AsyncJob.run(() -> vmCloudServerService.remove(new LambdaUpdateWrapper<VmCloudServer>().in(VmCloudServer::getAccountId, cloudAccountIds)));
        // 删除磁盘
        AsyncJob.run(() -> vmCloudDiskService.remove(new LambdaUpdateWrapper<VmCloudDisk>().in(VmCloudDisk::getAccountId, cloudAccountIds)));
        // 删除镜像
        AsyncJob.run(() -> vmCloudImageService.remove(new LambdaUpdateWrapper<VmCloudImage>().in(VmCloudImage::getAccountId, cloudAccountIds)));
        // 删除监控数据
        AsyncJob.run(() -> {
            DeleteByQueryRequest queryRequest = new DeleteByQueryRequest.Builder().index(PerfMetricMonitorData.class.getAnnotation(Document.class).indexName())
                    .query(new Query.Builder()
                            .terms(new TermsQuery.Builder().field("cloudAccountId.keyword")
                                    .terms(new TermsQueryField.Builder()
                                            .value(cloudAccountIds
                                                    .stream()
                                                    .map(FieldValue::of).toList())
                                            .build()).build()).build()).build();
            try {
                elasticsearchClient.deleteByQuery(queryRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void deleteDataSource(String cloudAccountId) {
        deleteDataSource(List.of(cloudAccountId));
    }

    /**
     * 监控插入
     *
     * @param saveBatchOrUpdateParams 插入更新数据所需要的参数
     */
    private void perfMetricMonitorSaveOrUpdate(SaveBatchOrUpdateParams<F2CPerfMetricMonitorData> saveBatchOrUpdateParams) {
        List<F2CPerfMetricMonitorData> vmCloudServers = saveBatchOrUpdateParams.getSyncRecord();
        List<PerfMetricMonitorData> perfMetricMonitorDataList = new ArrayList<>();
        vmCloudServers.forEach(v -> {
            PerfMetricMonitorData perfMetricMonitorData = new PerfMetricMonitorData();
            BeanUtils.copyProperties(v, perfMetricMonitorData);
            perfMetricMonitorData.setCloudAccountId(saveBatchOrUpdateParams.getCloudAccountId());
            //数据索引ID，使用云账号ID+资源类型+资源ID+指标+时间点做为索引
            List<? extends Serializable> ids = Arrays.asList(perfMetricMonitorData.getCloudAccountId(), perfMetricMonitorData.getEntityType(), perfMetricMonitorData.getInstanceId(), perfMetricMonitorData.getMetricName(), perfMetricMonitorData.getTimestamp(), Objects.isNull(perfMetricMonitorData.getDevice()) ? "" : perfMetricMonitorData.getDevice());
            perfMetricMonitorData.setId(Joiner.on("-").join(ids));
            perfMetricMonitorDataList.add(perfMetricMonitorData);
        });
        perfMetricMonitorDataRepository.deleteAll(perfMetricMonitorDataList);
        List<List<PerfMetricMonitorData>> lists = CommonUtil.averageAssign(perfMetricMonitorDataList, 5000);
        for (List<PerfMetricMonitorData> list : lists) {
            perfMetricMonitorDataRepository.saveAll(list);
        }
    }

    /**
     * 代理执行同步动作
     *
     * @param cloudAccountId      云账号id
     * @param regions             区域
     * @param jobSyncResourceType 同步资源类型
     * @param execMethod          同步函数
     * @param saveBatchOrUpdate   插入更新函数
     * @param writeJobRecord      写入任务记录函数
     * @param remote              删除不存在的云账户函数
     * @param <T>                 同步对象类型
     */
    private <T> void proxy(String cloudAccountId, List<Credential.Region> regions, com.fit2cloud.constants.JobConstants.JobSyncResourceType jobSyncResourceType,
                           BiFunction<ICloudProvider, String, List<T>> execMethod,
                           Consumer<SaveBatchOrUpdateParams<T>> saveBatchOrUpdate,
                           Consumer<SaveBatchOrUpdateParams<T>> writeJobRecord,
                           Runnable remote) {
        proxy(cloudAccountId, regions, jobSyncResourceType.getMessage(),
                s -> ProviderConstants.valueOf(s).getCloudProvider(),
                syncTime -> initJobRecord(jobSyncResourceType, syncTime, cloudAccountId),
                execMethod,
                (account, region) -> getParams(account.getCredential(), region.getRegionId(), account.getSyncTimeStampStr()),
                saveBatchOrUpdate,
                writeJobRecord,
                remote);
    }

    /**
     * 初始化任务记录
     *
     * @param jobSyncResourceType 资源类型
     * @param syncTime            同步时间
     * @param cloudAccountId      云账户id
     * @return 任务记录对象F
     */
    private JobRecord initJobRecord(com.fit2cloud.constants.JobConstants.JobSyncResourceType jobSyncResourceType, LocalDateTime syncTime, String cloudAccountId) {
        JobRecord jobRecord = new JobRecord();
        jobRecord.setDescription(jobSyncResourceType.getMessage());
        jobRecord.setStatus(JobStatusConstants.SYNCING);
        jobRecord.setParams(new HashMap<>());
        jobRecord.setType(jobSyncResourceType.getJobType());
        jobRecord.setCreateTime(syncTime);
        jobRecord.setUpdateTime(syncTime);
        // 插入任务数据
        baseJobRecordService.save(jobRecord);
        // 插入关联关系
        JobRecordResourceMapping jobRecordResourceMapping = new JobRecordResourceMapping();
        jobRecordResourceMapping.setResourceId(cloudAccountId);
        jobRecordResourceMapping.setJobType(jobSyncResourceType.getJobType());
        jobRecordResourceMapping.setResourceType(jobSyncResourceType.name());
        jobRecordResourceMapping.setJobRecordId(jobRecord.getId());
        jobRecordResourceMapping.setCreateTime(syncTime);
        jobRecordResourceMapping.setUpdateTime(syncTime);
        jobRecordResourceMappingService.save(jobRecordResourceMapping);
        return jobRecord;
    }

    /**
     * 获取函数执行参数
     *
     * @param credential       认证信息
     * @param region           区域信息
     * @param syncTimeStampStr 开始同步的时间
     * @return json参数
     */
    private String getParams(String credential, String region, String syncTimeStampStr) {
        HashMap<String, String> params = new HashMap<>();
        params.put("credential", credential);
        params.put("regionId", region);
        params.put("syncTimeStampStr", syncTimeStampStr);
        return JsonUtil.toJSONString(params);
    }

    /**
     * 获取云账号
     *
     * @param map 参数Map
     * @return 云账号
     */
    private String getCloudAccountId(Map<String, Object> map) {
        return (String) map.get(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name());
    }

    /**
     * 获取同步区域
     *
     * @param map 参数Map
     * @return 同步区域
     */
    private List<Credential.Region> getRegions(Map<String, Object> map) {
        // todo 这个地方不能强转,强转后会出现线程阻塞
        String jsonString = JsonUtil.toJSONString(map.get(JobConstants.CloudAccount.REGIONS.name()));
        return JsonUtil.parseArray(jsonString, Credential.Region.class);
    }

    /**
     * 将云主机同步对象转换为实体对
     *
     * @param f2CVirtualMachine 同步对象
     * @param cloudAccountId    实体对象
     * @return 实体对象
     */
    public static VmCloudServer toVmCloudServer(F2CVirtualMachine f2CVirtualMachine, String cloudAccountId, LocalDateTime updateTime) {
        VmCloudServer vmCloudServer = new VmCloudServer();
        BeanUtils.copyProperties(f2CVirtualMachine, vmCloudServer);
        vmCloudServer.setAccountId(cloudAccountId);
        vmCloudServer.setInstanceUuid(f2CVirtualMachine.getInstanceUUID());
        vmCloudServer.setInstanceName(f2CVirtualMachine.getName());
        vmCloudServer.setRemoteIp(f2CVirtualMachine.getRemoteIP());
        vmCloudServer.setOsInfo(f2CVirtualMachine.getOsInfo());
        Optional.ofNullable(f2CVirtualMachine.getCreateTime()).ifPresent((createTime) -> vmCloudServer.setCreateTime(DateUtil.timestampToDatetime(createTime)));
        Optional.ofNullable(f2CVirtualMachine.getExpiredTime()).ifPresent((expiredTime) -> vmCloudServer.setExpiredTime(DateUtil.timestampToDatetime(expiredTime)));
        vmCloudServer.setUpdateTime(updateTime);
        vmCloudServer.setIpArray(JsonUtil.toJSONString(f2CVirtualMachine.getIpArray()));
        Optional.ofNullable(f2CVirtualMachine.getSecurityGroupIds()).ifPresent((theSecurityGroupIds) -> vmCloudServer.setSecurityGroupIds(JsonUtil.toJSONString(theSecurityGroupIds)));
        return vmCloudServer;
    }

    /**
     * 将云主机磁盘对象转换实体对象
     *
     * @param disk           磁盘对象
     * @param region         区域对象
     * @param cloudAccountId 云账号
     * @return 实体对象
     */
    private VmCloudDisk toVmDisk(F2CDisk disk, Credential.Region region, String cloudAccountId, LocalDateTime updateTime) {
        VmCloudDisk vmCloudDisk = new VmCloudDisk();
        vmCloudDisk.setAccountId(cloudAccountId);
        vmCloudDisk.setDescription(disk.getDescription());
        vmCloudDisk.setRegion(region.getRegionId());
        vmCloudDisk.setDiskId(disk.getDiskId());
        vmCloudDisk.setDiskName(disk.getDiskName());
        vmCloudDisk.setDiskType(disk.getDiskType());
        vmCloudDisk.setBootable(disk.isBootable());
        vmCloudDisk.setCategory(disk.getCategory());
        vmCloudDisk.setDatastoreId(disk.getDatastoreUniqueId());
        vmCloudDisk.setDevice(disk.getDevice());
        vmCloudDisk.setProjectId(disk.getProjectId());
        vmCloudDisk.setDeleteWithInstance(disk.getDeleteWithInstance());
        vmCloudDisk.setInstanceUuid(disk.getInstanceUuid());
        vmCloudDisk.setSize(disk.getSize());
        vmCloudDisk.setStatus(disk.getStatus());
        vmCloudDisk.setDiskChargeType(disk.getDiskChargeType());
        vmCloudDisk.setZone(disk.getZone());
        if (disk.getCreateTime() != 0) {
            vmCloudDisk.setCreateTime(DateUtil.timestampToDatetime(disk.getCreateTime()));
        }
        vmCloudDisk.setUpdateTime(updateTime);
        vmCloudDisk.setImageId(disk.getImageId());
        return vmCloudDisk;
    }

    /**
     * 将同步镜像转化为实体对象
     *
     * @param image          同步镜像对象
     * @param region         区域对象
     * @param cloudAccountId 云账号id
     * @return 实例对象
     */
    private VmCloudImage toVmImage(F2CImage image, Credential.Region region, String cloudAccountId, LocalDateTime updateTime) {
        VmCloudImage vmCloudImage = new VmCloudImage();
        vmCloudImage.setRegion(region.getRegionId());
        vmCloudImage.setRegionName(region.getName());
        vmCloudImage.setImageId(image.getId());
        vmCloudImage.setDiskInfos(image.getDiskInfos());
        vmCloudImage.setDiskSize(image.getDiskSize());
        vmCloudImage.setDescription(image.getDescription());
        vmCloudImage.setImageName(image.getName());
        vmCloudImage.setAccountId(cloudAccountId);
        vmCloudImage.setOs(image.getOs());
        vmCloudImage.setStatus(F2CImageStatus.normal);
        vmCloudImage.setUpdateTime(updateTime);
        vmCloudImage.setImageType(image.getImageType());
        return vmCloudImage;
    }

    /**
     * 将宿主机对象转换实体对象
     *
     * @param host           宿主机对象
     * @param cloudAccountId 云账号
     * @return 实体对象
     */
    private VmCloudHost toVmHost(F2CHost host, String cloudAccountId, LocalDateTime updateTime) {
        VmCloudHost vmCloudHost = new VmCloudHost();
        BeanUtils.copyProperties(host, vmCloudHost);
        vmCloudHost.setAccountId(cloudAccountId);
//        vmCloudHost.setRegion(host.getDataCenterId());
//        vmCloudHost.setZone(host.getClusterId());
        vmCloudHost.setCpuAllocated(host.getCpuMHzAllocated());
        vmCloudHost.setCpuTotal(host.getCpuMHzTotal());
        vmCloudHost.setCpuMhzPerOneCore(host.getCpuMHzPerOneCore());
        vmCloudHost.setUpdateTime(updateTime);
        vmCloudHost.setCpuUsed(host.getCpuMHzUsed());
        vmCloudHost.setMemoryUsed(host.getMemoryUsed());
        return vmCloudHost;
    }

    /**
     * 将存储器对象转换实体对象
     *
     * @param datastore      存储器对象
     * @param cloudAccountId 云账号
     * @return 实体对象
     */
    private VmCloudDatastore toVmDatastore(F2CDatastore datastore, String cloudAccountId, LocalDateTime updateTime) {
        VmCloudDatastore vmCloudDatastore = new VmCloudDatastore();
        BeanUtils.copyProperties(datastore, vmCloudDatastore);
        vmCloudDatastore.setAccountId(cloudAccountId);
//        vmCloudDatastore.setRegion(datastore.getDataCenterId());
//        vmCloudDatastore.setZone(datastore.getClusterId());
        vmCloudDatastore.setDatastoreId(datastore.getDataStoreId());
        vmCloudDatastore.setDatastoreName(datastore.getDataStoreName());
        vmCloudDatastore.setUpdateTime(updateTime);
        vmCloudDatastore.setAllocatedSpace(datastore.getAllocatedSpace());
        return vmCloudDatastore;
    }
}
