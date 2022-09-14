package com.fit2cloud.controller.editor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.request.pub.OrderRequest;

import java.awt.*;
import java.beans.PropertyEditorSupport;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  10:20 AM
 * @Version 1.0
 * @注释:
 */
public class CredentialEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        JsonNode platform = JsonUtil.parseObject(text).get("platform");
        Object value = getValue();
        boolean paintable = isPaintable();
        Component customEditor = getCustomEditor();
        Object source = getSource();
        String s = source.toString();
        String javaInitializationString = getJavaInitializationString();
        OrderRequest orderRequest = null;
        try {
            orderRequest = new ObjectMapper().readValue(text, OrderRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        setValue(orderRequest);
    }
}
