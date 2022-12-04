package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.identity.v2.builder.EndpointBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.net.URI;
import java.util.List;

/**
 * Endpoint Model is used to describe a network address which is described by URL's and other service
 * information depending on the context it was retrieved in.
 *
 * @author Jeremy Unruh
 * @see <a href="http://docs.openstack.org/api/openstack-identity-service/2.0/content/GET_listEndpointsForToken_v2.0_tokens__tokenId__endpoints_Token_Operations.html#GET_listEndpointsForToken_v2.0_tokens__tokenId__endpoints_Token_Operations-Response"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeystoneEndpoint implements Endpoint {

    private static final long serialVersionUID = 1L;

    private String type;
    private String id;
    private String name;
    private String region;
    private URI publicURL;
    private URI internalURL;
    private URI adminURL;
    private String tenantId;
    private String versionId;
    private URI versionInfo;
    private URI versionList;

    public static EndpointBuilder builder() {
        return new EndPointConcreteBuilder();
    }

    @Override
    public EndpointBuilder toBuilder() {
        return new EndPointConcreteBuilder(this);
    }

    public String getType() {
        return type;
    }

    /**
     * @return the id for this endpoint (null if a new endpoint is being created)
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name of this endpoint, or null when the endpoint is part of the Access ServiceCatalog
     */
    public String getName() {
        return name;
    }

    /**
     * @return the admin URL for this endpoint, or null when the endpoint is part of the Access ServiceCatalog
     */
    public URI getAdminURL() {
        return adminURL;
    }

    /**
     * @return the public URL for this endpoint
     */
    public URI getPublicURL() {
        return publicURL;
    }

    /**
     * @return the internal URL for this endpoint
     */
    public URI getInternalURL() {
        return internalURL;
    }

    /**
     * @return the region of the endpoint or null
     */
    public String getRegion() {
        return region;
    }

    /**
     * @return the tenant identifier for this endpoint or null
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @return the version id or null
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * @return the version information when endpoint is listed as part of Access Service Catalog, otherwise null
     */
    public URI getVersionInfo() {
        return versionInfo;
    }

    /**
     * @return the version list when endpoint is listed as part of Access Service Catalog, otherwise null
     */
    public URI getVersionList() {
        return versionList;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, versionId, region, publicURL, internalURL, adminURL, versionInfo, versionList,
                tenantId, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        KeystoneEndpoint that = KeystoneEndpoint.class.cast(obj);
        return Objects.equal(this.id, that.id) && Objects.equal(this.versionId, that.versionId)
                && Objects.equal(this.region, that.region) && Objects.equal(this.publicURL, that.publicURL)
                && Objects.equal(this.internalURL, that.internalURL) && Objects.equal(this.adminURL, that.adminURL)
                && Objects.equal(this.versionInfo, that.versionInfo) && Objects.equal(this.versionList, that.versionList)
                && Objects.equal(this.tenantId, that.tenantId) && Objects.equal(this.type, that.type);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("type", type)
                .add("region", region).add("publicURL", publicURL)
                .add("internalURL", internalURL).add("adminURL", adminURL)
                .add("versionId", versionId).add("versionInfo", versionInfo).add("versionList", versionList)
                .toString();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Endpoints extends ListResult<KeystoneEndpoint> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("endpoints")
        private List<KeystoneEndpoint> endpoints;

        public List<KeystoneEndpoint> value() {
            return endpoints;
        }
    }

    public static class EndPointConcreteBuilder implements EndpointBuilder {

        protected KeystoneEndpoint model;

        protected EndPointConcreteBuilder() {
            this(new KeystoneEndpoint());
        }

        EndPointConcreteBuilder(KeystoneEndpoint model) {
            this.model = model;
        }

        /**
         * @see KeystoneEndpoint#getRegion()
         */
        public EndpointBuilder region(String region) {
            model.region = region;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getPublicURL()
         */
        public EndpointBuilder publicURL(URI publicURL) {
            model.publicURL = publicURL;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getInternalURL()
         */
        public EndpointBuilder internalURL(URI internalURL) {
            model.internalURL = internalURL;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getTenantId()
         */
        public EndpointBuilder tenantId(String tenantId) {
            model.tenantId = tenantId;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getType()
         */
        public EndpointBuilder type(String type) {
            model.type = type;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getId()
         */
        public EndpointBuilder id(String id) {
            model.id = id;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getName()
         */
        public EndpointBuilder name(String name) {
            model.name = name;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getAdminURL()
         */
        public EndpointBuilder adminURL(URI adminURL) {
            model.adminURL = adminURL;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getVersionInfo()
         */
        public EndpointBuilder versionInfo(URI versionInfo) {
            model.versionInfo = versionInfo;
            return this;
        }

        /**
         * @see KeystoneEndpoint#getVersionList()
         */
        public EndpointBuilder versionList(URI versionList) {
            model.versionList = versionList;
            return this;
        }

        @Override
        public KeystoneEndpoint build() {
            return model;
        }

        @Override
        public EndpointBuilder from(Endpoint in) {
            this.model = (KeystoneEndpoint) in;
            return this;
        }
    }
}
