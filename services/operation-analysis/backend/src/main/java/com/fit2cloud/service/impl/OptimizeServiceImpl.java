package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.constants.AnalysisConstants;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.optimize.PageOptimizeBaseRequest;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.OptimizeSuggestDTO;
import com.fit2cloud.factory.optimize.IOptimizeStrategy;
import com.fit2cloud.factory.optimize.OptimizeFactory;
import com.fit2cloud.service.IOptimizeService;
import com.fit2cloud.service.IOptimizeSuggestStrategyService;
import com.fit2cloud.service.IPermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author jianneng
 * {@code @date} 2022/12/24 11:29
 **/
@Service
public class OptimizeServiceImpl implements IOptimizeService {

    @Resource
    private IPermissionService permissionService;
    @Resource
    private CurrentUserResourceService currentUserResourceService;
    @Resource
    private IOptimizeSuggestStrategyService iOptimizeSuggestStrategyService;

    /**
     * 查询符合优化策略的云主机数据
     *
     * @param request 分页查询云主机参数
     * @return 返回符合查询策略的云主机
     */
    @Override
    public IPage<AnalysisServerDTO> pageServer(PageOptimizeBaseRequest request) {
        request.setAccountIds(CollectionUtils.isNotEmpty(request.getAccountIds()) ? request.getAccountIds()
                : currentUserResourceService.currentUserCloudServerList().stream().map(VmCloudServer::getAccountId).distinct().toList());
        request.setSourceIds(permissionService.getSourceIds());
        IOptimizeStrategy strategyImpl = OptimizeFactory.getOptimizeApplyStrategy(request.getOptimizeSuggestCode());
        if (Objects.isNull(strategyImpl)) {
            throw new Fit2cloudException(ErrorCodeConstants.UNSUPPORTED_STRATEGY.getCode(), ErrorCodeConstants.UNSUPPORTED_STRATEGY.getMessage(new Object[]{request.getOptimizeSuggestCode()}));
        }
        String strategy = iOptimizeSuggestStrategyService.optimizeSuggestStrategy(request.getOptimizeSuggestCode());
        request.setStrategyContent(strategy);
        return strategyImpl.execute(request);
    }

    /**
     * 查询优化建议
     */
    @Override
    public List<OptimizeSuggestDTO> optimizeSuggests() {
        List<OptimizeSuggestDTO> result = new ArrayList<>();
        Stream.of(AnalysisConstants.OptimizeSuggestEnum.values()).forEach(item ->
                result.add(new OptimizeSuggestDTO(item.getName(), item.getIndex(), item.getOptimizeSuggestCode(), Integer.toUnsignedLong(item.getValue()), "")));
        return result;
    }

}
