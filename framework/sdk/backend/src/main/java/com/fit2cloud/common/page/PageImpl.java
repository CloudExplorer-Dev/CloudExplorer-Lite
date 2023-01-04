package com.fit2cloud.common.page;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.utils.ColumnNameUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/3  10:43}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class PageImpl<T> extends Page<T> {

    /**
     * @param current         当前页
     * @param size            每页条数
     * @param total           total
     * @param searchCount     是否Count total
     * @param recordClazz     分页泛型对象
     * @param orderItem       排序对象
     * @param appendTableName 是否给排序对象添加表名
     */
    public PageImpl(long current, long size, long total, boolean searchCount, Class<T> recordClazz, OrderItem orderItem, boolean appendTableName) {
        super(current, size, total, searchCount);
        String column = ColumnNameUtil.getColumnName(orderItem.getColumn(), recordClazz, appendTableName);
        orderItem.setColumn(column);
        setOrders(List.of(orderItem));
    }

    /**
     * @param current     当前页
     * @param size        每页条数
     * @param total       total
     * @param searchCount 是否Count total
     * @param orderItem   排序对象
     * @param tableName   表名
     */
    public PageImpl(long current, long size, long total, boolean searchCount, OrderItem orderItem, String tableName) {
        super(current, size, total, searchCount);
        if (StringUtils.isNotEmpty(tableName)) {
            orderItem.setColumn(tableName + "." + orderItem.getColumn());
            setOrders(List.of(orderItem));
        }
    }

    /**
     * @param current     当前页
     * @param size        每页条数
     * @param recordClazz 分页泛型对象
     * @param orderItem   排序对象
     * @param <T>         分页泛型对象
     * @return 分页对象
     */
    public static <T> Page<T> of(long current, long size, Class<T> recordClazz, OrderItem orderItem) {
        return new PageImpl<>(current, size, 0L, true, recordClazz, orderItem, false);
    }

    /**
     * @param current         当前页
     * @param size            每页条数
     * @param recordClazz     分页泛型对象
     * @param orderItem       排序对象
     * @param appendTableName 是否给排序对象添加表名
     * @param <T>             分页泛型对象
     * @return 分页对象
     */
    public static <T> Page<T> of(long current, long size, Class<T> recordClazz, OrderItem orderItem, boolean appendTableName) {
        return new PageImpl<>(current, size, 0L, true, recordClazz, orderItem, appendTableName);
    }

    /**
     * @param current   当前页
     * @param size      每页条数
     * @param orderItem 排序对象
     * @param tableName 表名
     * @param <T>       分页泛型对象
     * @return 分页对象
     */
    public static <T> Page<T> of(long current, long size, OrderItem orderItem, String tableName) {
        return new PageImpl<>(current, size, 0L, true, orderItem, tableName);
    }

}
