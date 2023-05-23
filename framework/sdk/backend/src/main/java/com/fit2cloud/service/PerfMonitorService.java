package com.fit2cloud.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.es.constants.IndexConstants;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.dto.PerfMonitorEchartsDTO;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import com.fit2cloud.request.PerfMonitorRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    public Map<String, List<PerfMonitorEchartsDTO>> getPerfMonitorData(PerfMonitorRequest request) {
        Map<String, List<PerfMonitorEchartsDTO>> resultMap = new HashMap<>();
        if (!elasticsearchProvide.existsIndex(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode())) {
            return resultMap;
        }
        List<PerfMetricMonitorData> esList = elasticsearchProvide.searchByQuery(IndexConstants.CE_PERF_METRIC_MONITOR_DATA.getCode(), getSearchQuery(request), PerfMetricMonitorData.class);
        if (esList.size() > 0) {
            Long startTime = request.getStartTime();
            Long endTime = request.getEndTime();
            // 对没有数据的时间点进行补全
            // 获取最新一条监控数据的period
            Integer period = esList.stream().sorted(Comparator.comparing(PerfMetricMonitorData::getTimestamp).reversed()).toList().get(0).getPeriod() * 1000;
            Long statPeriodTime = startTime % period;
            startTime = startTime - statPeriodTime;
            List<Long> timeList = new ArrayList<>();
            while (startTime <= endTime) {
                timeList.add(startTime);
                startTime = startTime + period;
            }
            if (StringUtils.equalsIgnoreCase(request.getMetricName(), "DISK_USED_UTILIZATION")) {
                Map<String, List<PerfMetricMonitorData>> diskMap = esList.stream().filter(v -> StringUtils.isNotEmpty(v.getDevice())).collect(Collectors.groupingBy(PerfMetricMonitorData::getDevice));
                diskMap.forEach((k, v) -> {
                    // 补全
                    List<PerfMetricMonitorData> diskDeviceList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(v)) {
                        extracted(timeList, v, diskDeviceList);
                    }
                    if (CollectionUtils.isNotEmpty(diskDeviceList)) {
                        v.addAll(diskDeviceList);
                    }
                    resultMap.put(k, getObjetData(request, v.stream().sorted(Comparator.comparing(PerfMetricMonitorData::getTimestamp)).collect(Collectors.toList())));
                });
            } else {
                // 补全
                List<PerfMetricMonitorData> resultList = new ArrayList<>();
                resultList.addAll(esList);
                List<PerfMetricMonitorData> completionList = new ArrayList<>();
                timeList.forEach(time -> {
                    List<PerfMetricMonitorData> tList = esList.stream().filter(v -> v.getTimestamp().longValue() == time).toList();
                    if (CollectionUtils.isEmpty(tList)) {
                        PerfMetricMonitorData source = new PerfMetricMonitorData();
                        BeanUtils.copyProperties(source, source);
                        source.setAverage(null);
                        source.setTimestamp(time);
                        completionList.add(source);
                    }
                });
                resultList.addAll(completionList);
                List<PerfMetricMonitorData> list = resultList.stream().sorted(Comparator.comparing(PerfMetricMonitorData::getTimestamp)).collect(Collectors.toList());
                resultMap.put("other", getObjetData(request, list));
            }
        }
        return resultMap;
    }

    /**
     * 时间点空数据补全
     */
    private static void extracted(List<Long> timeList, List<PerfMetricMonitorData> v, List<PerfMetricMonitorData> resultList) {
        timeList.forEach(time -> {
            List<PerfMetricMonitorData> tList = v.stream().filter(d -> d.getTimestamp().longValue() == time).toList();
            if (CollectionUtils.isEmpty(tList)) {
                PerfMetricMonitorData source = v.get(0);
                PerfMetricMonitorData vo = new PerfMetricMonitorData();
                BeanUtils.copyProperties(source, vo);
                vo.setAverage(null);
                vo.setTimestamp(time);
                resultList.add(vo);
            }
        });
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
        List<QueryUtil.QueryCondition> queryConditions = elasticsearchProvide.getDefaultQueryConditions(List.of(request.getCloudAccountId()), request.getMetricName(), request.getEntityType(), request.getStartTime(), request.getEndTime());
        QueryUtil.QueryCondition instanceId = new QueryUtil.QueryCondition(true, "instanceId.keyword", request.getInstanceId(), QueryUtil.CompareType.EQ);
        queryConditions.add(instanceId);
        BoolQuery.Builder boolQuery = QueryUtil.getQuery(queryConditions);
        ///TODO 这个地方到时候可能要改，ES一次只能查询10000条数据，如果查询一年的监控数据，这里可能需要调整
        NativeQueryBuilder query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(0, 10000))
                .withQuery(new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder().bool(boolQuery.build()).build())
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}));
        return query.build();
    }

}
