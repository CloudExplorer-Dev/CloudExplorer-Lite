package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.ScrollRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.mapper.BaseJobRecordResourceMappingMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.ConditionTypeConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.ScanRuleConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.view.ComplianceGroupRequest;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.SupportCloudAccountResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.SupportPlatformResourceResponse;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanResultResponse;
import com.fit2cloud.dao.constants.ComplianceStatus;
import com.fit2cloud.dao.constants.ResourceType;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceScanResourceResult;
import com.fit2cloud.dao.entity.ComplianceScanResult;
import com.fit2cloud.dao.jentity.Rule;
import com.fit2cloud.dao.jentity.Rules;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.InstanceFieldCompare;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.response.JobRecordResourceResponse;
import com.fit2cloud.service.IComplianceRuleService;
import com.fit2cloud.service.IComplianceScanResourceResultService;
import com.fit2cloud.service.IComplianceScanResultService;
import com.fit2cloud.service.IComplianceScanService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transform.transforms.QueryConfig;
import org.elasticsearch.client.transform.transforms.SourceConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.fit2cloud.provider.entity.InstanceFieldCompare.*;
import static com.fit2cloud.provider.entity.InstanceFieldCompare.NOT_EXIST;

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
    private BaseJobRecordResourceMappingMapper jobRecordResourceMappingMapper;
    @Resource
    private IComplianceScanResultService complianceScanResultService;
    @Resource
    private IComplianceScanResourceResultService complianceScanResourceResultService;


    @SneakyThrows
    @Override
    public Page<ComplianceResourceResponse> pageResource(String complianceRuleId, Integer currentPage, Integer limit, ComplianceResourceRequest complianceResourceRequest) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        // todo 构建全量数据查询
        SearchRequest pageSearch = new SearchRequest.Builder().from((currentPage - 1) * limit).size(limit)
                .query(getResourceQuery(complianceRule, complianceResourceRequest))
                .aggregations("valueCount", new ValueCountAggregation.Builder().field("id").build()._toAggregation())
                .build();
        SearchResponse<ResourceInstance> search = elasticsearchClient.search(pageSearch, ResourceInstance.class);
        // todo 根据条件的全量分页数据
        HitsMetadata<ResourceInstance> hits = search.hits();
        Page<ComplianceResourceResponse> responsePage = Page.of(currentPage, limit);
        // todo 查询资源 根据当前规则所属 如果当前规则为视为不合规 那么这些就是不合规资源 如果规则为视为合规 那么这些就是合规资源
        List<ComplianceResourceResponse> complianceResourceResponses = searchComplianceResource(complianceRule, limit,
                hits.hits().stream()
                        .map(Hit::source)
                        .filter(Objects::nonNull)
                        .map(ResourceInstance::getId).toList(),
                QueryUtil.getQuery(complianceResourceRequest));
        // 获取全部云账号数据
        List<CloudAccount> cloudAccounts = cloudAccountService.list();
        // 转换重新设置属性
        List<ComplianceResourceResponse> complianceResourceResponseList = hits.hits()
                .stream().filter(item -> Objects.nonNull(item.source()))
                .map(item -> getComplianceResourceResponse(complianceResourceResponses, cloudAccounts, item.source()))
                .toList();
        responsePage.setTotal(((Double) search.aggregations().get("valueCount").valueCount().value()).longValue());
        responsePage.setRecords(complianceResourceResponseList);
        return responsePage;
    }


    @SneakyThrows
    private SearchRequest getPageComplianceSearch(ComplianceRule rule, String cloudAccountId, ComplianceStatus status) {
        ComplianceResourceRequest complianceResourceRequest = new ComplianceResourceRequest();
        complianceResourceRequest.setComplianceStatus(status);
        complianceResourceRequest.setCloudAccountId(cloudAccountId);
        complianceResourceRequest.setResourceType(rule.getResourceType());
        SearchRequest pageSearch = new SearchRequest.Builder()
                .index(ResourceInstance.class.getAnnotation(Document.class).indexName())
                .query(getResourceQuery(rule, complianceResourceRequest))
                .aggregations(Map.of("id", new Aggregation.Builder().terms(new TermsAggregation.Builder().field("id").size(Integer.MAX_VALUE).build()).build()))
                .build();
        return pageSearch;
    }

    @SneakyThrows
    private SearchResponse<ResourceInstance> listComplianceResource(SearchRequest searchRequest) {
        return elasticsearchClient.search(searchRequest, ResourceInstance.class);
    }

    @SneakyThrows
    private void updateByQuery(Query query) {
        UpdateByQueryRequest build = new UpdateByQueryRequest.Builder().index(ResourceInstance.class.getAnnotation(Document.class).indexName()).query(query)
                .script(new Script.Builder().inline(s -> s.source("ctx._source['status']=false;")).build())
                .refresh(true)
                .build();
        elasticsearchClient.updateByQuery(build);
    }

    private List<Query> getComplianceOtherQueries(ComplianceRule complianceRule, String cloudAccountId) {
        List<Query> otherQueries = new ArrayList<>();
        otherQueries.add(new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(complianceRule.getResourceType()).build()).build());
        otherQueries.add(new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build());
        return otherQueries;
    }


    /**
     * @param complianceResourceResponses 根据规则查询到的数据
     * @param cloudAccounts               云账号数据
     * @param resourceInstance            资源实例对象
     * @return 资源响应对象
     */
    private static ComplianceResourceResponse getComplianceResourceResponse(List<ComplianceResourceResponse> complianceResourceResponses,
                                                                            List<CloudAccount> cloudAccounts,
                                                                            ResourceInstance resourceInstance) {
        ComplianceResourceResponse complianceResourceResponse = new ComplianceResourceResponse();
        BeanUtils.copyProperties(resourceInstance, complianceResourceResponse);
        // 设置实例数据
        complianceResourceResponse.setInstance((Map<String, Object>) resourceInstance.getInstance().get(resourceInstance.getPlatform() + "_" + resourceInstance.getResourceType()));
        // 视为不合规
        complianceResourceResponse.setComplianceStatus(ComplianceStatus.COMPLIANCE);
        complianceResourceResponses.stream().filter(c -> c.getId().equals(resourceInstance.getId())).findFirst().ifPresent(c -> complianceResourceResponse.setComplianceStatus(ComplianceStatus.NOT_COMPLIANCE));
        complianceResourceResponse.setCloudAccountName(complianceResourceResponse.getResourceName());
        cloudAccounts.stream().filter(c -> c.getId().equals(resourceInstance.getCloudAccountId())).findFirst().ifPresent(cloudAccount -> complianceResourceResponse.setCloudAccountName(cloudAccount.getName()));
        return complianceResourceResponse;
    }


    @Override
    public List<SupportCloudAccountResourceResponse> listSupportCloudAccountResource() {
        List<CloudAccount> cloudAccounts = cloudAccountService.list();
        return cloudAccounts.stream()
                .filter(cloudAccount -> Arrays.stream(ProviderConstants.values()).anyMatch(p -> StringUtils.equals(p.name(), cloudAccount.getPlatform())))
                .map(cloudAccount -> {
                    List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> exec = CommonUtil.exec(ICloudProvider.of(cloudAccount.getPlatform()), ICloudProvider::getResourceSyncDimensionConstants);
                    List<DefaultKeyValue<String, String>> resourceTypes = exec.stream().map(DefaultKeyValue::getKey).map(resourceTypeConstants -> new DefaultKeyValue<>(resourceTypeConstants.getMessage(), resourceTypeConstants.name())).toList();
                    SupportCloudAccountResourceResponse cloudAccountResourceResponse = new SupportCloudAccountResourceResponse();
                    cloudAccountResourceResponse.setCloudAccount(cloudAccount);
                    cloudAccountResourceResponse.setResourceTypes(resourceTypes);
                    return cloudAccountResourceResponse;
                })
                .toList();
    }

    @Override
    public List<JobRecordResourceResponse> listJobRecord() {
        List<CloudAccount> cloudAccounts = cloudAccountService.list();
        List<String> supportResourceIds = cloudAccounts.stream()
                .filter(cloudAccount -> Arrays.stream(ProviderConstants.values()).anyMatch(p -> StringUtils.equals(p.name(), cloudAccount.getPlatform())))
                .map(CloudAccount::getId)
                .toList();
        return jobRecordResourceMappingMapper.findLastResourceJobRecord(supportResourceIds, List.of(JobTypeConstants.SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB.getCode()));

    }

    @Override
    public List<SupportPlatformResourceResponse> listSupportPlatformResource() {
        return Arrays.stream(ProviderConstants.values()).map(platform -> {
            List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> exec = CommonUtil.exec(ICloudProvider.of(platform.name()), ICloudProvider::getResourceSyncDimensionConstants);
            List<DefaultKeyValue<String, String>> resourceTypes = exec.stream().map(resourceTypeConstants -> new DefaultKeyValue<>(resourceTypeConstants.getKey().getMessage(), resourceTypeConstants.getKey().name())).toList();
            SupportPlatformResourceResponse supportPlatformResourceResponse = new SupportPlatformResourceResponse();
            supportPlatformResourceResponse.setPlatform(platform.name());
            supportPlatformResourceResponse.setResourceTypes(resourceTypes);
            return supportPlatformResourceResponse;
        }).toList();

    }


    @Override
    public List<ComplianceScanResult> scanCompliance(ResourceTypeConstants resourceTypeConstants) {
        return complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, resourceTypeConstants.name()))
                .stream()
                .map(this::scanCompliance)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public List<ComplianceScanResourceResult> scanComplianceResource(ResourceTypeConstants resourceTypeConstants) {
        List<ComplianceRule> complianceRules = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, resourceTypeConstants.name()));
        List<ComplianceScanResourceResult> resultAll = new ArrayList<>();
        for (ComplianceRule complianceRule : complianceRules) {
            List<ComplianceScanResourceResult> complianceScanResourceResults = cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>()
                            .eq(CloudAccount::getPlatform, complianceRule.getPlatform()))
                    .stream()
                    .flatMap(cloudAccount -> listComplianceScanResourceResult(complianceRule, cloudAccount.getId()).stream())
                    .toList();
            resultAll.addAll(complianceScanResourceResults);
        }
        return resultAll;
    }

    @Override
    public List<ComplianceScanResourceResult> scanComplianceResource(ResourceTypeConstants resourceType, String cloudAccountId) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        List<ComplianceRule> complianceRules = complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>()
                .eq(ComplianceRule::getResourceType, resourceType.name())
                .eq(ComplianceRule::getPlatform, cloudAccount.getPlatform()));
        List<ComplianceScanResourceResult> resultAll = new ArrayList<>();
        for (ComplianceRule complianceRule : complianceRules) {
            ComplianceResourceRequest complianceResourceRequest = new ComplianceResourceRequest();
            complianceResourceRequest.setResourceType(complianceRule.getResourceType());
            complianceResourceRequest.setCloudAccountId(cloudAccountId);
            List<ComplianceScanResourceResult> complianceScanResourceResults = listComplianceScanResourceResult(complianceRule, cloudAccountId)
                    .stream()
                    .toList();
            resultAll.addAll(complianceScanResourceResults);
        }
        return resultAll;
    }

    @SneakyThrows
    private List<ComplianceScanResourceResult> listComplianceScanResourceResult(ComplianceRule rule, String cloudAccountId) {
        SearchRequest pageSearch = getPageComplianceSearch(rule, cloudAccountId, ComplianceStatus.COMPLIANCE);
        SearchResponse<ResourceInstance> response = elasticsearchClient.search(pageSearch, ResourceInstance.class);
        List<String> resourceIds = response.aggregations().get("id").sterms().buckets().array().stream().map(StringTermsBucket::key).toList();
        List<ComplianceScanResourceResult> complianceScanResourceResults = resourceIds.stream().map(id -> new ComplianceScanResourceResult(null, rule.getId(), rule.getResourceType(), cloudAccountId, id, ComplianceStatus.COMPLIANCE, null, null)).toList();
        SearchResponse<ResourceInstance> notComplianceResource = elasticsearchClient.search(getPageComplianceSearch(rule, cloudAccountId, ComplianceStatus.NOT_COMPLIANCE), ResourceInstance.class);
        List<ComplianceScanResourceResult> notComplianceResourceList = notComplianceResource.aggregations().get("id").sterms().buckets().array().stream().map(StringTermsBucket::key).toList().stream().map(id -> new ComplianceScanResourceResult(null, rule.getId(), rule.getResourceType(), cloudAccountId, id, ComplianceStatus.NOT_COMPLIANCE, null, null)).toList();
        List<ComplianceScanResourceResult> all = new ArrayList<>(complianceScanResourceResults);
        all.addAll(notComplianceResourceList);
        return all;
    }

    @Override
    public List<ComplianceScanResult> scanCompliance(ResourceTypeConstants resourceTypeConstants, String cloudAccountId) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        return complianceRuleService.list(new LambdaQueryWrapper<ComplianceRule>().eq(ComplianceRule::getResourceType, resourceTypeConstants.name())
                        .eq(ComplianceRule::getPlatform, cloudAccount.getPlatform()))
                .stream()
                .map(complianceRule -> this.scan(complianceRule, cloudAccountId))
                .toList();
    }

    @Override
    public void scanComplianceResourceOrSave(ResourceTypeConstants resourceType, String cloudAccountId) {
        complianceScanResourceResultService.saveOrUpdate(scanComplianceResource(resourceType, cloudAccountId), resourceType, cloudAccountId);
    }




    @Override
    public void scanComplianceOrSave() {
        complianceScanResultService.saveOrUpdate(scanCompliance());
    }

    @Override
    public void scanComplianceOrSave(String complianceRuleId) {
        complianceScanResultService.saveOrUpdate(scanCompliance(complianceRuleId));
    }

    @Override
    public void scanComplianceOrSave(ComplianceRule complianceRule) {
        complianceScanResultService.saveOrUpdate(scanCompliance(complianceRule));
    }

    @Override
    public void scanComplianceOrSave(ResourceTypeConstants resourceTypeConstants, String cloudAccountId) {
        complianceScanResultService.saveOrUpdate(scanCompliance(resourceTypeConstants, cloudAccountId));
    }

    @Override
    public void scanComplianceOrSave(ResourceTypeConstants resourceTypeConstants) {
        complianceScanResultService.saveOrUpdate(scanCompliance(resourceTypeConstants));
    }

    @Override
    public List<ComplianceScanResult> scanCompliance() {
        return complianceRuleService
                .list()
                .stream()
                .map(this::scanCompliance)
                .flatMap(List::stream)
                .toList();
    }


    @Override
    public List<ComplianceScanResult> scanCompliance(String complianceRuleId) {
        ComplianceRule complianceRule = complianceRuleService.getById(complianceRuleId);
        return scanCompliance(complianceRule);
    }

    @Override
    public List<ComplianceScanResult> scanCompliance(ComplianceRule complianceRule) {
        List<CloudAccount> cloudAccounts = cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>().eq(CloudAccount::getPlatform, complianceRule.getPlatform()));
        return cloudAccounts
                .stream()
                .map(cloudAccount -> scan(complianceRule, cloudAccount.getId()))
                .toList();
    }

    /**
     * 扫描
     *
     * @param complianceRule 扫描规则
     * @param cloudAccountId 云账号id
     * @return 扫描结果
     */
    public ComplianceScanResult scan(ComplianceRule complianceRule, String cloudAccountId) {
        Rules rules = complianceRule.getRules();
        List<Query> otherQueries = getComplianceOtherQueries(complianceRule, cloudAccountId);
        // todo 根据查询条件 构建出不合规的查询条件
        Query query = getQuery(rules, otherQueries, ScanRuleConstants.NOT_COMPLIANCE);
        // todo 根据规则查询资源数量
        Long notComplianceCount = getResourceValueCount(query);
        // todo 查询到所有资源数量
        Long allResourceValueCount = getAllResourceValueCount(complianceRule.getResourceType(), complianceRule.getPlatform(), cloudAccountId);
        // todo 合规资源数
        int complianceCount = allResourceValueCount.intValue() - notComplianceCount.intValue();
        return new ComplianceScanResult(
                null,
                complianceRule.getId(),
                complianceRule.getResourceType(),
                cloudAccountId,
                complianceCount,
                notComplianceCount.intValue(),
                allResourceValueCount.intValue(),
                allResourceValueCount > 0 && notComplianceCount > 0 ? ComplianceStatus.NOT_COMPLIANCE : ComplianceStatus.COMPLIANCE,
                null,
                null
        );
    }

    /**
     * 查询资源
     *
     * @param complianceRule 合规规则
     * @param limit          每页条数
     * @param resourceIds    当前页的资源id
     * @param queryList      查询条件
     * @return 查询资源 如果规则属于合规 那么查询出来的是合规资源 如果规则视为不合规 那么查询出来的是不合规资源
     */
    @SneakyThrows
    private List<ComplianceResourceResponse> searchComplianceResource(ComplianceRule complianceRule, Integer limit, List<String> resourceIds, List<Query> queryList) {
        List<Query> queries = new ArrayList<>(queryList);
        queries.add(new Query.Builder().terms(new TermsQuery.Builder()
                .terms(new TermsQueryField.Builder().value(resourceIds.stream().map(FieldValue::of).toList()).build()).field("id")
                .build()).build());
        Query query = getQuery(complianceRule.getRules(), queries, ScanRuleConstants.NOT_COMPLIANCE);
        SearchRequest request = new SearchRequest.Builder().size(limit).query(query).build();
        SearchResponse<ComplianceResourceResponse> search = elasticsearchClient.search(request, ComplianceResourceResponse.class);
        return search.hits().hits().stream().map(Hit::source).filter(Objects::nonNull).toList();
    }

    /**
     * 根据扫描规则获取es查询条件
     *
     * @param complianceRule 合规规则对象
     * @param request        请求对象
     * @return es查询对象
     */
    private Query getResourceQuery(ComplianceRule complianceRule, ComplianceResourceRequest request) {
        List<Query> query = new ArrayList<>(QueryUtil.getQuery(request));
        query.add(new Query.Builder().term(new TermQuery.Builder().field("resourceType").value(complianceRule.getResourceType()).build()).build());
        query.add(new Query.Builder().term(new TermQuery.Builder().field("platform").value(complianceRule.getPlatform()).build()).build());
        // 规则视为合规
        if (Objects.equals(request.getComplianceStatus(), ComplianceStatus.COMPLIANCE)) {
            return getQuery(complianceRule.getRules(), query, ScanRuleConstants.COMPLIANCE);
        } else if (Objects.equals(request.getComplianceStatus(), ComplianceStatus.NOT_COMPLIANCE)) {
            return getQuery(complianceRule.getRules(), query, ScanRuleConstants.NOT_COMPLIANCE);
        } else {
            return new Query.Builder().bool(new BoolQuery.Builder().must(query).build()).build();
        }
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
     * @param rule         规则对象
     * @param otherQueries 其他条件
     * @return 查询对象
     */
    private Query getQuery(Rules rule, List<Query> otherQueries, ScanRuleConstants status) {
        // 获取不合规的查询条件
        if (status.equals(ScanRuleConstants.NOT_COMPLIANCE)) {
            // 如果当前规则视为合规
            if (rule.getConditionType().equals(ConditionTypeConstants.AND)) {
                // 视为合规 然后获取不合规的查询条件 则需要排除Rule的规则
                if (rule.getScanRule().equals(ScanRuleConstants.COMPLIANCE)) {
                    return getQueryByNotRuleMustQueryList(rule.getRules(), otherQueries, ConditionTypeConstants.AND);
                } else {
                    // 反之只需要符合规则则为不合规查询条件
                    return getQueryByRule(rule.getRules(), otherQueries, ConditionTypeConstants.AND);
                }
            } else {
                // 如果視為合規
                if (rule.getScanRule().equals(ScanRuleConstants.COMPLIANCE)) {
                    return getQueryByNotRuleMustQueryList(rule.getRules(), otherQueries, ConditionTypeConstants.OR);
                } else {
                    return getQueryByRule(rule.getRules(), otherQueries, ConditionTypeConstants.OR);
                }
            }
        } else {
            // 如果当前规则视为合规
            if (rule.getConditionType().equals(ConditionTypeConstants.AND)) {
                // 如果视为合规
                if (rule.getScanRule().equals(ScanRuleConstants.COMPLIANCE)) {
                    return getQueryByRule(rule.getRules(), otherQueries, ConditionTypeConstants.AND);
                } else {
                    return getQueryByNotRuleMustQueryList(rule.getRules(), otherQueries, ConditionTypeConstants.AND);
                }
            } else {
                // 如果視為合規
                if (rule.getScanRule().equals(ScanRuleConstants.COMPLIANCE)) {
                    return getQueryByRule(rule.getRules(), otherQueries, ConditionTypeConstants.OR);
                } else {
                    return getQueryByNotRuleMustQueryList(rule.getRules(), otherQueries, ConditionTypeConstants.OR);
                }
            }
        }
    }

    /**
     * 获取查询Query 不等于规则条件 并且等于must条件
     *
     * @param rules         规则条件
     * @param mustQueryList most条件
     * @param conditionType 类型 AND是规则之间是并且的关系 OR 是规则之间是或者的关系
     * @return 查询条件
     */
    private Query getQueryByNotRuleMustQueryList(List<Rule> rules, List<Query> mustQueryList, ConditionTypeConstants conditionType) {
        // 获取不适用嵌套查询的Query
        List<Query> queries = rules.stream().filter(this::isNotNestedQuery)
                .map(this::getScriptQuery).collect(Collectors.toCollection(ArrayList::new));
        // 获取使用嵌套查询的Query
        List<Query> nestedQuery = listNestedQuery(rules);
        queries.addAll(nestedQuery);

        Query query = conditionType.equals(ConditionTypeConstants.AND) ? new BoolQuery.Builder().must(queries).build()._toQuery() : new BoolQuery.Builder().should(queries).build()._toQuery();
        Query not = new BoolQuery.Builder().mustNot(query).build()._toQuery();
        List<Query> all = new ArrayList<>(mustQueryList);
        all.add(not);
        all.addAll(mustQueryList);
        return new BoolQuery.Builder().must(all).build()._toQuery();
    }

    /**
     * 判断是否使用嵌套nested查询
     *
     * @param rule 规则对象
     * @return 是否使用nested查询 使用嵌套查询返回true
     */
    private boolean isNestedQuery(Rule rule) {
        List<InstanceFieldCompare> nestedArrayCompare = List.of(EQ, GE, GT, LE, LT);
        // filterArray 用于存储nested查询 复杂数组
        if (rule.getField().startsWith("filterArray")) {
            return nestedArrayCompare.contains(InstanceFieldCompare.valueOf(rule.getCompare()));
        } else {
            return false;
        }
    }

    /**
     * 是否不使用嵌套查询
     *
     * @param rule 规则
     * @return 不使用嵌套查询返回true
     */
    private boolean isNotNestedQuery(Rule rule) {
        return !isNestedQuery(rule);
    }

    private Query getQueryByRule(List<Rule> rules, List<Query> mustQueryList, ConditionTypeConstants conditionType) {
        List<Query> queries = rules.stream().filter(this::isNotNestedQuery).map(this::getScriptQuery).collect(Collectors.toCollection(ArrayList::new));
        // 获取嵌套查询
        List<Query> nestedQuery = listNestedQuery(rules);
        queries.addAll(nestedQuery);
        BoolQuery.Builder bool = new BoolQuery.Builder();
        Query query = conditionType.equals(ConditionTypeConstants.AND) ? new BoolQuery.Builder().must(queries).build()._toQuery() : new BoolQuery.Builder().should(queries).build()._toQuery();
        List<Query> all = new ArrayList<>(mustQueryList);
        all.add(query);
        bool.must(all);
        return new BoolQuery.Builder().must(all).build()._toQuery();
    }


    /**
     * 获取 nested 数组嵌套对象查询
     *
     * @param rules 规则
     * @return 查询对象
     */
    private List<Query> listNestedQuery(List<Rule> rules) {
        // todo filterArray 字段需要使用nested查询
        List<Rule> filterArray = rules.stream().filter(this::isNestedQuery).toList();
        Map<String, List<Query>> nestPathScriptQueryMap = filterArray.stream().collect(Collectors.groupingBy(s -> getNestedPath(s.getField()), Collectors.mapping(this::getScriptQuery, Collectors.toList())));
        List<Query> queries = nestPathScriptQueryMap.entrySet().stream().map(n -> new Query.Builder().nested(new NestedQuery.Builder().path(n.getKey())
                .query(new Query.Builder().bool(new BoolQuery.Builder().must(n.getValue()).build()).build()).build()).build()).toList();
        return queries;

    }

    /**
     * 获取 nested查询 path
     *
     * @param field 字段
     * @return nested查询path
     */
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
