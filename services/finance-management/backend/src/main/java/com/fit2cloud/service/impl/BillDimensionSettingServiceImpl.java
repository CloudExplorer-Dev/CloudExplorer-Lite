package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.common.cache.CloudAccountCache;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.query.convert.QueryFieldValueConvert;
import com.fit2cloud.common.util.AuthUtil;
import com.fit2cloud.common.util.EsFieldUtil;
import com.fit2cloud.common.util.EsScriptUtil;
import com.fit2cloud.common.util.MappingUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.*;
import com.fit2cloud.controller.request.AuthorizeResourcesRequest;
import com.fit2cloud.controller.request.NotAuthorizeResourcesRequest;
import com.fit2cloud.controller.response.AuthorizeResourcesResponse;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import com.fit2cloud.dao.jentity.BillAuthorizeRuleCondition;
import com.fit2cloud.dao.mapper.BillDimensionSettingMapper;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.IBillDimensionSettingService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BillDimensionSettingServiceImpl extends ServiceImpl<BillDimensionSettingMapper, BillDimensionSetting> implements IBillDimensionSettingService {
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private IBaseWorkspaceService workspaceService;
    @Resource
    private IBaseOrganizationService organizationService;
    @Resource
    private IBaseCloudAccountService cloudAccountService;


    @Override
    public List<DefaultKeyValue<String, String>> authorizeValues(String groupField) {
        String groupKeyByField;
        if (groupField.startsWith("tags")) {
            if (groupField.equals("tags")) {
                return findTags();
            } else {
                groupKeyByField = groupField + "." + FieldType.Keyword.jsonValue();
            }
        } else {
            if (authorizeKeys().stream().noneMatch(item -> item.getValue().equals(groupField))) {
                throw new Fit2cloudException(ErrorCodeConstants.BILL_DIMENSION_SETTING_NOT_SUPPORT_AUTHORIZE_FIELD.getCode(), ErrorCodeConstants.BILL_DIMENSION_SETTING_NOT_SUPPORT_AUTHORIZE_FIELD.getMessage(new Object[]{groupField}));
            }
            groupKeyByField = EsFieldUtil.getGroupKeyByField(groupField);
        }
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder().withAggregation("group", new TermsAggregation.Builder().field(groupKeyByField).size(Integer.MAX_VALUE).build()._toAggregation());
        SearchHits<CloudBill> response = elasticsearchTemplate.search(nativeQueryBuilder.build(), CloudBill.class);
        if (response.hasAggregations()) {
            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
            assert aggregations != null;
            StringTermsAggregate terms = aggregations.aggregations().get(0).aggregation().getAggregate().sterms();
            return terms.buckets().array().stream().map(item -> new DefaultKeyValue<>(MappingUtil.mapping(groupField, item.key()), item.key())).toList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<DefaultKeyValue<String, String>> authorizeKeys() {
        return BillFieldConstants.BILL_FIELD.entrySet().stream().filter(field -> field.getValue().authorize()).map(field -> new DefaultKeyValue<>(field.getValue().label(), field.getKey())).toList();
    }

    @Override
    public BillDimensionSetting getBillDimensionSetting(String authorizeId, String type) {
        return getOne(new LambdaQueryWrapper<BillDimensionSetting>().eq(BillDimensionSetting::getAuthorizeId, authorizeId).eq(BillDimensionSetting::getType, AuthorizeTypeConstants.valueOf(type)));
    }

    @Override
    public BillDimensionSetting saveOrUpdate(String authorizeId, String type, BillAuthorizeRule authorizeRule) {
        if (AuthorizeTypeConstants.valueOf(type).equals(AuthorizeTypeConstants.ORGANIZATION)) {
            if (Objects.isNull(organizationService.getById(authorizeId))) {
                throw new Fit2cloudException(ErrorCodeConstants.BILL_DIMENSION_SETTING_AUTHORIZE_ID_NOT_EXIST.getCode(), ErrorCodeConstants.BILL_DIMENSION_SETTING_AUTHORIZE_ID_NOT_EXIST.getMessage());
            }
        } else {
            if (Objects.isNull(workspaceService.getById(authorizeId))) {
                throw new Fit2cloudException(ErrorCodeConstants.BILL_DIMENSION_SETTING_AUTHORIZE_ID_NOT_EXIST.getCode(), ErrorCodeConstants.BILL_DIMENSION_SETTING_AUTHORIZE_ID_NOT_EXIST.getMessage());
            }
        }
        BillDimensionSetting res = getOne(new LambdaQueryWrapper<BillDimensionSetting>().eq(BillDimensionSetting::getAuthorizeId, authorizeId).eq(BillDimensionSetting::getType, AuthorizeTypeConstants.valueOf(type)));
        if (Objects.isNull(res)) {
            BillDimensionSetting billDimensionSetting = new BillDimensionSetting();
            billDimensionSetting.setAuthorizeId(authorizeId);
            billDimensionSetting.setType(AuthorizeTypeConstants.valueOf(type));
            billDimensionSetting.setAuthorizeRule(authorizeRule);
            billDimensionSetting.setUpdateFlag(true);
            save(billDimensionSetting);
            return billDimensionSetting;
        } else {
            res.setAuthorizeRule(authorizeRule);
            res.setUpdateTime(LocalDateTime.now());
            res.setUpdateFlag(true);
            updateById(res);
        }
        return res;
    }


    @Override
    public void authorize() {
        synchronized (EsWriteLockConstants.WRITE_LOCK) {
            // 获取到授权规则
            List<BillDimensionSetting> billDimensionSettings = list();
            billDimensionSettings.stream().sorted(Comparator.comparing(BillDimensionSetting::getCreateTime)).forEach(this::authorize);
        }
    }

    @Override
    public void authorize(BillDimensionSetting billDimensionSetting) {
        authorize(billDimensionSetting, null, null);
    }

    /**
     * 授权
     *
     * @param billDimensionSetting 授权规则
     * @param month                月份
     * @param cloudAccountId       云账号id
     */
    @Override
    public void authorize(BillDimensionSetting billDimensionSetting, String month, String cloudAccountId) {
        synchronized (EsWriteLockConstants.WRITE_LOCK) {
            if (isAuth(List.of(billDimensionSetting.getAuthorizeRule()))) {
                if (verification(billDimensionSetting.getAuthorizeId(), billDimensionSetting.getType())) {
                    Query query = analysisBillAuthorizeRule(billDimensionSetting.getAuthorizeRule(), cloudAccountId, month);
                    UpdateByQueryRequest build = new UpdateByQueryRequest.Builder()
                            .index(CloudBill.class.getAnnotation(Document.class).indexName())
                            .refresh(true).query(query)
                            .script(getAuthorizeScript(billDimensionSetting)).build();
                    try {
                        elasticsearchClient.updateByQuery(build);
                    } catch (Exception e) {
                        throw new Fit2cloudException(ErrorCodeConstants.BILL_DIMENSION_SETTING_AUTHORIZE_ERROR.getCode(), ErrorCodeConstants.BILL_DIMENSION_SETTING_AUTHORIZE_ERROR.getMessage());
                    }
                }
            }
        }
    }

    public boolean isAuth(List<BillAuthorizeRule> billAuthorizeRuleList) {
        for (BillAuthorizeRule billAuthorizeRule : billAuthorizeRuleList) {
            if (CollectionUtils.isNotEmpty(billAuthorizeRule.getConditions())) {
                return true;
            } else {
                if (CollectionUtils.isNotEmpty(billAuthorizeRule.getChildren())) {
                    boolean auth = isAuth(billAuthorizeRule.getChildren());
                    if (auth) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    @Override
    public void authorize(String billDimensionSettingId, String month, String cloudAccountId) {
        BillDimensionSetting billDimensionSetting = getById(billDimensionSettingId);
        // 清除历史授权
        clearAuthorize(billDimensionSetting.getAuthorizeId(), billDimensionSetting.getType(), month, cloudAccountId);
        // 使用最新授权
        authorize(billDimensionSetting, month, cloudAccountId);
    }

    @Override
    public void authorEditSetting() {
        List<BillDimensionSetting> billDimensionSettings = list()
                .stream()
                .sorted(Comparator.comparing(BillDimensionSetting::getUpdateTime))
                .toList();

        for (BillDimensionSetting billDimensionSetting : billDimensionSettings) {
            if (checkDimensionSetting(billDimensionSetting)) {
                // 清除历史授权数据
                clearAuthorize(billDimensionSetting, null, null);
                // 授权
                authorize(billDimensionSetting);
                // 设置未修改
                billDimensionSetting.setUpdateFlag(false);
                updateById(billDimensionSetting);
            }
        }
    }


    /**
     * 校验当前授权规则是否可进行授权
     *
     * @param billDimensionSetting 授权规则
     * @return 是否可授权 true 可进行授权,false 不可授权
     */
    private boolean checkDimensionSetting(BillDimensionSetting billDimensionSetting) {
        if (billDimensionSetting.getType().equals(AuthorizeTypeConstants.ORGANIZATION)) {
            Organization organization = organizationService.getById(billDimensionSetting.getAuthorizeId());
            if (Objects.nonNull(organization)) {
                return billDimensionSetting.getUpdateFlag().equals(true);
            }
        } else if (billDimensionSetting.getType().equals(AuthorizeTypeConstants.WORKSPACE)) {
            Workspace workspace = workspaceService.getById(billDimensionSetting.getAuthorizeId());
            if (Objects.nonNull(workspace)) {
                return billDimensionSetting.getUpdateFlag().equals(true);
            }
        }
        this.remove(new LambdaQueryWrapper<BillDimensionSetting>().eq(BillDimensionSetting::getAuthorizeId, billDimensionSetting.getAuthorizeId()));
        return false;
    }

    @SneakyThrows
    @Override
    public Page<AuthorizeResourcesResponse> getAuthorizeResources(Integer page, Integer limit, AuthorizeResourcesRequest request) {
        SearchRequest searchRequest = new SearchRequest.Builder()
                .aggregations(getAuthorizeResourcesAggregation(page, limit))
                .query(getQueryByAuthorizeResourcesRequest(request))
                .index(CloudBill.class.getAnnotation(Document.class)
                        .indexName())
                .build();
        return getAuthorizeResourcesResponsePage(page, limit, searchRequest);
    }

    @Override
    @SneakyThrows
    public Page<AuthorizeResourcesResponse> getNotAuthorizeResources(Integer page, Integer limit, NotAuthorizeResourcesRequest request) {
        SearchRequest searchRequest = new SearchRequest.Builder()
                .aggregations(getAuthorizeResourcesAggregation(page, limit))
                .query(getQueryByNotAuthorizeResourcesRequest(request))
                .index(CloudBill.class.getAnnotation(Document.class).indexName())
                .build();
        return getAuthorizeResourcesResponsePage(page, limit, searchRequest);

    }


    /**
     * 清除授权数据
     *
     * @param authorizeId 授权对象id
     * @param type        授权对象类型
     */
    @Override
    public void clearAuthorize(String authorizeId, AuthorizeTypeConstants type) {
        clearAuthorize(authorizeId, type, null, null);
    }

    private void clearAuthorize(String authorizeId, AuthorizeTypeConstants type, String month, String cloudAccountId) {
        synchronized (EsWriteLockConstants.WRITE_LOCK) {
            List<Query> authQueryList = new ArrayList<>();
            Query auth = QueryUtil.getQuery(QueryUtil.CompareType.EQ, type.equals(AuthorizeTypeConstants.ORGANIZATION) ? "organizationId" : "workspaceId", authorizeId);
            if (StringUtils.isNotEmpty(month)) {
                Query monthQuery = new Query.Builder().script(new ScriptQuery.Builder().script(s -> EsScriptUtil.getMonthOrYearScript(s, "MONTH", month)).build()).build();
                authQueryList.add(monthQuery);
            }
            if (StringUtils.isNotEmpty(cloudAccountId)) {
                Query cloudAccountQuery = new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build();
                authQueryList.add(cloudAccountQuery);
            }
            if (type.equals(AuthorizeTypeConstants.ORGANIZATION)) {
                auth = new Query.Builder().bool(new BoolQuery.Builder().must(auth).mustNot(new Query.Builder().exists(new ExistsQuery.Builder().field("workspaceId").build()).build()).build()).build();
            }
            authQueryList.add(auth);
            UpdateByQueryRequest.Builder orgTree = new UpdateByQueryRequest.Builder()
                    .query(new BoolQuery.Builder().must(authQueryList).build()._toQuery())
                    .index(CloudBill.class.getAnnotation(Document.class).indexName())
                    .script(s -> s.inline(s1 -> s1.source("ctx._source['workspaceId']=null;ctx._source['organizationId']=null;ctx._source['orgTree']=params.orgTree;")
                            .params(new HashMap<>() {{
                                put("orgTree", JsonData.of(new HashMap<>()));
                            }})));
            try {
                elasticsearchClient.updateByQuery(orgTree.refresh(true).build());
            } catch (Exception e) {
                throw new Fit2cloudException(1000, e.getMessage());
            }
        }
    }

    @Override
    public void clearAuthorize(String billDimensionSettingId, String month, String cloudAccountId) {
        BillDimensionSetting billDimensionSetting = getById(billDimensionSettingId);
        clearAuthorize(billDimensionSetting.getAuthorizeId(), billDimensionSetting.getType(), month, cloudAccountId);
    }

    @Override
    public void clearAuthorize(BillDimensionSetting billDimensionSetting, String month, String cloudAccountId) {
        clearAuthorize(billDimensionSetting.getAuthorizeId(), billDimensionSetting.getType(), month, cloudAccountId);
    }


    /**
     * 获取炸裂字段
     *
     * @param field 字段field
     * @return 炸裂执行函数
     */
    public String getBurstField(String field) {
        return "(doc['%s'].length>0?doc['%s'][0]:'')".formatted(field, field);
    }

    /**
     * 转换为授权资源对象
     *
     * @param bucket 授权桶对象
     * @return 授权资源对象
     */
    private AuthorizeResourcesResponse toAuthorizeResources(StringTermsBucket bucket) {
        Aggregate hits = bucket.aggregations().get("hits");
        List<Hit<JsonData>> hitList = hits.topHits().hits().hits();
        if (hitList.size() > 0) {
            Hit<JsonData> jsonDataHit = hitList.get(0);
            if (Objects.nonNull(jsonDataHit.source())) {
                CloudBill cloudBill = jsonDataHit.source().to(CloudBill.class);
                AuthorizeResourcesResponse authorizeResourcesResponse = new AuthorizeResourcesResponse();
                authorizeResourcesResponse.setResourceId(cloudBill.getResourceId());
                authorizeResourcesResponse.setResourceName(cloudBill.getResourceName());
                authorizeResourcesResponse.setCloudAccountId(cloudBill.getCloudAccountId());
                authorizeResourcesResponse.setCloudAccountName(CloudAccountCache.getCacheOrUpdate(cloudBill.getCloudAccountId()));
                authorizeResourcesResponse.setProjectId(cloudBill.getProjectId());
                authorizeResourcesResponse.setProjectName(cloudBill.getProjectName());
                authorizeResourcesResponse.setProductId(cloudBill.getProductId());
                authorizeResourcesResponse.setProductName(cloudBill.getProductName());
                authorizeResourcesResponse.setProvider(cloudBill.getProvider());
                authorizeResourcesResponse.setTags(cloudBill.getTags());
                return authorizeResourcesResponse;
            }
        }
        return null;
    }

    /**
     * 获取资源授权对象返回值
     *
     * @param page          分页对象
     * @param limit         每页大小
     * @param searchRequest es查询对手
     * @return 账单资源授权对象
     */
    private Page<AuthorizeResourcesResponse> getAuthorizeResourcesResponsePage(Integer page, Integer limit, SearchRequest searchRequest) throws IOException {
        SearchResponse<CloudBill> search = elasticsearchClient.search(searchRequest, CloudBill.class);
        Map<String, Aggregate> aggregations = search.aggregations();
        Aggregate count = aggregations.get("count");
        Aggregate group = aggregations.get("group");
        long total = count.cardinality().value();

        Aggregation.Builder.ContainerBuilder terms = new Aggregation.Builder().terms(new TermsAggregation.Builder().field("aggregations.ledgerResource.keyword").size(limit).build());
        terms.aggregations("hits", new Aggregation.Builder().topHits(new TopHitsAggregation.Builder().size(1).build()).build());
        // 所有的聚合key
        List<String> keys = group.sterms().buckets().array().stream().map(StringTermsBucket::key).filter(Objects::nonNull).toList();
        SearchRequest hitSearch = new SearchRequest.Builder().query(
                new Query.Builder().bool(new BoolQuery.Builder().must(new TermsQuery.Builder().field("aggregations.ledgerResource.keyword")
                                .terms(new TermsQueryField.Builder()
                                        .value(keys.stream()
                                                .map(k -> new FieldValue.Builder()
                                                        .stringValue(k)
                                                        .build())
                                                .toList())
                                        .build()).build()._toQuery())
                        .build()).build()
        ).aggregations("group", terms.build()).build();
        SearchResponse<CloudBill> hitsRes = elasticsearchClient.search(hitSearch, CloudBill.class);
        Aggregate histGroup = hitsRes.aggregations().get("group");
        List<AuthorizeResourcesResponse> authorizeResourcesResponses = histGroup.sterms().buckets().array().stream().map(this::toAuthorizeResources).filter(Objects::nonNull).toList();
        Page<AuthorizeResourcesResponse> authorizeResourcesResponsePage = Page.of(page, limit, total);
        authorizeResourcesResponsePage.setRecords(authorizeResourcesResponses);
        return authorizeResourcesResponsePage;
    }

    /**
     * 获取查询条件
     *
     * @param request 请求对象
     * @return es查询对象
     */
    private Query getQueryByAuthorizeResourcesRequest(AuthorizeResourcesRequest request) {
        String type = request.getType();
        List<Query> queries = getQuery(request);
        List<Query> qs = new ArrayList<>(queries);
        Query auth = AuthUtil.getOrgAuthQuery(AuthorizeTypeConstants.valueOf(type).equals(AuthorizeTypeConstants.ORGANIZATION) ? RoleConstants.ROLE.ORGADMIN : RoleConstants.ROLE.USER,
                request.getAuthorizeId(),
                org -> organizationService.getOrgLevel(org));
        if (Objects.nonNull(auth)) {
            qs.add(auth);
        }
        return new Query.Builder().bool(new BoolQuery.Builder().filter(qs).build()).build();
    }

    /**
     * 获取未授权账单查询对象
     *
     * @param request 请求对象
     * @return es查询对象
     */
    private Query getQueryByNotAuthorizeResourcesRequest(NotAuthorizeResourcesRequest request) {
        ArrayList<Query> queries = new ArrayList<>(getQuery(request));
        ExistsQuery existsQuery = new ExistsQuery.Builder().field("organizationId").build();
        return new Query.Builder().bool(new BoolQuery.Builder().must(queries).mustNot(s -> s.exists(existsQuery)).build()).build();
    }

    /**
     * 根据条件 构建es查询对象
     *
     * @param request 请求对象
     * @return 查询对象
     */
    private List<Query> getQuery(Object request) {
        return QueryUtil.getQuery(request, QueryFieldValueConvert.builder().field("cloudAccountName").convert(cloudAccountName -> {
            if (Objects.isNull(cloudAccountName) || StringUtils.isEmpty(cloudAccountName.toString())) {
                return null;
            }
            LambdaQueryWrapper<CloudAccount> like = new LambdaQueryWrapper<CloudAccount>().like(true, CloudAccount::getName, cloudAccountName);
            List<CloudAccount> list = cloudAccountService.list(like);
            if (CollectionUtils.isEmpty(list)) {
                return new ArrayList<>();
            }
            return list.stream().map(CloudAccount::getId).toList();
        }).build());
    }


    /**
     * 获取授权资源聚合对象
     * todo 如果使用一个语句聚合数据会很慢
     * {
     * 	"aggregations": {
     * 		"count": {
     * 			"cardinality": {
     * 				"field": "aggregations.ledgerResource.keyword"
     *                        }* 		},
     * 		"group": {
     * 			"aggregations": {
     * 				"hits": {
     * 					"top_hits": {
     * 						"size": 1
     *                    }
     *                },
     * 				"bucket_sort": {
     * 					"bucket_sort": {
     * 						"from": 50,
     * 						"size": 10
     *                    }
     *                }
     *            },
     * 			"terms": {
     * 				"field": "aggregations.ledgerResource.keyword",
     * 				"size": 2147483647
     *            }
     *        }
     *    },
     * 	"query": {
     * 		"bool": {
     * 			"must": [],
     * 			"must_not": [{
     * 				"exists": {
     * 					"field": "organizationId"
     *                }
     *            }]
     *        }
     *    }
     * }
     * 当前查询携带top_hits会很慢,所以俩次查询 反而很快
     *
     * @param currentPage 当前页
     * @param limit       每页多少条
     * @return es聚合对象
     */
    private Map<String, Aggregation> getAuthorizeResourcesAggregation(Integer currentPage, Integer limit) {
        Aggregation count = new Aggregation.Builder().cardinality(CardinalityAggregation.of(s -> s.field("aggregations.ledgerResource.keyword"))).build();
        Aggregation.Builder.ContainerBuilder group = new Aggregation.Builder().terms(new TermsAggregation.Builder().field("aggregations.ledgerResource.keyword").size(Integer.MAX_VALUE).build());
        group.aggregations("bucket_sort", new Aggregation.Builder().bucketSort(BucketSortAggregation.of(s -> s.from(currentPage <= 0 ? 0 : (currentPage - 1) * limit).size(limit))).build());
        HashMap<String, Aggregation> aggregationHashMap = new HashMap<>();
        aggregationHashMap.put("count", count);
        aggregationHashMap.put("group", group.build());
        return aggregationHashMap;
    }


    /**
     * 校验当前授权id是否有效
     *
     * @param authorizeId 授权id
     * @param type        授权类型
     * @return 是否有效
     */
    private boolean verification(String authorizeId, AuthorizeTypeConstants type) {
        if (type.equals(AuthorizeTypeConstants.ORGANIZATION)) {
            Organization organization = organizationService.getById(authorizeId);
            return !Objects.isNull(organization);
        } else {
            Workspace workspace = workspaceService.getById(authorizeId);
            return !Objects.isNull(workspace);
        }
    }


    /**
     * @return tag下面的所有字段可分组字段
     */
    private List<DefaultKeyValue<String, String>> findTags() {
        Map<String, Object> mapping = elasticsearchTemplate.indexOps(CloudBill.class).getMapping();
        return EsFieldUtil.getChildEsField(mapping, "tags");
    }

    /**
     * 获取授权脚本
     *
     * @param billDimensionSetting 授权规则对象
     * @return 授权脚本
     */
    public Script getAuthorizeScript(BillDimensionSetting billDimensionSetting) {
        Map<String, JsonData> params = getParams(billDimensionSetting);
        return new Script.Builder().inline(s -> s.source(getSource(billDimensionSetting.getType())).params(params)).build();
    }

    /**
     * 获取执行脚本
     *
     * @param authorizeType 授权类型
     * @return 执行脚本
     */
    public String getSource(AuthorizeTypeConstants authorizeType) {
        String workspace = "ctx._source['workspaceId']=params.workspaceId;ctx._source['organizationId']=params.organizationId;ctx._source['orgTree']=params.orgTree;";
        String org = "ctx._source['organizationId']=params.organizationId;ctx._source['orgTree']=params.orgTree;ctx._source['workspaceId']=null;";
        return authorizeType.equals(AuthorizeTypeConstants.WORKSPACE) ? workspace : org;
    }

    /**
     * 获取参数
     *
     * @param setting 授权规则对象
     * @return 脚本参数
     */
    public Map<String, JsonData> getParams(BillDimensionSetting setting) {
        HashMap<String, JsonData> res = new HashMap<>();
        if (setting.getType().equals(AuthorizeTypeConstants.WORKSPACE)) {
            // 工作空间id
            Workspace workspace = workspaceService.getById(setting.getAuthorizeId());
            // 组织id
            String organizationId = workspace.getOrganizationId();
            List<Organization> upOrg = getUpOrg(organizationId, new ArrayList<>());
            res.put("workspaceId", JsonData.of(workspace.getId()));
            res.put("orgTree", JsonData.of(toOrgTree(upOrg)));
            res.put("organizationId", JsonData.of(organizationId));
            return res;
        } else {
            List<Organization> upOrg = getUpOrg(setting.getAuthorizeId(), new ArrayList<>());
            res.put("orgTree", JsonData.of(toOrgTree(upOrg)));
            res.put("organizationId", JsonData.of(setting.getAuthorizeId()));
            return res;

        }
    }

    /**
     * 获取当前组织上面的所有组织
     *
     * @param orgId 组织id
     * @param res   数据
     * @return 当前组织以及上级时间
     */
    public List<Organization> getUpOrg(String orgId, List<Organization> res) {
        Organization organization = organizationService.getById(orgId);
        if (Objects.nonNull(organization)) {
            res.add(organization);
            if (StringUtils.isNotEmpty(organization.getPid())) {
                return getUpOrg(organization.getPid(), res);
            }
        }
        return res;
    }

    /**
     * 将n级组织映射为组织 key value的类型
     *
     * @param organizations 组织
     * @return 组织级别
     */
    public Map<String, String> toOrgTree(List<Organization> organizations) {
        return IntStream.range(0, organizations.size()).boxed().map(index -> new DefaultKeyValue<>((organizations.size() - index) + "级组织", organizations.get(index).getId())).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));
    }

    /**
     * 解析账单授权规则
     *
     * @param authorizeRule 账单授权规则
     * @return es查询对象
     */
    public Query analysisBillAuthorizeRule(BillAuthorizeRule authorizeRule) {
        return analysisBillAuthorizeRule(authorizeRule, null, null);
    }

    /**
     * 解析账单授权规则
     *
     * @param authorizeRule  账单授权规则
     * @param cloudAccountId 云账号id
     * @param month          月份 yyyy-MM-dd
     * @return es查询对象
     */
    public Query analysisBillAuthorizeRule(BillAuthorizeRule authorizeRule, String cloudAccountId, String month) {
        BillAuthorizeConditionTypeConstants conditionType = authorizeRule.getConditionType();
        // todo 修改
        List<Query> queries = toQuery(authorizeRule);
        List<Query> qs = new ArrayList<>();
        if (StringUtils.isNotEmpty(cloudAccountId)) {
            Query cloudAccountQuery = new Query.Builder().term(new TermQuery.Builder().field("cloudAccountId").value(cloudAccountId).build()).build();
            qs.add(cloudAccountQuery);
        }
        if (StringUtils.isNotEmpty(month)) {
            Query monthQuery = new Query.Builder().script(new ScriptQuery.Builder().script(s -> EsScriptUtil.getMonthOrYearScript(s, "MONTH", month)).build()).build();
            qs.add(monthQuery);
        }
        if (conditionType.equals(BillAuthorizeConditionTypeConstants.OR)) {
            qs.add(new Query.Builder().bool(new BoolQuery.Builder().should(queries).build()).build());
        } else {
            qs.addAll(queries);
        }
        return new Query.Builder().bool(new BoolQuery.Builder().must(qs).build()).build();
    }

    /**
     * 转换为Query
     *
     * @param billAuthorizeRuleConditionList 授权数组
     * @return Query对象
     */
    public List<Query> groupToQuery(List<BillAuthorizeRuleCondition> billAuthorizeRuleConditionList) {
        return billAuthorizeRuleConditionList
                .stream().map(billAuthorizeRuleCondition -> new Query.Builder().terms(new TermsQuery.Builder().field(billAuthorizeRuleCondition.getField().startsWith("tags") ? billAuthorizeRuleCondition.getField() + "." + FieldType.Keyword.jsonValue() : EsFieldUtil.getGroupKeyByField(billAuthorizeRuleCondition.getField())).terms(TermsQueryField.of(s -> s.value(billAuthorizeRuleCondition.getValue().stream().map(FieldValue::of).toList()))).build()).build()).toList();

    }

    /**
     * 转换为查询条件
     *
     * @param billAuthorizeRule 授权对象
     * @return 查询条件
     */
    private List<Query> toQuery(BillAuthorizeRule billAuthorizeRule) {
        List<Query> queries = new ArrayList<>(groupToQuery(billAuthorizeRule.getConditions()));
        if (CollectionUtils.isNotEmpty(billAuthorizeRule.getChildren())) {
            Query query = toQuery(billAuthorizeRule.getChildren(), billAuthorizeRule.getConditionType());
            queries.add(query);
        }
        return queries;
    }

    /**
     * 转换为查询条件
     *
     * @param billAuthorizeRuleList  授权数组
     * @param conditionTypeConstants 类型
     * @return 查询条件
     */
    public Query toQuery(List<BillAuthorizeRule> billAuthorizeRuleList, BillAuthorizeConditionTypeConstants conditionTypeConstants) {
        List<Query> q = billAuthorizeRuleList.stream().map(authorizeRule -> {
            List<Query> allQueryList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(authorizeRule.getConditions())) {
                List<Query> queries = groupToQuery(authorizeRule.getConditions());
                allQueryList.addAll(queries);
            }
            if (CollectionUtils.isNotEmpty(authorizeRule.getChildren())) {
                Query query = toQuery(authorizeRule.getChildren(), conditionTypeConstants);
                allQueryList.add(query);
            }
            if (CollectionUtils.isEmpty(allQueryList)) {
                return null;
            }
            if (authorizeRule.getConditionType().equals(BillAuthorizeConditionTypeConstants.OR)) {
                return new Query.Builder().bool(new BoolQuery.Builder().should(allQueryList).build()).build();
            } else {
                return new Query.Builder().bool(new BoolQuery.Builder().must(allQueryList).build()).build();
            }

        }).filter(Objects::nonNull).toList();
        if (conditionTypeConstants.equals(BillAuthorizeConditionTypeConstants.OR)) {
            return new Query.Builder().bool(new BoolQuery.Builder().should(q).build()).build();
        } else {
            return new Query.Builder().bool(new BoolQuery.Builder().must(q).build()).build();
        }
    }


}
