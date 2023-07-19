package com.fit2cloud.common.platform.credential.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.impl.proxmox.ProxmoxBaseCloudProvider;
import com.fit2cloud.common.provider.impl.proxmox.client.PveClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/29  6:55 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ProxmoxCredential implements Credential {
    /**
     * proxmox服务器ip
     */
    @Form(inputType = InputType.Text, label = "Proxmox服务IP")
    @JsonProperty("ip")
    private String ip;

    /**
     * proxmox服务器ip
     */
    @Form(inputType = InputType.Text, label = "Proxmox服务端口")
    @JsonProperty("port")
    private int port;

    /**
     * 认证领域
     */
    @Form(inputType = InputType.SingleSelect, valueField = "value",
            textField = "key",
            clazz = ProxmoxBaseCloudProvider.class,
            
            method = "getRealmList",
            label = "Realm",
            propsInfo = "{\"style\":{\"width\":\"100%\",\"--el-form-inline-content-width\":\"100%\"}}"
    )
    @JsonProperty("realm")
    private String realm;
    /**
     * 用户名
     */
    @Form(inputType = InputType.Text, label = "用户名")
    @JsonProperty("userName")
    private String userName;
    /**
     * 密码
     */
    @Form(inputType = InputType.Password, label = "密码")
    @JsonProperty("password")
    private String password;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getRealm() {
        return realm;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean verification() {
        regions();
        return true;
    }

    @Override
    public List<Region> regions() {
        PveClient pveClient = new PveClient(this.ip, this.port);
        if (pveClient.login(userName, password, realm)) {
            PveClient.PVENodes nodes = pveClient.getNodes();
            return nodes.index()
                    .getResponse().getJSONArray("data")
                    .toList()
                    .stream()
                    .map(item -> item instanceof Map<?, ?> ? (Map<?, ?>) item : null)
                    .filter(Objects::nonNull)
                    .map(item -> new Region(item.get("node").toString(), item.get("node").toString(), ""))
                    .toList();

        } else {
            throw new Fit2cloudException(100, "认证失败");
        }

    }
}
