package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ServerRequest;
import com.fit2cloud.dao.entity.OptimizationStrategy;
import com.fit2cloud.dao.entity.OptimizationStrategyIgnoreResource;
import com.fit2cloud.dao.mapper.OptimizationStrategyIgnoreResourceMapper;
import com.fit2cloud.dao.mapper.OptimizationStrategyMapper;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.optimization.VmCloudServerDTO;
import com.fit2cloud.service.IOptimizationStrategyIgnoreResourceService;
import com.fit2cloud.service.IPermissionService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 优化策略忽略资源 服务实现类
 * </p>
 *
 * @author fit2cloud
 */
@Service
public class OptimizationStrategyIgnoreResourceServiceImpl extends ServiceImpl<OptimizationStrategyIgnoreResourceMapper, OptimizationStrategyIgnoreResource> implements IOptimizationStrategyIgnoreResourceService {

    @Resource
    private OptimizationStrategyIgnoreResourceMapper optimizationStrategyIgnoreResourceMapper;
    @Resource
    private OptimizationStrategyMapper optimizationStrategyMapper;
    @Resource
    private BaseVmCloudServerMapper vmCloudServerMapper;
    @Resource
    private IPermissionService permissionService;

    /**
     * 批量插入忽视资源通过优化策略id
     *
     * @param optimizationStrategyId 优化策略id
     * @param resourceIdList         资源id列表
     */
    @Override
    public boolean batchInsertIgnoreResourceByOptimizationStrategyId(String optimizationStrategyId, List<String> resourceIdList) {
        OptimizationStrategy optimizationStrategy = optimizationStrategyMapper.selectById(optimizationStrategyId);
        if (Objects.isNull(optimizationStrategy)) {
            throw new Fit2cloudException(ErrorCodeConstants.NOT_EXISTS_OPTIMIZE_SUGGEST_STRATEGY.getCode(), ErrorCodeConstants.NOT_EXISTS_OPTIMIZE_SUGGEST_STRATEGY.getMessage());
        }
        batchRemoveOptimizationStrategyIgnoreResource(optimizationStrategyId, resourceIdList);
        List<OptimizationStrategyIgnoreResource> insertList = resourceIdList.stream().map(resourceId -> new OptimizationStrategyIgnoreResource(UUID.randomUUID().toString().replaceAll("-", ""), optimizationStrategyId, resourceId)).collect(Collectors.toList());
        saveBatch(insertList);
        return true;
    }

    /**
     * 批量删除优化策略忽略资源
     *
     * @param optimizationStrategyId 优化策略id
     * @param resourceIdList         资源id列表
     */
    @Override
    public boolean batchRemoveOptimizationStrategyIgnoreResource(String optimizationStrategyId, List<String> resourceIdList) {
        if (CollectionUtils.isEmpty(resourceIdList)) {
            throw new Fit2cloudException(ErrorCodeConstants.RESOURCE_REQUIRED.getCode(), ErrorCodeConstants.RESOURCE_REQUIRED.getMessage());
        }
        LambdaQueryWrapper<OptimizationStrategyIgnoreResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(true, OptimizationStrategyIgnoreResource::getOptimizationStrategyId, optimizationStrategyId);
        wrapper.in(true, OptimizationStrategyIgnoreResource::getResourceId, resourceIdList);
        List<OptimizationStrategyIgnoreResource> ignoreResourceList = optimizationStrategyIgnoreResourceMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(ignoreResourceList)) {
            optimizationStrategyIgnoreResourceMapper.deleteBatchIds(ignoreResourceList.stream().map(OptimizationStrategyIgnoreResource::getId).toList());
        }
        return true;
    }

    /**
     * 删除忽略资源通过优化策略id
     *
     * @param optimizationStrategyId 优化策略id
     */
    @Override
    public boolean deleteIgnoreResourceByOptimizationStrategyId(String optimizationStrategyId) {
        try {
            MPJLambdaWrapper<OptimizationStrategyIgnoreResource> wrapper = new MPJLambdaWrapper<>();
            wrapper.eq(true, OptimizationStrategyIgnoreResource::getOptimizationStrategyId, optimizationStrategyId);
            List<OptimizationStrategyIgnoreResource> strategyIgnoreResource = optimizationStrategyIgnoreResourceMapper.selectList(wrapper);
            if (Objects.nonNull(strategyIgnoreResource)) {
                optimizationStrategyIgnoreResourceMapper.deleteBatchIds(strategyIgnoreResource.stream().map(OptimizationStrategyIgnoreResource::getId).toList());
            }
            return true;
        } catch (Exception e) {
            LogUtil.error(ErrorCodeConstants.DELETE_OPTIMIZE_STRATEGY_IGNORE_ERROR.getMessage(new String[]{e.getMessage()}));
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 该方法用于资源优化策略优化资源类型为云主机的优化范围配置查询
     * 分页获取优化策略云主机列表
     * 获取用户权限下的云主机
     * 排出已删除与错误的云主机
     * 查询条件包含优化策略ID时，根据是否是查询忽略资源，查询忽略资源，或者排除忽略资源
     *
     * @param request 请求
     * @return {@link IPage }<{@link VmCloudServerDTO }>
     */
    @Override
    public IPage<VmCloudServerDTO> pageVmCloudServerList(PageServerRequest request) {
        if (Objects.isNull(request.getOptimizationStrategyId()) && request.isIgnore()) {
            return new Page<>();
        }
        Page<VmCloudServerDTO> page = PageUtil.of(request, VmCloudServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(VmCloudServer.class);
        wrapper.selectAs(CloudAccount::getName, AnalysisServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisServerDTO::getPlatform);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, VmCloudServer::getAccountId);
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE, SpecialAttributesConstants.StatusField.FAILED));
        wrapper.like(StringUtils.isNotBlank(request.getIpArray()), VmCloudServer::getIpArray, request.getIpArray());
        wrapper.like(StringUtils.isNotBlank(request.getInstanceName()), VmCloudServer::getInstanceName, request.getInstanceName());
        if (Objects.nonNull(request.getOptimizationStrategyId())) {
            List<String> ignoreResourceIdList = getOptimizationStrategyIgnoreResourceIdList(request.getOptimizationStrategyId());
            if (request.isIgnore()) {
                if (CollectionUtils.isEmpty(ignoreResourceIdList)) {
                    return new Page<>();
                }
                wrapper.in(CollectionUtils.isNotEmpty(ignoreResourceIdList), VmCloudServer::getId, ignoreResourceIdList);
            } else {
                wrapper.notIn(CollectionUtils.isNotEmpty(ignoreResourceIdList), VmCloudServer::getId, ignoreResourceIdList);
            }
        }
        return vmCloudServerMapper.selectJoinPage(page, VmCloudServerDTO.class, wrapper);
    }

    @Override
    public List<VmCloudServerDTO> vmCloudServerList(ServerRequest request) {
        if (Objects.isNull(request.getOptimizationStrategyId()) && request.isIgnore()) {
            return new ArrayList<>();
        }
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(VmCloudServer.class);
        wrapper.selectAs(CloudAccount::getName, AnalysisServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisServerDTO::getPlatform);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, VmCloudServer::getAccountId);
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE, SpecialAttributesConstants.StatusField.FAILED));
        wrapper.like(StringUtils.isNotBlank(request.getIpArray()), VmCloudServer::getIpArray, request.getIpArray());
        if (Objects.nonNull(request.getOptimizationStrategyId())) {
            List<String> ignoreResourceIdList = getOptimizationStrategyIgnoreResourceIdList(request.getOptimizationStrategyId());
            if (request.isIgnore()) {
                if (CollectionUtils.isEmpty(ignoreResourceIdList)) {
                    return new ArrayList<>();
                }
                wrapper.in(CollectionUtils.isNotEmpty(ignoreResourceIdList), VmCloudServer::getId, ignoreResourceIdList);
            } else {
                wrapper.notIn(CollectionUtils.isNotEmpty(ignoreResourceIdList), VmCloudServer::getId, ignoreResourceIdList);
            }
        }
        return vmCloudServerMapper.selectJoinList(VmCloudServerDTO.class, wrapper);
    }


    /**
     * 得到优化策略忽略资源id列表
     *
     * @param optimizationStrategyId 优化策略id
     * @return {@link List }<{@link String }>
     */
    @Override
    public List<String> getOptimizationStrategyIgnoreResourceIdList(String optimizationStrategyId) {
        MPJLambdaWrapper<OptimizationStrategyIgnoreResource> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(true, OptimizationStrategyIgnoreResource::getOptimizationStrategyId, optimizationStrategyId);
        return optimizationStrategyIgnoreResourceMapper.selectList(wrapper).stream().map(OptimizationStrategyIgnoreResource::getResourceId).toList();
    }


}
