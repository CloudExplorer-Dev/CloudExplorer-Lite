package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.L2Policy;
import org.openstack4j.model.gbp.builder.L2PolicyBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for L2 Policy
 *
 * @author vinod borole
 */
@JsonRootName("l2_policy")
public class GbpL2Policy implements L2Policy {

    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    @JsonProperty("network_id")
    private String networkId;
    @JsonProperty("l3_policy_id")
    private String l3PolicyId;
    private Boolean shared;
    @JsonProperty("policy_target_groups")
    private List<String> policyTargetGroups;

    public static L2PolicyBuilder builder() {
        return new L2PolicyConcreteBuilder();
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
    public L2PolicyBuilder toBuilder() {
        return new L2PolicyConcreteBuilder(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public String getL3PolicyId() {
        return l3PolicyId;
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public List<String> getPolicyTargetGroups() {
        return policyTargetGroups;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("networkId", networkId).add("l3PolicyId", l3PolicyId).add("shared", shared)
                .add("policyTargetGroups", policyTargetGroups).toString();
    }

    public static class L2Policies extends ListResult<GbpL2Policy> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("l2_policies")
        private List<GbpL2Policy> l2Policies;

        @Override
        protected List<GbpL2Policy> value() {
            return l2Policies;
        }

    }

    public static class L2PolicyConcreteBuilder implements L2PolicyBuilder {

        private GbpL2Policy l2Policy;

        public L2PolicyConcreteBuilder(GbpL2Policy gbpL2Policy) {
            this.l2Policy = gbpL2Policy;
        }

        public L2PolicyConcreteBuilder() {
            this(new GbpL2Policy());
        }

        @Override
        public L2Policy build() {
            return l2Policy;
        }

        @Override
        public L2PolicyBuilder from(L2Policy in) {
            l2Policy = (GbpL2Policy) in;
            return this;
        }

        @Override
        public L2PolicyBuilder name(String name) {
            l2Policy.name = name;
            return this;
        }

        @Override
        public L2PolicyBuilder description(String description) {
            l2Policy.description = description;
            return this;
        }

        @Override
        public L2PolicyBuilder isShared(boolean shared) {
            l2Policy.shared = shared;
            return this;
        }

        @Override
        public L2PolicyBuilder networkId(String id) {
            l2Policy.id = id;
            return this;
        }

        @Override
        public L2PolicyBuilder l3PolicyId(String id) {
            l2Policy.l3PolicyId = id;
            return this;
        }

        @Override
        public L2PolicyBuilder policyTargetGroups(List<String> ids) {
            l2Policy.policyTargetGroups = ids;
            return this;
        }
    }


}
