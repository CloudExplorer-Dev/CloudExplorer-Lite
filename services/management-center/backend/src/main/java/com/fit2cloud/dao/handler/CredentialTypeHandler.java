package com.fit2cloud.dao.handler;

import com.fit2cloud.common.utils.RSAUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/1  5:29 PM
 * @Version 1.0
 * @注释:
 */
public class CredentialTypeHandler implements TypeHandler<String> {
    /**
     * 加密所需要的共钥
     */
    private final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArLtXvQbdIm78Z8Zns74qFvWr6Z4xk2eqgdcdqcaLshN5FNjAYyqgVtrOdVUx0WL2tUL88Aj142hfvHnqMSRdAftEC6zKNutgckj30cjd4HEPzR441Gr3Wp4TSVEhBZWeyfinQnEIX1Fb5wkuBQ8uinUmnc+ZOVi+EFY5elIpD9oBOYJtZOIuttB5v2aI0eUY6fqcKd3wQRfkHhaskvVk64uOSvTEaqxffQ+hWZdxQb3pTST4p8tlaxuWXbP9Der8d256cwV0Lid1WMuIiLP/JiNAdJL3J9W1Ln7k4mn5EAzHxYrVwBS9zsh3FTZtXyMs5j8lsfHdCMpOcFisjYTk9wIDAQAB";
    /**
     * 加密所需要的私钥
     */
    private final String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCsu1e9Bt0ibvxnxmezvioW9avpnjGTZ6qB1x2pxouyE3kU2MBjKqBW2s51VTHRYva1QvzwCPXjaF+8eeoxJF0B+0QLrMo262BySPfRyN3gcQ/NHjjUavdanhNJUSEFlZ7J+KdCcQhfUVvnCS4FDy6KdSadz5k5WL4QVjl6UikP2gE5gm1k4i620Hm/ZojR5Rjp+pwp3fBBF+QeFqyS9WTri45K9MRqrF99D6FZl3FBvelNJPiny2VrG5Zds/0N6vx3bnpzBXQuJ3VYy4iIs/8mI0B0kvcn1bUufuTiafkQDMfFitXAFL3OyHcVNm1fIyzmPyWx8d0Iyk5wWKyNhOT3AgMBAAECggEAG+afA3+KVLeMuc6IjbYX4mGIhVHyPBcwqYxv1C6n/fm7QyzH1rzZ7W2mj7lVNLdBloHdjYBFB5iibjVbKNjw1RMbKK+eckSO7abvsMk7Xbelj8Jj95vXnKGQB/W82cQTRBiQG+FaMttXdvg6oMKVB/RoanFQFL1K5iSnYCTJUS/nWo5N0mF8IbtpUi128Ojq2bA/yUJZa39YD0t2OlZlO9FEUXPDHki4ICrMI5cKfxbOMTaa5mZ+PF6Z/w2kbVs2t8UuoWL9P1CvK1SC1cMqYchgTXMR7rBj9yJE9qDT7T5stdHf+BFK9iI3b12xlRQAh3QaJDa0j/WL0BwbGTmwUQKBgQDpMJX9IXVspPjFs9v8X5dk02IX5MJL/UMJZezokvqUX5gv+fQaFEXvehSu96B4/9Wrrgpdut+Y7E1SjWQskX3ZMTz3abfGj3SkRrQdGyaNalCyD1ht3kdpuVT3h4ZlI84qOWZLeszaK2M5r73qQcnaA3BRTuIS5v2MlwvqzaBSewKBgQC9oMzlGTfhxKlwrndqxl3dChc/aLOv7MEuIofyACh7d1WxV9H6ht3XvBoipHBqArXDpIBpkW2AGtye5RHGEDFCWX5nW3xuvK/ayrYajcOcRcqrqTKehpXrtHgJGGQe4D3LWAnlS+BckQlOk4k5SjGsYDSBF+3dzQMT1k2ZI3V8tQKBgF1faQ+c0CoavMpdRJ7/n91tjv/GR86psKgYug4+nDvBM20yhv3SVFaZ6aJN4WHR/zrpS5lQdKwFIskFeLEn6N6JCsJ/AGlvk+MbtI9bCcriYUGLR3tm3+2DmnQrPjNNbQYXSdC7cT+C1A6LQl6EzVCQw+6qyXeMKsaazNjOUX31AoGBAIeEh2mi4CDk3wK8bHNOWH5DW+Hg/alYVaaW3vi/zC6QVhlrXZDhGWG3+CWRxxS74xJyWaMYX10R1WFo647Uu59jPca3xvGMv1BpVkMb1RO1fOW5AYfxPus4CiDV4Zd/Qio0PL41v53nuPMC1SqwL1e1WVB4vXGpte1TVhl/EdORAoGAWR5doI017H5jg1fQ1D2drVm2J6vuXuJNPi/UrtS1FQkpVi+oUNJu028nzWBUlD9bnVYiuOAvnHgZJhv2SoMdCOiWxwhItGXZY00jx6PbyJ3V/yIEE7rAliu/1K0TgmiZ34MZhKxDfr0WDWGUhpDcv15VG7EJ8R7VZN+6MOhk3+Q=";

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, RSAUtil.encrypt(parameter, publicKey));
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return RSAUtil.decrypt(rs.getString(columnName), privateKey);
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return RSAUtil.decrypt(rs.getString(columnIndex), privateKey);
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return RSAUtil.decrypt(cs.getString(columnIndex), privateKey);
    }
}
