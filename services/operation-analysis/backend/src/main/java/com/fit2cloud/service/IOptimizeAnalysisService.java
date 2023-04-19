package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.optimize.PageOptimizationRequest;
import com.fit2cloud.dto.AnalysisServerDTO;

/**
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public interface IOptimizeAnalysisService {

    /**
     * 云主机优化建议
     *
     * @param request 分页查询云主机参数
     * @return IPage<AnalysisServerDTO>
     */
    IPage<AnalysisServerDTO> pageServer(PageOptimizationRequest request);

}
