package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudImage;
import com.fit2cloud.base.mapper.BaseVmCloudImageMapper;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.controller.request.images.PageVmCloudImageRequest;
import com.fit2cloud.controller.request.images.VmCloudImageRequest;
import com.fit2cloud.dao.mapper.VmCloudImageMapper;
import com.fit2cloud.dto.VmCloudImageDTO;
import com.fit2cloud.service.IVmCloudImageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class VmCloudImageServiceImpl extends ServiceImpl<BaseVmCloudImageMapper, VmCloudImage> implements IVmCloudImageService {

    @Resource
    private VmCloudImageMapper imageMapper;

    @Override
    public IPage<VmCloudImageDTO> pageVmCloudImage(PageVmCloudImageRequest request) {
        // 构建查询参数
        QueryWrapper<VmCloudImageDTO> wrapper = addQuery(request);
        Page<VmCloudImageDTO> page = PageUtil.of(request, VmCloudImageDTO.class, new OrderItem(ColumnNameUtil.getColumnName(VmCloudImage::getCreateTime, true), false), true);
        return imageMapper.pageList(page, wrapper);
    }

    private QueryWrapper<VmCloudImageDTO> addQuery(PageVmCloudImageRequest request) {
        QueryWrapper<VmCloudImageDTO> wrapper = new QueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(request.getImageName()), ColumnNameUtil.getColumnName(VmCloudImage::getImageName, true), request.getImageName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()), ColumnNameUtil.getColumnName(CloudAccount::getName, true), request.getAccountName());
        return wrapper;
    }

    @Override
    public List<VmCloudImage> listVmCloudImage(String request) {
        return listVmCloudImage(JsonUtil.parseObject(request, VmCloudImageRequest.class));
    }

    public List<VmCloudImage> listVmCloudImage(VmCloudImageRequest request) {
        LambdaQueryWrapper<VmCloudImage> queryWrapper = new LambdaQueryWrapper<VmCloudImage>()
                .eq(StringUtils.isNotBlank(request.getAccountId()), VmCloudImage::getAccountId, request.getAccountId())
                .eq(StringUtils.isNotBlank(request.getRegion()), VmCloudImage::getRegion, request.getRegion())
                .eq(StringUtils.isNotBlank(request.getRegionId()), VmCloudImage::getRegion, request.getRegionId())
                .like(StringUtils.isNotBlank(request.getOs()), VmCloudImage::getOs, request.getOs())
                .ne(VmCloudImage::getStatus, "DELETED");
        queryWrapper.orderByAsc(VmCloudImage::getImageName);
        return list(queryWrapper);
    }

}
