package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.page.PageImpl;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.ComplianceStatus;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanRuleGroupResponse;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceRuleCloudAccount;
import com.fit2cloud.dao.entity.ComplianceRuleGroup;
import com.fit2cloud.dao.jentity.Rule;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.entity.InstanceFieldCompare;
import com.fit2cloud.service.IComplianceRuleGroupService;
import com.fit2cloud.service.IComplianceRuleService;
import com.fit2cloud.service.IComplianceScanService;
import jodd.util.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  16:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class ComplianceScanServiceImpl implements IComplianceScanService {
    @Resource
    private IComplianceRuleService complianceRuleService;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private IComplianceRuleGroupService complianceRuleGroupService;

    @Override
    public List<ComplianceScanResponse> list(ComplianceScanRequest request) {
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        // 获取所有规则
        List<ComplianceRule> rules = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>()
                .eq(StringUtils.isNotEmpty(request.getResourceType()), ComplianceRule::getResourceType, request.getResourceType())
                .eq(ComplianceRule::getEnable, true));
        return rules.stream().map(r -> proxy.scanCompliance(r, request.getCloudAccountId())).toList();
    }

    @Override
    public ComplianceScanResponse scanComplianceByRuleId(String complianceRuleId) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        return proxy.scanCompliance(complianceRule, null);
    }

    @Override
    @Cacheable(value = "compliance_scan", key = "#root.targetClass.simpleName+':compliance_scan_rule_group:'+#complianceRuleGroupId")
    public ComplianceScanRuleGroupResponse scanComplianceRuleGroupByGroupId(String complianceRuleGroupId) {
        return getComplianceScanRuleGroupResponse(complianceRuleGroupId);
    }

    @Override
    public Page<ComplianceScanResponse> page(Integer currentPage, Integer limit, ComplianceScanRequest request) {
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        Page<ComplianceRule> page = PageImpl.of(currentPage, limit, ComplianceRule.class, Objects.isNull(request.getOrder()) ? new OrderItem() : request.getOrder());
        CloudAccount cloudAccount = cloudAccountService.getById(request.getCloudAccountId());
        // todo 构建查询条件
        QueryWrapper<ComplianceRule> wrapper = new QueryWrapper<ComplianceRule>()
                .like(StringUtil.isNotEmpty(request.getComplianceRuleName()), ColumnNameUtil.getColumnName(ComplianceRule::getName, false), request.getComplianceRuleName())
                .eq(StringUtil.isNotEmpty(request.getCloudAccountId()), ColumnNameUtil.getColumnName(ComplianceRule::getPlatform, false), Objects.nonNull(cloudAccount) ? cloudAccount.getPlatform() : "")
                .eq(StringUtils.isNotEmpty(request.getResourceType()), ColumnNameUtil.getColumnName(ComplianceRule::getResourceType, false), request.getResourceType())
                .eq(StringUtils.isNotEmpty(request.getComplianceRuleGroupId()), ColumnNameUtil.getColumnName(ComplianceRule::getRuleGroupId, false), request.getComplianceRuleGroupId())
                .eq(ColumnNameUtil.getColumnName(ComplianceRule::getEnable, false), true);
        //todo 分页查询
        Page<ComplianceRule> complianceRulePage = complianceRuleService.page(page, wrapper);
        Page<ComplianceScanResponse> responsePage = Page.of(currentPage, limit);
        BeanUtils.copyProperties(complianceRulePage, responsePage);
        //todo 转换对象
        responsePage.setRecords(complianceRulePage.getRecords().stream().map(r -> proxy.scanCompliance(r, request.getCloudAccountId())).toList());
        return responsePage;
    }

    @SneakyThrows
    @Override
    public Page<ComplianceResourceResponse> pageResource(String complianceRuleId, Integer currentPage, Integer limit, ComplianceResourceRequest complianceResourceRequest) {
        SearchRequest pageSearch = new SearchRequest.Builder().from((currentPage - 1) * limit).size(limit)
                .query(getResourceQuery(complianceRuleId, complianceResourceRequest))
                .aggregations("valueCount", new ValueCountAggregation.Builder().field("id").build()._toAggregation())
                .build();
        SearchResponse<ResourceInstance> search = elasticsearchClient.search(pageSearch, ResourceInstance.class);
        Page<ComplianceResourceResponse> responsePage = Page.of(currentPage, limit);
        HitsMetadata<ResourceInstance> hits = search.hits();
        List<ComplianceResourceResponse> complianceResourceResponses = searchComplianceResource(complianceRuleId, limit,
                hits.hits().stream()
                        .map(Hit::source)
                        .filter(Objects::nonNull)
                        .map(ResourceInstance::getId).toList(), QueryUtil.getQuery(complianceResourceRequest));

        List<CloudAccount> cloudAccounts = cloudAccountService.list();
        List<ComplianceResourceResponse> complianceResourceResponseList = hits.hits().stream().filter(item -> Objects.nonNull(item.source())).map(item -> {
            ComplianceResourceResponse complianceResourceResponse = new ComplianceResourceResponse();
            ResourceInstance source = item.source();
            BeanUtils.copyProperties(source, complianceResourceResponse);
            complianceResourceResponse.setInstance((Map<String, Object>) source.getInstance().get(source.getPlatform() + "_" + source.getResourceType()));
            complianceResourceResponse.setComplianceStatus(ComplianceStatus.COMPLIANCE);
            complianceResourceResponse.setCloudAccountName(complianceResourceResponse.getResourceName());
            cloudAccounts.stream().filter(c -> c.getId().equals(source.getCloudAccountId())).findFirst().ifPresent(cloudAccount -> complianceResourceResponse.setCloudAccountName(cloudAccount.getName()));
            complianceResourceResponses.stream().filter(c -> c.getId().equals(source.getId())).findFirst().ifPresent(c -> complianceResourceResponse.setComplianceStatus(ComplianceStatus.NOT_COMPLIANCE));
            return complianceResourceResponse;
        }).toList();
        responsePage.setTotal(((Double) search.aggregations().get("valueCount").valueCount().value()).longValue());
        responsePage.setRecords(complianceResourceResponseList);
        return responsePage;
    }

    @Override
    @CachePut(value = "compliance_scan", key = "#root.targetClass.simpleName+':compliance_scan_rule_group:'+#complianceRuleGroupId")
    public ComplianceScanRuleGroupResponse updateCacheScanComplianceRuleGroupByGroupId(String complianceRuleGroupId) {
        return getComplianceScanRuleGroupResponse(complianceRuleGroupId);
    }

    @Override
    public List<ComplianceScanResponse> updateCacheScanComplianceByInstanceType(ResourceTypeConstants resourceTypeConstants) {
        List<ComplianceRule> list = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, resourceTypeConstants.name()));
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        return list.stream().flatMap(r -> proxy.updateCacheScanComplianceByRuleId(r.getId()).stream()).toList();

    }

    @Override
    public List<ComplianceScanResponse> updateCacheScanComplianceByRuleId(String complianceRuleId) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        List<ComplianceScanResponse> responses = new ArrayList<>();
        responses.add(proxy.updateCacheScanComplianceByRuleId(complianceRule, null));
        for (CloudAccount cloudAccount : cloudAccountService.list()) {
            responses.add(updateCacheScanCompliance(complianceRuleId, cloudAccount.getId()));
        }
        return responses;
    }

    @Override
    public ComplianceScanResponse updateCacheScanCompliance(String complianceRuleId, String cloudAccountId) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        return proxy.updateCacheScanComplianceByRuleId(complianceRule, null);
    }

    @CachePut(value = "compliance_scan", key = "#root.targetClass.simpleName+':compliance_scan_rule:'+#complianceRule.id+':'+#complianceRule.platform+':'+#cloudAccountId")
    public ComplianceScanResponse updateCacheScanComplianceByRuleId(ComplianceRule complianceRule, String cloudAccountId) {
        return getComplianceScanResponse(complianceRule, cloudAccountId);
    }


    /**
     * @param complianceRule 合规规则
     * @param cloudAccountId 云账号id
     * @return 合规规则
     */
    @Cacheable(value = "compliance_scan", key = "#root.targetClass.simpleName+':compliance_scan_rule:'+#complianceRule.id+':'+#complianceRule.platform+':'+#cloudAccountId")
    public ComplianceScanResponse scanCompliance(ComplianceRule complianceRule, String cloudAccountId) {
        return getComplianceScanResponse(complianceRule, cloudAccountId);
    }


    /**
     * 查询合规资源
     *
     * @param complianceRuleId 合规规则组id
     * @param limit            每页条数
     * @param resourceIds      资源id数组
     * @param queryList        查询条件
     * @return 合规资源数据
     */
    @SneakyThrows
    private List<ComplianceResourceResponse> searchComplianceResource(String complianceRuleId, Integer limit, List<String> resourceIds, List<Query> queryList) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        List<Query> queries = new ArrayList<>(queryList);
        queries.add(new Query.Builder().terms(new TermsQuery.Builder().terms(new TermsQueryField.Builder().value(resourceIds.stream().map(FieldValue::of).toList()).build()).field("id")
                .build()).build());
        Query query = getQuery(complianceRule.getRules(), queries);
        SearchRequest request = new SearchRequest.Builder().size(limit).query(query).build();
        SearchResponse<ComplianceResourceResponse> search = elasticsearchClient.search(request, ComplianceResourceResponse.class);
        return search.hits().hits().stream().map(Hit::source).filter(Objects::nonNull).toList();
    }

    /**
     * 根据扫描规则获取es查询条件
     *
     * @param complianceRuleId 合规规则id
     * @param request          资源过滤请求对象
     * @return es查询对象
     */
    public Query getResourceQuery(String complianceRuleId, ComplianceResourceRequest request) {
        List<Query> query = QueryUtil.getQuery(request);
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        List<Query> allQueries = new ArrayList<>(query);
        allQueries.add(new Query.Builder().term(new TermQuery.Builder().field("platform").value(complianceRule.getPlatform()).build()).build());
        if (CollectionUtils.isNotEmpty(request.getComplianceStatus()) && new HashSet<>(request.getComplianceStatus()).size() == 1) {
            if (request.getComplianceStatus().contains(ComplianceStatus.NOT_COMPLIANCE.name())) {
                return getQuery(complianceRule.getRules(), query);
            } else {
                List<Query> queries = complianceRule.getRules().stream().map(this::getScriptQuery).toList();
                return new Query.Builder().bool(new BoolQuery.Builder().mustNot(queries).must(allQueries).build()).build();
            }
        } else {
            return new Query.Builder().bool(new BoolQuery.Builder().must(allQueries).build()).build();
        }
    }

    /**
     * @param source             原始对象
     * @param allComplianceCount 所有资源数量
     * @param notComplianceCount 不合规资源数量
     * @return 合规扫描对象
     */
    private ComplianceScanResponse toComplianceScanResponse(Object source, long allComplianceCount, long notComplianceCount) {
        ComplianceScanResponse complianceScanResponse = new ComplianceScanResponse();
        BeanUtils.copyProperties(source, complianceScanResponse);
        complianceScanResponse.setComplianceCount(allComplianceCount - notComplianceCount);
        complianceScanResponse.setNotComplianceCount(notComplianceCount);
        complianceScanResponse.setScanStatus(allComplianceCount == (allComplianceCount - notComplianceCount) ? ComplianceStatus.COMPLIANCE : ComplianceStatus.NOT_COMPLIANCE);
        return complianceScanResponse;
    }


    /**
     * 获取扫描规则
     *
     * @param complianceRule 扫描规则对象
     * @param cloudAccountId 云账户id
     * @return ComplianceScanResponse
     */
    private ComplianceScanResponse getComplianceScanResponse(ComplianceRule complianceRule, String cloudAccountId) {
        List<Rule> rules = complianceRule.getRules();
        List<Query> otherQueries = new ArrayList<>();
        otherQueries.add(new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(complianceRule.getResourceType()).build()).build());
        otherQueries.add(new Query.Builder().term(new TermQuery.Builder().field("platform").value(complianceRule.getPlatform()).build()).build());
        if (StringUtils.isNotEmpty(cloudAccountId)) {
            otherQueries.add(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build());
        }
        // todo 将扫描规则构建为查询语句
        Query query = getQuery(rules, otherQueries);
        // todo 查询不合规资源数量
        Long notComplianceCount = getResourceValueCount(query);
        // todo 查询到所有资源数量
        Long allResourceValueCount = getAllResourceValueCount(complianceRule.getResourceType(), complianceRule.getPlatform(), cloudAccountId);
        return toComplianceScanResponse(complianceRule, allResourceValueCount, notComplianceCount);

    }

    /**
     * 根据合规规则组id 获规则组下面规则的合规情况
     *
     * @param complianceRuleGroupId 合规规则组id
     * @return 合规组结果
     */
    private ComplianceScanRuleGroupResponse getComplianceScanRuleGroupResponse(String complianceRuleGroupId) {
        ComplianceRuleGroup complianceRuleGroup = complianceRuleGroupService.getById(complianceRuleGroupId);
        List<ComplianceRule> complianceRules = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>().eq(ComplianceRule::getRuleGroupId, complianceRuleGroup.getId()).eq(ComplianceRule::getEnable, true));
        List<ComplianceScanResponse> complianceScanResponses = complianceRules.stream().map(rule -> scanComplianceByRuleId(rule.getId())).toList();
        ComplianceScanRuleGroupResponse complianceScanRuleGroupResponse = new ComplianceScanRuleGroupResponse();
        complianceScanRuleGroupResponse.setComplianceRuleCount(complianceScanResponses.stream().filter(c -> c.getScanStatus().equals(ComplianceStatus.COMPLIANCE)).count());
        complianceScanRuleGroupResponse.setNotComplianceRuleCount(complianceScanResponses.stream().filter(c -> c.getScanStatus().equals(ComplianceStatus.NOT_COMPLIANCE)).count());
        if (complianceScanRuleGroupResponse.getNotComplianceRuleCount() > 0) {
            complianceScanRuleGroupResponse.setHigh(complianceScanResponses.stream().filter(c -> c.getScanStatus().equals(ComplianceStatus.NOT_COMPLIANCE) && c.getRiskLevel().equals(RiskLevel.HIGH)).count());
            complianceScanRuleGroupResponse.setLow(complianceScanResponses.stream().filter(c -> c.getScanStatus().equals(ComplianceStatus.NOT_COMPLIANCE) && c.getRiskLevel().equals(RiskLevel.LOW)).count());
            complianceScanRuleGroupResponse.setMiddle(complianceScanResponses.stream().filter(c -> c.getScanStatus().equals(ComplianceStatus.NOT_COMPLIANCE) && c.getRiskLevel().equals(RiskLevel.MIDDLE)).count());
        }
        complianceScanRuleGroupResponse.setRuleGroupName(complianceRuleGroup.getName());
        return complianceScanRuleGroupResponse;
    }

    /***
     * 查询资源数量根据查询条件
     * @param query 查询条件
     * @return 资源数量
     */
    @SneakyThrows
    private Long getResourceValueCount(Query query) {
        Aggregation aggregation = new ValueCountAggregation.Builder().field("id").build()._toAggregation();
        SearchRequest terms = new SearchRequest.Builder().query(query).aggregations("valueCount", aggregation).build();
        SearchResponse<ResourceInstance> search = elasticsearchClient.search(terms, ResourceInstance.class);
        ValueCountAggregate valueCount = search.aggregations().get("valueCount").valueCount();
        return Math.round(valueCount.value());
    }

    /**
     * @param resourceType   资源类型
     * @param platform       供应商 - 云平台
     * @param cloudAccountId 云账号id
     * @return 所有资源数量
     */
    private Long getAllResourceValueCount(String resourceType, String platform, String cloudAccountId) {
        if (StringUtils.isNotEmpty(cloudAccountId)) {
            return getResourceValueCount(new Query.Builder().bool(new BoolQuery.Builder().must(
                            new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(resourceType).build()).build(),
                            new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build())
                    .build()).build());
        } else {
            return getResourceValueCount(new Query.Builder().bool(new BoolQuery.Builder().must(
                            new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(resourceType).build()).build(),
                            new Query.Builder().term(new TermQuery.Builder().field("platform").value(platform).build()).build())
                    .build()).build());

        }
    }

    /**
     * 根据规则获取查询对象
     *
     * @param rules 规则
     * @return 查询对象
     */
    private Query getQuery(List<Rule> rules, List<Query> otherQueries) {
        List<Query> queries = rules.stream().filter(r -> !r.getField().startsWith("filterArray")).map(this::getScriptQuery).collect(Collectors.toCollection(ArrayList::new));
        // 获取嵌套查询
        List<Query> nestedQuery = listNestedQuery(rules);
        queries.addAll(otherQueries);
        queries.addAll(nestedQuery);
        return new Query.Builder().bool(new BoolQuery.Builder().must(queries).build()).build();
    }

    /**
     * 获取 nested 数组嵌套对象查询
     *
     * @param rules 规则
     * @return 查询对象
     */
    private List<Query> listNestedQuery(List<Rule> rules) {
        // todo filterArray 字段需要使用nested查询
        List<Rule> filterArray = rules.stream().filter(r -> r.getField().startsWith("filterArray")).toList();
        Map<String, List<Query>> nestPathScriptQueryMap = filterArray.stream().collect(Collectors.groupingBy(s -> getNestedPath(s.getField()), Collectors.mapping(this::getScriptQuery, Collectors.toList())));
        List<Query> queries = nestPathScriptQueryMap.entrySet().stream().map(n -> new Query.Builder().nested(new NestedQuery.Builder().path(n.getKey())
                .query(new Query.Builder().bool(new BoolQuery.Builder().must(n.getValue()).build()).build()).build()).build()).toList();
        return queries;

    }

    private String getNestedPath(String field) {
        String[] split = field.split("\\.");
        // 注意 filterArray下面存储的都是 filterArray.xxxx:[] 所以当前不会出现越界问题,如果出现越界那就是配置不对
        return split[0] + "." + split[1];
    }


    /**
     * 根据一个规则获取一个Query
     *
     * @param rule 规则
     * @return 查询对象
     */
    private Query getScriptQuery(Rule rule) {
        InstanceFieldCompare instanceFieldCompare = InstanceFieldCompare.valueOf(rule.getCompare());
        HashMap<String, JsonData> params = new HashMap<>() {{
            put("field", JsonData.of(rule.getField()));

        }};
        if (Objects.nonNull(rule.getValue())) {
            params.put("value", JsonData.of(rule.getValue()));
        }
        return new Query.Builder().script(new ScriptQuery.Builder()
                .script(Script.of(s -> s.inline(new InlineScript.Builder().source(instanceFieldCompare.getScript())
                        .params(params).build()))).build()).build();
    }
}
