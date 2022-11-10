package com.fit2cloud.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.dto.PerfMonitorEchartsDTO;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.request.PerfMonitorRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianneng
 * @date 2022/10/30 20:29
 **/
@Service
@Lazy
public class PerfMonitorService {
    private static final String CE_PERF_METRIC_MONITOR_DATA = "ce-perf-metric-monitor-data";
    @Resource
    private ElasticsearchProvide elasticsearchProvide;

    public List<PerfMonitorEchartsDTO> getPerfMonitorData(PerfMonitorRequest request){
        List<PerfMonitorEchartsDTO> result = new ArrayList<>();
        List<PerfMetricMonitorData> list = elasticsearchProvide.searchByQuery(CE_PERF_METRIC_MONITOR_DATA, getSearchQuery(request), PerfMetricMonitorData.class);
        if(list.size()>0){
            PerfMonitorEchartsDTO perfMonitorEchartsDTO = new PerfMonitorEchartsDTO();
            perfMonitorEchartsDTO.setMetricName(request.getMetricName());
            perfMonitorEchartsDTO.setResourceId(request.getInstanceId());
            perfMonitorEchartsDTO.setUnit(list.get(0).getUnit());
            //获取值集合
            List<Object> avgList = list.stream()
                    .map(PerfMetricMonitorData::getAverage)
                    .collect(Collectors.toList());
            perfMonitorEchartsDTO.setValues(avgList);
            //获取时间节点集合
            List<Long> timestampList = list.stream()
                    .map(PerfMetricMonitorData::getTimestamp)
                    .collect(Collectors.toList());
            perfMonitorEchartsDTO.setTimestamps(timestampList);
            result.add(perfMonitorEchartsDTO);
        }
        return result;
    }

    /**
     * 构建复合查询对象
     *
     * @param request     请求对象
     * @return 复合查询对象
     */
    private org.springframework.data.elasticsearch.core.query.Query getSearchQuery(PerfMonitorRequest request) {
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        QueryUtil.QueryCondition startTimesTamp = new QueryUtil.QueryCondition(true, "timestamp", request.getStartTime(), QueryUtil.CompareType.GTE);
        queryConditions.add(startTimesTamp);
        QueryUtil.QueryCondition endTimesTamp = new QueryUtil.QueryCondition(true, "timestamp", request.getEndTime(), QueryUtil.CompareType.LTE);
        queryConditions.add(endTimesTamp);
        QueryUtil.QueryCondition entityType = new QueryUtil.QueryCondition(true, "entityType.keyword", request.getEntityType(), QueryUtil.CompareType.EQ);
        queryConditions.add(entityType);
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
                .withAggregation("count", new Aggregation.Builder().valueCount(new ValueCountAggregation.Builder().field("_id").build()).build());
        return query.build();
    }
 }
