package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseJobRecordResourceMappingMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.*;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.vm.BatchOperateVmRequest;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dto.InitJobRecordDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.response.JobRecordResourceResponse;
import com.fit2cloud.service.IVmCloudServerService;
import com.fit2cloud.service.JobRecordCommonService;
import com.fit2cloud.service.OrganizationCommonService;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class VmCloudServerServiceImpl extends ServiceImpl<BaseVmCloudServerMapper, VmCloudServer> implements IVmCloudServerService {

    @Resource
    private OrganizationCommonService organizationCommonService;
    @Resource
    private VmCloudServerMapper vmCloudServerMapper;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private CommonThreadPool commonThreadPool;
    @Resource
    private JobRecordCommonService jobRecordCommonService;

    @Resource
    private BaseJobRecordResourceMappingMapper baseJobRecordResourceMappingMapper;

    /**
     * 云主机批量操作
     */
    private Map<OperatedTypeEnum, Consumer<String>> batchOperationMap;
    @PostConstruct
    private void init(){
        batchOperationMap = new HashMap<>();
        batchOperationMap.put(OperatedTypeEnum.POWER_ON,this::powerOn);
        batchOperationMap.put(OperatedTypeEnum.HARD_REBOOT,this::hardRebootInstance);
        batchOperationMap.put(OperatedTypeEnum.HARD_SHUTDOWN,this::hardShutdownInstance);
        batchOperationMap.put(OperatedTypeEnum.BATCH_DELETE,this::deleteInstance);
    }
    @Override
    public IPage<VmCloudServerDTO> pageVmCloudServer(PageVmCloudServerRequest request) {
        // 普通用户
        if (CurrentUserUtils.isUser()) {
            request.setWorkspaceId(CurrentUserUtils.getWorkspaceId());
        }
        // 组织管理员
        if (CurrentUserUtils.isOrgAdmin()) {
            request.setOrganizationId(CurrentUserUtils.getOrganizationId());
            request.setOrganizationIds(organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId()));
        }
        // 构建查询参数
        QueryWrapper<VmCloudServerDTO> wrapper = addQuery(request);
        Page<VmCloudServerDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudServerDTO> result = vmCloudServerMapper.pageList(page, wrapper);
        return result;
    }

    private QueryWrapper<VmCloudServerDTO> addQuery(PageVmCloudServerRequest request) {
        QueryWrapper<VmCloudServerDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudServerDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_server.update_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()), "vm_cloud_disk.workspace_id", request.getWorkspaceId());
        //wrapper.in(CollectionUtils.isNotEmpty(request.getOrganizationIds()),"vm_cloud_disk.organization_id",request.getOrganizationIds());
        wrapper.like(StringUtils.isNotBlank(request.getInstanceName()), "vm_cloud_server.instance_name", request.getInstanceName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()), "cloud_account.name", request.getAccountName());
        wrapper.like(StringUtils.isNotBlank(request.getIpArray()), "vm_cloud_server.ip_array", request.getIpArray());

        wrapper.in(CollectionUtils.isNotEmpty(request.getInstanceStatus()), "vm_cloud_server.instance_status", request.getInstanceStatus());
        return wrapper;
    }

    @Override
    public boolean powerOff(String vmId) {
        operate(vmId,OperatedTypeEnum.POWER_OFF.getDescription(),ICloudProvider::powerOff,
                F2CInstanceStatus.Stopping.name(),F2CInstanceStatus.Stopped.name(),this::modifyResource,
                jobRecordCommonService::initJobRecord,jobRecordCommonService::modifyJobRecord);
        return true;
    }

    @Override
    public boolean powerOn(String vmId) {
        operate(vmId,OperatedTypeEnum.POWER_ON.getDescription(),ICloudProvider::powerOn,
                F2CInstanceStatus.Starting.name(),F2CInstanceStatus.Running.name(),this::modifyResource,
                jobRecordCommonService::initJobRecord,jobRecordCommonService::modifyJobRecord);
        return true;
    }


    public boolean shutdownInstance(String vmId) {
        operate(vmId,OperatedTypeEnum.SHUTDOWN.getDescription(),ICloudProvider::shutdownInstance,
                F2CInstanceStatus.Stopping.name(),F2CInstanceStatus.Stopped.name(),this::modifyResource,
                jobRecordCommonService::initJobRecord,jobRecordCommonService::modifyJobRecord);
        return true;
    }

    public boolean hardShutdownInstance(String vmId){
        operate(vmId,OperatedTypeEnum.HARD_SHUTDOWN.getDescription(),ICloudProvider::hardShutdownInstance,
                F2CInstanceStatus.Stopping.name(),F2CInstanceStatus.Stopped.name(),this::modifyResource,
                jobRecordCommonService::initJobRecord,jobRecordCommonService::modifyJobRecord);
        return true;
    }

    public boolean hardRebootInstance(String vmId){
        operate(vmId,OperatedTypeEnum.HARD_REBOOT.getDescription(),ICloudProvider::hardRebootInstance,
                F2CInstanceStatus.Rebooting.name(),F2CInstanceStatus.Running.name(),this::modifyResource,
                jobRecordCommonService::initJobRecord,jobRecordCommonService::modifyJobRecord);
        return true;
    }
    @Override
    public boolean shutdownInstance(String vmId,Boolean powerOff){
        if(powerOff){
            return powerOff(vmId);
        }else{
            return shutdownInstance(vmId);
        }
    }

    @Override
    public boolean rebootInstance(String vmId) {
        operate(vmId,OperatedTypeEnum.REBOOT.getDescription(),ICloudProvider::rebootInstance,
                F2CInstanceStatus.Rebooting.name(),F2CInstanceStatus.Running.name(),this::modifyResource,
                jobRecordCommonService::initJobRecord,jobRecordCommonService::modifyJobRecord);
        return true;
    }

    @Override
    public boolean deleteInstance(String vmId) {
        operate(vmId,OperatedTypeEnum.DELETE.getDescription(),ICloudProvider::deleteInstance,
                F2CInstanceStatus.Deleting.name(),F2CInstanceStatus.Deleted.name(), this::modifyResource,
                jobRecordCommonService::initJobRecord,jobRecordCommonService::modifyJobRecord);
        return true;
    }

    @Override
    public boolean batchOperate(BatchOperateVmRequest request) {
        if (request.getInstanceIds().size() == 0) {
            throw new Fit2cloudException(ErrorCodeConstants.SELECT_AT_LEAST_ONE_VM.getCode(), ErrorCodeConstants.SELECT_AT_LEAST_ONE_VM.getMessage());
        }
        OperatedTypeEnum operatedType = OperatedTypeEnum.valueOf(request.getOperate());
        if(batchOperationMap.get(operatedType)==null){
            throw new Fit2cloudException(ErrorCodeConstants.NOT_SUPPORTED_TEMPORARILY.getCode(), ErrorCodeConstants.NOT_SUPPORTED_TEMPORARILY.getMessage()+" - "+request.getOperate());
        }
        request.getInstanceIds().stream().forEach(instanceId -> {
            try {
                batchOperationMap.get(operatedType).accept(instanceId);
            } catch (Throwable e) {
                throw new Fit2cloudException(ErrorCodeConstants.VM_BATCH_OPERATE_FAIL.getCode(), e.getMessage());
            }
        });
        return true;
    }

    @Override
    public List<JobRecordResourceResponse> findCloudServerOperateStatus(List<String> vmIds) {
        return baseJobRecordResourceMappingMapper.findLastResourceJobRecord(vmIds,JobTypeConstants.CLOUD_SERVER_OPERATE_JOB.getCode());
    }

    @Override
    public VmCloudServerDTO getById(String vmId) {
        QueryWrapper<VmCloudServerDTO> wrapper = new QueryWrapper<>();
        wrapper.eq("vm_cloud_server.id",vmId);
        VmCloudServerDTO vo = vmCloudServerMapper.getById(wrapper);
        return vo;
    }


    /**
     * 云主机操作
     * @param vmId 云主机ID
     * @param jobDescription 任务描述
     * @param execMethod 操作方法
     * @param modifyResource 修改云主机
     * @param iniJobMethod 初始化任务
     * @param modifyJobRecord 修改任务
     * @param beforeStatus 初始化资源状态
     * @param afterStatus 最终资源状态
     */
    private void operate(String vmId,String jobDescription, BiFunction<ICloudProvider, String,Boolean> execMethod,
                          String beforeStatus, String afterStatus,Consumer<VmCloudServer> modifyResource,
                         Function<InitJobRecordDTO, JobRecord> iniJobMethod,Consumer<JobRecord> modifyJobRecord) {
        commonThreadPool.addTask(()->{
            try{
                LocalDateTime createTime = DateUtil.getSyncTime();
                QueryWrapper<VmCloudServer> wrapper = new QueryWrapper<VmCloudServer>()
                        .eq(ColumnNameUtil.getColumnName(VmCloudServer::getId, true), vmId);
                VmCloudServer vmCloudServer = baseMapper.selectOne(wrapper);
                if (vmCloudServer == null) {
                    throw new Fit2cloudException(ErrorCodeConstants.VM_NOT_EXIST.getCode(), ErrorCodeConstants.VM_NOT_EXIST.getMessage());
                }
                String instanceStatus = vmCloudServer.getInstanceStatus();
                //初始化任务
                JobRecord jobRecord = iniJobMethod.apply(
                        InitJobRecordDTO.builder()
                                .jobDescription(jobDescription).jobStatus(JobStatusConstants.EXECUTION_ING).jobType(JobTypeConstants.CLOUD_SERVER_OPERATE_JOB).resourceId(vmCloudServer.getId()).resourceType(ResourceTypeEnum.CLOUD_SERVER).createTime(createTime)
                        .build());
                vmCloudServer.setInstanceStatus(beforeStatus);
                modifyResource.accept(vmCloudServer);
                CloudAccount cloudAccount = cloudAccountService.getById(vmCloudServer.getAccountId());
                Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
                HashMap<String, String> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudServer.getRegion());
                params.put("uuId", vmCloudServer.getInstanceUuid());
                OperatedTypeEnum operatedType = OperatedTypeEnum.getByDescription(jobDescription);
                try{
                    boolean result = CommonUtil.exec(cloudProvider,JsonUtil.toJSONString(params),execMethod);
                    if(result){
                        vmCloudServer.setInstanceStatus(afterStatus);
                        jobRecord.setStatus(JobStatusConstants.SUCCESS);
                        switch (operatedType){
                            case POWER_OFF:
                            case SHUTDOWN:
                            case HARD_SHUTDOWN:
                                vmCloudServer.setLastShutdownTime(DateUtil.getSyncTime());
                            case POWER_ON:
                                vmCloudServer.setLastShutdownTime(null);
                            default:
                        }
                    }
                }catch (Exception e){
                    vmCloudServer.setInstanceStatus(instanceStatus);
                    jobRecord.setStatus(JobStatusConstants.FAILED);
                    jobRecord.setResult(e.getMessage());
                    LogUtil.error("Cloud server exec operate fail - {}",e.getMessage());
                    e.printStackTrace();
                }
                modifyResource.accept(vmCloudServer);
                modifyJobRecord.accept(jobRecord);
            } catch (Throwable e) {
                LogUtil.error("Cloud server operate fail - {}",e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    private void modifyResource(VmCloudServer vmCloudServer){
        baseMapper.updateById(vmCloudServer);
    }

}
