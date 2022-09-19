package com.fit2cloud.common.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fit2cloud.request.pub.PageOrderRequestInterface;

import java.util.Collections;

public class PageUtil {

    public static <Q extends PageOrderRequestInterface, T> Page<T> of(Q pageRequest, Class<T> clazz, boolean appendTableName) {
        return of(pageRequest, clazz, null, appendTableName);
    }

    public static <Q extends PageOrderRequestInterface, T> Page<T> of(Q pageRequest, Class<T> clazz) {
        return of(pageRequest, clazz, null, false);
    }

    public static <Q extends PageOrderRequestInterface, T> Page<T> of(Q pageRequest, Class<T> clazz, OrderItem defaultOrder) {
        return of(pageRequest, clazz, defaultOrder, false);
    }

    public static <Q extends PageOrderRequestInterface, T> Page<T> of(Q pageRequest, Class<T> clazz, OrderItem defaultOrder, boolean appendTableName) {
        Page<T> page = PageDTO.of(pageRequest.getCurrentPage(), pageRequest.getPageSize());
        if (pageRequest.getOrder() != null) {
            String column = ColumnNameUtil.getColumnName(pageRequest.getOrder().getColumn(), clazz, appendTableName);
            if (column != null) {
                page.setOrders(Collections.singletonList(pageRequest.getOrder().resetColumn(column)));
            } else {
                if (defaultOrder != null) {
                    page.setOrders(Collections.singletonList(defaultOrder));
                }
            }
        }
        return page;
    }
}
