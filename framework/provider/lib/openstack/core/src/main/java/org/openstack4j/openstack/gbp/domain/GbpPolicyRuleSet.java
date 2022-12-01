package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.PolicyRuleSet;
import org.openstack4j.model.gbp.builder.PolicyRuleSetBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Policy rule set
 *
 * @author vinod borole
 */
@JsonRootName("policy_rule_set")
public class GbpPolicyRuleSet implements PolicyRuleSet {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    private boolean shared;
    @JsonProperty("parent_id")
    private String parentId;
    @JsonProperty("child_policy_rule_sets")
    private List<String> childPolicyRuleSets;
    @JsonProperty("policy_rules")
    private List<String> policyRules;

    public static PolicyRuleSetBuilder builder() {
        return new PolicyRuleSetConcreteBuilder();
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
    public boolean isShared() {
        return shared;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public List<String> getChildPolicyRuleSets() {
        return childPolicyRuleSets;
    }

    @Override
    public List<String> getPolicyRules() {
        return policyRules;
    }

    @Override
    public PolicyRuleSetBuilder toBuilder() {
        return new PolicyRuleSetConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("parentId", parentId).add("childPolicyRuleSets", childPolicyRuleSets).add("shared", shared).add("policyRules", policyRules).toString();
    }

    public static class PolicyRuleSets extends ListResult<GbpPolicyRuleSet> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("policy_rule_sets")
        private List<GbpPolicyRuleSet> policyRuleSets;

        @Override
        protected List<GbpPolicyRuleSet> value() {
            return policyRuleSets;
        }

    }

    public static class PolicyRuleSetConcreteBuilder implements PolicyRuleSetBuilder {

        private GbpPolicyRuleSet policyRuleSet;

        public PolicyRuleSetConcreteBuilder(GbpPolicyRuleSet gbpPolicyRuleSet) {
            this.policyRuleSet = gbpPolicyRuleSet;
        }

        public PolicyRuleSetConcreteBuilder() {
            this(new GbpPolicyRuleSet());
        }

        @Override
        public PolicyRuleSet build() {
            return policyRuleSet;
        }

        @Override
        public PolicyRuleSetBuilder from(PolicyRuleSet in) {
            this.policyRuleSet = (GbpPolicyRuleSet) in;
            return this;
        }

        @Override
        public PolicyRuleSetBuilder name(String name) {
            this.policyRuleSet.name = name;
            return this;
        }

        @Override
        public PolicyRuleSetBuilder description(String description) {
            this.policyRuleSet.description = description;
            return this;
        }

        @Override
        public PolicyRuleSetBuilder shared(boolean shared) {
            this.policyRuleSet.shared = shared;
            return this;
        }

        @Override
        public PolicyRuleSetBuilder rules(List<String> ruleIds) {
            this.policyRuleSet.policyRules = ruleIds;
            return this;
        }

    }

}
