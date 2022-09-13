package com.fit2cloud.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnNameUtil {

    public static String getColumnName(String name, Class clazz) {
        return getColumnName(getFieldByName(name, clazz));
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
