package org.openstack4j.openstack.identity.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.identity.AuthStore;
import org.openstack4j.model.identity.AuthVersion;

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

    public Credentials(String username, String password, String domainId) {
        this();
        passwordCreds.setCredentials(username, password);
        setDomainId(domainId);
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
        return getDomainId();
    }

    @JsonIgnore
    @Override
    public String getName() {
        return getDomainName();
    }

    @JsonIgnore
    @Override
    public AuthVersion getVersion() {
        return AuthVersion.V3;
    }

    private static final class PasswordCredentials {

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
