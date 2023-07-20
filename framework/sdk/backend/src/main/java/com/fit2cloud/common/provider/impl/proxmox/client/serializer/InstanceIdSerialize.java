package com.fit2cloud.common.provider.impl.proxmox.client.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.InstanceId;

import java.io.IOException;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/20  16:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class InstanceIdSerialize extends JsonDeserializer<InstanceId> {
    @Override
    public InstanceId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        InstanceId instanceId = jsonParser.getCodec().readValue(jsonParser, InstanceId.class);
        System.out.println(instanceId);
        return instanceId;

    }
}
