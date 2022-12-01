package org.openstack4j.openstack.identity.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.Service;
import org.openstack4j.model.identity.v3.builder.ServiceBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * V3 OpenStack service
 */
@JsonRootName("service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeystoneService implements Service, Comparable<Service> {

    private static final long serialVersionUID = 1L;
    private Integer version;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String type;
    @JsonProperty
    private List<KeystoneEndpoint> endpoints;
    private String description;
    private Boolean enabled = true;

    private Map<String, String> links;

    public static ServiceBuilder builder() {
        return new ServiceConcreteBuilder();
    }

    @Override
    public ServiceBuilder toBuilder() {
        return new ServiceConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getVersion() {
        return version;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Endpoint> getEndpoints() {
        return endpoints;
    }

    /**
     * @return the enabled status of the service
     */
    @Override
    public boolean isEnabled() {
        return enabled != null && enabled;
    }

    /**
     * set service enabled status
     *
     * @param enabled the new enabled status
     */
    @Override
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
                .add("description", description)
                .add("type", type)
                .add("version", version)
                .add("enabled", enabled)
                .add("links", links)
                .toString();
    }

    @Override
    public int compareTo(Service s) {
        return getVersion().compareTo(s.getVersion());
    }

    public static class Services extends ListResult<KeystoneService> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("services")
        private List<KeystoneService> list;

        @Override
        protected List<KeystoneService> value() {
            return list;
        }
    }

    public static class Catalog extends ListResult<KeystoneService> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("catalog")
        private List<KeystoneService> list;

        @Override
        protected List<KeystoneService> value() {
            return list;
        }
    }

    public static class ServiceConcreteBuilder implements ServiceBuilder {

        private KeystoneService model;

        ServiceConcreteBuilder() {
            this(new KeystoneService());
        }

        ServiceConcreteBuilder(KeystoneService model) {
            this.model = model;
        }

        @Override
        public Service build() {
            return model;
        }

        @Override
        public ServiceBuilder from(Service in) {
            model = (KeystoneService) in;
            return this;
        }

        @Override
        public ServiceBuilder version(Integer version) {
            model.version = version;
            return this;
        }

        @Override
        public ServiceBuilder id(String id) {
            model.id = id;
            return this;
        }

        @Override
        public ServiceBuilder description(String description) {
            model.description = description;
            return this;
        }

        @Override
        public ServiceBuilder type(String type) {
            model.type = type;
            return this;
        }

        @Override
        public ServiceBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public ServiceBuilder enabled(boolean enabled) {
            model.enabled = enabled;
            return this;
        }

        @Override
        public ServiceBuilder links(Map<String, String> links) {
            model.links = links;
            return this;
        }
    }
}
