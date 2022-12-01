package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.PolicyAction;
import org.openstack4j.model.gbp.builder.PolicyActionCreateBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Policy Action
 *
 * @author vinod borole
 */
@JsonRootName("policy_action")
public class GbpPolicyAction implements PolicyAction {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    private Boolean shared;
    @JsonProperty("action_type")
    private PolicyActionProtocol actionType;
    @JsonProperty("action_value")
    private String actionValue;

    public static PolicyActionCreateBuilder builder() {
        return new PolicyActionConcreteBuilder();
    }

    @Override
    public PolicyActionCreateBuilder toBuilder() {
        return new PolicyActionConcreteBuilder(this);
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
        return this.shared == null ? false : shared;
    }

    @Override
    public PolicyActionProtocol getActionType() {
        return actionType;
    }

    @Override
    public String getActionValue() {
        return actionValue;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("actionType", actionType).add("actionValue", actionValue).add("shared", shared).toString();
    }

    public static class PolicyActions extends ListResult<GbpPolicyAction> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("policy_actions")
        private List<GbpPolicyAction> policyActions;

        @Override
        protected List<GbpPolicyAction> value() {
            return policyActions;
        }

    }

    public static class PolicyActionConcreteBuilder implements PolicyActionCreateBuilder {

        private GbpPolicyAction policyAction;

        public PolicyActionConcreteBuilder(GbpPolicyAction gbpPolicyAction) {
            this.policyAction = gbpPolicyAction;
        }

        public PolicyActionConcreteBuilder() {
            this(new GbpPolicyAction());
        }

        @Override
        public PolicyAction build() {
            return policyAction;
        }

        @Override
        public PolicyActionCreateBuilder from(PolicyAction in) {
            this.policyAction = (GbpPolicyAction) in;
            return this;
        }

        @Override
        public PolicyActionCreateBuilder name(String name) {
            this.policyAction.name = name;
            return this;
        }

        @Override
        public PolicyActionCreateBuilder description(String description) {
            this.policyAction.description = description;
            return this;
        }

        @Override
        public PolicyActionCreateBuilder actionType(PolicyActionProtocol actionType) {
            this.policyAction.actionType = actionType;
            return this;
        }

        @Override
        public PolicyActionCreateBuilder shared(boolean shared) {
            this.policyAction.shared = shared;
            return this;
        }

    }


}
