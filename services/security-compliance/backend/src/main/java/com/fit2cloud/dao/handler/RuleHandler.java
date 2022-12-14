package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.jentity.Rule;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/8  17:23}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class RuleHandler implements TypeHandler<List<Rule>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<Rule> rule, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JsonUtil.toJSONString(rule));
    }

    @Override
    public List<Rule> getResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.parseArray(resultSet.getString(s), Rule.class);
    }

    @Override
    public List<Rule> getResult(ResultSet resultSet, int i) throws SQLException {
        return JsonUtil.parseArray(resultSet.getString(i), Rule.class);
    }

    @Override
    public List<Rule> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return JsonUtil.parseArray(callableStatement.getString(i), Rule.class);
    }
}
