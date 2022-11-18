package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.mapping.RuntimeFieldType;
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
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.query.convert.QueryFieldValueConvert;
import com.fit2cloud.common.util.EsFieldUtil;
import com.fit2cloud.common.util.MappingUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.AuthorizeTypeConstants;
import com.fit2cloud.constants.BillAuthorizeConditionTypeConstants;
import com.fit2cloud.constants.BillFieldConstants;
import com.fit2cloud.controller.request.AuthorizeResourcesRequest;
import com.fit2cloud.controller.request.NotAuthorizeResourcesRequest;
import com.fit2cloud.controller.response.AuthorizeResourcesResponse;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import com.fit2cloud.dao.jentity.BillAuthorizeRuleGroup;
import com.fit2cloud.dao.mapper.BillDimensionSettingMapper;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.IBillDimensionSettingService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
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
                throw new Fit2cloudException(10000, "不支持的的字段类型");
            }
            groupKeyByField = getGroupKeyByField(groupField);
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
                throw new Fit2cloudException(10001, "授权id不存在");
            }
        } else {
            if (Objects.isNull(workspaceService.getById(authorizeId))) {
                throw new Fit2cloudException(10002, "授权id不存在");
            }
        }
        BillDimensionSetting res = getOne(new LambdaQueryWrapper<BillDimensionSetting>().eq(BillDimensionSetting::getAuthorizeId, authorizeId).eq(BillDimensionSetting::getType, AuthorizeTypeConstants.valueOf(type)));
        if (Objects.isNull(res)) {
            BillDimensionSetting billDimensionSetting = new BillDimensionSetting();
            billDimensionSetting.setAuthorizeId(authorizeId);
            billDimensionSetting.setType(AuthorizeTypeConstants.valueOf(type));
            billDimensionSetting.setAuthorizeRule(authorizeRule);
            save(billDimensionSetting);
            if (CollectionUtils.isNotEmpty(authorizeRule.getBillAuthorizeRuleSettingGroups())) {
                // 授权
                authorize(billDimensionSetting);
            }
            return billDimensionSetting;
        } else {
            // 清除授权数据
            clearAuthorize(authorizeId, AuthorizeTypeConstants.valueOf(type));
            res.setAuthorizeRule(authorizeRule);
            updateById(res);
        }
        if (CollectionUtils.isNotEmpty(res.getAuthorizeRule().getBillAuthorizeRuleSettingGroups())) {
            // 授权
            authorize(res);
        }
        return res;
    }


    @Override
    public void authorize() {
        // 获取到授权规则
        List<BillDimensionSetting> billDimensionSettings = list();
        billDimensionSettings.stream().sorted(Comparator.comparing(BillDimensionSetting::getCreateTime)).forEach(this::authorize);
    }

    @Override
    public void authorize(BillDimensionSetting billDimensionSetting) {
        try {
            BillAuthorizeRule authorizeRule = billDimensionSetting.getAuthorizeRule();
            if (verification(billDimensionSetting.getAuthorizeId(), billDimensionSetting.getType())) {
                Query query = analysisBillAuthorizeRule(authorizeRule);
                UpdateByQueryRequest build = new UpdateByQueryRequest.Builder().index(CloudBill.class.getAnnotation(Document.class).indexName()).refresh(true).query(query).script(getAuthorizeScript(billDimensionSetting)).build();
                elasticsearchClient.updateByQuery(build);
            }
        } catch (Exception e) {
            LogUtil.error("规则授权失败:" + billDimensionSetting + e.getMessage());
            e.printStackTrace();
            throw new Fit2cloudException(111, "规则授权失败");
        }
    }

    @SneakyThrows
    @Override
    public Page<AuthorizeResourcesResponse> getAuthorizeResources(Integer page, Integer limit, AuthorizeResourcesRequest request) {
        String source = "emit(%s)".formatted(String.join("+", getBurstField("projectId"), getBurstField("productId"), getBurstField("resourceId"), findTags().stream().map(t -> getBurstField(t.getValue() + "." + FieldType.Keyword.jsonValue())).collect(Collectors.joining("+"))));
        SearchRequest searchRequest = new SearchRequest.Builder().runtimeMappings("productIdAndProjectIdAndResourceId", s -> s.type(RuntimeFieldType.Keyword).script(a -> a.inline(s1 -> s1.source(source)))).aggregations(getAuthorizeResourcesAggregation(page, limit)).query(getQueryByAuthorizeResourcesRequest(request)).build();
        return getAuthorizeResourcesResponsePage(page, limit, searchRequest);
    }

    @Override
    @SneakyThrows
    public Page<AuthorizeResourcesResponse> getNotAuthorizeResources(Integer page, Integer limit, NotAuthorizeResourcesRequest request) {
        String source = "emit(%s)".formatted(String.join("+", getBurstField("projectId"), getBurstField("productId"), getBurstField("resourceId"), findTags().stream().map(t -> getBurstField(t.getValue() + "." + FieldType.Keyword.jsonValue())).collect(Collectors.joining("+"))));
        SearchRequest searchRequest = new SearchRequest.Builder().runtimeMappings("productIdAndProjectIdAndResourceId", s -> s.type(RuntimeFieldType.Keyword).script(a -> a.inline(s1 -> s1.source(source)))).aggregations(getAuthorizeResourcesAggregation(page, limit)).query(getQueryByNotAuthorizeResourcesRequest(request)).build();
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
        Query auth = QueryUtil.getQuery(QueryUtil.CompareType.EQ, type.equals(AuthorizeTypeConstants.ORGANIZATION) ? "organizationId" : "workspaceId", authorizeId);
        if (type.equals(AuthorizeTypeConstants.ORGANIZATION)) {
            auth = new Query.Builder().bool(new BoolQuery.Builder().must(auth).mustNot(new Query.Builder().exists(new ExistsQuery.Builder().field("workspaceId").build()).build()).build()).build();
        }
        UpdateByQueryRequest.Builder orgTree = new UpdateByQueryRequest.Builder().query(auth).index(CloudBill.class.getAnnotation(Document.class).indexName()).script(s -> s.inline(s1 -> s1.source("ctx._source['workspaceId']=null;ctx._source['organizationId']=null;ctx._source['orgTree']=params.orgTree;").params(new HashMap<>() {{
            put("orgTree", JsonData.of(new HashMap<>()));
        }})));
        try {
            elasticsearchClient.updateByQuery(orgTree.refresh(true).build());
        } catch (IOException e) {
            throw new Fit2cloudException(1000, e.getMessage());
        }
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
        List<AuthorizeResourcesResponse> authorizeResourcesResponses = group.sterms().buckets().array().stream().map(this::toAuthorizeResources).filter(Objects::nonNull).toList();
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
        Query auth = QueryUtil.getQuery(QueryUtil.CompareType.EQ, AuthorizeTypeConstants.valueOf(type).equals(AuthorizeTypeConstants.ORGANIZATION) ? "organizationId" : "workspaceId", request.getAuthorizeId());
        List<Query> queries = getQuery(request);
        List<Query> qs = new ArrayList<>(queries);
        qs.add(auth);
        return new Query.Builder().bool(new BoolQuery.Builder().must(qs).build()).build();
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
            LambdaQueryWrapper<CloudAccount> like = new LambdaQueryWrapper<CloudAccount>().like(true, CloudAccount::getName, cloudAccountName);
            List<CloudAccount> list = cloudAccountService.list(like);
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.stream().map(CloudAccount::getId).toList();
        }).build());
    }


    /**
     * 获取授权资源聚合对象
     *
     * @param currentPage 当前页
     * @param limit       每页多少条
     * @return es聚合对象
     */
    private Map<String, Aggregation> getAuthorizeResourcesAggregation(Integer currentPage, Integer limit) {
        Aggregation count = new Aggregation.Builder().cardinality(CardinalityAggregation.of(s -> s.field("productIdAndProjectIdAndResourceId"))).build();
        Aggregation.Builder.ContainerBuilder group = new Aggregation.Builder().terms(new TermsAggregation.Builder().field("productIdAndProjectIdAndResourceId").size(Integer.MAX_VALUE).build());
        group.aggregations("hits", new Aggregation.Builder().topHits(TopHitsAggregation.of(s -> s.size(1))).build());
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
     * 获取分组字段
     *
     * @param groupField 实例对象字段
     * @return 聚合字段
     */
    public String getGroupKeyByField(String groupField) {
        Field field = FieldUtils.getField(CloudBill.class, groupField, true);
        if (field.isAnnotationPresent(org.springframework.data.elasticsearch.annotations.Field.class)) {
            if (field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class).type().equals(org.springframework.data.elasticsearch.annotations.FieldType.Keyword)) {
                return field.getName();
            }
        } else if (field.isAnnotationPresent(MultiField.class)) {
            MultiField annotation = field.getAnnotation(MultiField.class);
            return Arrays.stream(annotation.otherFields()).filter(a -> a.type().equals(org.springframework.data.elasticsearch.annotations.FieldType.Keyword)).findFirst().map(f -> {
                return field.getName() + "." + f.suffix();
            }).orElseThrow(() -> new Fit2cloudException(111, "不支持的分组字段" + groupField));
        }
        throw new Fit2cloudException(111, "不支持的分组字段" + groupField);
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
        BillAuthorizeConditionTypeConstants conditionType = authorizeRule.getConditionType();
        List<Query> queries = authorizeRule.getBillAuthorizeRuleSettingGroups().stream().map(this::groupToQuery).toList();
        if (conditionType.equals(BillAuthorizeConditionTypeConstants.OR)) {
            return new Query.Builder().bool(new BoolQuery.Builder().should(queries).build()).build();
        } else {
            return new Query.Builder().bool(new BoolQuery.Builder().must(queries).build()).build();
        }

    }

    /**
     * 将规则组 转换为
     *
     * @param group 组织组
     * @return es查询对象
     */
    public Query groupToQuery(BillAuthorizeRuleGroup group) {
        BillAuthorizeConditionTypeConstants conditionType = group.getConditionType();
        List<Query> queries = group.getBillAuthorizeRules().stream().map(billAuthorizeRuleCondition -> new Query.Builder().terms(new TermsQuery.Builder().field(billAuthorizeRuleCondition.getField().startsWith("tags") ? billAuthorizeRuleCondition.getField() + "." + FieldType.Keyword.jsonValue() : getGroupKeyByField(billAuthorizeRuleCondition.getField())).terms(TermsQueryField.of(s -> s.value(billAuthorizeRuleCondition.getValue().stream().map(FieldValue::of).toList()))).build()).build()).toList();
        if (conditionType.equals(BillAuthorizeConditionTypeConstants.OR)) {
            return new Query.Builder().bool(new BoolQuery.Builder().should(queries).build()).build();
        } else {
            return new Query.Builder().bool(new BoolQuery.Builder().must(queries).build()).build();
        }
    }


}
