package com.fit2cloud.common.es;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregate;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.es.constants.EsErrorCodeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.es.entity.PerfMetricMonitorData;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
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
            List<T> resultList = searchHits.stream().map(SearchHit::getContent).toList();
            return resultList;

        } catch (ElasticsearchException e) {
            LogUtil.error("[ elasticsearch ]CODE[{}] >>{}", EsErrorCodeConstants.SEARCH_FAILED.getCode(), e.getMessage() + " " + e.getMessage());
            throw new Fit2cloudException(e.status(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error("[ elasticsearch ]CODE[{}] >>{}", EsErrorCodeConstants.SEARCH_FAILED.getCode(), e.getMessage() + " " + e.getMessage());
            throw new Fit2cloudException(100010, e.getMessage());
        }
    }

    /**
     * 批量存储监控数据
     *
     * @param perfMetricMonitorDataList
     */
    public void bulkInsert(List<PerfMetricMonitorData> perfMetricMonitorDataList, String index) {
        int counter = 0;
        try {
            List<IndexQuery> queries = new ArrayList<>();
            System.out.println("完成批量插入数据!");
            for (PerfMetricMonitorData perfMetricMonitorData : perfMetricMonitorDataList) {
                //数据索引ID，使用云账号ID+资源类型+资源ID+指标+时间点做为索引
                StringBuffer idSB = new StringBuffer();
                idSB.append(perfMetricMonitorData.getCloudAccountId());
                idSB.append("-");
                idSB.append(perfMetricMonitorData.getEntityType());
                idSB.append("-");
                idSB.append(perfMetricMonitorData.getInstanceId());
                idSB.append("-");
                idSB.append(perfMetricMonitorData.getMetricName());
                idSB.append("-");
                idSB.append(perfMetricMonitorData.getTimestamp());
                perfMetricMonitorData.setId(idSB.toString());
                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(idSB.toString())
                        .withObject(perfMetricMonitorData)
                        .withIndex(index).build();
                queries.add(indexQuery);
                if (counter % 1000 == 0) {
                    elasticsearchTemplate.bulkIndex(queries, PerfMetricMonitorData.class);
                    queries.clear();
                }
                counter++;
            }
            System.out.println("批次总计：" + counter);
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries, PerfMetricMonitorData.class);
                System.out.println("插入总计：" + queries.size());
            } else {
                System.out.println("插入总计：" + (counter * 1000));
            }
            System.out.println("完成批量插入数据!");
        } catch (Exception e) {
            System.out.println("数据批量插入ES失败:" + e.getMessage());
            throw e;
        }
    }

}
