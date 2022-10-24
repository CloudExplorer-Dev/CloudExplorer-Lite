package com.fit2cloud.common.utils;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Arrays;
import java.util.List;


/**
 * @Author:张少虎
 * @Date: 2022/9/27  6:27 PM
 * @Version 1.0
 * @注释:
 */
public class QueryUtil {

    public enum CompareType {
        LT, LTE, GT, GTE, EQ, NOT_EQ, IN, NOT_IN, LIKE,NOT_EXIST
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QueryCondition {
        /**
         * 当前条件是否生效
         */
        private boolean condition;
        /**
         * 字段
         */
        private String field;
        /**
         * 值
         */
        private Object value;
        /**
         * 比较条件
         */
        private CompareType compare = CompareType.EQ;
    }

    /**
     * 获取es插叙
     *
     * @param queryConditions 查询条件
     * @return 查询条件
     */
    public static QueryBuilder getRestQuery(QueryCondition... queryConditions) {
        return getRestQuery(Arrays.stream(queryConditions).toList());
    }

    /**
     * 获取es 查询
     *
     * @param queryConditions 查询条件
     * @return 查询条件
     */
    public static QueryBuilder getRestQuery(List<QueryCondition> queryConditions) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (QueryCondition queryCondition : queryConditions) {
            if (queryCondition.condition) {
                switch (queryCondition.compare) {
                    case LT:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(queryCondition.field).lt(queryCondition.value));
                        break;
                    case LTE:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(queryCondition.field).lte(queryCondition.value));
                        break;
                    case GT:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(queryCondition.field).gt(queryCondition.value));
                        break;
                    case GTE:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(queryCondition.field).gte(queryCondition.value));
                    case EQ:
                        boolQueryBuilder.must(QueryBuilders.termQuery(queryCondition.field, queryCondition.value));
                        break;
                    case LIKE:
                        boolQueryBuilder.must(QueryBuilders.matchQuery(queryCondition.field, queryCondition.value));
                    case NOT_EQ:
                        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(queryCondition.field, queryCondition.value));
                        break;
                    case IN:
                        boolQueryBuilder.must(QueryBuilders.termQuery(queryCondition.field, queryCondition.value));
                        break;
                    case NOT_IN:
                        boolQueryBuilder.mustNot(QueryBuilders.termQuery(queryCondition.field, queryCondition.value));
                        break;
                }
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 适用于新版本的查询
     *
     * @param queryConditions 查询对象
     * @return 多条件查询
     */
    public static BoolQuery.Builder getQuery(QueryCondition... queryConditions) {
        return getQuery(Arrays.stream(queryConditions).toList());
    }

    /**
     * 适用于新版本的查询
     *
     * @param queryConditions 查询对象
     * @return 多条件查询
     */
    public static BoolQuery.Builder getQuery(List<QueryCondition> queryConditions) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        for (QueryCondition queryCondition : queryConditions) {
            if (queryCondition.condition) {
                switch (queryCondition.compare) {
                    case LT:
                        boolQueryBuilder.must(new Query.Builder().range(new RangeQuery.Builder().lt(JsonData.of(queryCondition.value)).field(queryCondition.field).build()).build());
                        break;
                    case LTE:
                        boolQueryBuilder.must(new Query.Builder().range(new RangeQuery.Builder().lte(JsonData.of(queryCondition.value)).field(queryCondition.field).build()).build());
                        break;
                    case GT:
                        boolQueryBuilder.must(new Query.Builder().range(new RangeQuery.Builder().gt(JsonData.of(queryCondition.value)).field(queryCondition.field).build()).build());
                        break;
                    case GTE:
                        boolQueryBuilder.must(new Query.Builder().range(new RangeQuery.Builder().gte(JsonData.of(queryCondition.value)).field(queryCondition.field).build()).build());
                    case EQ:
                        boolQueryBuilder.must(new Query.Builder().term(new TermQuery.Builder().value(getFieldValue(queryCondition.value)).field(queryCondition.field).build()).build());
                        break;
                    case LIKE:
                        boolQueryBuilder.must(new Query.Builder().match(new MatchQuery.Builder().query(getFieldValue(queryCondition.value)).field(queryCondition.field).build()).build());
                        break;
                    case NOT_EQ:
                        boolQueryBuilder.mustNot(new Query.Builder().match(new MatchQuery.Builder().query(getFieldValue(queryCondition.value)).field(queryCondition.field).build()).build());
                        break;
                    case IN:
                        boolQueryBuilder.must(new Query.Builder().terms(new TermsQuery.Builder().field(queryCondition.field).terms(getTermsQueryField(queryCondition.value)).build()).build());
                        break;
                    case NOT_IN:
                        boolQueryBuilder.mustNot(new Query.Builder().terms(new TermsQuery.Builder().field(queryCondition.field).terms(getTermsQueryField(queryCondition.value)).build()).build());
                        break;
                    case NOT_EXIST:
                        boolQueryBuilder.must(new Query.Builder().exists(new ExistsQuery.Builder().field(queryCondition.field).build()).build());
                        break;
                }
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 将数据转换为es查询所需数据
     *
     * @param query 查询数据
     * @return es查询数据
     */
    private static FieldValue getFieldValue(Object query) {
        if (query instanceof Long) {
            return FieldValue.of((long) query);
        }
        if (query instanceof Double) {
            return FieldValue.of((double) query);
        }
        if (query instanceof String) {
            return FieldValue.of((String) query);
        }
        if (query instanceof Boolean) {
            return FieldValue.of((boolean) query);
        }
        throw new RuntimeException("非法的参数");
    }

    /**
     * 将数据转换为es查询所需数据
     *
     * @param query 查询数据
     * @return es查询数据
     */
    private static TermsQueryField getTermsQueryField(Object query) {
        if (query instanceof List<?>) {
            List<FieldValue> fieldValues = ((List<?>) query).stream().map(QueryUtil::getFieldValue).toList();
            return new TermsQueryField.Builder().value(fieldValues).build();
        } else {
            throw new RuntimeException("非法的参数");
        }
    }
}
