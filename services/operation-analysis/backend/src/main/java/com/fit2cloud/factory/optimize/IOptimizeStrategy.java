package com.fit2cloud.factory.optimize;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.optimize.PageOptimizeBaseRequest;
import com.fit2cloud.controller.request.optimize.strategy.OptimizeStrategyBaseRequest;
import com.fit2cloud.dto.AnalysisServerDTO;

/**
 * @author jianneng
 * @date 2023/4/7 10:13
 **/
public interface IOptimizeStrategy {

    /**
     * 执行策略查询
     */
    IPage<AnalysisServerDTO> execute(PageOptimizeBaseRequest request);

    /**
     * 策略默认参数
     */
    Object defaultStrategy(String optimizeSuggestCode);

    /**
     * 获取策略内容
     */
    String getStrategyContent(OptimizeStrategyBaseRequest req);

}
