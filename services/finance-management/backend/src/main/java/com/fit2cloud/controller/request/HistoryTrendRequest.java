package com.fit2cloud.controller.request;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/28  3:27 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class HistoryTrendRequest {
    /**
     * 根据字段构建es查询
     * 当前对象为冗余对象 所以没有查询条件字段
     *
     * @return es查询
     */
    public List<Query> toQuery() {
        return List.of();
    }
}
