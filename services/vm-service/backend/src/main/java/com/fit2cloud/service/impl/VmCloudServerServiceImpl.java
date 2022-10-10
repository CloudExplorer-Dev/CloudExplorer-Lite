package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.vm.BatchOperateVmRequest;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.util.CommonUtil;
import com.fit2cloud.service.IVmCloudServerService;
import com.fit2cloud.service.OrganizationCommonService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

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
        return operateInstance(vmId, OperatedTypeEnum.POWER_OFF);
    }

    @Override
    public boolean powerOn(String vmId) {
        return operateInstance(vmId, OperatedTypeEnum.POWER_ON);
    }

    @Override
    public boolean shutdownInstance(String vmId) {
        return operateInstance(vmId, OperatedTypeEnum.SHUTDOWN);
    }

    @Override
    public boolean rebootInstance(String vmId) {
        return operateInstance(vmId, OperatedTypeEnum.REBOOT);
    }

    @Override
    public boolean deleteInstance(String vmId) {
        return operateInstance(vmId, OperatedTypeEnum.DELETE);
    }

    @Override
    public boolean batchOperate(BatchOperateVmRequest request) {
        if (request.getInstanceIds().size() == 0) {
            throw new Fit2cloudException(ErrorCodeConstants.SELECT_AT_LEAST_ONE_VM.getCode(), ErrorCodeConstants.SELECT_AT_LEAST_ONE_VM.getMessage());
        }
        LogUtil.debug("批量操作虚拟机:{}", request.getInstanceIds().size());
        AtomicReference<Long> doneCount = new AtomicReference<>(0L);
        request.getInstanceIds().stream().forEach(instanceId -> {
            try {
                operateInstance(instanceId, OperatedTypeEnum.valueOf(request.getOperate()));
                doneCount.getAndSet(doneCount.get() + 1);
            } catch (Exception e) {
                LogUtil.error("error - {} - {}", instanceId, e.getMessage());
                throw e;
            }
        });
        LogUtil.debug("批量操作虚拟机成功:{}", doneCount);
        return false;
    }

    private boolean operateInstance(String vmId, OperatedTypeEnum operatedType) {
        boolean result = false;
        QueryWrapper<VmCloudServer> wrapper = new QueryWrapper<VmCloudServer>()
                .eq(ColumnNameUtil.getColumnName(VmCloudServer::getId, true), vmId);
        VmCloudServer vmCloudServer = baseMapper.selectOne(wrapper);
        if (vmCloudServer == null) {
            throw new Fit2cloudException(ErrorCodeConstants.VM_NOT_EXIST.getCode(), ErrorCodeConstants.VM_NOT_EXIST.getMessage());
        }
        CloudAccount cloudAccount = cloudAccountService.getById(vmCloudServer.getAccountId());
        F2CInstanceStatus resultStatus = F2CInstanceStatus.valueOf(vmCloudServer.getInstanceStatus());
        Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
        try {
            HashMap<String, String> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudServer.getRegion());
            params.put("uuid", vmCloudServer.getInstanceUuid());
            switch (operatedType) {
                case POWER_ON:
                    result = CommonUtil.exec(cloudProvider, JsonUtil.toJSONString(params), ICloudProvider::powerOn);
                    resultStatus = F2CInstanceStatus.Running;
                    break;
                case POWER_OFF:
                    result = CommonUtil.exec(cloudProvider, JsonUtil.toJSONString(params), ICloudProvider::powerOff);
                    resultStatus = F2CInstanceStatus.Stopped;
                    break;
                case SHUTDOWN:
                    result = CommonUtil.exec(cloudProvider, JsonUtil.toJSONString(params), ICloudProvider::shutdownInstance);
                    resultStatus = F2CInstanceStatus.Stopped;
                    break;
                case REBOOT:
                    result = CommonUtil.exec(cloudProvider, JsonUtil.toJSONString(params), ICloudProvider::rebootInstance);
                    resultStatus = F2CInstanceStatus.Running;
                    break;
                case DELETE:
                    result = CommonUtil.exec(cloudProvider, JsonUtil.toJSONString(params), ICloudProvider::deleteInstance);
                    resultStatus = F2CInstanceStatus.Deleted;
                    break;
                default:
            }
            if (result) {
                vmCloudServer.setInstanceStatus(resultStatus.name());
                baseMapper.update(vmCloudServer, wrapper);
                return true;
            }
        } catch (Exception e) {
            LogUtil.error("ERROR CODE:{}-{}", ErrorCodeConstants.VM_OPERATE_FAIL.getCode(), e.getMessage());
            throw new Fit2cloudException(ErrorCodeConstants.VM_OPERATE_FAIL.getCode(), e.getMessage());
        }
        return false;
    }

}
