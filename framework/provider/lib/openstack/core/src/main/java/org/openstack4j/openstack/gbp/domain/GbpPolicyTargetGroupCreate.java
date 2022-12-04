package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import org.openstack4j.model.gbp.PolicyTargetGroupCreate;
import org.openstack4j.model.gbp.builder.PolicyTargetGroupBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * Model implementation for Policy target group create
 *
 * @author vinod borole
 */
@JsonRootName("policy_target_group")
public class GbpPolicyTargetGroupCreate implements PolicyTargetGroupCreate {

    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    @JsonProperty("consumed_policy_rule_sets")
    private Map<String, String> consumedPolicyRuleSets;
    @JsonProperty("provided_policy_rule_sets")
    private Map<String, String> providedPolicyRuleSets;
    @JsonProperty("l2_policy_id")
    private String l2PolicyId;
    @JsonProperty("network_service_policy_id")
    private String networkServicePolicyId;
    @JsonProperty("policy_targets")
    private List<String> policyTargets;
    @JsonProperty("service_management")
    private Boolean serviceManagement;
    private Boolean shared;
    @JsonProperty("subnets")
    private List<String> subnets;

    public static PolicyTargetGroupBuilder builder() {
        return new PolicyTargetConcreteGroupBuilder();
    }

    @Override
    public PolicyTargetGroupBuilder toBuilder() {
        return new PolicyTargetConcreteGroupBuilder(this);
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
    public Map<String, String> getConsumedPolicyRuleSets() {
        return consumedPolicyRuleSets;
    }

    @Override
    public Map<String, String> getProvidedPolicyRuleSets() {
        return providedPolicyRuleSets;
    }

    @Override
    public String getL2PolicyId() {
        return l2PolicyId;
    }

    @Override
    public String getNetworkServicePolicyId() {
        return networkServicePolicyId;
    }

    @Override
    public List<String> getPolicyTargets() {
        return policyTargets;
    }

    @Override
    public boolean isServiceManagement() {
        return this.serviceManagement == null ? false : serviceManagement;
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public List<String> getSubnets() {
        return subnets;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("consumedPolicyRuleSets", consumedPolicyRuleSets).add("providedPolicyRuleSets", providedPolicyRuleSets)
                .add("l2PolicyId", l2PolicyId).add("networkServicePolicyId", networkServicePolicyId)
                .add("policyTargets", policyTargets).add("serviceManagement", serviceManagement).add("shared", shared).add("subnets", subnets).toString();
    }

    public static class PolicyTargetGroups extends ListResult<GbpPolicyTargetGroupCreate> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("policy_target_groups")
        private List<GbpPolicyTargetGroupCreate> policyTargetGroups;

        @Override
        public List<GbpPolicyTargetGroupCreate> value() {
            return policyTargetGroups;
        }
    }

    public static class PolicyTargetConcreteGroupBuilder implements PolicyTargetGroupBuilder {
        private GbpPolicyTargetGroupCreate policyTargetGroup;

        public PolicyTargetConcreteGroupBuilder(GbpPolicyTargetGroupCreate gbpPolicyTargetGroup) {
            this.policyTargetGroup = gbpPolicyTargetGroup;
        }

        public PolicyTargetConcreteGroupBuilder() {
            this(new GbpPolicyTargetGroupCreate());
        }

        @Override
        public PolicyTargetGroupCreate build() {
            return policyTargetGroup;
        }

        @Override
        public PolicyTargetGroupBuilder from(PolicyTargetGroupCreate in) {
            policyTargetGroup = (GbpPolicyTargetGroupCreate) in;
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder name(String name) {
            policyTargetGroup.name = name;
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder description(String description) {
            this.policyTargetGroup.description = description;
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder isShared(boolean shared) {
            this.policyTargetGroup.shared = shared;
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder consumedPolicyRuleSets(List<String> policyRuleSet) {
            this.policyTargetGroup.consumedPolicyRuleSets = Maps.newHashMap();
            for (String id : policyRuleSet) {
                this.policyTargetGroup.consumedPolicyRuleSets.put(id, "");
            }
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder providedPolicyRuleSets(List<String> policyRuleSet) {
            this.policyTargetGroup.providedPolicyRuleSets = Maps.newHashMap();
            for (String id : policyRuleSet) {
                this.policyTargetGroup.providedPolicyRuleSets.put(id, "");
            }
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder policyTargets(List<String> policyTargets) {
            this.policyTargetGroup.policyTargets = policyTargets;
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder networkServicePolicyId(String id) {
            this.policyTargetGroup.networkServicePolicyId = id;
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder l2Policy(String id) {
            this.policyTargetGroup.l2PolicyId = id;
            return this;
        }

        @Override
        public PolicyTargetGroupBuilder serviceManagement(boolean serviceManagement) {
            this.policyTargetGroup.serviceManagement = serviceManagement;
            return this;
        }

    }

}
