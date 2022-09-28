package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.log.entity.OperatedLogVO;
import com.fit2cloud.common.log.entity.SystemLogVO;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.controller.request.es.PageOperatedLogRequest;
import com.fit2cloud.controller.request.es.PageSystemLogRequest;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.service.ILogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/23 17:08
 **/
@Service
public class LogServiceImpl implements ILogService {

    private static final String CE_FILE_SYSTEM_LOGS = "ce-file-system-logs";
    private static final String CE_FILE_API_LOGS = "ce-file-api-logs";

    @Resource
    private ElasticsearchProvide provide;

    @Override
    public IPage<SystemLogVO> systemLogs(PageSystemLogRequest request) {
        Page<SystemLogVO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
        return provide.searchByQuery(CE_FILE_SYSTEM_LOGS, getSearchQuery(request.getCurrentPage(), request.getPageSize(), JsonUtil.toJSONString(request), request.getOrder()), SystemLogVO.class, page);
    }


    @Override
    public IPage<OperatedLogVO> operatedLogs(PageOperatedLogRequest request) {
        Page<OperatedLogVO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
        if (StringUtils.equalsIgnoreCase("loginLog", request.getType())) {
            request.setType(null);
            request.setResourceType(ResourceTypeEnum.SYSTEM.getDescription());
        }
        if (StringUtils.equalsIgnoreCase("vmOperateLog", request.getType())) {
            request.setType(null);
            request.setResourceType(ResourceTypeEnum.VIRTUAL_MACHINE.getDescription());
        }
        if (StringUtils.equalsIgnoreCase("diskOperateLog", request.getType())) {
            request.setType(null);
            request.setResourceType(ResourceTypeEnum.DISK.getDescription());
        }
        if (StringUtils.equalsIgnoreCase("allLog", request.getType())) {
            request.setType(null);
            //request.setResourceType(ResourceTypeEnum.DISK.getDescription());
        }
        return provide.searchByQuery(CE_FILE_API_LOGS, getSearchQuery(request.getCurrentPage(), request.getPageSize(), JsonUtil.toJSONString(request), request.getOrder()), OperatedLogVO.class, page);
    }


    /**
     * 构建复合查询对象
     *
     * @param currentPage 当前页
     * @param pageSize    每个大小
     * @param request     请求对象
     * @param order       排序
     * @return 复合查询对象
     */
    private org.springframework.data.elasticsearch.core.query.Query getSearchQuery(Integer currentPage, Integer pageSize, String request, OrderRequest order) {

        NativeQueryBuilder query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(currentPage, pageSize))
                .withQuery(buildQuery(request))
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}))
                .withAggregation("count", new Aggregation.Builder().valueCount(new ValueCountAggregation.Builder().field("_id").build()).build());
        if (order != null && StringUtils.isNotEmpty(order.getColumn())) {
            query.withSort(Sort.by(order.isAsc() ? Sort.Order.asc(order.getColumn()) : Sort.Order.desc(order.getColumn())));
        }
        return query.build();
    }

    /**
     * 构建查询对象
     *
     * @param paramsJson 查询对象json
     * @return 查询对象
     */
    private Query buildQuery(String paramsJson) {
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        ObjectNode params = JsonUtil.parseObject(paramsJson);
        Iterator<String> fieldNames = params.fieldNames();
        while (fieldNames.hasNext()) {
            String name = fieldNames.next();
            if (StringUtils.equalsIgnoreCase("currentPage", name) || StringUtils.equalsIgnoreCase("pageSize", name) || StringUtils.equalsIgnoreCase("order", name)) {
                continue;
            }
            JsonNode jsonNode = params.get(name);
            String value = jsonNode.asText();
            QueryUtil.QueryCondition condition = new QueryUtil.QueryCondition(StringUtils.isNotEmpty(value) && !StringUtils.equalsIgnoreCase("null", value), name, value, QueryUtil.CompareType.LIKE);
            queryConditions.add(condition);
        }
        BoolQuery.Builder query = QueryUtil.getQuery(queryConditions);
        return new Query.Builder().bool(query.build()).build();
    }
}
