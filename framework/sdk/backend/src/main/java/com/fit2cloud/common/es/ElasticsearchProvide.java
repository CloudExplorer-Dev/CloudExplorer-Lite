package com.fit2cloud.common.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregate;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.es.constants.EsErrorCodeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import org.elasticsearch.search.aggregations.Aggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * es服务
 *
 * @author jianneng
 * @date 2022/9/22 16:54
 **/
@Component
public class ElasticsearchProvide {
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 验证索引是否存在
     *
     * @param index
     * @return
     */
    public boolean existsIndex(String index) {
        return elasticsearchTemplate.indexOps(IndexCoordinates.of(index)).exists();
    }

    /**
     * 默认参数
     *
     * @param index
     * @return
     */
    public SearchRequest.Builder requestBuilder(String index) {
        SearchRequest.Builder builder = new SearchRequest.Builder();
        builder.index(index)
                //过滤字段
                .source(s -> s.filter(f -> f.excludes("@version", "@timestamp", "host", "tags")));
        return builder;
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
                    ValueCountAggregate valueCountAggregate = aggregations.aggregations().get(0).aggregation().getAggregate().valueCount();
                    page.setTotal(Double.valueOf(valueCountAggregate.value()).longValue());
                } catch (Exception ignored) {
                    page.setTotal(response.getTotalHits());
                }
            } else {
                page.setTotal(response.getTotalHits());
            }
            if (null == searchHits || searchHits.size() == 0) {
                return page;
            }
            List<T> resultList = searchHits.stream().map(SearchHit::getContent).toList();
            page.setRecords(resultList);

            return page;
        } catch (ElasticsearchException e) {
            LogUtil.error("[ elasticsearch ]CODE[{}] >>{}", EsErrorCodeConstants.SEARCH_FAILED.getCode(), e.getMessage() + " " + e.getMessage());
            throw new Fit2cloudException(e.status(), e.getMessage());
        } catch (Exception e){
            LogUtil.error("[ elasticsearch ]CODE[{}] >>{}", EsErrorCodeConstants.SEARCH_FAILED.getCode(), e.getMessage() + " " + e.getMessage());
            throw new Fit2cloudException(100010, e.getMessage());
        }
    }

}
