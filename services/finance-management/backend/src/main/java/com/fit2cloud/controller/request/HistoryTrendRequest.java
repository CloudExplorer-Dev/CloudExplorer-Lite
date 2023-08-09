package com.fit2cloud.controller.request;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/28  3:27 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class HistoryTrendRequest extends BaseViewRequest {


    /**
     * 根据字段构建es查询
     * 当前对象为冗余对象 所以没有查询条件字段
     *
     * @return es查询
     */
    @Override
    public List<Query> toQuery() {
        return super.toQuery();
    }


}
