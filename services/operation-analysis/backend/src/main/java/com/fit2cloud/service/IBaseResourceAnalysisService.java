package com.fit2cloud.service;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDatastore;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceAnalysisRequest;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceUsedTrendRequest;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.response.ResourceAllocatedInfo;
import com.fit2cloud.dto.AnalysisDatastoreDTO;
import com.fit2cloud.dto.AnalysisHostDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public interface IBaseResourceAnalysisService {

    /**
     * 宿主机明细
     *
     * @param request 宿主机分页查询参数
     * @return IPage<AnalysisHostDTO>
     */
    IPage<AnalysisHostDTO> pageHost(PageHostRequest request);

    long countHost();

    long countHost(String cloudAccountId);

    /**
     * 存储器明细
     *
     * @param request 存储器分页查询参数
     * @return IPage<AnalysisDatastoreDTO>
     */
    IPage<AnalysisDatastoreDTO> pageDatastore(PageDatastoreRequest request);

    long countDatastore();

    long countDatastore(String cloudAccountId);

    /**
     * 私有云VMWare vSphere、OpenStack
     *
     * @return List<CloudAccount>
     */
    List<CloudAccount> getAllPrivateCloudAccount();

    /**
     * 获取集群
     *
     * @param request 资源分析参数
     * @return List<Map < String, String>>
     */
    List<Map<String, String>> getCluster(ResourceAnalysisRequest request);

    /**
     * 获取宿主机
     *
     * @param request 资源分析参数
     * @return List<VmCloudHost>
     */
    List<VmCloudHost> getVmHost(ResourceAnalysisRequest request);

    /**
     * 获取存储器
     *
     * @param request 资源分析参数
     * @return List<VmCloudDatastore>
     */
    List<VmCloudDatastore> getVmCloudDatastore(ResourceAnalysisRequest request);

    /**
     * 资源分配情况
     *
     * @param request 资源分析参数
     * @return Map<String, ResourceAllocatedInfo>
     */
    Map<String, ResourceAllocatedInfo> getResourceAllocatedInfo(ResourceAnalysisRequest request);

    /**
     * 资源分布情况
     *
     * @param request 资源分析参数
     * @return Map<String, List < KeyValue>>
     */
    Map<String, List<KeyValue>> getResourceSpreadInfo(ResourceAnalysisRequest request);

    List<ChartData> getResourceUsedTrendData(ResourceUsedTrendRequest resourceUsedTrendRequest);

    List<ChartData> convertToTrendData(SearchHits<PerfMetricMonitorData> response, CalendarInterval intervalUnit);

    Query getRangeQuery(List<QueryUtil.QueryCondition> queryConditions, CalendarInterval intervalPosition);

}
