package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDatastore;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceAnalysisRequest;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceUsedTrendRequest;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.response.ResourceAllocatedInfo;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.dto.AnalyticsDatastoreDTO;
import com.fit2cloud.dto.AnalyticsHostDTO;

import java.util.List;
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public interface IBaseResourceAnalysisService {

    /**
     * 宿主机明细
     * @param request
     * @return
     */
    IPage<AnalyticsHostDTO> pageHost(PageHostRequest request);
    /**
     * 存储器明细
     * @param request
     * @return
     */
    IPage<AnalyticsDatastoreDTO> pageDatastore(PageDatastoreRequest request);
    /**
     * 私有云VMWare vSphere、OpenStack
     * @return
     */
    List<CloudAccount> getAllPrivateCloudAccount();

    /**
     * 获取集群
     * @param request
     * @return
     */
    List<Map<String, String>> getCluster(ResourceAnalysisRequest request);
    /**
     * 获取宿主机
     * @param request
     * @return
     */
    List<VmCloudHost> getVmHost(ResourceAnalysisRequest request);

    /**
     * 获取存储器
     * @param request
     * @return
     */
    List<VmCloudDatastore> getVmCloudDatastore(ResourceAnalysisRequest request);

    /**
     * 资源分配情况
     * @param request
     * @return
     */
    Map<String, ResourceAllocatedInfo> getResourceAllocatedInfo(ResourceAnalysisRequest request);

    /**
     * 资源分布情况
     * @param request
     * @return
     */
    Map<String,List<KeyValue>> getResourceSpreadInfo(ResourceAnalysisRequest request);

    List<ChartData> getResourceUsedTrendData(ResourceUsedTrendRequest resourceUsedTrendRequest);

}
