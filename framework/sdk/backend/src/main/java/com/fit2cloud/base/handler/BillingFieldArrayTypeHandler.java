package com.fit2cloud.base.handler;

import com.fit2cloud.base.entity.json_entity.BillingField;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/29  17:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class BillingFieldArrayTypeHandler implements TypeHandler<List<BillingField>> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<BillingField> stringObjectMap, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JsonUtil.toJSONString(stringObjectMap));
    }

    @Override
    public List<BillingField> getResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.parseArray(resultSet.getString(s), BillingField.class);
    }

    @Override
    public List<BillingField> getResult(ResultSet resultSet, int i) throws SQLException {
        return JsonUtil.parseArray(resultSet.getString(i), BillingField.class);
    }

    @Override
    public List<BillingField> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return JsonUtil.parseArray(callableStatement.getString(i), BillingField.class);
    }
}
