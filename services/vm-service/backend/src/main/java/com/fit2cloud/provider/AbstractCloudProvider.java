package com.fit2cloud.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.entity.F2CDisk;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:35 AM
 * @Version 1.0
 * @注释:
 */
public abstract class AbstractCloudProvider<C extends Credential> implements ICloudProvider {

    @Override
    public FormObject getCreateServerForm() {
        throw new RuntimeException("Form does not exist");
    }

    /**
     * 获取认证信息
     *
     * @param credential 认证字符串
     * @return 认证对象
     */
    protected C getCredential(String credential) {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        try {
            return new ObjectMapper().readValue(credential, new ObjectMapper().constructType(trueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取认证信息
     *
     * @param req 请求对象字符串
     * @return 认证对象
     */
    protected C getCredentialByRequest(String req) {
        ObjectNode jsonNodes = JsonUtil.parseObject(req);
        JsonNode credential = jsonNodes.get("credential");
        if (credential != null) {
            return getCredential(credential.asText());
        }
        throw new Fit2cloudException(1001, "不存在认证对象");
    }

    @Override
    public boolean detachDisk(String req) {
        return false;
    }

    @Override
    public boolean deleteDisk(String req) {
        return false;
    }

    @Override
    public boolean enlargeDisk(String req) {
        return false;
    }

    @Override
    public boolean attachDisk(String req) {
        return false;
    }

    @Override
    public List<F2CDisk> createDisks(String req) {
        return null;
    }
}
