package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.optimize.PageOptimizeBaseRequest;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.OptimizeSuggestDTO;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public interface IOptimizeService {

    /**
     * 云主机优化建议
     *
     * @param request 分页查询云主机参数
     * @return IPage<AnalysisServerDTO>
     */
    IPage<AnalysisServerDTO> pageServer(PageOptimizeBaseRequest request);

    /**
     * 优化建议
     */
    List<OptimizeSuggestDTO> optimizeSuggests();


}
