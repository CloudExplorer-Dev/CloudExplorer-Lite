package com.fit2cloud.common.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.common.platform.credential.Credential;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class AbstractBaseCloudProvider<C extends Credential> implements IBaseCloudProvider {
    /**
     * 获取认证信息
     *
     * @param credential 认证字符串
     * @return 认证对象
     */
    protected C getCredential(String credential) {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        try {
            return new ObjectMapper().readValue(credential, new ObjectMapper().constructType(trueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
