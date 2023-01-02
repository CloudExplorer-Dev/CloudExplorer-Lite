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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.ComplianceStatus;
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
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.entity.InstanceFieldCompare;
import com.fit2cloud.service.IComplianceRuleGroupService;
import com.fit2cloud.service.IComplianceRuleService;
import com.fit2cloud.service.IComplianceScanService;
import jodd.util.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private ComplianceRuleMapper complianceRuleMapper;

    @Override
    public List<ComplianceScanResponse> list(ComplianceScanRequest request) {
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        // 获取所有规则
        List<ComplianceRule> rules = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>()
                .eq(StringUtils.isNotEmpty(request.getResourceType()), ComplianceRule::getResourceType, request.getResourceType())
                .eq(ComplianceRule::getEnable, true));
        List<CloudAccount> cloudAccountList = cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>().eq(StringUtils.isNotEmpty(request.getCloudAccountId()), CloudAccount::getId, request.getCloudAccountId()));
        return rules.stream().flatMap(r -> cloudAccountList.stream().filter(cloudAccount -> cloudAccount.getPlatform().equals(r.getPlatform())).map(cloudAccount -> proxy.scanCompliance(r, cloudAccount))).toList();
    }

    @Override
    public List<ComplianceScanResponse> scanComplianceByRuleId(String complianceRuleId) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        List<CloudAccount> cloudAccounts = cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>().eq(CloudAccount::getPlatform, complianceRule.getPlatform()));
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        return cloudAccounts.stream().map(cloudAccount -> proxy.scanCompliance(complianceRule, cloudAccount)).toList();
    }

    @Override
    @Cacheable(value = "compliance_scan", key = "#root.targetClass.simpleName+':'+#root.methodName+':'+#complianceRuleGroupId")
    public ComplianceScanRuleGroupResponse getRuleGroupCompliance(String complianceRuleGroupId) {
        ComplianceRuleGroup complianceRuleGroup = complianceRuleGroupService.getById(complianceRuleGroupId);
        List<ComplianceRule> complianceRules = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>().eq(ComplianceRule::getRuleGroupId, complianceRuleGroup.getId()));
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        Map<Boolean, List<DefaultKeyValue<ComplianceRule, Boolean>>> group = complianceRules.stream().map(rule -> {
            List<ComplianceScanResponse> complianceScanResponses = proxy.scanComplianceByRuleId(rule.getId());
            return new DefaultKeyValue<>(rule, complianceScanResponses.stream().allMatch(c -> c.getScanStatus().equals(ComplianceStatus.COMPLIANCE)));
        }).collect(Collectors.groupingBy(KeyValue::getValue));
        ComplianceScanRuleGroupResponse complianceScanRuleGroupResponse = new ComplianceScanRuleGroupResponse();
        complianceScanRuleGroupResponse.setComplianceRuleCount(group.containsKey(true) ? ((Integer) group.get(true).size()).longValue() : 0);
        complianceScanRuleGroupResponse.setNotComplianceRuleCount(group.containsKey(false) ? ((Integer) group.get(false).size()).longValue() : 0);
        if (group.containsKey(false)) {
            complianceScanRuleGroupResponse.setHigh(group.get(false).stream().filter(g -> g.getKey().getRiskLevel().equals(RiskLevel.HIGH)).toList().size());
            complianceScanRuleGroupResponse.setLow(group.get(false).stream().filter(g -> g.getKey().getRiskLevel().equals(RiskLevel.LOW)).toList().size());
            complianceScanRuleGroupResponse.setMiddle(group.get(false).stream().filter(g -> g.getKey().getRiskLevel().equals(RiskLevel.MIDDLE)).toList().size());
        }
        complianceScanRuleGroupResponse.setRuleGroupName(complianceRuleGroup.getName());
        return complianceScanRuleGroupResponse;
    }

    @Override
    public Page<ComplianceScanResponse> page(Integer currentPage, Integer limit, ComplianceScanRequest request) {
        Page<ComplianceRuleCloudAccount> page = Page.of(currentPage, limit);
        page.setSearchCount(true);
        page.setOptimizeCountSql(false);
        QueryWrapper<ComplianceRuleCloudAccount> wrapper = new QueryWrapper<ComplianceRuleCloudAccount>()
                .like(StringUtil.isNotEmpty(request.getComplianceRuleName()), ColumnNameUtil.getColumnName(ComplianceRuleCloudAccount::getName, false), request.getComplianceRuleName())
                .eq(StringUtils.isNotEmpty(request.getCloudAccountId()), ColumnNameUtil.getColumnName(ComplianceRuleCloudAccount::getCloudAccountId, false), request.getCloudAccountId())
                .eq(StringUtils.isNotEmpty(request.getResourceType()), ColumnNameUtil.getColumnName(ComplianceRuleCloudAccount::getResourceType, false), request.getResourceType())
                .eq(ColumnNameUtil.getColumnName(ComplianceRuleCloudAccount::getEnable, false), true);
        IPage<ComplianceRuleCloudAccount> complianceRuleCloudAccountIPage = complianceRuleMapper.page(page, wrapper);
        Page<ComplianceScanResponse> responsePage = Page.of(currentPage, limit);
        BeanUtils.copyProperties(complianceRuleCloudAccountIPage, responsePage);
        responsePage.setRecords(complianceRuleCloudAccountIPage.getRecords().stream().map(this::scanCompliance).toList());
        return responsePage;
    }

    @SneakyThrows
    @Override
    public Page<ComplianceResourceResponse> pageResource(String complianceRuleId, Integer currentPage, Integer limit, ComplianceResourceRequest complianceResourceRequest) {
        SearchRequest pageSearch = new SearchRequest.Builder().from((currentPage - 1) * limit).size(limit)
                .query(getResourceQuery(complianceRuleId, complianceResourceRequest))
                .aggregations("valueCount", new ValueCountAggregation.Builder().field("id").build()._toAggregation())
                .build();
        List<CloudAccount> cloudAccounts = cloudAccountService.list();
        SearchResponse<ResourceInstance> search = elasticsearchClient.search(pageSearch, ResourceInstance.class);
        Page<ComplianceResourceResponse> responsePage = Page.of(currentPage, limit);
        HitsMetadata<ResourceInstance> hits = search.hits();
        List<ComplianceResourceResponse> complianceResourceResponses = searchComplianceResource(complianceRuleId, limit,
                hits.hits().stream()
                        .map(Hit::source)
                        .filter(Objects::nonNull)
                        .map(ResourceInstance::getId).toList(), QueryUtil.getQuery(complianceResourceRequest));
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

    public Query getResourceQuery(String complianceRuleId, ComplianceResourceRequest request) {
        List<Query> query = QueryUtil.getQuery(request);
        if (CollectionUtils.isNotEmpty(request.getComplianceStatus()) && new HashSet<>(request.getComplianceStatus()).size() == 1) {
            ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
            if (request.getComplianceStatus().contains(ComplianceStatus.NOT_COMPLIANCE.name())) {
                return getQuery(complianceRule.getRules(), query);
            } else {
                List<Query> queries = complianceRule.getRules().stream().map(this::getScriptQuery).toList();
                return new Query.Builder().bool(new BoolQuery.Builder().mustNot(queries).must(query).build()).build();
            }
        } else {
            return new Query.Builder().bool(new BoolQuery.Builder().must(query).build()).build();
        }
    }

    @SneakyThrows
    private List<ComplianceResourceResponse> searchComplianceResource(String complianceRuleId, Integer limit, List<String> resourceIds, List<Query> querys) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        List<Query> queries = new ArrayList<>(querys);
        queries.add(new Query.Builder().terms(new TermsQuery.Builder().terms(new TermsQueryField.Builder().value(resourceIds.stream().map(FieldValue::of).toList()).build()).field("id")
                .build()).build());
        Query query = getQuery(complianceRule.getRules(), queries);
        SearchRequest request = new SearchRequest.Builder().size(limit).query(query).build();
        SearchResponse<ComplianceResourceResponse> search = elasticsearchClient.search(request, ComplianceResourceResponse.class);
        return search.hits().hits().stream().map(Hit::source).filter(Objects::nonNull).toList();
    }


    private ComplianceScanResponse toComplianceScanResponse(Object source, long allComplianceCount, long notComplianceCount) {
        ComplianceScanResponse complianceScanResponse = new ComplianceScanResponse();
        BeanUtils.copyProperties(source, complianceScanResponse);
        complianceScanResponse.setComplianceCount(allComplianceCount - notComplianceCount);
        complianceScanResponse.setNotComplianceCount(notComplianceCount);
        complianceScanResponse.setScanStatus(allComplianceCount == (allComplianceCount - notComplianceCount) ? ComplianceStatus.COMPLIANCE : ComplianceStatus.NOT_COMPLIANCE);
        return complianceScanResponse;
    }

    /**
     * 获取合规数据
     *
     * @param complianceRuleCloudAccount 合规云账号信息
     * @return 规则合规数据
     */
    public ComplianceScanResponse scanCompliance(ComplianceRuleCloudAccount complianceRuleCloudAccount) {
        ComplianceScanServiceImpl proxy = (ComplianceScanServiceImpl) AopContext.currentProxy();
        ComplianceRule complianceRule = new ComplianceRule();
        BeanUtils.copyProperties(complianceRuleCloudAccount, complianceRule);
        CloudAccount cloudAccount = new CloudAccount();
        cloudAccount.setId(complianceRuleCloudAccount.getCloudAccountId());
        cloudAccount.setName(complianceRuleCloudAccount.getCloudAccountName());
        return proxy.scanCompliance(complianceRule, cloudAccount);
    }

    /**
     * @param complianceRule 合规规则
     * @param cloudAccount   云账号对象
     * @return 合规规则
     */
    @Cacheable(value = "compliance_scan", key = "#root.targetClass.simpleName+':'+#root.methodName+':['+#complianceRule.id+'_'+#cloudAccount.id+']'")
    public ComplianceScanResponse scanCompliance(ComplianceRule complianceRule, CloudAccount cloudAccount) {
        List<Rule> rules = complianceRule.getRules();
        List<Query> otherQueries = new ArrayList<>();
        otherQueries.add(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccount.getId()).build()).build());
        Query query = getQuery(rules, otherQueries);
        // todo 不合规资源
        Long notComplianceCount = getResourceValueCount(query);
        // 所有资源
        Long allCount = getResourceValueCount(new Query.Builder().bool(new BoolQuery.Builder().must(
                        new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(complianceRule.getResourceType()).build()).build(),
                        new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccount.getId()).build()).build())
                .build()).build());
        ComplianceScanResponse complianceScanResponse = toComplianceScanResponse(complianceRule, allCount, notComplianceCount);
        complianceScanResponse.setCloudAccountId(cloudAccount.getId());
        complianceScanResponse.setCloudAccountName(cloudAccount.getName());
        return complianceScanResponse;
    }


    @SneakyThrows
    private Long getResourceValueCount(Query query) {
        Aggregation aggregation = new ValueCountAggregation.Builder().field("id").build()._toAggregation();
        SearchRequest terms = new SearchRequest.Builder().query(query).aggregations("valueCount", aggregation).build();
        SearchResponse<ResourceInstance> search = elasticsearchClient.search(terms, ResourceInstance.class);
        ValueCountAggregate valueCount = search.aggregations().get("valueCount").valueCount();
        return Math.round(valueCount.value());
    }


    /**
     * 根据规则获取查询对象
     *
     * @param rules 规则
     * @return 查询对象
     */
    public Query getQuery(List<Rule> rules, List<Query> otherQueries) {
        List<Query> queries = rules.stream().map(this::getScriptQuery).collect(Collectors.toCollection(ArrayList::new));
        queries.addAll(otherQueries);
        return new Query.Builder().bool(new BoolQuery.Builder().must(queries).build()).build();
    }


    /**
     * 根据一个规则获取一个Query
     *
     * @param rule 规则
     * @return 查询对象
     */
    public Query getScriptQuery(Rule rule) {
        InstanceFieldCompare instanceFieldCompare = InstanceFieldCompare.valueOf(rule.getCompare());
        HashMap<String, JsonData> params = new HashMap<>() {{
            put("field", JsonData.of(rule.getField()));
            put("value", JsonData.of(rule.getValue()));
        }};
        return new Query.Builder().script(new ScriptQuery.Builder()
                .script(Script.of(s -> s.inline(new InlineScript.Builder().source(instanceFieldCompare.getScript())
                        .params(params).build()))).build()).build();
    }
}
