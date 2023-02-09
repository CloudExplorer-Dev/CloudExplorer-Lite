package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.jentity.Rules;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/8  17:23}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class RuleHandler implements TypeHandler<Rules> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Rules rules, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JsonUtil.toJSONString(rules));
    }

    @Override
    public Rules getResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.parseObject(resultSet.getString(s), Rules.class);
    }

    @Override
    public Rules getResult(ResultSet resultSet, int i) throws SQLException {
        return JsonUtil.parseObject(resultSet.getString(i), Rules.class);
    }

    @Override
    public Rules getResult(CallableStatement callableStatement, int i) throws SQLException {
        return JsonUtil.parseObject(callableStatement.getString(i), Rules.class);
    }
}
