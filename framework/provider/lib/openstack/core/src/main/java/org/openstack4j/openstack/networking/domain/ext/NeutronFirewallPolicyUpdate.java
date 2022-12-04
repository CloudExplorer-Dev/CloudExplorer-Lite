package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.FirewallPolicyUpdate;
import org.openstack4j.model.network.ext.builder.FirewallPolicyUpdateBuilder;

import java.util.List;

/**
 * An entity used to update Neutron Firewall Policy (FwaaS).
 *
 * @author Vishvesh Deshmukh
 */
@JsonRootName("firewall_policy")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronFirewallPolicyUpdate implements FirewallPolicyUpdate {

    private static final long serialVersionUID = 1L;

    private String name;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String description;

    private Boolean shared;

    private Boolean audited;

    @JsonProperty("firewall_rules")
    private List<String> firewallRules;

    /**
     * @return FirewallPolicyUpdateBuilder
     */
    public static FirewallPolicyUpdateBuilder builder() {
        return new FirewallPolicyUpdateConcreteBuilder();
    }

    /**
     * Wrap this FirewallPolicyUpdate to a builder
     *
     * @return FirewallPolicyUpdateBuilder
     */
    @Override
    public FirewallPolicyUpdateBuilder toBuilder() {
        return new FirewallPolicyUpdateConcreteBuilder(this);
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
    public Boolean isShared() {
        return shared != null && shared;
    }

    @Override
    public Boolean isAudited() {
        return audited != null && audited;
    }

    @JsonIgnore
    @Override
    public List<String> getFirewallRuleIds() {
        return firewallRules;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("shared", shared).add("audited", audited)
                .add("tenantId", tenantId).add("description", description)
                .add("firewallRuleIds", firewallRules)
                .toString();
    }

    public static class FirewallPolicyUpdateConcreteBuilder implements FirewallPolicyUpdateBuilder {
        NeutronFirewallPolicyUpdate f;

        public FirewallPolicyUpdateConcreteBuilder() {
            this(new NeutronFirewallPolicyUpdate());
        }

        public FirewallPolicyUpdateConcreteBuilder(NeutronFirewallPolicyUpdate f) {
            this.f = f;
        }

        @Override
        public FirewallPolicyUpdate build() {
            return f;
        }

        @Override
        public FirewallPolicyUpdateBuilder from(FirewallPolicyUpdate in) {
            this.f = (NeutronFirewallPolicyUpdate) in;
            return this;
        }

        @Override
        public FirewallPolicyUpdateBuilder name(String name) {
            f.name = name;
            return this;
        }

        @Override
        public FirewallPolicyUpdateBuilder description(String description) {
            f.description = description;
            return this;
        }

        @Override
        public FirewallPolicyUpdateBuilder shared(Boolean shared) {
            f.shared = shared;
            return this;
        }

        @Override
        public FirewallPolicyUpdateBuilder audited(Boolean audited) {
            f.audited = audited;
            return this;
        }

        @Override
        public FirewallPolicyUpdateBuilder firewallRules(List<String> ruleIdList) {
            f.firewallRules = ruleIdList;
            return this;
        }
    }
}
