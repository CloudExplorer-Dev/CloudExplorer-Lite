package com.fit2cloud.base.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/17  16:00}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class MapTypeHandler implements TypeHandler<Map<String, Object>> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Map<String, Object> stringObjectMap, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JsonUtil.toJSONString(stringObjectMap));
    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.parseObject(resultSet.getString(s), new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, int i) throws SQLException {
        return JsonUtil.parseObject(resultSet.getString(i), new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public Map<String, Object> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return JsonUtil.parseObject(callableStatement.getString(i), new TypeReference<Map<String, Object>>() {
        });
    }
}
