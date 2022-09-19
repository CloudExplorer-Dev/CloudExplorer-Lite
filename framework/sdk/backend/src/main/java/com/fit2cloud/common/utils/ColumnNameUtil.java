package com.fit2cloud.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnNameUtil {

    private static String getTableName(Class clazz) {
        TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
        String table = tableName.value();
        if (StringUtils.isEmpty(table)) {
            while (clazz != null) {
                tableName = (TableName) clazz.getAnnotation(TableName.class);
                table = tableName.value();
                if (StringUtils.isNotEmpty(table)) {
                    break;
                }
                clazz = clazz.getSuperclass();
            }
        }
        return table;
    }

    /**
     * 根据实体类方法查找table中column
     *
     * @param func
     * @param appendTableName
     * @param <T>
     * @return
     */
    public static <T> String getColumnName(SFunction<T, ?> func, boolean appendTableName) {
        LambdaMeta meta = LambdaUtils.extract(func);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return getColumnName(fieldName, meta.getInstantiatedClass(), appendTableName);
    }

    /**
     * 根据实体类中参数名查找table中column
     *
     * @param name
     * @param clazz
     * @param appendTableName
     * @return
     */
    public static String getColumnName(String name, Class clazz, boolean appendTableName) {
        String tableName = null;
        if (appendTableName) {
            tableName = getTableName(clazz);
        }
        String columnName = getColumnName(getFieldByName(name, clazz));
        return StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(columnName) ? tableName + "." + columnName : columnName;
    }

    public static String getColumnName(String name, Class clazz) {
        return getColumnName(name, clazz, false);
    }

    private static String getColumnName(Field field) {
        if (field == null) {
            return null;
        }
        if (field.isAnnotationPresent(TableField.class)) {
            TableField column = field.getAnnotation(TableField.class);
            return column.value();
        } else if (field.isAnnotationPresent(TableId.class)) {
            TableId column = field.getAnnotation(TableId.class);
            return column.value();
        }
        return field.getName();
    }

    private static Field getFieldByName(String name, Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        for (Field field : fieldList) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

}
