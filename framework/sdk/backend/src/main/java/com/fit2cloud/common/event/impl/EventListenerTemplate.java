package com.fit2cloud.common.event.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fit2cloud.common.event.EventListener;
import com.fit2cloud.common.event.annotaion.Event;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.ClassScanUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  15:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class EventListenerTemplate implements EventListener {
    @Resource(name = "workThreadPool")
    private ThreadPoolExecutor threadPoolExecutor;
    private static final Map<String, DefaultKeyValue<Class<?>, Method>> events = new HashMap<>();

    static {
        List<Class<?>> classList = ClassScanUtil.getClassList("com.fit2cloud");
        for (Class<?> aClass : classList) {
            if (aClass.isAnnotationPresent(Event.class)) {
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Event.class)) {
                        try {

                            events.put(method.getAnnotation(Event.class).value(), new DefaultKeyValue<>(aClass, method));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
        }
    }

    @Override
    public void on(String event, Object[] args) {
        if (events.containsKey(event)) {
            DefaultKeyValue<Class<?>, Method> e = events.get(event);
            CompletableFuture.runAsync(() -> {
                try {
                    Type[] genericParameterTypes = e.getValue().getGenericParameterTypes();
                    Object[] arg = new Object[args.length];
                    for (int i = 0; i < args.length && i < genericParameterTypes.length; i++) {
                        String s = JsonUtil.toJSONString(args[i]);
                        JavaType javaType = JsonUtil.mapper.getTypeFactory().constructType(genericParameterTypes[i]);
                        arg[i] = JsonUtil.mapper.readValue(s, javaType);
                    }
                    Object execBean = null;
                    try {
                        execBean = SpringUtil.getBean(e.getKey());
                    } catch (Exception ex) {
                        execBean = e.getKey().getConstructor().newInstance();
                    }
                    e.getValue().invoke(execBean, arg);
                } catch (Exception ex) {
                    LogUtil.error(ex);
                    throw new RuntimeException(ex);
                }
            }, threadPoolExecutor);

        }
    }


}
