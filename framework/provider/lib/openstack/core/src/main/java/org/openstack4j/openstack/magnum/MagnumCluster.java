package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Cluster;
import org.openstack4j.model.magnum.ClusterBuilder;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumCluster implements Cluster {
    private static final long serialVersionUID = 1L;
    @JsonProperty("status")
    private String status;
    @JsonProperty("cluster_template_id")
    private String clusterTemplateId;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("links")
    private List<GenericLink> links;
    @JsonProperty("stack_id")
    private String stackId;
    @JsonProperty("master_count")
    private Integer masterCount;
    @JsonProperty("create_timeout")
    private Integer createTimeout;
    @JsonProperty("node_count")
    private Integer nodeCount;
    @JsonProperty("discovery_url")
    private String discoveryUrl;
    @JsonProperty("keypair")
    private String keypair;
    @JsonProperty("name")
    private String name;

    public static ClusterBuilder builder() {
        return new ClusterConcreteBuilder();
    }

    @Override
    public ClusterBuilder toBuilder() {
        return new ClusterConcreteBuilder(this);
    }

    public String getStatus() {
        return status;
    }

    public String getClusterTemplateId() {
        return clusterTemplateId;
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

    public Integer getMasterCount() {
        return masterCount;
    }

    public Integer getCreateTimeout() {
        return createTimeout;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public String getDiscoveryUrl() {
        return discoveryUrl;
    }

    public String getKeypair() {
        return keypair;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("status", status)
                .add("clusterTemplateId", clusterTemplateId).add("uuid", uuid).add("links", links)
                .add("stackId", stackId).add("masterCount", masterCount).add("createTimeout", createTimeout)
                .add("nodeCount", nodeCount).add("discoveryUrl", discoveryUrl).add("keypair", keypair).add("name", name)
                .toString();
    }

    /**
     * Concrete builder containing MagnumCluster as model
     */
    public static class ClusterConcreteBuilder implements ClusterBuilder {
        MagnumCluster model;

        public ClusterConcreteBuilder() {
            this(new MagnumCluster());
        }

        public ClusterConcreteBuilder(MagnumCluster model) {
            this.model = model;
        }

        @Override
        public Cluster build() {
            return model;
        }

        @Override
        public ClusterBuilder from(Cluster in) {
            if (in != null)
                this.model = (MagnumCluster) in;
            return this;
        }

        @Override
        public ClusterBuilder status(String status) {
            model.status = status;
            return this;
        }

        @Override
        public ClusterBuilder clusterTemplateId(String clusterTemplateId) {
            model.clusterTemplateId = clusterTemplateId;
            return this;
        }

        @Override
        public ClusterBuilder uuid(String uuid) {
            model.uuid = uuid;
            return this;
        }

        @Override
        public ClusterBuilder links(List<GenericLink> links) {
            model.links = links;
            return this;
        }

        @Override
        public ClusterBuilder stackId(String stackId) {
            model.stackId = stackId;
            return this;
        }

        @Override
        public ClusterBuilder masterCount(Integer masterCount) {
            model.masterCount = masterCount;
            return this;
        }

        @Override
        public ClusterBuilder createTimeout(Integer createTimeout) {
            model.createTimeout = createTimeout;
            return this;
        }

        @Override
        public ClusterBuilder nodeCount(Integer nodeCount) {
            model.nodeCount = nodeCount;
            return this;
        }

        @Override
        public ClusterBuilder discoveryUrl(String discoveryUrl) {
            model.discoveryUrl = discoveryUrl;
            return this;
        }

        @Override
        public ClusterBuilder keypair(String keypair) {
            model.keypair = keypair;
            return this;
        }

        @Override
        public ClusterBuilder name(String name) {
            model.name = name;
            return this;
        }
    }

    public static class Clusters extends ListResult<MagnumCluster> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("clusters")
        private List<MagnumCluster> list;

        @Override
        public List<MagnumCluster> value() {
            return list;
        }
    }
}
