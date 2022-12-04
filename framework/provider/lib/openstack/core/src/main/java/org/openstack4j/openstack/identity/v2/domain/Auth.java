package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Auth implements org.openstack4j.openstack.common.Auth {

    private static final long serialVersionUID = 1L;

    private String tenantId;
    private String tenantName;
    @JsonIgnore
    private transient Type type;

    protected Auth(Type type) {
        this.type = type;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @JsonIgnore
    public Type getType() {
        return type;
    }
}
