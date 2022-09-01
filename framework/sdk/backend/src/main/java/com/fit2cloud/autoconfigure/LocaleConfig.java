package com.fit2cloud.autoconfigure;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.Validator;

import java.util.Locale;

/**
 * @Author:张少虎
 * @Date: 2022/8/31  4:22 PM
 * @Version 1.0
 * @注释: 国际化配置类  请求头携带 Accept-Language
 *                               zh-CN:中国
 *                               zh-TW:中国繁体
 *                               en-US:英文
 *                   所有响应头都会携带:Content-Language
 *                                zh-CN:中国
 *                                zh-TW:中国繁体
 *                                en-US:英文
 *                    注意!!! 如果不携带请求头默认为zh-CN语言  响应头Content-Language:zh-CN
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {
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
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }


    /**
     * 添加一个网络拦截器
     * @param registry 处理注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CeLocaleChangeInterceptor());
    }


    public static class CeLocaleChangeInterceptor extends LocaleChangeInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            response.setLocale(LocaleContextHolder.getLocale());
            return true;
        }
    }
}
