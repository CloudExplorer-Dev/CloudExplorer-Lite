package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.dto.optimization.VmCloudServerDTO;

import jakarta.servlet.http.HttpServletResponse;


/**
 * 描述：云主机优化服务
 *
 * @author jianneng
 */
public interface ICloudServerOptimizationService {


    /**
     * 根据优化策略规则分页查询云主机列表
     *
     * @param request 请求
     * @return {@link IPage }<{@link VmCloudServerDTO }>
     * @author jianneng
     */
    IPage<VmCloudServerDTO> pageList(PageServerRequest request);

    /**
     * 下载excel
     *
     * @param request  请求
     * @param version  版本
     * @param response 返回值
     */
    void downloadExcel(PageServerRequest request, String version, HttpServletResponse response);


}
