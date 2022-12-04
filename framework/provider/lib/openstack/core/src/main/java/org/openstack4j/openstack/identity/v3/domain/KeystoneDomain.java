package org.openstack4j.openstack.identity.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.builder.DomainBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

@JsonRootName("domain")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeystoneDomain implements Domain {

    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    private String description;
    private Map<String, String> options = Maps.newHashMap();
    private Map<String, String> links;
    private boolean enabled;

    /**
     * @return the domain builder
     */
    public static DomainBuilder builder() {
        return new DomainConcreteBuilder();
    }

    @Override
    public DomainBuilder toBuilder() {
        return new DomainConcreteBuilder(this);
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
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * set endpoint enabled
     *
     * @param enabled the new enabled status
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("description", description)
                .add("name", name)
                .add("links", links)
                .add("enabled", enabled)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, description, enabled, name, links);
    }

    public static class DomainConcreteBuilder implements DomainBuilder {

        KeystoneDomain model;

        public DomainConcreteBuilder() {
            this(new KeystoneDomain());
        }

        DomainConcreteBuilder(KeystoneDomain model) {
            this.model = model;
        }

        @Override
        public Domain build() {
            return model;
        }

        @Override
        public DomainBuilder from(Domain in) {
            if (in != null)
                this.model = (KeystoneDomain) in;
            return this;
        }

        @Override
        public DomainBuilder id(String id) {
            model.id = id;
            return this;
        }

        @Override
        public DomainBuilder description(String description) {
            model.description = description;
            return this;
        }

        @Override
        public DomainBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public DomainBuilder options(Map<String, String> options) {
            model.options = options;
            return this;
        }

        @Override
        public DomainBuilder links(Map<String, String> links) {
            model.links = links;
            return this;
        }

        @Override
        public DomainBuilder enabled(boolean enabled) {
            model.enabled = enabled;
            return this;
        }

    }

    public static class Domains extends ListResult<KeystoneDomain> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("domains")
        protected List<KeystoneDomain> list;

        @Override
        protected List<KeystoneDomain> value() {
            return list;
        }

    }

}
