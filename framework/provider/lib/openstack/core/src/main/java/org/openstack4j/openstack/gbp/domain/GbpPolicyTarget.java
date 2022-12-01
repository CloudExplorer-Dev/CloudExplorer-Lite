package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.PolicyTarget;
import org.openstack4j.model.gbp.builder.PolicyTargetBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Policy Target
 *
 * @author vinod borole
 */
@JsonRootName("policy_target")
public class GbpPolicyTarget implements PolicyTarget {

    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    @JsonProperty("cluster_id")
    private String clusterId;
    @JsonProperty("policy_target_group_id")
    private String policyTargetGroupId;
    @JsonProperty("port_id")
    private String portId;

    public static PolicyTargetBuilder builder() {
        return new PolicyTargetConcreteBuilder();
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
    public String getClusterId() {
        return clusterId;
    }

    @Override
    public String getPolicyTargetGroupId() {
        return policyTargetGroupId;
    }

    @Override
    public String getPortId() {
        return portId;
    }

    @Override
    public PolicyTargetBuilder toBuilder() {
        return new PolicyTargetConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("clusterId", clusterId).add("policyTargetGroupId", policyTargetGroupId).add("portId", portId).toString();
    }

    public static class PolicyTargets extends ListResult<GbpPolicyTarget> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("policy_targets")
        private List<GbpPolicyTarget> policyTargets;

        @Override
        protected List<GbpPolicyTarget> value() {
            return policyTargets;
        }

    }

    public static class PolicyTargetConcreteBuilder implements PolicyTargetBuilder {

        private GbpPolicyTarget policyTarget;

        public PolicyTargetConcreteBuilder(GbpPolicyTarget gbpPolicyTarget) {
            this.policyTarget = gbpPolicyTarget;
        }

        public PolicyTargetConcreteBuilder() {
            this(new GbpPolicyTarget());
        }

        @Override
        public PolicyTarget build() {
            return policyTarget;
        }

        @Override
        public PolicyTargetBuilder from(PolicyTarget in) {
            policyTarget = (GbpPolicyTarget) in;
            return this;
        }

        @Override
        public PolicyTargetBuilder portId(String portId) {
            policyTarget.portId = portId;
            return this;
        }

        @Override
        public PolicyTargetBuilder policyTargetGroupId(String policyTargetGroupId) {
            policyTarget.policyTargetGroupId = policyTargetGroupId;
            return this;
        }

        @Override
        public PolicyTargetBuilder clusterId(String clusterId) {
            policyTarget.clusterId = clusterId;
            return this;
        }

        @Override
        public PolicyTargetBuilder description(String description) {
            policyTarget.description = description;
            return this;
        }

        @Override
        public PolicyTargetBuilder name(String name) {
            policyTarget.name = name;
            return this;
        }

    }
}
