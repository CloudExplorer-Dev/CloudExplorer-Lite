package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.base.mapper.BaseRecycleBinMapper;
import com.fit2cloud.base.service.IBaseRecycleBinService;
import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.fit2cloud.common.constants.ResourceTypeConstants;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.controller.request.vm.BatchRecycleRequest;
import com.fit2cloud.controller.request.vm.PageRecycleBinRequest;
import com.fit2cloud.dao.mapper.RecycleBinMapper;
import com.fit2cloud.dto.RecycleBinDTO;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : LiuDi
 * @date : 2023/1/12 14:48
 */
@Service
public class RecycleBinServiceImpl extends ServiceImpl<BaseRecycleBinMapper, RecycleBin> implements IRecycleBinService {
    @Resource
    private RecycleBinMapper recycleBinMapper;
    @Resource
    private IPermissionService permissionService;

    @Resource
    private IVmCloudServerService iVmCloudServerService;

    @Resource
    private IVmCloudDiskService diskService;

    @Resource
    private IBaseRecycleBinService baseRecycleService;

    @Resource
    private IBaseSystemParameterService baseSystemParameterService;

    @Override
    public IPage<RecycleBinDTO> pageRecycleBin(PageRecycleBinRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        Page<RecycleBinDTO> page = PageUtil.of(request, RecycleBinDTO.class, new OrderItem(ColumnNameUtil.getColumnName(RecycleBinDTO::getCreateTime, false), false), true);
        // 构建查询参数
        QueryWrapper<RecycleBinDTO> wrapper = addQuery(request);
        return recycleBinMapper.pageRecycleBin(page, wrapper);
    }

    @Override
    public boolean batchDeleteResource(BatchRecycleRequest request) {
        List<String> recycleIds = request.getRecycleIds();
        if (CollectionUtils.isNotEmpty(recycleIds)) {
            throw new RuntimeException("批量回收ID不能为空");
        }
        for (String recycleId : recycleIds) {
            this.deleteResource(recycleId);
        }
        return true;
    }

    @Override
    public boolean deleteResource(String recycleId) {
        if (StringUtils.isEmpty(recycleId)) {
            throw new RuntimeException("回收ID不能为空");
        }
        RecycleBin recycleBin = baseMapper.selectById(recycleId);
        if (recycleBin != null && StringUtils.isNotEmpty(recycleBin.getResourceId())) {
            ResourceTypeConstants resourceType = recycleBin.getResourceType();
            // 删除虚拟机
            if (resourceType.equals(ResourceTypeConstants.VM)) {
                iVmCloudServerService.deleteInstance(recycleBin.getResourceId());
            }
            // 删除磁盘
            if (resourceType.equals(ResourceTypeConstants.DISK)) {
                VmCloudDiskDTO diskInfo = diskService.cloudDisk(recycleBin.getResourceId());
                if (diskInfo != null && StringUtils.isEmpty(diskInfo.getInstanceUuid())) {
                    diskService.delete(recycleBin.getResourceId());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean batchRecoverResource(BatchRecycleRequest request) {
        if (CollectionUtils.isEmpty(request.getRecycleIds())) {
            throw new RuntimeException("批量恢复资源ID不能为空");
        }
        for (String recycleId : request.getRecycleIds()) {
            baseRecycleService.updateRecycleRecordOnRecover(recycleId);
        }
        return true;
    }

    @Override
    public boolean recoverResource(String recycleId) {
        if (StringUtils.isEmpty(recycleId)) {
            throw new RuntimeException("回收ID不能为空");
        }
        return baseRecycleService.updateRecycleRecordOnRecover(recycleId);
    }

    private QueryWrapper<RecycleBinDTO> addQuery(PageRecycleBinRequest request) {
        QueryWrapper<RecycleBinDTO> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getResourceName()), "recycleInfo.resource_name", request.getResourceName());
        return wrapper;
    }

    public boolean getRecycleEnableStatus() {
        String recycleEnableStatus = baseSystemParameterService.getValue("recycle_bin.enable");
        return "true".equals(recycleEnableStatus) ? true : false;
    }

}
