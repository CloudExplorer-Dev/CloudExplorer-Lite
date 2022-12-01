package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.identity.AuthStore;
import org.openstack4j.model.identity.AuthVersion;

import java.io.Serializable;

@JsonRootName("auth")
public class Credentials extends Auth implements AuthStore {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "passwordCredentials")
    private PasswordCredentials passwordCreds = new PasswordCredentials();


    public Credentials() {
        super(Type.CREDENTIALS);
    }

    public Credentials(String username, String password) {
        this();
        passwordCreds.setCredentials(username, password);
    }

    public Credentials(String username, String password, String tenantName) {
        this();
        passwordCreds.setCredentials(username, password);
        setTenantName(tenantName);
    }

    public Credentials(String username, String password, String tenantName, String tenantId) {
        this();
        passwordCreds.setCredentials(username, password);
        setTenantName(tenantName);
        setTenantId(tenantId);
    }

    @JsonIgnore
    public String getUsername() {
        return passwordCreds.username;
    }

    @JsonIgnore
    public String getPassword() {
        return passwordCreds.password;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return getTenantId();
    }

    @JsonIgnore
    @Override
    public String getName() {
        return getTenantName();
    }

    @JsonIgnore
    @Override
    public AuthVersion getVersion() {
        return AuthVersion.V2;
    }

    private static final class PasswordCredentials implements Serializable {

        private static final long serialVersionUID = 1L;

        @JsonProperty
        String username;
        @JsonProperty
        String password;

        public void setCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
