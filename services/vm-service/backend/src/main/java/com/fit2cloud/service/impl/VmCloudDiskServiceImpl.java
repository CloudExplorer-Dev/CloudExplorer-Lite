package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseRecycleBinService;
import com.fit2cloud.base.service.IBaseVmCloudServerService;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.constants.RecycleBinStatusConstants;
import com.fit2cloud.common.constants.ResourceTypeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.CreateJobRecordRequest;
import com.fit2cloud.controller.request.ExecProviderMethodRequest;
import com.fit2cloud.controller.request.GrantRequest;
import com.fit2cloud.controller.request.ResourceState;
import com.fit2cloud.controller.request.disk.CreateVmCloudDiskRequest;
import com.fit2cloud.controller.request.disk.ListVmRequest;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.controller.request.disk.VmCloudDiskRequest;
import com.fit2cloud.dao.mapper.VmCloudDiskMapper;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.provider.impl.vsphere.util.ResourceConstants;
import com.fit2cloud.service.*;
import com.fit2cloud.utils.ConvertUtils;
import com.fit2cloud.vm.ICloudProvider;
import com.fit2cloud.vm.constants.DeleteWithInstance;
import com.fit2cloud.vm.constants.F2CDiskStatus;
import com.fit2cloud.vm.constants.F2CInstanceStatus;
import com.fit2cloud.vm.entity.F2CDisk;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class VmCloudDiskServiceImpl extends ServiceImpl<BaseVmCloudDiskMapper, VmCloudDisk> implements IVmCloudDiskService {
    @Resource
    private VmCloudDiskMapper diskMapper;
    @Resource
    private VmCloudServerMapper serverMapper;
    @Resource
    private IBaseVmCloudServerService vmCloudServerService;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private IResourceOperateService resourceOperateService;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private IBaseRecycleBinService recycleService;
    @Resource
    private WorkspaceCommonService workspaceCommonService;
    @Resource
    private OrganizationCommonService organizationCommonService;

    @Override
    public IPage<VmCloudDiskDTO> pageVmCloudDisk(PageVmCloudDiskRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        Page<VmCloudDiskDTO> page = PageUtil.of(request, VmCloudDiskDTO.class, new OrderItem(ColumnNameUtil.getColumnName(VmCloudDiskDTO::getCreateTime, true), false), true);
        // 构建查询参数
        QueryWrapper<VmCloudDiskDTO> wrapper = addQuery(request);

        IPage<VmCloudDiskDTO> result = diskMapper.pageList(page, wrapper);
        result.convert(this::resetVmCloudServerDTO);
        return result;
    }

    @Override
    public List<VmCloudDiskDTO> listVMCloudDisk(VmCloudDiskRequest request) {
        return listVmCloudDisk(request.toPageVmCloudDiskRequest());
    }

    private VmCloudDiskDTO resetVmCloudServerDTO(VmCloudDiskDTO vmCloudDiskDTO) {
        if (vmCloudDiskDTO.getBootable()) {
            vmCloudDiskDTO.setBootableText("系统盘");
        } else {
            vmCloudDiskDTO.setBootableText("数据盘");
        }
        return vmCloudDiskDTO;
    }

    public List<VmCloudDiskDTO> listVmCloudDisk(PageVmCloudDiskRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        QueryWrapper<VmCloudDiskDTO> wrapper = addQuery(request);
        return diskMapper.pageList(wrapper).stream().map(this::resetVmCloudServerDTO).toList();
    }

    @Override
    public long countDisk() {
        List<String> sourceIds = permissionService.getSourceIds();
        return this.count(new LambdaQueryWrapper<VmCloudDisk>()
                .in(CollectionUtils.isNotEmpty(sourceIds), VmCloudDisk::getSourceId, sourceIds)
                .ne(VmCloudDisk::getStatus, F2CDiskStatus.DELETED)
        );
    }

    private <T extends VmCloudDisk> QueryWrapper<T> addQuery(PageVmCloudDiskRequest request) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        wrapper.eq(StringUtils.isNotBlank(request.getId()), ColumnNameUtil.getColumnName(VmCloudDisk::getId, true), request.getId());
        wrapper.in(CollectionUtils.isNotEmpty(request.getIds()), ColumnNameUtil.getColumnName(VmCloudDisk::getId, true), request.getIds());
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()), ColumnNameUtil.getColumnName(VmCloudDisk::getSourceId, true), request.getWorkspaceId());
        wrapper.like(StringUtils.isNotBlank(request.getDiskName()), ColumnNameUtil.getColumnName(VmCloudDisk::getDiskName, true), request.getDiskName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()), ColumnNameUtil.getColumnName(CloudAccount::getName, true), request.getAccountName());
        wrapper.like(StringUtils.isNotBlank(request.getVmInstanceName()), ColumnNameUtil.getColumnName(VmCloudServer::getInstanceName, true), request.getVmInstanceName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getBootable()), ColumnNameUtil.getColumnName(VmCloudDisk::getBootable, true), request.getBootable());
        wrapper.in(CollectionUtils.isNotEmpty(request.getDiskType()), ColumnNameUtil.getColumnName(VmCloudDisk::getDiskType, true), request.getDiskType());
        wrapper.in(CollectionUtils.isNotEmpty(request.getDeleteWithInstance()), ColumnNameUtil.getColumnName(VmCloudDisk::getDeleteWithInstance, true), request.getDeleteWithInstance());
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(VmCloudDisk::getAccountId, true), request.getAccountIds());

        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), ColumnNameUtil.getColumnName(VmCloudDisk::getSourceId, true), request.getSourceIds());
        wrapper.in(CollectionUtils.isNotEmpty(request.getWorkspaceIds()), ColumnNameUtil.getColumnName(VmCloudDisk::getSourceId, true), request.getWorkspaceIds());
        if (CollectionUtils.isNotEmpty(request.getOrganizationIds())) {
            List<String> orgWorkspaceList = workspaceCommonService.getWorkspaceIdsByOrgIds(request.getOrganizationIds());
            orgWorkspaceList.addAll(request.getOrganizationIds());
            wrapper.in(CollectionUtils.isNotEmpty(orgWorkspaceList), ColumnNameUtil.getColumnName(VmCloudDisk::getSourceId, true), orgWorkspaceList);
        }

        // 默认不展示已删除状态的磁盘
        if (StringUtils.isEmpty(request.getStatus())) {
            wrapper.ne(ColumnNameUtil.getColumnName(VmCloudDisk::getStatus, true), F2CDiskStatus.DELETED);
        } else {
            if (RecycleBinStatusConstants.ToBeRecycled.name().equalsIgnoreCase(request.getStatus())) {
                wrapper.and(wrapperInner -> wrapperInner.eq(ColumnNameUtil.getColumnName(RecycleBin::getStatus, true), RecycleBinStatusConstants.ToBeRecycled.name())
                        .ne(ColumnNameUtil.getColumnName(VmCloudDisk::getStatus, true), F2CDiskStatus.DELETED));
            } else if (F2CInstanceStatus.Deleted.name().equalsIgnoreCase(request.getStatus())) {
                wrapper.eq(ColumnNameUtil.getColumnName(VmCloudDisk::getStatus, true), F2CDiskStatus.DELETED);
            } else {
                wrapper.in(ColumnNameUtil.getColumnName(VmCloudDisk::getStatus, true), request.getStatus())
                        .and(wrapperInner1 -> wrapperInner1.ne(ColumnNameUtil.getColumnName(RecycleBin::getStatus, true), RecycleBinStatusConstants.ToBeRecycled.name())
                                .or(wrapperInner2 -> wrapperInner2.isNull(ColumnNameUtil.getColumnName(RecycleBin::getStatus, true))));
            }
        }
        return wrapper;
    }

    @Override
    public List<VmCloudServerDTO> cloudServerList(ListVmRequest req) {
        List<String> sourceIds = permissionService.getSourceIds();

        QueryWrapper<VmCloudServer> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(VmCloudServer::getAccountId, req.getAccountId())
                .eq(VmCloudServer::getZone, req.getZone())
                .ne(VmCloudServer::getInstanceStatus, F2CInstanceStatus.Deleted.name());
        if (CollectionUtils.isNotEmpty(sourceIds)) {
            wrapper.lambda().in(VmCloudServer::getSourceId, sourceIds);
        }
        return serverMapper.selectServerList(wrapper);
    }

    @Override
    public VmCloudDiskDTO cloudDisk(String id) {
        List<VmCloudDiskDTO> list = this.listVmCloudDisk(new PageVmCloudDiskRequest().setId(id));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public FormObject getCreateDiskForm(String platform) {
        ICloudProvider iCloudProvider = PluginsContextHolder.getPlatformExtension(ICloudProvider.class, platform);
        try {
            return iCloudProvider.getCreateDiskForm();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get create disk form!" + e.getMessage(), e);
        }
    }

    @Override
    public boolean createDisk(CreateVmCloudDiskRequest request) {
        try {
            // 创建前:插入一条磁盘记录，状态为初始化
            String id = UUID.randomUUID().toString().replace("-", "");
            VmCloudDisk initVmCloudDisk = request.toVmCloudDisk(id, F2CDiskStatus.INIT);
            baseMapper.insert(initVmCloudDisk);

            // 创建中实体状态
            VmCloudDisk creatingVmCloudDisk = request.toVmCloudDisk(id, F2CDiskStatus.CREATING);

            // 创建完成实体状态
            VmCloudDisk finishedVmCloudDisk = request.toVmCloudDisk(id, F2CDiskStatus.IN_USE);
            String sourceId = getSourceId(initVmCloudDisk.getAccountId(), request.getInstanceUuid());
            if (StringUtils.isNotBlank(sourceId)) {
                finishedVmCloudDisk.setSourceId(sourceId);
            }

            if (request.getInstanceUuid() != null) {
                request.setIsAttached(true);
            }

            CloudAccount cloudAccount = cloudAccountService.getById(request.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), request.getRegionId());
            params.putAll(request);
            params.put("zone", request.getZone());
            params.put("diskName", request.getDiskName());
            params.put("diskType", request.getDiskType());
            params.put("diskMode", request.getDiskMode());
            params.put("datastore", request.getDatastore());
            params.put("size", request.getSize());
            params.put("description", request.getDescription());
            if (request.getIsAttached()) {
                params.put("isAttached", request.getIsAttached());
                params.put("instanceUuid", request.getInstanceUuid());
                params.put("deleteWithInstance", request.getDeleteWithInstance());
                //获取云主机计费方式
                QueryWrapper<VmCloudServer> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(VmCloudServer::getAccountId, request.getAccountId())
                        .eq(VmCloudServer::getInstanceUuid, request.getInstanceUuid());
                VmCloudServer vmCloudServer = vmCloudServerService.getOne(queryWrapper);
                if (Objects.nonNull(vmCloudServer)) {
                    params.put("diskChargeType", vmCloudServer.getInstanceChargeType());
                }
            }

            // 执行
            ResourceState<VmCloudDisk, F2CDisk> resourceState = ResourceState.<VmCloudDisk, F2CDisk>builder()
                    .beforeResource(initVmCloudDisk)
                    .processingResource(creatingVmCloudDisk)
                    .afterResource(finishedVmCloudDisk)
                    .updateResourceMethodNeedTransfer(this::convertAndUpdateCloudDisk)
                    .updateResourceMethod(this::updateCloudDisk)
                    .deleteResourceMethod(this::deleteCloudDisk)
                    .saveResourceMethod(this::saveCloudDisk)
                    .build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder().execMethod(ICloudProvider::createDisk).methodParams(params).platform(platform).resultNeedTransfer(true).build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder()
                    .resourceOperateType(OperatedTypeEnum.CREATE_DISK)
                    .resourceId(initVmCloudDisk.getId())
                    .resourceType(ResourceTypeEnum.CLOUD_DISK)
                    .jobType(JobTypeConstants.CLOUD_DISK_CREATE_JOB)
                    .build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create cloud disk: " + e.getMessage());
        }
    }

    @Override
    public boolean enlarge(String id, long newDiskSize) {
        if (this.cloudDisk(id) == null) {
            throw new RuntimeException("找不到ID为[" + id + "]的资源或没有权限操作");
        }
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);

            // 扩容前状态
            VmCloudDisk beforeEnlarge = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeEnlarge);

            // 扩容中状态
            vmCloudDisk.setStatus(F2CDiskStatus.ENLARGING);
            VmCloudDisk processingEnlarge = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingEnlarge);

            // 扩容后状态
            vmCloudDisk.setStatus(beforeEnlarge.getStatus());
            vmCloudDisk.setSize(newDiskSize);
            VmCloudDisk afterEnlarge = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterEnlarge);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("newDiskSize", newDiskSize);
            params.put("instanceUuid", vmCloudDisk.getInstanceUuid());

            // 执行
            ResourceState<VmCloudDisk, Boolean> resourceState = ResourceState.<VmCloudDisk, Boolean>builder()
                    .beforeResource(beforeEnlarge)
                    .processingResource(processingEnlarge)
                    .afterResource(afterEnlarge)
                    .updateResourceMethod(this::updateCloudDisk)
                    .build();

            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder().
                    execMethod(ICloudProvider::enlargeDisk).
                    methodParams(params).
                    platform(platform)
                    .build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder().
                    resourceOperateType(OperatedTypeEnum.ENLARGE_DISK).
                    resourceId(id).
                    resourceType(ResourceTypeEnum.CLOUD_DISK).
                    jobType(JobTypeConstants.CLOUD_DISK_ENLARGE_JOB)
                    .build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to enlarge cloud disk: " + e.getMessage());
        }
    }

    @Override
    public boolean attach(String id, String instanceUuid, Boolean deleteWithInstance) {
        if (this.cloudDisk(id) == null) {
            throw new RuntimeException("找不到ID为[" + id + "]的资源或没有权限操作");
        }
        //todo 判断云主机?
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);

            // 挂载前状态
            VmCloudDisk beforeAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeAttach);

            // 挂载中状态
            vmCloudDisk.setStatus(F2CDiskStatus.ATTACHING);
            VmCloudDisk processingAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingAttach);

            // 挂载后状态
            vmCloudDisk.setStatus(F2CDiskStatus.IN_USE);
            vmCloudDisk.setInstanceUuid(instanceUuid);
            vmCloudDisk.setDeleteWithInstance(DeleteWithInstance.NO.name());
            String sourceId = getSourceId(vmCloudDisk.getAccountId(), instanceUuid);
            if (StringUtils.isNotBlank(sourceId)) {
                vmCloudDisk.setSourceId(sourceId);
            }
            if (deleteWithInstance) {
                vmCloudDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
            }
            VmCloudDisk afterAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterAttach);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("instanceUuid", instanceUuid);
            params.put("deleteWithInstance", DeleteWithInstance.NO.name());
            if (deleteWithInstance) {
                params.put("deleteWithInstance", DeleteWithInstance.YES.name());
            }

            // 执行
            ResourceState<VmCloudDisk, F2CDisk> resourceState = ResourceState.<VmCloudDisk, F2CDisk>builder()
                    .beforeResource(beforeAttach)
                    .processingResource(processingAttach)
                    .afterResource(afterAttach)
                    .updateResourceMethod(this::updateCloudDisk)
                    .updateResourceMethodNeedTransfer(this::updateCloudDisk)
                    .build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder()
                    .execMethod(ICloudProvider::attachDisk)
                    .methodParams(params)
                    .platform(platform)
                    .resultNeedTransfer(true)
                    .build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder()
                    .resourceOperateType(OperatedTypeEnum.ATTACH_DISK)
                    .resourceId(id)
                    .resourceType(ResourceTypeEnum.CLOUD_DISK)
                    .jobType(JobTypeConstants.CLOUD_DISK_ATTACH_JOB)
                    .build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to attach cloud disk: " + e.getMessage());
        }
    }

    @Override
    public boolean detach(String id) {
        if (this.cloudDisk(id) == null) {
            throw new RuntimeException("找不到ID为[" + id + "]的资源或没有权限操作");
        }
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);

            // 卸载前状态
            VmCloudDisk beforeDetach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeDetach);

            // 卸载中状态
            vmCloudDisk.setStatus(F2CDiskStatus.DETACHING);
            VmCloudDisk processingDetach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingDetach);

            // 卸载后状态
            vmCloudDisk.setStatus(F2CDiskStatus.AVAILABLE);
            vmCloudDisk.setInstanceUuid(ResourceConstants.NO_INSTANCE);
            vmCloudDisk.setDevice("");
            vmCloudDisk.setDeleteWithInstance("");
            VmCloudDisk afterDetach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterDetach);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("instanceUuid", beforeDetach.getInstanceUuid());

            // 执行
            ResourceState<VmCloudDisk, Boolean> resourceState = ResourceState.<VmCloudDisk, Boolean>builder()
                    .beforeResource(beforeDetach)
                    .processingResource(processingDetach)
                    .afterResource(afterDetach)
                    .updateResourceMethod(this::updateCloudDisk)
                    .build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder()
                    .execMethod(ICloudProvider::detachDisk)
                    .methodParams(params).
                    platform(platform)
                    .build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder()
                    .resourceOperateType(OperatedTypeEnum.DETACH_DISK)
                    .resourceId(id).resourceType(ResourceTypeEnum.CLOUD_DISK)
                    .jobType(JobTypeConstants.CLOUD_DISK_DETACH_JOB)
                    .build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to detach cloud disk!" + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String id) {
        if (this.cloudDisk(id) == null) {
            throw new RuntimeException("找不到ID为[" + id + "]的资源或没有权限操作");
        }
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);

            // 删除前状态
            VmCloudDisk beforeDelete = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeDelete);

            // 删除中状态
            vmCloudDisk.setStatus(F2CDiskStatus.DELETING);
            VmCloudDisk processingDelete = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingDelete);

            // 删除后状态
            vmCloudDisk.setStatus(F2CDiskStatus.DELETED);
            VmCloudDisk afterDelete = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterDelete);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());

            // 执行
            ResourceState<VmCloudDisk, Boolean> resourceState = ResourceState.<VmCloudDisk, Boolean>builder()
                    .beforeResource(beforeDelete)
                    .processingResource(processingDelete)
                    .afterResource(afterDelete)
                    .updateResourceMethod(this::updateCloudDisk)
                    .build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder()
                    .execMethod(ICloudProvider::deleteDisk)
                    .methodParams(params)
                    .platform(platform)
                    .build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder()
                    .resourceOperateType(OperatedTypeEnum.DELETE_DISK)
                    .resourceId(id)
                    .resourceType(ResourceTypeEnum.CLOUD_DISK)
                    .jobType(JobTypeConstants.CLOUD_DISK_DELETE_JOB)
                    .build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState);

            return true;
        } catch (Exception e) {
            throw new Fit2cloudException(ErrorCodeConstants.DISK_DELETE_FAIL.getCode(), "Failed to delete cloud disk!" + e.getMessage());
        }
    }

    @Override
    public boolean batchAttach(String[] ids, String instanceUuid, Boolean deleteWithInstance) {
        for (String id : ids) {
            attach(id, instanceUuid, deleteWithInstance);
        }
        return true;
    }

    @Override
    public boolean batchDetach(String[] ids) {
        for (String id : ids) {
            detach(id);
        }
        return true;
    }

    @Override
    public boolean batchDelete(String[] ids) {
        for (String id : ids) {
            delete(id);
        }
        return true;
    }

    /**
     * 更新磁盘 (磁盘扩容、卸载等操作命令执行后进行的操作)
     *
     * @param vmCloudDisk
     * @param
     */
    private void updateCloudDisk(VmCloudDisk vmCloudDisk) {
        vmCloudDisk.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(vmCloudDisk);
    }

    private void convertAndUpdateCloudDisk(VmCloudDisk vmCloudDisk, F2CDisk result) {
        if (vmCloudDisk.getDiskName() == null) {
            vmCloudDisk.setDiskName(result.getDiskName());
        }
        vmCloudDisk.setDiskId(result.getDiskId());
        vmCloudDisk.setDevice(result.getDevice());
        vmCloudDisk.setDatastoreId(result.getDatastoreUniqueId());
        vmCloudDisk.setDiskChargeType(result.getDiskChargeType());
        vmCloudDisk.setDiskType(result.getDiskType());
        if (StringUtils.isEmpty(vmCloudDisk.getDeleteWithInstance())) {
            vmCloudDisk.setDeleteWithInstance(result.getDeleteWithInstance());
        }
        vmCloudDisk.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(vmCloudDisk);
    }

    /**
     * 更新磁盘 (磁盘挂载后进行的操作)
     *
     * @param vmCloudDisk
     * @param f2CDisk
     */
    private void updateCloudDisk(VmCloudDisk vmCloudDisk, F2CDisk f2CDisk) {
        vmCloudDisk.setDevice(f2CDisk.getDevice());
        vmCloudDisk.setDiskId(f2CDisk.getDiskId());
        vmCloudDisk.setDiskName(f2CDisk.getDiskName());
        vmCloudDisk.setDeleteWithInstance(f2CDisk.getDeleteWithInstance());
        updateCloudDisk(vmCloudDisk);
    }

    /**
     * 删除磁盘
     *
     * @param vmCloudDisk
     * @param
     */
    private void deleteCloudDisk(VmCloudDisk vmCloudDisk) {
        baseMapper.deleteById(vmCloudDisk);
    }

    /**
     * 保存磁盘（创建磁盘成功后进行的操作）
     *
     * @param vmCloudDisk
     * @param result      云平台方法返回类型
     */
    private void saveCloudDisk(VmCloudDisk vmCloudDisk, F2CDisk result) {
        // 先删除预处理插入的数据
        baseMapper.deleteById(vmCloudDisk.getId());

        // 删除同步到的数据
        QueryWrapper<VmCloudDisk> qw = Wrappers.query();
        qw.lambda()
                .eq(VmCloudDisk::getDiskId, vmCloudDisk.getDiskId())
                .eq(VmCloudDisk::getRegion, vmCloudDisk.getRegion())
                .eq(VmCloudDisk::getAccountId, vmCloudDisk.getAccountId());
        List<VmCloudDisk> vmCloudDisks = baseMapper.selectList(qw);
        boolean exist = CollectionUtils.isNotEmpty(vmCloudDisks);
        if (exist) {
            baseMapper.delete(qw);
        }

        // 重新插入数据
        if (vmCloudDisk.getDiskName() == null) {
            vmCloudDisk.setDiskName(result.getDiskName());
        }
        vmCloudDisk.setDiskId(result.getDiskId());
        vmCloudDisk.setDevice(result.getDevice());
        vmCloudDisk.setDatastoreId(result.getDatastoreUniqueId());
        vmCloudDisk.setDiskChargeType(result.getDiskChargeType());
        if (StringUtils.isEmpty(vmCloudDisk.getDeleteWithInstance())) {
            vmCloudDisk.setDeleteWithInstance(result.getDeleteWithInstance());
        }
        baseMapper.insert(vmCloudDisk);
    }

    /**
     * 保存磁盘（创建云主机成功后进行的操作）
     *
     * @param f2cDisks
     * @param accountId
     * @param sourceId
     */
    public void saveCloudDisks(List<F2CDisk> f2cDisks, String accountId, String sourceId) {
        for (F2CDisk f2cDisk : f2cDisks) {
            VmCloudDisk vmCloudDisk = ConvertUtils.disk(f2cDisk);
            if (f2cDisk.getCreateTime() == 0) {
                vmCloudDisk.setCreateTime(LocalDateTime.now());
            }
            String diskId = UUID.randomUUID().toString().replace("-", "");
            QueryWrapper<VmCloudDisk> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(VmCloudDisk::getAccountId, accountId)
                    .eq(VmCloudDisk::getRegion, f2cDisk.getRegion())
                    .eq(VmCloudDisk::getDiskId, f2cDisk.getDiskId());

            boolean exist = baseMapper.exists(queryWrapper);
            if (exist) {
                diskId = baseMapper.selectOne(queryWrapper).getId();
            }
            vmCloudDisk.setUpdateTime(LocalDateTime.now());
            vmCloudDisk.setId(diskId);
            vmCloudDisk.setAccountId(accountId);
            vmCloudDisk.setSourceId(sourceId);
            vmCloudDisk.setDiskChargeType(f2cDisk.getDiskChargeType());
            if (exist) {
                baseMapper.updateById(vmCloudDisk);
            } else {
                baseMapper.insert(vmCloudDisk);
            }
        }
    }

    /**
     * 获取云主机所属的组织或者工作空间 ID
     *
     * @param accountId
     * @param instanceUuid
     * @return
     */
    private String getSourceId(String accountId, String instanceUuid) {
        QueryWrapper<VmCloudServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VmCloudServer::getAccountId, accountId)
                .eq(VmCloudServer::getInstanceUuid, instanceUuid);
        VmCloudServer vmCloudServer = vmCloudServerService.getOne(queryWrapper);
        if (vmCloudServer != null) {
            return vmCloudServer.getSourceId();
        } else {
            return null;
        }
    }

    /**
     * 云磁盘授权
     *
     * @param grantRequest
     * @return
     */
    @Override
    public boolean grant(GrantRequest grantRequest) {
        //先过滤可操作的
        List<String> resources = this.listVmCloudDisk(new PageVmCloudDiskRequest().setIds(Arrays.asList(grantRequest.getIds()))).stream().map(VmCloudDiskDTO::getId).toList();
        if (CollectionUtils.isEmpty(resources)) {
            throw new RuntimeException("必须指定有效的磁盘ID");
        }

        String sourceId = grantRequest.getGrant() ? grantRequest.getSourceId() : (CurrentUserUtils.isAdmin() ? "" : CurrentUserUtils.getOrganizationId()); //组织管理员解除授权就放在自己当前的组织根目录

        if (grantRequest.getGrant() && CurrentUserUtils.isOrgAdmin()) {
            //判断授权的id是否有权限
            List<String> sourceIds = organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId());
            sourceIds.addAll(workspaceCommonService.getWorkspaceIdsByOrgIds(sourceIds));
            if (!sourceIds.contains(sourceId)) {
                throw new RuntimeException("没有权限授权到目标组织或工作空间");
            }
        }

        UpdateWrapper<VmCloudDisk> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(VmCloudDisk::getId, resources)
                .set(VmCloudDisk::getSourceId, sourceId);
        return update(updateWrapper);
    }

    @Override
    public boolean batchRecycleDisks(String[] ids) {
        for (String id : ids) {
            recycleDisk(id);
        }
        return true;
    }

    @Override
    public boolean recycleDisk(String id) {
        if (this.cloudDisk(id) == null) {
            throw new RuntimeException("找不到ID为[" + id + "]的资源或没有权限操作");
        }
        recycleService.insertRecycleRecord(id, ResourceTypeConstants.DISK);
        return true;
    }
}
