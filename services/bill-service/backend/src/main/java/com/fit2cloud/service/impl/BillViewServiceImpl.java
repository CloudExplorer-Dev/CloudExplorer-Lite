package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptQuery;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.fit2cloud.common.conver.Convert;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.BillGroupConstants;
import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.BillView;
import com.fit2cloud.controller.response.Trend;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.dao.jentity.Group;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.BillViewService;
import com.fit2cloud.service.IBillRuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/25  5:52 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class BillViewServiceImpl implements BillViewService {
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private IBillRuleService billRuleService;


    @Override
    public BigDecimal getBillExpenses(String type, String value, BillExpensesRequest billExpensesRequest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type.equals("MONTH") ? "yyyy-MM" : "yyyy");
        try {
            simpleDateFormat.parse(value);
        } catch (ParseException e) {
            throw new Fit2cloudException(111, "时间格式正确");
        }
        ScriptQuery scriptQuery = new ScriptQuery.Builder().script(s -> getScript(s, type, value)).build();
        Aggregation aggregation = new Aggregation.Builder().sum(new SumAggregation.Builder().field("realTotalCost").build()).build();

        NativeQuery query = new NativeQueryBuilder().withQuery(new Query.Builder().script(scriptQuery).build()).withAggregation("sum", aggregation).build();
        SearchHits<CloudBill> response = elasticsearchTemplate.search(query, CloudBill.class);
        if (response.hasAggregations()) {
            try {
                ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
                assert aggregations != null;
                SumAggregate sum = aggregations.aggregations().get(0).aggregation().getAggregate().sum();
                return BigDecimal.valueOf(sum.value());
            } catch (Exception ignored) {
                return BigDecimal.valueOf(0);
            }
        }
        return BigDecimal.valueOf(0);
    }

    @Override
    public List<Trend> getTrend(String type, Integer historyNum, HistoryTrendRequest historyTrendRequest) {
        Aggregation.Builder.ContainerBuilder aggregation = new Aggregation.Builder().terms(new TermsAggregation.Builder().script(s -> getTermsAggregationScript(s, type)).size(historyNum).build());
        aggregation.aggregations("total", new Aggregation.Builder().sum(new SumAggregation.Builder().field("realTotalCost").build()).build());
        NativeQuery query = new NativeQueryBuilder().withAggregation("group1", aggregation.build()).build();
        SearchHits<CloudBill> response = elasticsearchTemplate.search(query, CloudBill.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
        assert aggregations != null;
        StringTermsAggregate sterms = aggregations.aggregations().get(0).aggregation().getAggregate().sterms();
        return sterms.buckets().array().stream().map(this::toTrend).toList();
    }

    @Override
    public Map<String, List<BillView>> billViewByRuleId(String ruleId, String month) {
        BillRule billRule = billRuleService.getById(ruleId);
        return searchBillView(billRule, getHistoryMonth(month, 6), "realTotalCost");
    }


    /**
     * 获取历史月份
     *
     * @param month   当前月份
     * @param history 获取多少个月
     * @return 月份数组
     */
    private static List<String> getHistoryMonth(String month, Integer history) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            simpleDateFormat.parse(month);
        } catch (ParseException e) {
            throw new Fit2cloudException(2222, "月份格式化错误,yyyy-MM");
        }
        Calendar instance = Calendar.getInstance();
        String[] split = month.split("-");
        return IntStream.range(0, history).boxed().map(i -> {
            instance.set(Calendar.YEAR, Integer.parseInt(split[0]));
            instance.set(Calendar.MONTH, Integer.parseInt(split[1]) - 1);
            instance.add(Calendar.MONTH, (i * -1));
            return String.format("%04d-%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1);
        }).toList();

    }


    /**
     * 解析聚合结果
     *
     * @param aggregations    聚合对象
     * @param getSumAggregate 获取Sum聚合
     * @return 解析后的聚合结果
     */
    public Map<String, List<BillView>> analysisBillView(AggregationsContainer<?> aggregations, NativeQuery query, Function<Map<String, Aggregate>, SumAggregate> getSumAggregate) {
        ElasticsearchAggregations aggr = (ElasticsearchAggregations) aggregations;
        DateHistogramAggregate dateHistogramAggregate = aggr.aggregations().get(0).aggregation().getAggregate().dateHistogram();
        return dateHistogramAggregate.buckets().array().stream().map(dateHistogramBucket -> {
            Aggregate group = dateHistogramBucket.aggregations().get("group");
            Aggregation groupQuery = getGroupQuery(query);
            List<BillView> billView = analysisBillView(group, groupQuery, 1, null, getSumAggregate);
            return new DefaultKeyValue<>(dateHistogramBucket.keyAsString(), billView);
        }).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));
    }

    private Aggregation getGroupQuery(NativeQuery query) {
        return query.getAggregations().get("billView").aggregations().get("group");
    }

    private String mapGroup(String field, String groupName) {
        if (BillGroupConstants.BILL_GROUP.containsKey(field)) {
            return CommonUtil.exec(BillGroupConstants.BILL_GROUP.get(field).conver(), groupName, Convert::conver);
        }
        return groupName;
    }

    /**
     * @param billRule         账单规则
     * @param months           趋势月份
     * @param realTotalCostKey 聚合Key 一般使用realTotalCost
     * @return 聚合后对象
     */
    public Map<String, List<BillView>> searchBillView(BillRule billRule, List<String> months, String realTotalCostKey) {
        // todo 获取查询参数根据账单规则
        NativeQuery query = getSearchBillViewQueryByRule(billRule, months, realTotalCostKey);
        // todo 查询
        SearchHits<CloudBill> search = elasticsearchTemplate.search(query, CloudBill.class);
        // todo 解析查询结果
        return analysisBillView(search.getAggregations(), query, a -> a.containsKey(realTotalCostKey) ? a.get(realTotalCostKey).sum() : null);
    }

    /**
     * 根据账单规则获取构建es查询条件
     *
     * @param billRule         账单规则
     * @param months           月份
     * @param realTotalCostKey 聚合sum key
     * @return es查询条件
     */
    private NativeQuery getSearchBillViewQueryByRule(BillRule billRule, List<String> months, String realTotalCostKey) {
        // todo 根据趋势月份构建查询条件
        Query q = new Query.Builder().script(new ScriptQuery.Builder().script(s -> getTermsAggregationScript(s, months)).build()).build();
        // todo 获取聚合对象
        Aggregation aggregationByGroups = getAggregationByGroups(null, billRule.getGroups(), 0, a -> a.aggregations(realTotalCostKey, new Aggregation.Builder().sum(new SumAggregation.Builder().field(realTotalCostKey).build()).build()));
        DateHistogramAggregation dateHistogramAggregation = new DateHistogramAggregation.Builder().field("billingCycle").format("yyyy-MM").calendarInterval(CalendarInterval.Month).build();
        Aggregation.Builder.ContainerBuilder group = new Aggregation.Builder().dateHistogram(dateHistogramAggregation).aggregations("group", aggregationByGroups);
        // todo 构建es查询加聚合对象
        return new NativeQueryBuilder().withQuery(q).withAggregation("billView", group.build()).build();
    }

    /**
     * @param aggregate   group聚合对象
     * @param groupIndex  group下标
     * @param superGroups 聚合副
     * @return 账单数据
     */
    private List<BillView> analysisBillView(Aggregate aggregate, Aggregation aggregation, Integer groupIndex, List<DefaultKeyValue<String, String>> superGroups, Function<Map<String, Aggregate>, SumAggregate> getSumAggregate) {
        return aggregate.sterms().buckets().array().stream().map(bu -> {
            Aggregate groupAggregate = bu.aggregations().get("group" + groupIndex);
            DefaultKeyValue<String, String> keyValue = new DefaultKeyValue<>();
            keyValue.setKey("group" + groupIndex);
            keyValue.setValue(mapGroup(aggregation.terms().field(), bu.key()));
            ArrayList<DefaultKeyValue<String, String>> defaultKeyValues = new ArrayList<>();
            if (groupAggregate != null) {
                if (CollectionUtils.isNotEmpty(superGroups)) {
                    defaultKeyValues.addAll(superGroups);
                }
                defaultKeyValues.add(keyValue);
                return analysisBillView(groupAggregate, aggregation.aggregations().get("group" + groupIndex), groupIndex + 1, defaultKeyValues, getSumAggregate);
            }
            SumAggregate sumAggregate = getSumAggregate.apply(bu.aggregations());
            if (Objects.nonNull(sumAggregate)) {
                if (CollectionUtils.isNotEmpty(superGroups)) {
                    defaultKeyValues.addAll(superGroups);
                }
                defaultKeyValues.add(keyValue);
                BillView billView = new BillView();
                billView.setValue(sumAggregate.value());
                billView.setBillGroupDetails(defaultKeyValues);
                return List.of(billView);
            }
            return null;
        }).filter(Objects::nonNull).flatMap(List::stream).toList();

    }


    /**
     * 根据Groups构建聚合查询
     *
     * @param superAggr       父级聚合
     * @param groups          账单规则groups
     * @param index           当前index
     * @param appendChildAggs 对最后一次分组进行聚合
     * @return 聚合对象
     */
    private Aggregation getAggregationByGroups(Aggregation.Builder.ContainerBuilder superAggr, List<Group> groups, Integer index, Consumer<Aggregation.Builder.ContainerBuilder> appendChildAggs) {
        if (Objects.nonNull(superAggr)) {
            if (index < groups.size()) {
                Aggregation.Builder.ContainerBuilder aggregationByGroup = getAggregationByGroup(groups.get(index));
                if (index.equals(groups.size() - 1)) {
                    appendChildAggs.accept(aggregationByGroup);
                    superAggr.aggregations("group" + index, aggregationByGroup.build());
                } else {
                    Aggregation aggregationByGroups = getAggregationByGroups(aggregationByGroup, groups, index + 1, appendChildAggs);
                    superAggr.aggregations("group" + index, aggregationByGroups);
                }
            } else {
                appendChildAggs.accept(superAggr);
            }
            return superAggr.build();
        } else {
            if (index < groups.size()) {
                Aggregation.Builder.ContainerBuilder aggregationByGroup = getAggregationByGroup(groups.get(index));
                return getAggregationByGroups(aggregationByGroup, groups, index + 1, appendChildAggs);
            } else {
                return null;
            }
        }
    }

    /**
     * 根据Group 构建聚合对象
     *
     * @param group group
     * @return es聚合对象
     */
    private Aggregation.Builder.ContainerBuilder getAggregationByGroup(Group group) {
        return new Aggregation.Builder().terms(new TermsAggregation.Builder().field(group.getField()).size(Integer.MAX_VALUE).missing("其他").build());
    }

    /**
     * 将esStringTermsBucket 转化为系统趋势对象
     *
     * @param bucket es StringTermsBucket桶对象
     * @return 系统趋势对象
     */
    private Trend toTrend(StringTermsBucket bucket) {
        String key = bucket.key();
        Trend trend = new Trend();
        double total = bucket.aggregations().get("total").sum().value();
        trend.setLabel(key);
        trend.setValue(total);
        return trend;
    }


    private ObjectBuilder<Script> getTermsAggregationScript(Script.Builder s, List<String> months) {
        String script = JsonUtil.toJSONString(months) + ".contains(doc['billingCycle'].value.year.toString()+'-'+(doc['billingCycle'].value.monthValue<10?'0'+doc['billingCycle'].value.monthValue:doc['billingCycle'].value.monthValue.toString()))";
        return s.inline(inlineScript -> inlineScript.lang("painless").source(script));

    }

    /**
     * 获取
     *
     * @param s    Script 脚本构造器
     * @param type 类型
     * @return 脚本构造
     */
    public ObjectBuilder<Script> getTermsAggregationScript(Script.Builder s, String type) {
        String monthScript = "doc['billingCycle'].value.year.toString()+'-'+(doc['billingCycle'].value.monthValue<10?'0'+doc['billingCycle'].value.monthValue:doc['billingCycle'].value.monthValue.toString())";
        String yearScript = "doc['billingCycle'].value.year.toString()";
        String script = type.equals("MONTH") ? monthScript : yearScript;
        return s.inline(inlineScript -> inlineScript.lang("painless").source(script));
    }

    /**
     * @param s    脚本构造器
     * @param type 类型
     * @return 脚本对象
     */
    public ObjectBuilder<Script> getScript(Script.Builder s, String type, String value) {
        String script = type.equals("MONTH") ? "doc['billingCycle'].value.monthValue==params.month&&doc['billingCycle'].value.year==params.year" : "doc['billingCycle'].value.year==params.year";
        return s.inline(inlineScript -> inlineScript.lang("painless").source(script).params(getParams(type, value)));
    }

    private Map<String, JsonData> getParams(String type, String value) {
        if (type.equals("MONTH")) {
            String[] split = value.split("-");
            return new HashMap<>() {{
                put("year", JsonData.of(Integer.parseInt(split[0])));
                put("month", JsonData.of(Integer.parseInt(split[1])));
            }};
        }
        return new HashMap<>() {{
            put("year", JsonData.of(Integer.parseInt(value)));
        }};

    }
}
