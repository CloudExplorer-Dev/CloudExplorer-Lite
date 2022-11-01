package com.fit2cloud.constants;

import com.fit2cloud.common.annotaion.Group;
import com.fit2cloud.es.entity.CloudBill;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:59 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class BillGroupConstants {
    public static Map<String, Group> BILL_GROUP;

    static {
        BILL_GROUP = Arrays.stream(FieldUtils.getAllFields(CloudBill.class))
                .filter(field -> field.isAnnotationPresent(Group.class)).map(field -> {
                    String name = field.getName();
                    Group annotation = field.getAnnotation(Group.class);
                    return new DefaultKeyValue<>(name, annotation);
                }).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));

    }

}
