package com.fit2cloud.service;

import com.fit2cloud.controller.request.optimize.strategy.OptimizeStrategyBaseRequest;

/**
 * @author jianneng
 * @date 2023/4/4 16:25
 **/
public interface IOptimizeSuggestStrategyService {

    /**
     * 根据策略类型获取优化策略
     *
     * @param optimizeSuggestCode 策略标识
     * @return 策略内容Map
     */
    String optimizeSuggestStrategy(String optimizeSuggestCode);

    boolean modify(OptimizeStrategyBaseRequest req);


}
