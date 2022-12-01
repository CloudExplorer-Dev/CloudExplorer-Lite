package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.PolicyRule;
import org.openstack4j.model.gbp.builder.PolicyRuleBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Policy rule
 *
 * @author vinod borole
 */
@JsonRootName("policy_rule")
public class GbpPolicyRule implements PolicyRule {

    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    @JsonProperty("policy_classifier_id")
    private String policyClassifierId;
    @JsonProperty("policy_actions")
    private List<String> policyActions;
    private Boolean shared;
    private Boolean enabled;

    public static PolicyRuleBuilder builder() {
        return new PolicyRuleConcreteBuilder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("policyClassifierId", policyClassifierId).add("policyActions", policyActions).add("shared", shared).add("enabled", enabled).toString();
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
    public String getPolicyClassifierId() {
        return policyClassifierId;
    }

    @Override
    public List<String> getPolicyActions() {
        return policyActions;
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled == null ? false : shared;
    }

    @Override
    public PolicyRuleBuilder toBuilder() {
        return new PolicyRuleConcreteBuilder(this);
    }

    public static class PolicyRules extends ListResult<GbpPolicyRule> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("policy_rules")
        private List<GbpPolicyRule> policyRules;

        @Override
        protected List<GbpPolicyRule> value() {
            return policyRules;
        }

    }

    public static class PolicyRuleConcreteBuilder implements PolicyRuleBuilder {

        private GbpPolicyRule policyRule;

        public PolicyRuleConcreteBuilder(GbpPolicyRule gbpPolicyRule) {
            this.policyRule = gbpPolicyRule;
        }

        public PolicyRuleConcreteBuilder() {
            this(new GbpPolicyRule());
        }

        @Override
        public PolicyRule build() {
            return policyRule;
        }

        @Override
        public PolicyRuleBuilder from(PolicyRule in) {
            this.policyRule = (GbpPolicyRule) in;
            return this;
        }

        @Override
        public PolicyRuleBuilder name(String name) {
            this.policyRule.name = name;
            return this;
        }

        @Override
        public PolicyRuleBuilder description(String description) {
            this.policyRule.description = description;
            return this;
        }

        @Override
        public PolicyRuleBuilder shared(boolean shared) {
            this.policyRule.shared = shared;
            return this;
        }

        @Override
        public PolicyRuleBuilder classifier(String classifierId) {
            this.policyRule.policyClassifierId = classifierId;
            return this;
        }

        @Override
        public PolicyRuleBuilder actions(List<String> actionIds) {
            this.policyRule.policyActions = actionIds;
            return this;
        }


    }

}
