package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/6  6:02 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AuthorizeRuleGrouHandler implements TypeHandler<BillAuthorizeRule> {


    @Override
    public void setParameter(PreparedStatement ps, int i, BillAuthorizeRule parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJSONString(parameter));
    }

    @Override
    public BillAuthorizeRule getResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnName), BillAuthorizeRule.class);
    }

    @Override
    public BillAuthorizeRule getResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnIndex), BillAuthorizeRule.class);
    }

    @Override
    public BillAuthorizeRule getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(cs.getString(columnIndex), BillAuthorizeRule.class);
    }
}
