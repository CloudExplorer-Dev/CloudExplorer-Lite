package com.fit2cloud.db_convert.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.db_convert.entity.json_entity.OldBillAuthorizeRule;
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
public class OldAuthorizeRuleHandler implements TypeHandler<OldBillAuthorizeRule> {


    @Override
    public void setParameter(PreparedStatement ps, int i, OldBillAuthorizeRule parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJSONString(parameter));
    }

    @Override
    public OldBillAuthorizeRule getResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnName), OldBillAuthorizeRule.class);
    }

    @Override
    public OldBillAuthorizeRule getResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnIndex), OldBillAuthorizeRule.class);
    }

    @Override
    public OldBillAuthorizeRule getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(cs.getString(columnIndex), OldBillAuthorizeRule.class);
    }
}
