package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.openstack4j.core.transport.internal.OSBadBooleanDeserializer;
import org.openstack4j.model.identity.v2.Role;
import org.openstack4j.model.identity.v2.builder.RoleBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A User based Role - see {@link Role}
 *
 * @author Jeremy Unruh
 */
@JsonRootName("role")
public class KeystoneRole implements Role {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String description;
    @JsonDeserialize(using = OSBadBooleanDeserializer.class)
    private Boolean enabled = true;
    private String tenantId;
    private String serviceId;

    public static RoleBuilder builder() {
        return new RoleConcreteBuilder();
    }

    @Override
    public RoleBuilder toBuilder() {
        return new RoleConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return (enabled != null && enabled);
    }

    /**
     * {@inheritDoc}
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("description", description)
                .add("enabled", enabled).add("tenantId", tenantId).add("serviceId", serviceId)
                .toString();
    }


    public static class Roles extends ListResult<KeystoneRole> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("roles")
        private List<KeystoneRole> list;

        @Override
        protected List<KeystoneRole> value() {
            return list;
        }


    }

    public static class RoleConcreteBuilder implements RoleBuilder {

        private KeystoneRole model;

        RoleConcreteBuilder() {
            this(new KeystoneRole());
        }

        RoleConcreteBuilder(KeystoneRole model) {
            this.model = model;
        }

        public RoleBuilder id(String id) {
            model.id = id;
            return this;
        }

        public RoleBuilder tenantId(String tenantId) {
            model.tenantId = tenantId;
            return this;
        }

        public RoleBuilder name(String name) {
            model.name = name;
            return this;
        }

        public RoleBuilder description(String description) {
            model.description = description;
            return this;
        }

        public RoleBuilder enabled(boolean enabled) {
            model.enabled = enabled;
            return this;
        }

        @Override
        public Role build() {
            return model;
        }

        @Override
        public RoleBuilder from(Role in) {
            model = (KeystoneRole) in;
            return this;
        }

    }
}
