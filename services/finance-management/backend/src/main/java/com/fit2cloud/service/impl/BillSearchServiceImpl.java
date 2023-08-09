package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.mapping.RuntimeField;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.common.util.AuthUtil;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.constants.CalendarConstants;
import com.fit2cloud.controller.request.BaseViewRequest;
import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.ExpensesResponse;
import com.fit2cloud.controller.response.Trend;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.BillSearchService;
import com.fit2cloud.service.IBillCurrencyService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/4/3  10:31}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class BillSearchServiceImpl implements BillSearchService {
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private IBaseOrganizationService organizationService;
    @Resource
    private IBillCurrencyService currencyService;

    @Override
    public List<Trend> trend(CalendarConstants calendarConstants, Integer historyNum) {
        return appendDefaultTreed(trend(calendarConstants, historyNum, null, List.of(), new BaseViewRequest().toRuntimeMappings()), historyNum);
    }

    @Override
    public List<Trend> trend(CalendarConstants calendarConstants, Integer historyNum, HistoryTrendRequest request) {
        return appendDefaultTreed(trend(calendarConstants, historyNum, null, request.toQuery(), request.toRuntimeMappings(currencyService.listCurrency())), historyNum);
    }

    /**
     * @param calendarConstants 日期类型
     * @param historyNum        历史num
     * @param otherQuery        其他查询过滤条件
     * @return 趋势数据
     */
    @SneakyThrows
    private List<Trend> trend(CalendarConstants calendarConstants, Integer historyNum, String endTime, List<Query> otherQuery, Map<String, RuntimeField> runtimeFieldMap) {
        String startTime = MonthUtil.getStartTime(calendarConstants, historyNum, endTime);
        // 查询format
        String format = calendarConstants.equals(CalendarConstants.MONTH) ? "yyyy-MM" : "yyyy";
        CalendarInterval calendarInterval = calendarConstants.equals(CalendarConstants.MONTH) ? CalendarInterval.Month : CalendarInterval.Year;
        // 构建区域查询
        Query rangeQuery = new RangeQuery.Builder()
                .gte(JsonData.of(startTime))
                .lt(StringUtils.isEmpty(endTime) ? null : JsonData.of(MonthUtil.addCalender(calendarConstants, endTime)))
                .field("billingCycle")
                .format("yyyy-MM||yyyy||yyyy-MM-dd HH:mm:ss").build()._toQuery();
        List<Query> queries = new ArrayList<>();
        queries.add(rangeQuery);
        queries.addAll(otherQuery);
        // 认证过滤条件
        Query authQuery = AuthUtil.getAuthQuery(org -> organizationService.getOrgLevel(org));
        if (Objects.nonNull(authQuery)) {
            queries.add(authQuery);
        }
        // 构建查询条件
        Query query = new BoolQuery.Builder().must(queries).build()._toQuery();
        // 构建时间趋势聚合
        DateHistogramAggregation dateHistogramAggregation = new DateHistogramAggregation.Builder()
                .field("billingCycle")
                .format(format)
                .calendarInterval(calendarInterval)
                .build();
        Aggregation.Builder.ContainerBuilder aggregation = new Aggregation.Builder().dateHistogram(dateHistogramAggregation);
        aggregation.aggregations("total", new Aggregation.Builder().sum(new SumAggregation.Builder().field("realTotalCost").build()).build());
        // 构建查询与聚合
        SearchRequest.Builder searchRequest = new SearchRequest.Builder()
                .aggregations("group1", aggregation.build())
                .query(query)
                .runtimeMappings(runtimeFieldMap);

        // 查询ing
        SearchResponse<CloudBill> search = elasticsearchClient.search(searchRequest.build(), CloudBill.class);
        Aggregate group1 = search.aggregations().get("group1");
        DateHistogramAggregate dateHistogramAggregate = group1.dateHistogram();
        // 映射为想要的数据
        return dateHistogramAggregate.buckets().array().stream().map(this::toTrend).toList();
    }

    /**
     * 没有当月数据默认加上
     *
     * @param trends     聚合趋势数据
     * @param historyNum 历史月份
     * @return 处理后的趋势
     */
    private List<Trend> appendDefaultTreed(List<Trend> trends, Integer historyNum) {
        List<String> historyMonth = MonthUtil.getHistoryMonth(historyNum);
        return historyMonth.stream().map(month -> trends.stream().filter(t -> t.getLabel().equals(month)).findFirst().orElseGet(() -> {
            Trend trend = new Trend();
            trend.setLabel(month);
            trend.setValue(0.0);
            return trend;
        })).toList();

    }

    /**
     * 将esStringTermsBucket 转化为系统趋势对象
     *
     * @param bucket es StringTermsBucket桶对象
     * @return 系统趋势对象
     */
    private Trend toTrend(DateHistogramBucket bucket) {
        Trend trend = new Trend();
        double total = bucket.aggregations().get("total").sum().value();
        trend.setLabel(bucket.keyAsString());
        trend.setValue(total);
        return trend;
    }


    @Override
    public ExpensesResponse expenses(CalendarConstants calendarConstants, String value) {
        List<Trend> trend = trend(calendarConstants, 1, value, List.of(getExpensesQuery(calendarConstants)), new BillExpensesRequest().toRuntimeMappings());
        return toExpensesResponse(calendarConstants, value, trend);
    }

    @Override
    public ExpensesResponse expenses(CalendarConstants calendarConstants, String value, BillExpensesRequest request) {
        List<Query> requestQuery = new ArrayList<>(request.toQuery());
        requestQuery.add(getExpensesQuery(calendarConstants));
        List<Trend> trend = trend(calendarConstants, 1, value, requestQuery, request.toRuntimeMappings(currencyService.listCurrency()));
        return toExpensesResponse(calendarConstants, value, trend);
    }

    /**
     * 获取上月花销查询Query
     *
     * @param calendarConstants 日期类型
     * @return 查询Query
     */
    private Query getExpensesQuery(CalendarConstants calendarConstants) {
        String script = calendarConstants.equals(CalendarConstants.MONTH) ?
                "doc['billingCycle'].value.year==params['year']&&doc['billingCycle'].value.monthValue==params['month']?" +
                        "true:" +
                        "doc['deductionDate'].value.dayOfMonth<params['day']" :
                "doc['billingCycle'].value.year==params['year']?" +
                        "true:" +
                        "doc['billingCycle'].value.monthValue<=params['month']" +
                        "&&(doc['billingCycle'].value.monthValue==params['month']?doc['deductionDate'].value.dayOfMonth<params['day']:true)";
        return new ScriptQuery.Builder().script(new Script.Builder()
                .inline(new InlineScript.Builder().source(script)
                        .params(
                                Map.of("day",
                                        JsonData.of(LocalDateTime.now().getDayOfMonth()),
                                        "month", JsonData.of(LocalDateTime.now().getMonthValue()),
                                        "year", JsonData.of(LocalDateTime.now().getYear())))
                        .build())
                .build()).build()._toQuery();

    }

    private ExpensesResponse toExpensesResponse(CalendarConstants calendarConstants, String value, List<Trend> trends) {
        ExpensesResponse expensesResponse = new ExpensesResponse(BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0));
        // 当月/年
        String currentLabel = MonthUtil.getStartTime(calendarConstants, 0, value);
        // 上月/年
        String upLabel = MonthUtil.getStartTime(calendarConstants, 1, value);
        // 设置当月/年值
        trends.stream().filter(t -> StringUtils.equals(currentLabel, t.getLabel()))
                .findFirst().ifPresent(current -> expensesResponse.setCurrent(BigDecimal.valueOf(current.getValue())));
        // 设置上月/年值
        trends.stream().filter(t -> StringUtils.equals(upLabel, t.getLabel()))
                .findFirst().ifPresent(current -> expensesResponse.setUp(BigDecimal.valueOf(current.getValue())));
        return expensesResponse;
    }


}
