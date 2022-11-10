package com.fit2cloud.constants;

import com.fit2cloud.common.annotaion.BillField;
import com.fit2cloud.es.entity.CloudBill;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:59 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class BillFieldConstants {
    public static Map<String, BillField> BILL_FIELD;

    static {
        BILL_FIELD = Arrays.stream(FieldUtils.getAllFields(CloudBill.class)).filter(field -> field.isAnnotationPresent(BillField.class)).map(field -> {
            String name = field.getName();
            BillField annotation = field.getAnnotation(BillField.class);
            return new DefaultKeyValue<>(name, annotation);
        }).collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));

    }
}
