package com.fit2cloud.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  11:39 PM
 * @Version 1.0
 * @注释: 操作spring ioc 工具类
 */


@Component
public class SpringUtil implements ApplicationContextAware, BeanFactoryAware {
    private static ApplicationContext applicationContext;
    private static DefaultListableBeanFactory listableBeanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        SpringUtil.listableBeanFactory = listableBeanFactory;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据名称从ioc容器中获取bean
     *
     * @param name bean名称
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public static void auto(Object o) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(o);

    }

    /**
     * 根据Class从ioc容器中获取bean
     *
     * @param clz beanClass
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return (T) applicationContext.getBean(clz);
    }

    /**
     * 根据Class从ioc容器中获取bean
     *
     * @param clz beanClass
     * @param <T>
     * @return bean实例，没有实例则返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBeanWithoutException(Class<T> clz) {
        try {
            return getBean(clz);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getBeanList(Class<T>[] clzs) throws BeansException {
        List<T> ts = new ArrayList<>();
        for (Class<T> clz : clzs) {
            T bean = getBean(clz);
            ts.add(bean);
        }
        return ts;
    }

    /**
     * 将bean放入ioc容器
     *
     * @param beanName 实例名称
     * @param object   实例
     */
    public static void setBean(String beanName, Object object) {
        //注意这里放入的bean在容器中是单例的
        listableBeanFactory.registerSingleton(beanName, object);
    }


    /**
     * 根据beanName销毁单利的bean
     *
     * @param beanName
     */
    public static void removeSingletonBean(String beanName) {
        listableBeanFactory.destroySingleton(beanName);
    }

    /**
     * 手动把对象放入容器，但是可以设置作用域
     *
     * @param beanName
     * @param clazz
     */
    public static void setBean(String beanName, Class<?> clazz) {
        BeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        //设置作用域
        beanDefinition.setScope("prototype");
        listableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    //根据beanName删除使用BeanDefinition创建的bean , Spring默认就是使用BeanDefinition创建的bean对象

    /**
     * 根据beanName删除使用beanDefinition创建的beadn，spring默认就是使用BeanDefinition创建bean对象
     *
     * @param beanName
     */
    public static void removeBean(String beanName) {
        listableBeanFactory.removeBeanDefinition(beanName);
    }

}
