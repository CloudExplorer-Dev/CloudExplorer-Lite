package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.PolicyActionUpdate;
import org.openstack4j.model.gbp.builder.PolicyActionUpdateBuilder;

/**
 * Model implementation for Policy Action
 *
 * @author vinod borole
 */
@JsonRootName("policy_action")
public class GbpPolicyActionUpdate implements PolicyActionUpdate {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private Boolean shared;

    public static PolicyActionUpdateBuilder builder() {
        return new PolicyActionUpdateConcreteBuilder();
    }

    @Override
    public PolicyActionUpdateBuilder toBuilder() {
        return new PolicyActionUpdateConcreteBuilder(this);
    }

    @Override
    public String getName() {
        return name;
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
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("name", name).add("desription", description)
                .add("shared", shared).toString();
    }

    public static class PolicyActionUpdateConcreteBuilder implements PolicyActionUpdateBuilder {

        private GbpPolicyActionUpdate policyAction;

        public PolicyActionUpdateConcreteBuilder(GbpPolicyActionUpdate gbpPolicyAction) {
            this.policyAction = gbpPolicyAction;
        }

        public PolicyActionUpdateConcreteBuilder() {
            this(new GbpPolicyActionUpdate());
        }

        @Override
        public PolicyActionUpdate build() {
            return policyAction;
        }

        @Override
        public PolicyActionUpdateBuilder name(String name) {
            this.policyAction.name = name;
            return this;
        }

        @Override
        public PolicyActionUpdateBuilder description(String description) {
            this.policyAction.description = description;
            return this;
        }


        @Override
        public PolicyActionUpdateBuilder shared(boolean shared) {
            this.policyAction.shared = shared;
            return this;
        }

        @Override
        public PolicyActionUpdateBuilder from(PolicyActionUpdate in) {
            this.policyAction = (GbpPolicyActionUpdate) in;
            return this;
        }

    }


}
