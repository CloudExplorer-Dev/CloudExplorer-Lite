package com.fit2cloud.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @Author:张少虎
 * @Date: 2022/9/14  10:42 AM
 * @Version 1.0
 * @注释:
 */
@Component
public class LocaleUtil implements ApplicationContextAware {
    private static MessageSource messageSource;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LocaleUtil.messageSource = applicationContext.getBean(MessageSource.class);
    }

    /**
     * 获取国际化message
     *
     * @param code           code
     * @param args           占位参数
     * @param defaultMessage 默认值
     * @return 国际化文本
     */
    public static String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage) {
        return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * 获取国际化message
     *
     * @param code           code
     * @param args           占位参数
     * @param defaultMessage 默认值
     * @param locale         地区
     * @return 国际化文本
     */
    public static String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * 获取国际化message
     *
     * @param code           code
     * @param defaultMessage 默认值
     * @return 国际化文本
     */
    public static String getMessage(String code, @Nullable String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, LocaleContextHolder.getLocale());
    }
}
