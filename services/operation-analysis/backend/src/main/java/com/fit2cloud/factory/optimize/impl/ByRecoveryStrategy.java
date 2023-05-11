package com.fit2cloud.factory.optimize.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseRecycleBinMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.constants.RecycleBinStatusConstants;
import com.fit2cloud.common.constants.ResourceTypeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.constants.OptimizationConstants;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.controller.request.optimize.PageOptimizeBaseRequest;
import com.fit2cloud.controller.request.optimize.strategy.BaseStatusRequest;
import com.fit2cloud.controller.request.optimize.strategy.ByResourceStatusRequest;
import com.fit2cloud.controller.request.optimize.strategy.OptimizeStrategyBaseRequest;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.factory.optimize.IOptimizeStrategy;
import com.fit2cloud.service.IPermissionService;
import com.fit2cloud.service.IServerAnalysisService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 建议回收策略查询
 *
 * @author jianneng
 * @date 2023/4/7 10:17
 **/
@Component
public class ByRecoveryStrategy implements IOptimizeStrategy {
    @Resource
    private IPermissionService permissionService;
    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;
    @Resource
    private BaseRecycleBinMapper baseRecycleBinMapper;
    @Resource
    private IServerAnalysisService iServerAnalysisService;


    @Override
    public IPage<AnalysisServerDTO> execute(PageOptimizeBaseRequest request) {
        return getRecover(request);
    }

    @Override
    public Object defaultStrategy(String optimizeSuggestCode) {
        ByResourceStatusRequest strategy = new ByResourceStatusRequest();
        BaseStatusRequest recovery = new BaseStatusRequest();
        recovery.setContinuedDays(30L);
        recovery.setContinuedRunning(false);
        strategy.setRecovery(recovery);
        return strategy;
    }

    private IPage<AnalysisServerDTO> getRecover(PageOptimizeBaseRequest request) {
        ByResourceStatusRequest baseStatusRequest = JsonUtil.parseObject(request.getStrategyContent(), ByResourceStatusRequest.class);
        BaseStatusRequest recoveryReq = baseStatusRequest.getRecovery();
        QueryWrapper<RecycleBin> recycleQuery = new QueryWrapper<>();
        recycleQuery.eq(true, "recycle_bin.resource_type", ResourceTypeConstants.VM.name());
        recycleQuery.eq(true, "recycle_bin.status", RecycleBinStatusConstants.ToBeRecycled.name());
        // 回收站的
        List<RecycleBin> recycleBins = baseRecycleBinMapper.selectList(recycleQuery);
        // 回收站的资源ID
        List<String> temp = recycleBins.stream().map(RecycleBin::getResourceId).toList();
        List<String> recycleBinServerIds = new ArrayList<>();
        List<VmCloudServer> recycleBinServer = new ArrayList<>();
        // 回收站资源
        if (CollectionUtils.isNotEmpty(temp)) {
            recycleBinServer = baseVmCloudServerMapper.selectBatchIds(temp);
        }
        if (CollectionUtils.isNotEmpty(request.getAccountIds())) {
            recycleBinServer = recycleBinServer.stream().filter(v -> request.getAccountIds().contains(v.getAccountId())).toList();
        }
        if (StringUtils.isNotEmpty(request.getInstanceName())) {
            recycleBinServer = recycleBinServer.stream().filter(v -> v.getInstanceName().contains(request.getInstanceName())).toList();
        }
        if (CollectionUtils.isNotEmpty(recycleBinServer)) {
            recycleBinServerIds.addAll(recycleBinServer.stream().filter(this::filterRecycleBinServer).filter(v -> !StringUtils.equalsIgnoreCase("Deleted", v.getInstanceStatus())).map(VmCloudServer::getId).toList());
        }
        OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggestCode());
        Page<AnalysisServerDTO> page = PageUtil.of(request, AnalysisServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        queryServerWrapper.eq(true, VmCloudServer::getInstanceStatus, SpecialAttributesConstants.StatusField.STOPPED);
        queryServerWrapper.le(VmCloudServer::getLastOperateTime, LocalDateTime.now().minusDays(recoveryReq.getContinuedDays()));
        if (CollectionUtils.isNotEmpty(recycleBinServerIds)) {
            queryServerWrapper.or(or -> or.in(true, VmCloudServer::getId, recycleBinServerIds));
        }
        IPage<AnalysisServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page, AnalysisServerDTO.class, queryServerWrapper);
        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            pageData.getRecords().forEach(vm -> {
                        assert optimizationConstants != null;
                        vm.setOptimizeSuggest(optimizationConstants.getName());
                        vm.setOptimizeSuggestCode(optimizationConstants.getCode());
                        if (recycleBinServerIds.contains(vm.getId())) {
                            vm.setContent("回收站");
                        } else {
                            vm.setContent("持续关机" + recoveryReq.getContinuedDays() + "天以上，闲置机器建议回收删除");
                        }
                    }
            );
            iServerAnalysisService.getVmPerfMetric(pageData.getRecords());
            return pageData;
        }
        return new Page<>();
    }

    /**
     * 过滤回收站云主机
     * 是否拥有资源相同目录权限
     * 自己的资源
     * 管理员
     */
    private boolean filterRecycleBinServer(VmCloudServer server) {
        List<String> sourceIds = permissionService.getSourceIds();
        //拥有权限
        boolean source = sourceIds.contains(server.getSourceId());
        //自己的机器
        boolean self = StringUtils.equalsIgnoreCase(server.getApplyUser(), CurrentUserUtils.getUser().getId());
        //管理员
        boolean isAdmin = CurrentUserUtils.isAdmin();
        return source || self || isAdmin;
    }

    /**
     * 云主机分析参数
     */
    private MPJLambdaWrapper<VmCloudServer> addServerAnalysisQuery(PageOptimizeBaseRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(VmCloudServer.class);
        wrapper.selectAs(CloudAccount::getName, AnalysisServerDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform, AnalysisServerDTO::getPlatform);
        wrapper.leftJoin(CloudAccount.class, CloudAccount::getId, VmCloudServer::getAccountId);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of("Deleted", "Failed"));
        wrapper.like(StringUtils.isNotBlank(request.getInstanceName()), VmCloudServer::getInstanceName, request.getInstanceName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudServer::getSourceId, request.getSourceIds());
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), VmCloudServer::getAccountId, request.getAccountIds());
        wrapper.orderByDesc(VmCloudServer::getCreateTime);
        return wrapper;
    }

    @Override
    public String getStrategyContent(OptimizeStrategyBaseRequest req) {
        if (Objects.isNull(req.getStatusRequest())) {
            throw new Fit2cloudException(ErrorCodeConstants.INVALID_PARAMETER.getCode(), ErrorCodeConstants.INVALID_PARAMETER.getMessage());
        }
        return JsonUtil.toJSONString(req.getStatusRequest());
    }


}
