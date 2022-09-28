package com.fit2cloud.common.scheduler.handler;

import com.mchange.v2.ser.SerializableUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/1  5:29 PM
 * @Version 1.0
 * @注释:
 */
public class ByteTypeHandler implements TypeHandler<Map<String, Object>> {

    @SneakyThrows
    @Override
    public void setParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        ps.setBytes(i, SerializableUtils.toByteArray(parameter));
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getResult(ResultSet rs, String columnName) throws SQLException {
        return deCode(rs.getBytes(columnName));
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return deCode(rs.getBytes(columnIndex));
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return deCode(cs.getBytes(columnIndex));
    }

    private Map<String, Object> deCode(byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes.length > 0) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (Map<String, Object>) objectInputStream.readObject();
        }
        return new HashMap<>();
    }
}
