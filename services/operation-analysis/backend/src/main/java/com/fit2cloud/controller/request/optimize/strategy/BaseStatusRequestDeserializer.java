package com.fit2cloud.controller.request.optimize.strategy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author jianneng
 * @date 2023/4/10 17:16
 **/
public class BaseStatusRequestDeserializer extends JsonDeserializer<BaseStatusRequest> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public BaseStatusRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return mapper.readValue(jsonParser.getText(), BaseStatusRequest.class);
    }
}
