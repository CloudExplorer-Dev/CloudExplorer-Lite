package com.fit2cloud.common.es;

import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.es.constants.EsErrorCodeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.QueryUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * es服务
 *
 * @author jianneng
 * {@code @date} 2022/9/22 16:54
 **/
@Component
public class ElasticsearchProvide {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 验证索引是否存在
     *
     * @param index 索引名称
     * @return boolean
     */
    public boolean existsIndex(String index) {
        return elasticsearchTemplate.indexOps(IndexCoordinates.of(index)).exists();
    }

    /**
     * 条件分页查询
     *
     * @param index 索引
     * @param query 条件查询构建起
     * @param <T>   数据类型
     * @param page  分页
     * @return T 类型的集合
     */
    public <T> Page<T> searchByQuery(String index, Query query, Class<T> clazz, Page<T> page) {
        try {
            SearchHits<T> response = elasticsearchTemplate.search(query, clazz, IndexCoordinates.of(index));
            List<SearchHit<T>> searchHits = response.getSearchHits();
            if (response.hasAggregations()) {
                try {
                    ElasticsearchAggregations aggregations = (ElasticsearchAggregations) response.getAggregations();
                    assert aggregations != null;
                    ValueCountAggregate valueCountAggregate = aggregations.aggregations().get(0).aggregation().getAggregate().valueCount();
                    page.setTotal(Double.valueOf(valueCountAggregate.value()).longValue());
                } catch (Exception ignored) {
                    page.setTotal(response.getTotalHits());
                }
            } else {
                page.setTotal(response.getTotalHits());
            }
            if (searchHits.size() == 0) {
                return page;
            }
            List<T> resultList = searchHits.stream().map(SearchHit::getContent).toList();
            page.setRecords(resultList);

            return page;
        } catch (Exception e) {
            LogUtil.error("[ elasticsearch ]CODE[{}] >>{}", EsErrorCodeConstants.SEARCH_FAILED.getCode(), e.getMessage() + " " + e.getMessage());
            throw new Fit2cloudException(100010, e.getMessage());
        }
    }

    /**
     * 条件查询
     *
     * @param index 索引
     * @param query 条件查询构建起
     * @param <T>   数据类型
     * @return T 类型的集合
     */
    public <T> List<T> searchByQuery(String index, Query query, Class<T> clazz) {
        try {
            SearchHits<T> response = elasticsearchTemplate.search(query, clazz, IndexCoordinates.of(index));
            List<SearchHit<T>> searchHits = response.getSearchHits();
            return searchHits.stream().map(SearchHit::getContent).toList();

        } catch (Exception e) {
            LogUtil.error("[ elasticsearch ]CODE[{}] >>{}", EsErrorCodeConstants.SEARCH_FAILED.getCode(), e.getMessage() + " " + e.getMessage());
            throw new Fit2cloudException(100010, e.getMessage());
        }
    }

    /**
     * 删除index下数据
     * clazz 数据对象
     *
     * @param index 索引名称
     * @param query 查询条件
     * @param clazz 对象类型
     */
    public void delete(String index, Query query, Class<?> clazz) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(index);
        try {
            org.springframework.data.elasticsearch.core.query.ByQueryResponse response = elasticsearchTemplate.delete(query, clazz, indexCoordinates);
            LogUtil.debug("清理数量:%s", response.getDeleted());
        } catch (Exception e) {
            LogUtil.error(String.format("[ delete elasticsearch ]CODE[%s] >>%s", EsErrorCodeConstants.SEARCH_FAILED.getCode(), e.getMessage()));
            throw new Fit2cloudException(100010, e.getMessage());
        }
    }

    /**
     * 默认的查询条件
     *
     * @param accountIds 云账号
     * @param metricName 指标名称
     * @param entityType 资源类型
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return List<QueryUtil.QueryCondition>
     */
    public List<QueryUtil.QueryCondition> getDefaultQueryConditions(List<String> accountIds, String metricName, String entityType, Long startTime, Long endTime) {
        List<QueryUtil.QueryCondition> queryConditions = new ArrayList<>();
        QueryUtil.QueryCondition start = new QueryUtil.QueryCondition(Objects.nonNull(startTime), "timestamp", startTime, QueryUtil.CompareType.GTE);
        queryConditions.add(start);
        QueryUtil.QueryCondition end = new QueryUtil.QueryCondition(Objects.nonNull(endTime), "timestamp", endTime, QueryUtil.CompareType.LTE);
        queryConditions.add(end);
        QueryUtil.QueryCondition type = new QueryUtil.QueryCondition(StringUtils.isNotEmpty(entityType), "entityType.keyword", entityType, QueryUtil.CompareType.EQ);
        QueryUtil.QueryCondition metric = new QueryUtil.QueryCondition(StringUtils.isNotEmpty(metricName), "metricName.keyword", metricName, QueryUtil.CompareType.EQ);
        queryConditions.add(metric);
        QueryUtil.QueryCondition accountId = new QueryUtil.QueryCondition(CollectionUtils.isNotEmpty(accountIds), "cloudAccountId.keyword", accountIds, QueryUtil.CompareType.IN);
        queryConditions.add(accountId);
        queryConditions.add(type);
        return queryConditions;
    }

}
