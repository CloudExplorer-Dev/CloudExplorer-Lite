package com.fit2cloud.common.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.es.constants.EsErrorCodeConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * es服务
 * @author jianneng
 * @date 2022/9/22 16:54
 **/
@Component
public class ElasticsearchProvide {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    /**
     * 验证索引是否存在
     * @param index
     * @return
     */
    public boolean existsIndex(String index) {
        try {
            BooleanResponse booleanResponse = elasticsearchClient.indices()
                    .exists(existsRequest ->
                            existsRequest.index(index)
                    );
            return booleanResponse.value();
        }catch (Exception e){
            throw new Fit2cloudException(EsErrorCodeConstants.INDEX_NOT_EXIST.getCode(), EsErrorCodeConstants.INDEX_NOT_EXIST.getMessage());
        }
    }

    /**
     * 默认参数
     * @param index
     * @return
     */
    public SearchRequest.Builder requestBuilder(String index){
        SearchRequest.Builder builder=new SearchRequest.Builder();
        builder.index(index)
                //过滤字段
                .source(s->s.filter(f->f.excludes("@version","@timestamp","host","tags")));
        return  builder;
    }

    /**
     * 条件分页查询
     *
     * @param index         索引
     * @param builder 条件查询构建起
     * @param <T>           数据类型
     * @param page           分页
     * @return T 类型的集合
     */
    public <T> Page searchByQuery(String index, SearchRequest.Builder builder, Class<T> clazz, Page page) {
        try {
            SearchResponse<T> response = elasticsearchClient.search(builder.build(), clazz);
            List<Hit<T>> hits = response.hits().hits();
            if (null == hits || hits.size() == 0) {
                return page;
            }
            List<T> resultList = new ArrayList<>(hits.size());
            response.hits().hits().stream().map(Hit::source).forEach(v->{resultList.add(v);});
            page.setRecords(resultList);
            page.setTotal(response.hits().total().value());
            return page;
        } catch (ElasticsearchException e) {
            LogUtil.error("[ elasticsearch ]CODE[{}] >>{}",EsErrorCodeConstants.SEARCH_FAILED.getCode(),e.getMessage()+" "+e.getMessage());
            throw new Fit2cloudException(e.status(),e.getMessage());
        } catch (IOException e) {
            LogUtil.error("[ elasticsearch ][{}] >>{}",index,e.getMessage());
            throw new Fit2cloudException(EsErrorCodeConstants.SEARCH_FAILED.getCode(), EsErrorCodeConstants.SEARCH_FAILED.getMessage()+" "+e.getMessage());
        }
    }

}
