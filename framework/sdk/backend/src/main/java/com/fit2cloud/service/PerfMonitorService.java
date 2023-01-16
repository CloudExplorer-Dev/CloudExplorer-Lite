package com.fit2cloud.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.dto.PerfMonitorEchartsDTO;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.request.PerfMonitorRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jianneng
 **/
@Service
@Lazy
public class PerfMonitorService {
    @Resource
    private ElasticsearchProvide elasticsearchProvide;

    public Map<String,List<PerfMonitorEchartsDTO>> getPerfMonitorData(PerfMonitorRequest request) {
        Map<String,List<PerfMonitorEchartsDTO>> resultMap = new HashMap<>();
        List<PerfMetricMonitorData> tmpList = elasticsearchProvide.searchByQuery(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode(), getSearchQuery(request), PerfMetricMonitorData.class);
        if (tmpList.size() > 0) {
            List<PerfMetricMonitorData> list = tmpList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(PerfMetricMonitorData::getId))), ArrayList::new));//过滤重复ID
            if(StringUtils.equalsIgnoreCase(request.getMetricName(),"DISK_USED_UTILIZATION")){
                Map<String,List<PerfMetricMonitorData>> diskMap = list.stream().filter(v->StringUtils.isNotEmpty(v.getDevice())).collect(Collectors.groupingBy(PerfMetricMonitorData::getDevice));
                diskMap.forEach((k,v)->{
                    resultMap.put(k,getObjetData(request, v));
                });
            }else{
                resultMap.put("other",getObjetData(request, list));
            }
        }
        return resultMap;
    }

    private static List<PerfMonitorEchartsDTO> getObjetData(PerfMonitorRequest request, List<PerfMetricMonitorData> v) {
        List<PerfMonitorEchartsDTO> result = new ArrayList<>();
        PerfMonitorEchartsDTO perfMonitorEchartsDTO = new PerfMonitorEchartsDTO();
        perfMonitorEchartsDTO.setMetricName(request.getMetricName());
        perfMonitorEchartsDTO.setResourceId(request.getInstanceId());
        perfMonitorEchartsDTO.setUnit(v.get(0).getUnit());
        //获取值集合
        List<Object> avgList = v.stream()
                .map(PerfMetricMonitorData::getAverage)
                .collect(Collectors.toList());
        perfMonitorEchartsDTO.setValues(avgList);
        //获取时间节点集合
        List<Long> timestampList = v.stream()
                .map(PerfMetricMonitorData::getTimestamp)
                .collect(Collectors.toList());
        perfMonitorEchartsDTO.setTimestamps(timestampList);
        result.add(perfMonitorEchartsDTO);
        return result;
    }

    /**
     * 构建复合查询对象
     *
     * @param request 请求对象
     * @return 复合查询对象
     */
    private org.springframework.data.elasticsearch.core.query.Query getSearchQuery(PerfMonitorRequest request) {
        List<QueryUtil.QueryCondition> queryConditions = elasticsearchProvide.getDefaultQueryConditions(List.of(request.getCloudAccountId()),request.getMetricName(),request.getEntityType(),request.getStartTime(), request.getEndTime());
        QueryUtil.QueryCondition instanceId = new QueryUtil.QueryCondition(true, "instanceId.keyword", request.getInstanceId(), QueryUtil.CompareType.EQ);
        queryConditions.add(instanceId);
        QueryUtil.QueryCondition metricName = new QueryUtil.QueryCondition(true, "metricName.keyword", request.getMetricName(), QueryUtil.CompareType.EQ);
        queryConditions.add(metricName);
        QueryUtil.QueryCondition accountId = new QueryUtil.QueryCondition(true, "cloudAccountId.keyword", request.getCloudAccountId(), QueryUtil.CompareType.EQ);
        queryConditions.add(accountId);
        BoolQuery.Builder boolQuery = QueryUtil.getQuery(queryConditions);
        ///TODO 这个地方到时候可能要改，ES一次只能查询10000条数据，如果查询一年的监控数据，这里可能需要调整
        NativeQueryBuilder query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 10000))
                .withQuery(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().bool(boolQuery.build()).build())
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}))
                ;
        return query.build();
    }

}
