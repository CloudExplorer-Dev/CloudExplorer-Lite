package com.fit2cloud.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  2:43 PM
 * @Version 1.0
 * @注释:
 */
public class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 转换为格式化的json
        //mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //修改日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 将对象转为json字符串
     *
     * @param object 需要转换的对象
     * @return 字符串
     */
    public static String toJSONString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(">> convert to json failed ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param json  需要转换的json字符串
     * @param clazz 转换后的对象class
     * @param <T>   转换后的对象
     * @return 转换后的对象Class转入的类型
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            LOGGER.error(">> parseObject failed ", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 字符串转ObjectNode类型
     *
     * @param json 需要转换的json字符串
     * @return 类似于 fastjson 的 JSONObject
     */
    public static ObjectNode parseObject(String json) {
        try {
            return (ObjectNode) mapper.readTree(json);
        } catch (Exception e) {
            LOGGER.error(">> parseObject to ObjectNode  failed ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串转对象列表
     *
     * @param json  需要转换的json字符串
     * @param clazz 数组中每个元素的泛形对象 例如 User.clsss
     * @param <T>   数组中每个元素的泛形
     * @return 返回一个解析后的Array
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            // 注意这里之前有个bug
            // return  mapper.readValue(json,new TypeReference<List<T>>(){});
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            LOGGER.error(">> parseArray failed ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串转ArrayNode
     *
     * @param json 需要转换的json字符串
     * @return ArrayNode 类似于fastjson的JSONArray
     */
    public static ArrayNode parseArray(String json) {
        try {
            return (ArrayNode) mapper.readTree(json);
        } catch (Exception e) {
            LOGGER.error(">> parseArray to ArrayNode  failed ", e);
            throw new RuntimeException(e);
        }
    }

}
