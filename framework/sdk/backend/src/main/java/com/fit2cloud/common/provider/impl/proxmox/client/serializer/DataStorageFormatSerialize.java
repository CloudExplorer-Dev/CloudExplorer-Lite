package com.fit2cloud.common.provider.impl.proxmox.client.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.AccessPattern;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/17  19:08}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class DataStorageFormatSerialize extends JsonDeserializer<List<String>> {


    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<Object> readValue = jsonParser.getCodec().readValue(jsonParser, new TypeReference<List<Object>>() {
        });
        return readValue
                .stream()
                .filter(item -> item instanceof Map)
                .findFirst()
                .map(item -> (Map<String, Integer>) item).map(map -> map.entrySet().stream()
                        .filter(en -> Objects.nonNull(en.getValue()) && en.getValue().equals(1))
                        .map(Map.Entry::getKey).toList())
                .orElse(List.of("raw"));
    }

    @Override
    public List<String> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return List.of("raw");
    }

    @Override
    public AccessPattern getNullAccessPattern() {
        return super.getNullAccessPattern();
    }
}
