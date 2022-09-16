package com.fit2cloud.common.utils;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.JavaType;
import com.fit2cloud.security.CeGrantedAuthority;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class CeGrantedAuthorityListTypeHandler extends JacksonTypeHandler {


    public CeGrantedAuthorityListTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    protected Object parse(String json) {
        try {
            JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, CeGrantedAuthority.class);
            return getObjectMapper().readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
