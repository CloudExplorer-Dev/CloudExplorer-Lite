package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.PolicyTargetGroup;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Policy target group
 *
 * @author vinod borole
 */
@JsonRootName("policy_target_group")
public class GbpPolicyTargetGroup implements PolicyTargetGroup {

    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    @JsonProperty("consumed_policy_rule_sets")
    private List<String> consumedPolicyRuleSets;
    @JsonProperty("provided_policy_rule_sets")
    private List<String> providedPolicyRuleSets;
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
    public List<String> getConsumedPolicyRuleSets() {
        return consumedPolicyRuleSets;
    }

    @Override
    public List<String> getProvidedPolicyRuleSets() {
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


    public static class PolicyTargetGroups extends ListResult<GbpPolicyTargetGroup> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("policy_target_groups")
        private List<GbpPolicyTargetGroup> policyTargetGroups;

        @Override
        public List<GbpPolicyTargetGroup> value() {
            return policyTargetGroups;
        }
    }


}
