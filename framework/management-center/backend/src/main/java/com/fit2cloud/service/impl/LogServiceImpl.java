package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.log.entity.OperatedLogVO;
import com.fit2cloud.common.log.entity.SystemLogVO;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.PageUtil;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
        Page<SystemLogVO> page = PageUtil.of(request, SystemLogVO.class, true);
        if(!provide.existsIndex(CE_FILE_SYSTEM_LOGS)){
            return page;
        }
        return provide.searchByQuery(CE_FILE_SYSTEM_LOGS, getSearchQuery(request.getCurrentPage(), request.getPageSize(), JsonUtil.toJSONString(request), request.getOrder()), SystemLogVO.class, page);
    }


    @Override
    public IPage<OperatedLogVO> operatedLogs(PageOperatedLogRequest request) {
        Page<OperatedLogVO> page = PageUtil.of(request, OperatedLogVO.class, true);
        if(!provide.existsIndex(CE_FILE_SYSTEM_LOGS)){
            return page;
        }
        return provide.searchByQuery(CE_FILE_API_LOGS, getSearchQueryFroOperation(request.getCurrentPage(), request.getPageSize(), request, request.getOrder()), OperatedLogVO.class, page);
    }

    private org.springframework.data.elasticsearch.core.query.Query getSearchQueryFroOperation(Integer currentPage, Integer pageSize, PageOperatedLogRequest request, OrderRequest order) {
        NativeQueryBuilder query = new NativeQueryBuilder()
                .withPageable(PageRequest.of(currentPage - 1, pageSize))
                .withQuery(buildQueryForOperation(request))
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}))
                .withAggregation("count", new Aggregation.Builder().valueCount(new ValueCountAggregation.Builder().field("_id").build()).build());
        if (order != null && StringUtils.isNotEmpty(order.getColumn())) {
            if (StringUtils.equalsIgnoreCase(order.getColumn(), "createTime") || StringUtils.equalsIgnoreCase(order.getColumn(), "date")) {
                query.withSort(Sort.by(order.isAsc() ? Sort.Order.asc("@timestamp") : Sort.Order.desc("@timestamp")));
            } else {
                query.withSort(Sort.by(order.isAsc() ? Sort.Order.asc(order.getColumn()) : Sort.Order.desc(order.getColumn())));
            }
        } else {
            query.withSort(Sort.by(Sort.Order.desc("@timestamp")));
        }
        return query.build();
    }

    private Query buildQueryForOperation(PageOperatedLogRequest request) {
        if (StringUtils.equalsIgnoreCase("loginLog", request.getType())) {
            request.setResourceType(ResourceTypeEnum.SYSTEM.getCode());
        }
        if (StringUtils.equalsIgnoreCase("vmOperateLog", request.getType())) {
            request.setResourceType(ResourceTypeEnum.CLOUD_SERVER.getName());
        }
        if (StringUtils.equalsIgnoreCase("diskOperateLog", request.getType())) {
            request.setResourceType(ResourceTypeEnum.CLOUD_DISK.getName());
        }
        request.setType(null);
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        //平台管理日志，把登录日志以及资产资源过滤掉，云主机、磁盘
        if (Objects.isNull(request.getResourceType())) {
            queryConditions.add(new QueryUtil.QueryCondition(true, "operated", OperatedTypeEnum.LOGIN.getOperate(), QueryUtil.CompareType.NOT_EQ));
            queryConditions.add(new QueryUtil.QueryCondition(true, "resourceType.keyword", Arrays.asList(ResourceTypeEnum.CLOUD_SERVER.getName(), ResourceTypeEnum.CLOUD_DISK.getName()), QueryUtil.CompareType.NOT_IN));
        } else {
            queryConditions.add(new QueryUtil.QueryCondition(true, "resourceType.keyword", request.getResourceType(), QueryUtil.CompareType.EQ));
        }
        //不查询没有等级的数据
        queryConditions.add(new QueryUtil.QueryCondition(true, "level", null, QueryUtil.CompareType.NOT_EXIST));
        BoolQuery.Builder query = QueryUtil.getQuery(queryConditions);
        query.must(new Query.Builder().wildcard(new WildcardQuery.Builder().field("module").value("*").build()).build());
        if (StringUtils.isNotEmpty(request.getResourceName())) {
            query.must(new Query.Builder().wildcard(new WildcardQuery.Builder().field("resourceName.keyword").value("*" + request.getResourceName() + "*").build()).build());
        }
        if (StringUtils.isNotEmpty(request.getUser())) {
            query.must(new Query.Builder().wildcard(new WildcardQuery.Builder().field("user.keyword").value("*" + request.getUser() + "*").build()).build());
        }
        return new Query.Builder().bool(query.build()).build();
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
                .withPageable(PageRequest.of(currentPage - 1, pageSize))
                .withQuery(buildQuery(request))
                .withSourceFilter(new FetchSourceFilter(new String[]{}, new String[]{"@version", "@timestamp", "host", "tags"}))
                .withAggregation("count", new Aggregation.Builder().valueCount(new ValueCountAggregation.Builder().field("_id").build()).build());
        if (order != null && StringUtils.isNotEmpty(order.getColumn())) {
            if (StringUtils.equalsIgnoreCase(order.getColumn(), "createTime") || StringUtils.equalsIgnoreCase(order.getColumn(), "date")) {
                query.withSort(Sort.by(order.isAsc() ? Sort.Order.asc("@timestamp") : Sort.Order.desc("@timestamp")));
            } else {
                query.withSort(Sort.by(order.isAsc() ? Sort.Order.asc(order.getColumn()) : Sort.Order.desc(order.getColumn())));
            }
        } else {
            query.withSort(Sort.by(Sort.Order.desc("@timestamp")));
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
        //不查询没有等级的数据
        queryConditions.add(new QueryUtil.QueryCondition(true, "level", null, QueryUtil.CompareType.NOT_EXIST));
        BoolQuery.Builder query = QueryUtil.getQuery(queryConditions);
        return new Query.Builder().bool(query.build()).build();
    }

    @Override
    public void deleteEsData(String index, int m, Class<?> clazz) {
        //provide.delete(5,"ce-file-system-logs", SystemLog.class);
        RangeQuery.Builder rangeQuery = new RangeQuery.Builder();
        rangeQuery.field("@timestamp");
        rangeQuery.lt(JsonData.of("now-" + m + "m"));
        rangeQuery.format("epoch_millis");
        Query query = new Query.Builder().range(rangeQuery.build()).build();
        NativeQueryBuilder builder = new NativeQueryBuilder();
        builder.withQuery(query);
        provide.delete(index, builder.build(), clazz);
    }
}
