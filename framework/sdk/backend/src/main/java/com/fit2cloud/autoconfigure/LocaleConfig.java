package com.fit2cloud.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @Author:张少虎
 * @Date: 2022/8/31  4:22 PM
 * @Version 1.0
 * @注释: 国际化配置类  请求头携带 Accept-Language
 * zh-CN:中国
 * zh-TW:中国繁体
 * en-US:英文
 * 所有响应头都会携带:Content-Language
 * zh-CN:中国
 * zh-TW:中国繁体
 * en-US:英文
 * 注意!!! 如果不携带请求头默认为zh-CN语言  响应头Content-Language:zh-CN
 */
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class LocaleConfig {
    /**
     * 国际化消息源
     */
    @Resource
    private MessageSource messageSource;


    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        //设置默认区域
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
    }


    /**
     * 使用自定义LocalValidatorFactoryBean，
     * 设置Spring国际化消息源
     */
    @Bean
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }


}
