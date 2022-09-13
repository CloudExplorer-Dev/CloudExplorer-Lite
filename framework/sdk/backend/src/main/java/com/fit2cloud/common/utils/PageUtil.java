package com.fit2cloud.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fit2cloud.request.pub.PageOrderRequestInterface;

import java.util.Collections;

public class PageUtil {

    public static <Q extends PageOrderRequestInterface, T> Page<T> of(Q pageRequest, Class<T> clazz) {
        Page<T> page = PageDTO.of(pageRequest.getCurrentPage(), pageRequest.getPageSize());
        if (pageRequest.getOrder() != null) {
            String column = ColumnNameUtil.getColumnName(pageRequest.getOrder().getColumn(), clazz);
            if (column != null) {
                page.setOrders(Collections.singletonList(pageRequest.getOrder().resetColumn(column)));
            }
        }
        return page;
    }
}
