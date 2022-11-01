package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.jentity.Filter;
import com.fit2cloud.dao.jentity.Group;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/28  5:56 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class GroupHandler implements TypeHandler<List<Group>> {
    @Override
    public void setParameter(PreparedStatement ps, int i, List<Group> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJSONString(parameter));
    }

    @Override
    public List<Group> getResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.parseArray(rs.getString(columnName), Group.class);
    }

    @Override
    public List<Group> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.parseArray(rs.getString(columnIndex), Group.class);
    }

    @Override
    public List<Group> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.parseArray(cs.getString(columnIndex), Group.class);
    }
}
