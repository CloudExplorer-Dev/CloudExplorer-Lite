package org.openstack4j.openstack.identity.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import org.openstack4j.model.identity.v3.Role;
import org.openstack4j.model.identity.v3.builder.RoleBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * v3 role implementation
 */
@JsonRootName("role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeystoneRole implements Role {

    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty("domain_id")
    private String domainId;
    private Map<String, String> links;
    private Map<String, String> options = Maps.newHashMap();

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
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("domain_id", domainId)
                .add("links", links)
                .toString();
    }

    public static class RoleConcreteBuilder implements RoleBuilder {

        private KeystoneRole model;

        RoleConcreteBuilder() {
            this(new KeystoneRole());
        }

        RoleConcreteBuilder(KeystoneRole model) {
            this.model = model;
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

        @Override
        public RoleBuilder id(String id) {
            model.id = id;
            return this;
        }

        @Override
        public RoleBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public RoleBuilder domainId(String domainId) {
            model.domainId = domainId;
            return this;
        }

        @Override
        public RoleBuilder options(Map<String, String> options) {
            model.options = options;
            return this;
        }

        @Override
        public RoleBuilder links(Map<String, String> links) {
            model.links = links;
            return this;
        }
    }

    public static class Roles extends ListResult<KeystoneRole> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("roles")
        protected List<KeystoneRole> list;

        @Override
        public List<KeystoneRole> value() {
            return list;
        }

    }

}
