package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.identity.v2.TenantUser;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A User from a Tenant perspective implemenation
 *
 * @author Jeremy Unruh
 */
public class KeystoneTenantUser implements TenantUser {

    private static final long serialVersionUID = 1L;

    String id;
    String name;
    String email;
    boolean enabled;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id).add("name", name).add("email", email).add("enabled", enabled)
                .toString();
    }

    public static class TenantUsers extends ListResult<KeystoneTenantUser> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("users")
        private List<KeystoneTenantUser> list;

        public List<KeystoneTenantUser> value() {
            return list;
        }
    }

}
