package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Bay;
import org.openstack4j.model.magnum.BayBuilder;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumBay implements Bay {
    private static final long serialVersionUID = 1L;
    @JsonProperty("status")
    private String status;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("links")
    private List<GenericLink> links;
    @JsonProperty("stack_id")
    private String stackId;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("api_address")
    private String apiAddress;
    @JsonProperty("discovery_url")
    private String discoveryUrl;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("master_count")
    private Integer masterCount;
    @JsonProperty("coe_version")
    private String coeVersion;
    @JsonProperty("baymodel_id")
    private String baymodelId;
    @JsonProperty("master_addresses")
    private List<String> masterAddresses;
    @JsonProperty("node_count")
    private Integer nodeCount;
    @JsonProperty("node_addresses")
    private List<String> nodeAddresses;
    @JsonProperty("status_reason")
    private String statusReason;
    @JsonProperty("bay_create_timeout")
    private String bayCreateTimeout;
    @JsonProperty("name")
    private String name;

    public static BayBuilder builder() {
        return new BayConcreteBuilder();
    }

    @Override
    public BayBuilder toBuilder() {
        return new BayConcreteBuilder(this);
    }

    public String getStatus() {
        return status;
    }

    public String getUuid() {
        return uuid;
    }

    public List<GenericLink> getLinks() {
        return links;
    }

    public String getStackId() {
        return stackId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public String getDiscoveryUrl() {
        return discoveryUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getMasterCount() {
        return masterCount;
    }

    public String getCoeVersion() {
        return coeVersion;
    }

    public String getBaymodelId() {
        return baymodelId;
    }

    public List<String> getMasterAddresses() {
        return masterAddresses;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public List<String> getNodeAddresses() {
        return nodeAddresses;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public String getBayCreateTimeout() {
        return bayCreateTimeout;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("status", status).add("uuid", uuid).add("links", links)
                .add("stackId", stackId).add("createdAt", createdAt).add("apiAddress", apiAddress)
                .add("discoveryUrl", discoveryUrl).add("updatedAt", updatedAt).add("masterCount", masterCount)
                .add("coeVersion", coeVersion).add("baymodelId", baymodelId).add("masterAddresses", masterAddresses)
                .add("nodeCount", nodeCount).add("nodeAddresses", nodeAddresses).add("statusReason", statusReason)
                .add("bayCreateTimeout", bayCreateTimeout).add("name", name).toString();
    }

    /**
     * Concrete builder containing MagnumBay as model
     */
    public static class BayConcreteBuilder implements BayBuilder {
        MagnumBay model;

        public BayConcreteBuilder() {
            this(new MagnumBay());
        }

        public BayConcreteBuilder(MagnumBay model) {
            this.model = model;
        }

        @Override
        public Bay build() {
            return model;
        }

        @Override
        public BayBuilder from(Bay in) {
            if (in != null)
                this.model = (MagnumBay) in;
            return this;
        }

        @Override
        public BayBuilder status(String status) {
            model.status = status;
            return this;
        }

        @Override
        public BayBuilder uuid(String uuid) {
            model.uuid = uuid;
            return this;
        }

        @Override
        public BayBuilder links(List<GenericLink> links) {
            model.links = links;
            return this;
        }

        @Override
        public BayBuilder stackId(String stackId) {
            model.stackId = stackId;
            return this;
        }

        @Override
        public BayBuilder createdAt(String createdAt) {
            model.createdAt = createdAt;
            return this;
        }

        @Override
        public BayBuilder apiAddress(String apiAddress) {
            model.apiAddress = apiAddress;
            return this;
        }

        @Override
        public BayBuilder discoveryUrl(String discoveryUrl) {
            model.discoveryUrl = discoveryUrl;
            return this;
        }

        @Override
        public BayBuilder updatedAt(String updatedAt) {
            model.updatedAt = updatedAt;
            return this;
        }

        @Override
        public BayBuilder masterCount(Integer masterCount) {
            model.masterCount = masterCount;
            return this;
        }

        @Override
        public BayBuilder coeVersion(String coeVersion) {
            model.coeVersion = coeVersion;
            return this;
        }

        @Override
        public BayBuilder baymodelId(String baymodelId) {
            model.baymodelId = baymodelId;
            return this;
        }

        @Override
        public BayBuilder masterAddresses(List<String> masterAddresses) {
            model.masterAddresses = masterAddresses;
            return this;
        }

        @Override
        public BayBuilder nodeCount(Integer nodeCount) {
            model.nodeCount = nodeCount;
            return this;
        }

        @Override
        public BayBuilder nodeAddresses(List<String> nodeAddresses) {
            model.nodeAddresses = nodeAddresses;
            return this;
        }

        @Override
        public BayBuilder statusReason(String statusReason) {
            model.statusReason = statusReason;
            return this;
        }

        @Override
        public BayBuilder bayCreateTimeout(String bayCreateTimeout) {
            model.bayCreateTimeout = bayCreateTimeout;
            return this;
        }

        @Override
        public BayBuilder name(String name) {
            model.name = name;
            return this;
        }

        public static class Bays extends ListResult<MagnumBay> {
            private static final long serialVersionUID = 1L;
            @JsonProperty("bays")
            private List<MagnumBay> list;

            @Override
            public List<MagnumBay> value() {
                return list;
            }
        }
    }
}
