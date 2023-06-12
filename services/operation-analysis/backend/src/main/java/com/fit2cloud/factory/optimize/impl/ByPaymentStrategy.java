package com.fit2cloud.factory.optimize.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.exception.Fit2cloudException;
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

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 建议变更付费方式策略查询
 *
 * @author jianneng
 * @date 2023/4/7 10:17
 **/
@Component
public class ByPaymentStrategy implements IOptimizeStrategy {
    @Resource
    private IPermissionService permissionService;
    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;
    @Resource
    private IServerAnalysisService iServerAnalysisService;


    @Override
    public IPage<AnalysisServerDTO> execute(PageOptimizeBaseRequest request) {
        return getPayment(request);
    }

    @Override
    public Object defaultStrategy(String optimizeSuggestCode) {
        ByResourceStatusRequest strategy = new ByResourceStatusRequest();
        BaseStatusRequest cycle = new BaseStatusRequest();
        cycle.setContinuedDays(10L);
        cycle.setContinuedRunning(false);
        strategy.setCycle(cycle);
        BaseStatusRequest volume = new BaseStatusRequest();
        volume.setContinuedDays(10L);
        volume.setContinuedRunning(true);
        strategy.setVolume(volume);
        return strategy;
    }

    private IPage<AnalysisServerDTO> getPayment(PageOptimizeBaseRequest request) {
        ByResourceStatusRequest baseStatusRequest = JsonUtil.parseObject(request.getStrategyContent(), ByResourceStatusRequest.class);
        BaseStatusRequest cycleRequest = baseStatusRequest.getCycle();
        BaseStatusRequest volumeRequest = baseStatusRequest.getVolume();
        OptimizationConstants optimizationConstants = OptimizationConstants.getByCode(request.getOptimizeSuggestCode());
        Page<AnalysisServerDTO> page = PageUtil.of(request, AnalysisServerDTO.class, null, true);
        MPJLambdaWrapper<VmCloudServer> queryServerWrapper = addServerAnalysisQuery(request);
        // 按量付费资源持续开机时间超过day天建议变更付费方式为包年包月的云主机
        queryServerWrapper.and(a1 ->
                a1.and(a2 -> a2.eq(true, VmCloudServer::getInstanceStatus, "Running")
                                .eq(true, VmCloudServer::getInstanceChargeType, "PostPaid")
                                .le(AnalysisServerDTO::getLastOperateTime, LocalDateTime.now().minusDays(volumeRequest.getContinuedDays())))
                        .or(a -> {
                            // 包年包月资源持续关机时间超过day天建议变更付费方式为按量付费的云主机
                            a.eq(true, VmCloudServer::getInstanceStatus, SpecialAttributesConstants.StatusField.STOPPED);
                            a.eq(true, VmCloudServer::getInstanceChargeType, "PrePaid");
                            a.le(VmCloudServer::getLastOperateTime, LocalDateTime.now().minusDays(cycleRequest.getContinuedDays()));
                        })
        );
        IPage<AnalysisServerDTO> pageData = baseVmCloudServerMapper.selectJoinPage(page, AnalysisServerDTO.class, queryServerWrapper);
        // 根据状态判断变更方式
        if (CollectionUtils.isNotEmpty(pageData.getRecords())) {
            pageData.getRecords().forEach(vm -> {
                assert optimizationConstants != null;
                vm.setOptimizeSuggest(optimizationConstants.getName());
                vm.setOptimizeSuggestCode(optimizationConstants.getCode());
                StringJoiner sj = new StringJoiner("");
                if (StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), SpecialAttributesConstants.StatusField.VM_RUNNING)) {
                    sj.add("持续开机").add(String.valueOf(volumeRequest.getContinuedDays())).add("天以上，建议转为包年包月");
                }
                if (StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), SpecialAttributesConstants.StatusField.STOPPED)) {
                    sj.add("持续关机").add(String.valueOf(cycleRequest.getContinuedDays())).add("天以上，建议转为按需按量");
                }
                vm.setContent(sj.toString());
            });
            iServerAnalysisService.getVmPerfMetric(pageData.getRecords());
            return pageData;
        }
        return new Page<>();
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
