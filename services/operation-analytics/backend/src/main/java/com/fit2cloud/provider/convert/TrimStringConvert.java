package com.fit2cloud.provider.convert;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.StringUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/28  10:30}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TrimStringConvert extends AbstractBeanField<String, String> {
    @Override
    protected String convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return s.trim().replace("\t", "").replace("\r", "").replace("\n", "");
    }
}
