package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dto.AnalyticsServerDTO;
import com.fit2cloud.dto.KeyValue;

import java.util.List;
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public interface IServerAnalysisService {

    /**
     * 云主机明细
     * @param request
     * @return
     */
    IPage<AnalyticsServerDTO> pageServer(PageServerRequest request);

    /**
     * 所有云账号
     * @return
     */
    List<CloudAccount> getAllCloudAccount();

    /**
     * 获取宿主机
     * @param request
     * @return
     */
    List<VmCloudHost> getVmHost(ResourceAnalysisRequest request);

    Map<String,List<KeyValue>> spread(ResourceAnalysisRequest request);

    List<ChartData> vmIncreaseTrend(ResourceAnalysisRequest request);

    List<ChartData> getResourceTrendData(ResourceAnalysisRequest request);

    Map<String,List<BarTreeChartData>> analyticsVmCloudServerByOrgWorkspace(ResourceAnalysisRequest request);


}
