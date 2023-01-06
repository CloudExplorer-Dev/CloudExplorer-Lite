package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.controller.request.CreateJobRecordRequest;
import com.fit2cloud.controller.request.ExecProviderMethodRequest;
import com.fit2cloud.controller.request.ResourceState;
import com.fit2cloud.controller.request.disk.CreateVmCloudDiskRequest;
import com.fit2cloud.controller.request.disk.ListVmRequest;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.dao.mapper.VmCloudDiskMapper;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.impl.vsphere.util.ResourceConstants;
import com.fit2cloud.service.IResourceOperateService;
import com.fit2cloud.service.IVmCloudDiskService;
import com.fit2cloud.service.OrganizationCommonService;
import com.fit2cloud.service.WorkspaceCommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private OrganizationCommonService organizationCommonService;
    @Resource
    private WorkspaceCommonService workspaceCommonService;
    @Resource
    private VmCloudDiskMapper diskMapper;
    @Resource
    private VmCloudServerMapper serverMapper;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private IResourceOperateService resourceOperateService;

    @Override
    public IPage<VmCloudDiskDTO> pageVmCloudDisk(PageVmCloudDiskRequest request) {
        // 普通用户
        if (CurrentUserUtils.isUser() && StringUtils.isNotBlank(CurrentUserUtils.getWorkspaceId())) {
            request.setSourceIds(Arrays.asList(new String[]{CurrentUserUtils.getWorkspaceId()}));
        }
        // 组织管理员
        if (CurrentUserUtils.isOrgAdmin()) {
            List orgWorkspaceList = new ArrayList();
            orgWorkspaceList.add(CurrentUserUtils.getOrganizationId());
            orgWorkspaceList.addAll(organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId()));
            orgWorkspaceList.addAll(workspaceCommonService.getWorkspaceIdsByOrgIds(orgWorkspaceList));
            request.setSourceIds(orgWorkspaceList);
        }
        Page<VmCloudDiskDTO> page = PageUtil.of(request, VmCloudDiskDTO.class, new OrderItem(ColumnNameUtil.getColumnName(VmCloudDiskDTO::getCreateTime, true), false), true);
        // 构建查询参数
        QueryWrapper<VmCloudDiskDTO> wrapper = addQuery(request);

        IPage<VmCloudDiskDTO> result = diskMapper.pageList(page, wrapper);
        result.getRecords().forEach(v -> {
            if (v.getBootable()) {
                v.setBootableText("系统盘");
            } else {
                v.setBootableText("数据盘");
            }
        });
        return result;
    }

    private QueryWrapper<VmCloudDiskDTO> addQuery(PageVmCloudDiskRequest request) {
        QueryWrapper<VmCloudDiskDTO> wrapper = new QueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()), ColumnNameUtil.getColumnName(VmCloudDisk::getSourceId, true), request.getWorkspaceId());
        wrapper.like(StringUtils.isNotBlank(request.getDiskName()), ColumnNameUtil.getColumnName(VmCloudDisk::getDiskName, true), request.getDiskName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()), ColumnNameUtil.getColumnName(CloudAccount::getName, true), request.getAccountName());
        wrapper.like(StringUtils.isNotBlank(request.getVmInstanceName()), ColumnNameUtil.getColumnName(VmCloudServer::getInstanceName, true), request.getVmInstanceName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getBootable()), ColumnNameUtil.getColumnName(VmCloudDisk::getBootable, true), request.getBootable());
        wrapper.in(CollectionUtils.isNotEmpty(request.getDiskType()), ColumnNameUtil.getColumnName(VmCloudDisk::getDiskType, true), request.getDiskType());
        wrapper.in(CollectionUtils.isNotEmpty(request.getDeleteWithInstance()), ColumnNameUtil.getColumnName(VmCloudDisk::getDeleteWithInstance, true), request.getDeleteWithInstance());
        wrapper.in(CollectionUtils.isNotEmpty(request.getStatus()), ColumnNameUtil.getColumnName(VmCloudDisk::getStatus, true), request.getStatus());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), ColumnNameUtil.getColumnName(VmCloudDisk::getSourceId, true), request.getSourceIds());

        // 默认不展示已删除状态的磁盘
        if (CollectionUtils.isEmpty(request.getStatus())) {
            wrapper.notIn(ColumnNameUtil.getColumnName(VmCloudDisk::getStatus, true), F2CDiskStatus.DELETED);
        }
        return wrapper;
    }

    public List<VmCloudServerDTO> cloudServerList(ListVmRequest req) {
        return serverMapper.selectListByAccountId(req.getAccountId(), req.getZone());
    }

    public VmCloudDiskDTO cloudDisk(String id) {
        return diskMapper.selectDiskDetailById(id);
    }

    public FormObject getCreateDiskForm(String platform) {
        Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(platform).getCloudProvider();
        try {
            return cloudProvider.getConstructor().newInstance().getCreateDiskForm();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get create disk form!" + e.getMessage(), e);
        }
    }

    @Override
    public boolean createDisk(CreateVmCloudDiskRequest request) {
        try {
            // 创建前:插入一条磁盘记录，状态为初始化
            String id = UUID.randomUUID().toString();
            VmCloudDisk initVmCloudDisk = request.toVmCloudDisk(id, F2CDiskStatus.INIT);
            baseMapper.insert(initVmCloudDisk);

            // 创建中实体状态
            VmCloudDisk creatingVmCloudDisk = request.toVmCloudDisk(id, F2CDiskStatus.CREATING);

            // 创建完成实体状态
            VmCloudDisk finishedVmCloudDisk = request.toVmCloudDisk(id, F2CDiskStatus.IN_USE);

            if (request.getInstanceUuid() != null) {
                request.setIsAttached(true);
            }

            CloudAccount cloudAccount = cloudAccountService.getById(request.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), request.getRegionId());
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
            }

            // 执行
            ResourceState<VmCloudDisk, F2CDisk> resourceState = ResourceState.<VmCloudDisk, F2CDisk>builder()
                    .beforeResource(initVmCloudDisk)
                    .processingResource(creatingVmCloudDisk)
                    .afterResource(finishedVmCloudDisk)
                    .updateResourceMethod(this::updateCloudDisk)
                    .deleteResourceMethod(this::deleteCloudDisk)
                    .saveResourceMethod(this::saveCloudDisk)
                    .build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder().execMethod(ICloudProvider::createDisk).methodParams(params).platform(platform).build();
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

    public boolean enlarge(String id, long newDiskSize) {
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);

            // 扩容前状态
            VmCloudDisk beforeAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeAttach);

            // 扩容中状态
            vmCloudDisk.setStatus(F2CDiskStatus.ENLARGING);
            VmCloudDisk processingAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingAttach);

            // 扩容后状态
            vmCloudDisk.setStatus(beforeAttach.getStatus());
            vmCloudDisk.setSize(newDiskSize);
            VmCloudDisk afterAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterAttach);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("newDiskSize", newDiskSize);
            params.put("instanceUuid", vmCloudDisk.getInstanceUuid());

            // 执行
            ResourceState<VmCloudDisk, Boolean> resourceState = ResourceState.<VmCloudDisk, Boolean>builder()
                    .beforeResource(beforeAttach)
                    .processingResource(processingAttach)
                    .afterResource(afterAttach)
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

    public boolean attach(String id, String instanceUuid, Boolean deleteWithInstance) {
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
            ResourceState<VmCloudDisk, Boolean> resourceState = ResourceState.<VmCloudDisk, Boolean>builder()
                    .beforeResource(beforeAttach)
                    .processingResource(processingAttach)
                    .afterResource(afterAttach)
                    .updateResourceMethod(this::updateCloudDisk)
                    .build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder()
                    .execMethod(ICloudProvider::attachDisk)
                    .methodParams(params)
                    .platform(platform)
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

    public boolean detach(String id) {
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

    public boolean delete(String id) {
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
            throw new RuntimeException("Failed to delete cloud disk!" + e.getMessage(), e);
        }
    }

    public boolean batchAttach(String[] ids, String instanceUuid, Boolean deleteWithInstance) {
        for (String id : ids) {
            attach(id, instanceUuid, deleteWithInstance);
        }
        return true;
    }

    public boolean batchDetach(String[] ids) {
        for (String id : ids) {
            detach(id);
        }
        return true;
    }

    public boolean batchDelete(String[] ids) {
        for (String id : ids) {
            delete(id);
        }
        return true;
    }

    /**
     * 更新磁盘 (磁盘挂载、卸载等操作命令执行后进行的操作)
     *
     * @param vmCloudDisk
     * @param
     */
    private void updateCloudDisk(VmCloudDisk vmCloudDisk) {
        baseMapper.updateById(vmCloudDisk);
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
        baseMapper.insert(vmCloudDisk);
    }
}
