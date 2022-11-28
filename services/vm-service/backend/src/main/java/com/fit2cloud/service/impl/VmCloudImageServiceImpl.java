package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.VmCloudImage;
import com.fit2cloud.base.mapper.BaseVmCloudImageMapper;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.controller.request.images.PageVmCloudImageRequest;
import com.fit2cloud.controller.request.images.VmCloudImageRequest;
import com.fit2cloud.dao.mapper.VmCloudImageMapper;
import com.fit2cloud.dto.VmCloudImageDTO;
import com.fit2cloud.provider.impl.huawei.entity.OsConfig;
import com.fit2cloud.provider.impl.huawei.entity.request.HuaweiBaseRequest;
import com.fit2cloud.service.IVmCloudImageService;
import com.fit2cloud.service.OrganizationCommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    private OrganizationCommonService organizationCommonService;
    @Resource
    private VmCloudImageMapper imageMapper;

    @Override
    public IPage<VmCloudImageDTO> pageVmCloudImage(PageVmCloudImageRequest request) {
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
        QueryWrapper<VmCloudImageDTO> wrapper = addQuery(request);
        Page<VmCloudImageDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudImageDTO> result = imageMapper.pageList(page, wrapper);
        return result;
    }

    private QueryWrapper<VmCloudImageDTO> addQuery(PageVmCloudImageRequest request) {
        QueryWrapper<VmCloudImageDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudImageDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_image.update_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()), "vm_cloud_image.workspace_id", request.getWorkspaceId());
        wrapper.like(StringUtils.isNotBlank(request.getImageName()), "vm_cloud_image.image_name", request.getImageName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()), "cloud_account.name", request.getAccountName());
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

        return list(queryWrapper);
    }

    /**
     * 返回操作系统版本以及对应镜像信息
     * 镜像取符合版本的第一个镜像
     * @param request
     * @return
     */
    public List<OsConfig> listOsVersion(String request) {
        HuaweiBaseRequest baseRequest = JsonUtil.parseObject(request, HuaweiBaseRequest.class);
        VmCloudImageRequest req = JsonUtil.parseObject(request, VmCloudImageRequest.class);
        req.setRegion(baseRequest.getRegionId());
        List<OsConfig> result = new ArrayList<>();
        if(StringUtils.isEmpty(req.getRegion()) || StringUtils.isEmpty(req.getOs())){
            return result;
        }
        List<VmCloudImage> imagesTmp = listVmCloudImage(req);
        //只取公共镜像
        // TODO 这个地方加上选择镜像名称跟版本一样的镜像，去除其他特殊镜像
        List<VmCloudImage> images = imagesTmp.stream()
                .filter(v->StringUtils.equalsIgnoreCase("gold",v.getImageType()))
                .filter(v->StringUtils.equalsIgnoreCase(v.getImageName(),v.getOs()))
                .collect(Collectors.toList());
        //操作系统去重复
        Map<String, VmCloudImage> osMap = images.stream().collect(Collectors.toMap(VmCloudImage::getOs,a->a,(k1,k2)->k1));
        //转换对象
        osMap.values().stream().forEach(v->{
            if(v.getOs().indexOf(req.getOs())>-1){
                OsConfig osConfig = new OsConfig();
                osConfig.setOs(req.getOs());
                osConfig.setOsVersion(v.getOs());
                osConfig.setImageName(v.getImageName());
                osConfig.setImageId(v.getImageId());
                osConfig.setImageMinDiskSize(v.getDiskSize());
                result.add(osConfig);
            }
        });
        return result.stream().sorted(Comparator.comparing(OsConfig::getOsVersion)).collect(Collectors.toList());
    }

    public List<Map<String, String>> listOs(String request) {
        List<Map<String, String>> result = new ArrayList<>();
        List<String> osList = Arrays.asList("Windows","RedHat","CentOS","SUSE","Debian","OpenSUSE","Oracle Linux","Fedora","Ubuntu","EulerOS","CoreOS","ESXi","Other","openEuler");
        osList.stream().sorted().forEach(v->{
            Map<String,String> m = new HashMap<>();
            m.put("id",v);
            m.put("name",v);
            result.add(m);
        });
        return result;
    }

}
