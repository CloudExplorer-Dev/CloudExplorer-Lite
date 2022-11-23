package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.jentity.Filter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/28  5:57 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class FilterHandler implements TypeHandler<List<Filter>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Filter> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJSONString(parameter));
    }

    @Override
    public List<Filter> getResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.parseArray(rs.getString(columnName), Filter.class);
    }

    @Override
    public List<Filter> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.parseArray(rs.getString(columnIndex), Filter.class);
    }

    @Override
    public List<Filter> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.parseArray(cs.getString(columnIndex), Filter.class);
    }
}
