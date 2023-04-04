package com.fit2cloud.controller.request;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/28  3:29 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class BillExpensesRequest {
    /**
     * 冗余
     * 将当前对象转换为查询对象
     *
     * @return 查询条件
     */
    public List<Query> toQuery() {
        return List.of();
    }
}
