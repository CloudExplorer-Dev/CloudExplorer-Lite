package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.AnalysisConstants;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.optimize.strategy.OptimizeStrategyBaseRequest;
import com.fit2cloud.dao.entity.OptimizeSuggestStrategy;
import com.fit2cloud.dao.mapper.OptimizeSuggestStrategyMapper;
import com.fit2cloud.factory.optimize.IOptimizeStrategy;
import com.fit2cloud.factory.optimize.OptimizeFactory;
import com.fit2cloud.service.IOptimizeSuggestStrategyService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author jianneng
 * @date 2023/4/4 16:25
 **/
@Service
public class OptimizeSuggestStrategyServiceImpl implements IOptimizeSuggestStrategyService {

    @Resource
    private OptimizeSuggestStrategyMapper optimizeSuggestStrategyMapper;

    /**
     * 根据优化策略查询
     *
     * @param optimizeSuggestCode 策略标识
     */
    @Override
    public String optimizeSuggestStrategy(String optimizeSuggestCode) {
        IOptimizeStrategy strategy = getOptimizeStrategy(optimizeSuggestCode);
        OptimizeSuggestStrategy optimizeSuggestStrategy = getOneByType(optimizeSuggestCode);
        if (Objects.nonNull(optimizeSuggestStrategy)) {
            return optimizeSuggestStrategy.getStrategyContent();
        }
        try {
            return JsonUtil.toJSONString(strategy.defaultStrategy(optimizeSuggestCode));
        } catch (Throwable e) {
            throw new Fit2cloudException(ErrorCodeConstants.GET_OPTIMIZE_SUGGEST_STRATEGY_ERROR.getCode(), ErrorCodeConstants.GET_OPTIMIZE_SUGGEST_STRATEGY_ERROR.getMessage(new Object[]{e.getMessage()}));
        }
    }

    /**
     * 获取已支持的策略
     */
    private IOptimizeStrategy getOptimizeStrategy(String optimizeSuggestCode) {
        IOptimizeStrategy strategy = OptimizeFactory.getOptimizeApplyStrategy(optimizeSuggestCode);
        if (Objects.isNull(strategy)) {
            throw new Fit2cloudException(ErrorCodeConstants.UNSUPPORTED_STRATEGY.getCode(), ErrorCodeConstants.UNSUPPORTED_STRATEGY.getMessage(new Object[]{optimizeSuggestCode}));
        }
        return strategy;
    }

    /**
     * 编辑优化策略
     */
    @Override
    public boolean modify(OptimizeStrategyBaseRequest req) {
        IOptimizeStrategy strategyImpl = getOptimizeStrategy(req.getOptimizeSuggestCode());
        String strategyContent = strategyImpl.getStrategyContent(req);
        OptimizeSuggestStrategy optimizeSuggestStrategy = getOneByType(req.getOptimizeSuggestCode());
        if (Objects.isNull(optimizeSuggestStrategy)) {
            OptimizeSuggestStrategy add = new OptimizeSuggestStrategy();
            add.setStrategyType(AnalysisConstants.OptimizeSuggestEnum.byCode(req.getOptimizeSuggestCode()));
            add.setStrategyContent(strategyContent);
            add.setOperateUserId(CurrentUserUtils.getUser().getId());
            add.setCreateTime(LocalDateTime.now());
            add.setUpdateTime(LocalDateTime.now());
            optimizeSuggestStrategyMapper.insert(add);
        } else {
            optimizeSuggestStrategy.setUpdateTime(LocalDateTime.now());
            optimizeSuggestStrategy.setOperateUserId(CurrentUserUtils.getUser().getId());
            optimizeSuggestStrategy.setStrategyContent(strategyContent);
            optimizeSuggestStrategyMapper.updateById(optimizeSuggestStrategy);
        }
        return true;
    }

    public OptimizeSuggestStrategy getOneByType(String optimizeSuggestCode) {
        return optimizeSuggestStrategyMapper.selectOne(new LambdaQueryWrapper<OptimizeSuggestStrategy>().eq(true, OptimizeSuggestStrategy::getStrategyType, optimizeSuggestCode));
    }

}
