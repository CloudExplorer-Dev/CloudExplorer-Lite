package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.jentity.Rules;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/27  18:00}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ResourceTypeHandler implements TypeHandler<List<String>> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, String.join(",", strings));
    }

    @Override
    public List<String> getResult(ResultSet resultSet, String s) throws SQLException {
        if (StringUtils.isEmpty(resultSet.getString(s))) {
            return List.of();
        }
        return Arrays.stream(resultSet.getString(s).split(",")).distinct().toList();
    }

    @Override
    public List<String> getResult(ResultSet resultSet, int i) throws SQLException {
        if (StringUtils.isEmpty(resultSet.getString(i))) {
            return List.of();
        }
        return Arrays.stream(resultSet.getString(i).split(",")).distinct().toList();

    }

    @Override
    public List<String> getResult(CallableStatement callableStatement, int i) throws SQLException {
        if (StringUtils.isEmpty(callableStatement.getString(i))) {
            return List.of();
        }
        return Arrays.stream(callableStatement.getString(i).split(",")).distinct().toList();
    }
}
