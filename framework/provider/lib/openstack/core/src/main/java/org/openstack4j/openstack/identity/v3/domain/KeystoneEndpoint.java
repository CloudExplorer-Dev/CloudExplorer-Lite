package org.openstack4j.openstack.identity.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.builder.EndpointBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Keystone V3 endpoint model class
 */
@JsonRootName("endpoint")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeystoneEndpoint implements Endpoint {

    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String id;
    private String type;
    private String description;
    @JsonProperty("interface")
    private Facing iface;
    @JsonProperty("service_id")
    private String serviceId;
    private String name;
    @JsonProperty
    private String region;
    @JsonProperty("region_id")
    private String regionId;
    @JsonProperty
    private URL url;
    private Map<String, String> links;
    private Boolean enabled = true;

    /**
     * @return the endpoint builder
     */
    public static EndpointBuilder builder() {
        return new EndpointConcreteBuilder();
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public EndpointBuilder toBuilder() {
        return new EndpointConcreteBuilder();
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public Facing getIface() {
        return iface;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getRegion() {
        return region;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public String getRegionId() {
        return regionId;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public URL getUrl() {
        return url;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public Map<String, String> getLinks() {
        return links;
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return (enabled != null && enabled);
    }

    /**
     * set endpoint enabled
     *
     * @param enabled the new enabled status
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("type", type)
                .add("name", name)
                .add("description", description)
                .add("interface", iface)
                .add("serviceId", serviceId)
                .add("regionId", regionId)
                .add("url", url)
                .add("links", links)
                .add("enabled", enabled)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, name, description, iface, serviceId, regionId, url, links, enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        KeystoneEndpoint that = KeystoneEndpoint.class.cast(obj);
        return Objects.equal(this.id, that.id)
                && Objects.equal(this.type, that.type)
                && Objects.equal(this.name, that.name)
                && Objects.equal(this.description, that.description)
                && Objects.equal(this.iface, that.iface)
                && Objects.equal(this.serviceId, that.serviceId)
                && Objects.equal(this.regionId, that.regionId)
                && Objects.equal(this.url, that.url)
                && Objects.equal(this.links, that.links)
                && Objects.equal(this.enabled, that.enabled);
    }

    public static class Endpoints extends ListResult<KeystoneEndpoint> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("endpoints")
        private List<KeystoneEndpoint> list;

        @Override
        protected List<KeystoneEndpoint> value() {
            return list;
        }
    }

    public static class EndpointConcreteBuilder implements EndpointBuilder {

        KeystoneEndpoint model;

        EndpointConcreteBuilder() {
            this(new KeystoneEndpoint());
        }

        EndpointConcreteBuilder(KeystoneEndpoint model) {
            this.model = model;
        }

        /**
         * @see KeystoneEndpoint#getId()
         */
        @Override
        public EndpointBuilder id(String id) {
            model.id = id;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getType()
         */
        @Override
        public EndpointBuilder type(String type) {
            model.type = type;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getDescription()
         */
        @Override
        public EndpointBuilder description(String description) {
            model.description = description;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getIface()
         */
        @Override
        public EndpointBuilder iface(Facing iface) {
            model.iface = iface;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getServiceId()
         */
        @Override
        public EndpointBuilder serviceId(String serviceId) {
            model.serviceId = serviceId;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getName()
         */
        @Override
        public EndpointBuilder name(String name) {
            model.name = name;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getRegionId()
         */
        @Override
        public EndpointBuilder regionId(String regionId) {
            model.regionId = regionId;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getRegion()
         */
        @Override
        public EndpointBuilder region(String region) {
            model.region = region;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getUrl()
         */
        @Override
        public EndpointBuilder url(URL url) {
            model.url = url;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getLinks()
         */
        @Override
        public EndpointBuilder links(Map<String, String> links) {
            model.links = links;
            return this;
        }

        /**
         * @see KeystoneEndpoint#isEnabled()
         */
        @Override
        public EndpointBuilder enabled(boolean enabled) {
            model.enabled = enabled;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Endpoint build() {
            return model;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EndpointBuilder from(Endpoint in) {
            this.model = (KeystoneEndpoint) in;
            return this;
        }

    }

}
