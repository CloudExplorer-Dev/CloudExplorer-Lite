package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import org.openstack4j.model.gbp.L3Policy;
import org.openstack4j.model.gbp.builder.L3PolicyBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Model implementation for L3 Policy
 *
 * @author vinod borole
 */
@JsonRootName("l3_policy")
public class GbpL3Policy implements L3Policy {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    @JsonProperty("external_segments")
    private Map<String, List<String>> externalSegments;
    @JsonProperty("ip_pool")
    private String ipPool;
    @JsonProperty("ip_version")
    private int ipVersion;
    @JsonProperty("l2_policies")
    private List<String> l2Policies;
    private List<String> routers;
    private Boolean shared;
    @JsonProperty("subnet_prefix_length")
    private String subnetPrefixLength;

    public static L3PolicyBuilder builder() {
        return new L3PolicyConcreteBuilder();
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Map<String, List<String>> getExternalSegments() {
        return externalSegments;
    }

    @Override
    public String getIpPool() {
        return ipPool;
    }

    @Override
    public int getIpVersion() {
        return ipVersion;
    }

    @Override
    public List<String> getL2Policies() {
        return l2Policies;
    }

    @Override
    public List<String> getRouters() {
        return routers;
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public String getSubnetPrefixLength() {
        return subnetPrefixLength;
    }

    @Override
    public L3PolicyBuilder toBuilder() {
        return new L3PolicyConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("externalSegments", externalSegments).add("ipPool", ipPool).add("ipVersion", ipVersion)
                .add("l2Policies", l2Policies).add("routers", routers).add("shared", shared).add("subnetPrefixLength", subnetPrefixLength).toString();
    }

    public static class L3Policies extends ListResult<GbpL3Policy> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("l3_policies")
        private List<GbpL3Policy> l3Policies;

        @Override
        protected List<GbpL3Policy> value() {
            return l3Policies;
        }

    }

    public static class L3PolicyConcreteBuilder implements L3PolicyBuilder {

        private GbpL3Policy l3Policy;

        public L3PolicyConcreteBuilder(GbpL3Policy gbpL3Policy) {
            this.l3Policy = gbpL3Policy;
        }

        public L3PolicyConcreteBuilder() {
            this(new GbpL3Policy());
        }

        @Override
        public L3Policy build() {
            return l3Policy;
        }

        @Override
        public L3PolicyBuilder from(L3Policy in) {
            this.l3Policy = (GbpL3Policy) in;
            return this;
        }

        @Override
        public L3PolicyBuilder name(String name) {
            this.l3Policy.name = name;
            return this;
        }

        @Override
        public L3PolicyBuilder description(String description) {
            this.l3Policy.description = description;
            return this;
        }

        @Override
        public L3PolicyBuilder ipVersion(int ipVersion) {
            this.l3Policy.ipVersion = ipVersion;
            return this;
        }

        @Override
        public L3PolicyBuilder ippool(String ippool) {
            this.l3Policy.ipPool = ippool;
            return this;
        }

        @Override
        public L3PolicyBuilder subnetPrefixLength(String subnetPrefixLength) {
            this.l3Policy.subnetPrefixLength = subnetPrefixLength;
            return this;
        }

        @Override
        public L3PolicyBuilder isShared(boolean shared) {
            this.l3Policy.shared = shared;
            return this;
        }

        @Override
        public L3PolicyBuilder externalSegments(List<String> extSegmentIds) {
            this.l3Policy.externalSegments = Maps.newHashMap();
            for (String extSegId : extSegmentIds) {
                this.l3Policy.externalSegments.put(extSegId, new ArrayList<String>());
            }
            return this;
        }

    }
}
