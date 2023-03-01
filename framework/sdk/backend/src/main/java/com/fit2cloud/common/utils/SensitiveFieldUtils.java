package com.fit2cloud.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.constants.SensitiveFieldConstants;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author jianneng
 * @date 2023/3/1 10:42
 **/
public class SensitiveFieldUtils {
    private static List<String> sensitiveFields = new ArrayList<String>();

    static {
        synchronized(SensitiveFieldUtils.class){
            if(CollectionUtils.isEmpty(sensitiveFields)){
                SensitiveFieldConstants[] fieldConstants = SensitiveFieldConstants.values();
                sensitiveFields.addAll(Arrays.stream(fieldConstants).map(SensitiveFieldConstants::name).toList());
            }
        }
    }


    /**
     * 敏感字段值特殊处理
     */
    public static String desensitization(String jsonString){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Object node = mapper.readValue(jsonString,Object.class);
            if(node instanceof ArrayList<?>){
                ArrayNode resultArrayNode = JsonUtil.parseArray("[]");
                ArrayNode arrayNode = mapper.readValue(jsonString, ArrayNode.class);
                arrayNode.forEach(v->{
                    ObjectNode objectNode = (ObjectNode) v;
                    for(String sf : sensitiveFields){
                        if(v.has(sf)) {
                            String value = v.get(sf).textValue();
                            if(value!=null){
                                objectNode.put(sf,"********");
                            }
                        }
                    }
                    resultArrayNode.add(objectNode);
                });
                return JsonUtil.toJSONString(resultArrayNode);
            }
            if(node instanceof LinkedHashMap<?,?>){
                ObjectNode nodeObject = mapper.readValue(jsonString, ObjectNode.class);
                    for(String sf : sensitiveFields){
                        if(nodeObject.has(sf)) {
                            String value = nodeObject.get(sf).textValue();
                            if(value!=null){
                                nodeObject.put(sf,"********");
                            }
                        }
                    }
                    return JsonUtil.toJSONString(nodeObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonString;
    }

}
