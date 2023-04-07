package com.fit2cloud.base.handler;

import com.fit2cloud.common.utils.RSAUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author:张少虎
 * @Date: 2022/9/1  5:29 PM
 * @Version 1.0
 * @注释:
 */
public class CredentialTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, RSAUtil.encrypt(parameter, RSAUtil.getKeyPair().getKey()));
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return RSAUtil.decrypt(rs.getString(columnName), RSAUtil.getKeyPair().getValue());
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return RSAUtil.decrypt(rs.getString(columnIndex), RSAUtil.getKeyPair().getValue());
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return RSAUtil.decrypt(cs.getString(columnIndex), RSAUtil.getKeyPair().getValue());
    }
}
