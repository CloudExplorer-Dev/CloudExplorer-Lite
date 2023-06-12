package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dto.optimization.OptimizationRule;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 描述：优化规则处理程序
 *
 * @author jianneng
 */
public class OptimizationRuleHandler implements TypeHandler<OptimizationRule> {


    @Override
    public void setParameter(PreparedStatement ps, int i, OptimizationRule parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJSONString(parameter));
    }

    @Override
    public OptimizationRule getResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnName), OptimizationRule.class);
    }

    @Override
    public OptimizationRule getResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnIndex), OptimizationRule.class);
    }

    @Override
    public OptimizationRule getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(cs.getString(columnIndex), OptimizationRule.class);
    }
}
