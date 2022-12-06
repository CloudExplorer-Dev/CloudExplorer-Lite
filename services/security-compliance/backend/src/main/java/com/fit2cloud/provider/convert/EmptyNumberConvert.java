package com.fit2cloud.provider.convert;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/24  10:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class EmptyNumberConvert extends AbstractBeanField<String, Double> {
    @Override
    protected Double convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
