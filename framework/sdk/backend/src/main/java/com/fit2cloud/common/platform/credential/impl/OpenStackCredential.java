package com.fit2cloud.common.platform.credential.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.impl.openstack.utils.OpenStackBaseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenStackCredential implements Credential {

    @Form(inputType = InputType.Text, label = "Domain ID")
    @JsonProperty("domain")
    private String domain;

    @Form(inputType = InputType.Text, label = "Project ID")
    @JsonProperty("project")
    private String project;

    @Form(inputType = InputType.Text, label = "用户ID")
    @JsonProperty("userId")
    private String userId;

    @Form(inputType = InputType.Password, label = "密码")
    @JsonProperty("secret")
    private String secret;

    @Form(inputType = InputType.Text, label = "认证地址")
    @JsonProperty("endpoint")
    private String endpoint;

    @Override
    public boolean verification() {
        try {
            getOSClient();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public List<Region> regions() {
        OSClient.OSClientV3 osClient = getOSClient();
        List<Region> list = new ArrayList<>();
        List<String> regions = OpenStackBaseUtils.getRegionList(osClient);
        for (String region : regions) {
            list.add(
                    new Region()
                            .setRegionId(region)
                            .setName(region)
            );
        }

        return list;
    }

    @JsonIgnore
    public OSClient.OSClientV3 getOSClient() {
        return OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(userId, secret)
                .scopeToProject(Identifier.byId(project), Identifier.byId(domain))
                .authenticate();
    }
}
