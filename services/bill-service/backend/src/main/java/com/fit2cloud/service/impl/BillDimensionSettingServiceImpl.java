package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.util.MappingUtil;
import com.fit2cloud.constants.AuthorizeTypeConstants;
import com.fit2cloud.constants.BillAuthorizeConditionTypeConstants;
import com.fit2cloud.constants.BillFieldConstants;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import com.fit2cloud.dao.jentity.BillAuthorizeRuleGroup;
import com.fit2cloud.dao.mapper.BillDimensionSettingMapper;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.IBillDimensionSettingService;
import lombok.SneakyThrows;
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
            StringTermsAggregate sterms = aggregations.aggregations().get(0).aggregation().getAggregate().sterms();
            return sterms.buckets().array().stream().map(item -> new DefaultKeyValue<>(MappingUtil.mapping(groupField, item.key()), item.key())).toList();
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
            return billDimensionSetting;
        } else {
            res.setAuthorizeRule(authorizeRule);
            updateById(res);
        }
        return res;
    }


    @Override
    public void authorize() {
        // 获取到授权规则
        List<BillDimensionSetting> billDimensionSettings = list();
        billDimensionSettings.stream().sorted(Comparator.comparing(BillDimensionSetting::getCreateTime)).forEach(this::authorize);
    }

    @SneakyThrows
    @Override
    public void authorize(BillDimensionSetting billDimensionSetting) {
        BillAuthorizeRule authorizeRule = billDimensionSetting.getAuthorizeRule();
        if (verification(billDimensionSetting.getAuthorizeId(), billDimensionSetting.getType())) {
            Query query = analysisBillAuthorizeRule(authorizeRule);
            UpdateByQueryRequest build = new UpdateByQueryRequest.Builder().index(CloudBill.class.getAnnotation(Document.class).indexName()).query(query).script(getAuthorizeScript(billDimensionSetting)).build();
            elasticsearchClient.updateByQuery(build);
        }
    }

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
        Map<String, Map<String, Object>> properties = (Map<String, Map<String, Object>>) mapping.get("properties");
        if (properties.containsKey("tags")) {
            return getEsField((Map<String, Map<String, Object>>) properties.get("tags").get("properties"), null).stream().map(field -> new DefaultKeyValue<>(field, "tags." + field)).toList();
        }
        return new ArrayList<>();
    }

    /**
     * 获取标签字段
     *
     * @param properties mapping properties
     * @param parentName 传null 用于递归 可以传开头
     * @return 获取es下面的字段
     */
    public List<String> getEsField(Map<String, Map<String, Object>> properties, String parentName) {
        return properties.entrySet().stream().map((t) -> {
            Map<String, Object> value = t.getValue();
            if (value.get("type").toString().equals(co.elastic.clients.elasticsearch._types.mapping.FieldType.Text.jsonValue())) {
                return List.of(StringUtils.isEmpty(parentName) ? t.getKey() : parentName + "." + t.getKey());
            } else {
                return getEsField((Map<String, Map<String, Object>>) value.get("properties"), StringUtils.isEmpty(parentName) ? t.getKey() : parentName + "." + t.getKey());
            }
        }).flatMap(List::stream).toList();
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
        String workspace = "ctx._source['workspaceId']=params.workspaceId;ctx._source['organizationId']=params.organizationId;ctx._source['orgLevel']=params.orgLevel;";
        String org = "ctx._source['organizationId']=params.organizationId;ctx._source['orgLevel']=params.orgLevel;";
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
            res.put("orgLevel", JsonData.of(toOrgLevel(upOrg)));
            res.put("organizationId", JsonData.of(organizationId));
            return res;
        } else {
            List<Organization> upOrg = getUpOrg(setting.getAuthorizeId(), new ArrayList<>());
            res.put("orgLevel", JsonData.of(toOrgLevel(upOrg)));
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
    public Map<String, String> toOrgLevel(List<Organization> organizations) {
        return IntStream.range(0, organizations.size()).boxed().map(index -> new DefaultKeyValue<>(index + 1 + "级组织", organizations.get(index).getId())).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));
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
