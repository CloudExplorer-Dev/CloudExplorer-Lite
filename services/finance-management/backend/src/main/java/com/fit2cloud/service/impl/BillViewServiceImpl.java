package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.util.ObjectBuilder;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.util.EsFieldUtil;
import com.fit2cloud.common.util.EsScriptUtil;
import com.fit2cloud.common.util.MappingUtil;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.BillView;
import com.fit2cloud.controller.response.Trend;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.dao.jentity.Group;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.BillViewService;
import com.fit2cloud.service.IBillRuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Resource
    private IBaseOrganizationService organizationService;


    @Override
    public BigDecimal getBillExpenses(String type, String value, BillExpensesRequest billExpensesRequest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type.equals("MONTH") ? "yyyy-MM" : "yyyy");
        try {
            simpleDateFormat.parse(value);
        } catch (ParseException e) {
            throw new Fit2cloudException(ErrorCodeConstants.BILL_VIEW_DATE_FORMAT.getCode(), ErrorCodeConstants.BILL_VIEW_DATE_FORMAT.getMessage());
        }
        ScriptQuery scriptQuery = new ScriptQuery.Builder().script(s -> EsScriptUtil.getMonthOrYearScript(s, type, value)).build();
        Aggregation aggregation = new Aggregation.Builder().sum(new SumAggregation.Builder().field("realTotalCost").build()).build();
        Query q = new Query.Builder().script(scriptQuery).build();
        Query authQuery = getAuthQuery();
        if (Objects.nonNull(authQuery)) {
            q = new Query.Builder().bool(new BoolQuery.Builder().must(q, authQuery).build()).build();
        }
        NativeQuery query = new NativeQueryBuilder().withQuery(q).withAggregation("sum", aggregation).build();
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
        List<String> months = MonthUtil.getHistoryMonth(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM")), historyNum);
        // todo 根据趋势月份构建查询条件
        Query q = new Query.Builder().script(new ScriptQuery.Builder().script(s -> getTermsAggregationScript(s, months)).build()).build();
        Query authQuery = getAuthQuery();
        if (Objects.nonNull(authQuery)) {
            q = new Query.Builder().bool(new BoolQuery.Builder().must(q, authQuery).build()).build();
        }
        DateHistogramAggregation dateHistogramAggregation = new DateHistogramAggregation.Builder().field("billingCycle").format("yyyy-MM").calendarInterval(CalendarInterval.Month).build();
        Aggregation.Builder.ContainerBuilder aggregation = new Aggregation.Builder().dateHistogram(dateHistogramAggregation);
        aggregation.aggregations("total", new Aggregation.Builder().sum(new SumAggregation.Builder().field("realTotalCost").build()).build());
        NativeQuery query = new NativeQueryBuilder().withQuery(q).withAggregation("group1", aggregation.build()).build();
        SearchHits<CloudBill> response = elasticsearchTemplate.search(query, CloudBill.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
        assert aggregations != null;
        DateHistogramAggregate dateHistogramAggregate = aggregations.aggregations().get(0).aggregation().getAggregate().dateHistogram();
        return dateHistogramAggregate.buckets().array().stream().map(this::toTrend).toList();
    }

    /**
     * 获取当前用户角色的查询Query
     *
     * @return 获取当前用户角色查询Query
     */
    private Query getAuthQuery() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDto userDto = (UserDto) context.getAuthentication().getPrincipal();
        if (userDto.getCurrentRole().equals(RoleConstants.ROLE.ADMIN)) {
            // 如果是管理员则查询全部
            return null;
        }
        if (userDto.getCurrentRole().equals(RoleConstants.ROLE.ORGADMIN)) {
            String currentSource = userDto.getCurrentSource();
            int orgLevel = organizationService.getOrgLevel(currentSource);
            return new Query.Builder().term(new TermQuery.Builder().field(orgLevel + "级组织" + "." + "").value(currentSource).build()).build();
        }
        if (userDto.getCurrentRole().equals(RoleConstants.ROLE.USER)) {
            String currentSource = userDto.getCurrentSource();
            return new Query.Builder().term(new TermQuery.Builder().field("workspaceId").value(currentSource).build()).build();
        }
        return null;

    }

    @Override
    public Map<String, List<BillView>> billViewByRuleId(String ruleId, String month) {
        BillRule billRule = billRuleService.getById(ruleId);
        return searchBillView(billRule, MonthUtil.getHistoryMonth(month, 6), "realTotalCost");
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
        Query authQuery = getAuthQuery();
        if (Objects.nonNull(authQuery)) {
            q = new Query.Builder().bool(new BoolQuery.Builder().must(q, authQuery).build()).build();
        }
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
            keyValue.setValue(MappingUtil.mapping(aggregation.terms().field(), bu.key()));
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
        return new Aggregation.Builder().terms(new TermsAggregation.Builder().field(EsFieldUtil.getGroupKeyByField(group.getField())).size(Integer.MAX_VALUE).missing(StringUtils.isEmpty(group.getMissName()) ? "其他" : group.getMissName()).build());
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


    private ObjectBuilder<Script> getTermsAggregationScript(Script.Builder s, List<String> months) {
        String script = JsonUtil.toJSONString(months) + ".contains(doc['billingCycle'].value.year.toString()+'-'+(doc['billingCycle'].value.monthValue<10?'0'+doc['billingCycle'].value.monthValue:doc['billingCycle'].value.monthValue.toString()))";
        return s.inline(inlineScript -> inlineScript.lang("painless").source(script));

    }


}
