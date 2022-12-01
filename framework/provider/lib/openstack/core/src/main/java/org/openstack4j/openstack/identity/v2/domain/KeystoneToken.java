package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.identity.AuthVersion;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.TokenV2;

import java.util.Date;

public final class KeystoneToken implements TokenV2 {

    private static final long serialVersionUID = 1L;

    private String id;
    private Date expires;
    private KeystoneTenant tenant;

    @JsonProperty("issued_at")
    private Date created;

    public String getId() {
        return id;
    }

    public Date getExpires() {
        return expires;
    }

    public Tenant getTenant() {
        return tenant;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("created", created).add("expires", expires).add("tenant", tenant)
                .toString();
    }

    @JsonIgnore
    @Override
    public AuthVersion getVersion() {
        return AuthVersion.V2;
    }

}
