package org.openstack4j.openstack.identity.v2.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.openstack4j.model.identity.v2.Service;
import org.openstack4j.model.identity.v2.ServiceEndpoint;
import org.openstack4j.model.identity.v2.builder.ServiceEndpointBuilder;
import org.openstack4j.openstack.common.ListResult;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


/**
 * OpenStack ServiceEndpoint is an endpoint which is mapped to a {@link Service}
 *
 * @author Jeremy Unruh
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("endpoint")
public class KeystoneServiceEndpoint implements ServiceEndpoint {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;
    @JsonProperty("region")
    private String region;
    @JsonProperty("service_id")
    private String serviceId;
    @JsonProperty("publicurl")
    private URI publicURL;
    @JsonProperty("adminurl")
    private URI adminURL;
    @JsonProperty("internalurl")
    private URI internalURL;

    public static ServiceEndpointBuilder builder() {
        return new ServiceConcreteEndpointBuilder();
    }

    @Override
    public ServiceEndpointBuilder toBuilder() {
        return new ServiceConcreteEndpointBuilder(this);
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
    public String getRegion() {
        return region;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServiceId() {
        return serviceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getPublicURL() {
        return publicURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getAdminURL() {
        return adminURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getInternalURL() {
        return internalURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id, region, publicURL, internalURL, adminURL, serviceId);
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
        KeystoneServiceEndpoint that = KeystoneServiceEndpoint.class.cast(obj);
        return Objects.equal(this.id, that.id)
                && Objects.equal(this.publicURL, that.publicURL) && Objects.equal(this.region, that.region)
                && Objects.equal(this.internalURL, that.internalURL) && Objects.equal(this.adminURL, that.adminURL)
                && Objects.equal(this.serviceId, that.serviceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("region", region).add("publicURL", publicURL)
                .add("internalURL", internalURL).add("adminURL", adminURL)
                .add("serviceId", serviceId)
                .toString();
    }

    public static class ServiceEndpoints extends ListResult<KeystoneServiceEndpoint> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("endpoints")
        List<KeystoneServiceEndpoint> endpoints;

        public List<KeystoneServiceEndpoint> value() {
            return endpoints;
        }
    }

    public static class ServiceConcreteEndpointBuilder implements ServiceEndpointBuilder {

        private KeystoneServiceEndpoint model;

        ServiceConcreteEndpointBuilder() {
            this(new KeystoneServiceEndpoint());
        }

        ServiceConcreteEndpointBuilder(KeystoneServiceEndpoint model) {
            this.model = model;
        }

        public ServiceEndpointBuilder region(String region) {
            model.region = region;
            return this;
        }

        public ServiceEndpointBuilder serviceId(String serviceId) {
            model.serviceId = serviceId;
            return this;
        }

        public ServiceEndpointBuilder publicURL(String publicURL) {
            model.publicURL = uri(publicURL);
            return this;
        }

        public ServiceEndpointBuilder adminURL(String adminURL) {
            model.adminURL = uri(adminURL);
            return this;
        }

        public ServiceEndpointBuilder internalURL(String internalURL) {
            model.internalURL = uri(internalURL);
            return this;
        }

        private URI uri(String url) {
            if (url == null) return null;

            try {
                return new URI(url);
            } catch (URISyntaxException e) {
                LoggerFactory.getLogger(ServiceConcreteEndpointBuilder.class).error(e.getMessage(), e);
            }
            return null;
        }

        @Override
        public ServiceEndpoint build() {
            return model;
        }

        @Override
        public ServiceEndpointBuilder from(ServiceEndpoint in) {
            model = (KeystoneServiceEndpoint) in;
            return this;
        }

    }
}
