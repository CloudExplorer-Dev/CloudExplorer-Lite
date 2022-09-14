package com.fit2cloud.controller.editor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fit2cloud.request.pub.OrderRequest;

import java.beans.PropertyEditorSupport;

/**
 * @Author:张少虎
 * @Date: 2022/8/29  9:33 AM
 * @Version 1.0
 * @注释:
 */
public class OrderEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        OrderRequest orderRequest = null;
        try {
            orderRequest = new ObjectMapper().readValue(text, OrderRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        setValue(orderRequest);
    }



}
