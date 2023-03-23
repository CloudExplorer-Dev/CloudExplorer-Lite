package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.controller.request.disk.PageDiskRequest;
import com.fit2cloud.controller.request.disk.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dto.AnalysisDiskDTO;
import com.fit2cloud.dto.KeyValue;

import java.util.List;
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public interface IDiskAnalysisService {

    /**
     * 磁盘明细
     * @param request 分页查询磁盘参数
     * @return IPage<AnalysisDiskDTO>
     */
    IPage<AnalysisDiskDTO> pageDisk(PageDiskRequest request);

    /**
     * 所有云账号
     * @return List<CloudAccount>
     */
    List<CloudAccount> getAllCloudAccount();

    Map<String,List<KeyValue>> spread(ResourceAnalysisRequest request);

    List<ChartData> diskIncreaseTrend(ResourceAnalysisRequest request);

    Map<String,List<BarTreeChartData>> analysisCloudDiskByOrgWorkspace(ResourceAnalysisRequest request);

    long countDiskByCloudAccount(String cloudAccountId);

}
