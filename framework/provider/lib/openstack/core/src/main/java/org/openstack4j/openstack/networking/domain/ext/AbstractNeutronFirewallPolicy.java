package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.api.Apis;
import org.openstack4j.model.network.ext.FirewallPolicy;
import org.openstack4j.model.network.ext.FirewallRule;
import org.openstack4j.model.network.ext.builder.FirewallPolicyBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A Neutron Firewall (FwaaS) : Firewall Policy Entity.
 *
 * <p>This is the Parent class which is extended by {@link NeutronFirewallPolicy} & {@link NeutronFirewalPolicyRule} classes.
 * Prior has `@JsonRootName("firewall_policy")` attribute whereas the later doesn't have
 * (which is used by `rule_insert/rule_remove` calls - which doesn't require JsonRootName).</p>
 *
 * @author Vishvesh Deshmukh
 * @see NeutronFirewallPolicy
 * @see NeutronFirewallPolicyRule
 */
public class AbstractNeutronFirewallPolicy implements FirewallPolicy {

    private static final long serialVersionUID = 1L;

    protected String id;

    protected String name;

    @JsonProperty("tenant_id")
    protected String tenantId;

    protected String description;

    protected Boolean shared;

    protected Boolean audited;

    @JsonProperty("firewall_rules")
    protected List<String> firewallRules;

    protected List<NeutronFirewallRule> neutronFirewallRules;

    @JsonProperty("firewall_list")
    private List<String> firewallList;

    /**
     * @return FirewallPolicyBuilder
     */
    public static FirewallPolicyBuilder builder() {
        return new FirewallPolicyConcreteBuilder();
    }

    /**
     * Wrap this FirewallPolicy to a builder
     *
     * @return FirewallPolicyBuilder
     */
    @Override
    public FirewallPolicyBuilder toBuilder() {
        return new FirewallPolicyConcreteBuilder(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTenantId() {
        return tenantId;
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

    @JsonIgnore
    @Override
    public List<? extends FirewallRule> getNeutronFirewallRules() {
        neutronFirewallRules = new ArrayList<NeutronFirewallRule>();
        if (neutronFirewallRules == null && (firewallRules != null && firewallRules.size() > 0)) {
            neutronFirewallRules = new ArrayList<NeutronFirewallRule>();
            for (String ruleId : firewallRules) {
                NeutronFirewallRule rule = (NeutronFirewallRule) Apis.getNetworkingServices().
                        firewalls().firewallrule().get(ruleId);
                neutronFirewallRules.add(rule);
            }
        }
        return neutronFirewallRules;
    }

    @Override
    public List<String> getFirewallList() {
        return firewallList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("shared", shared).add("audited", audited)
                .add("tenantId", tenantId).add("description", description)
                .add("firewallRuleIds", firewallRules).add("neutronFirewallRules", neutronFirewallRules)
                .add("firewallList", firewallList)
                .toString();
    }

    public static class FirewallPolicies extends ListResult<NeutronFirewallPolicy> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("firewall_policies")
        List<NeutronFirewallPolicy> firewallPolicies;

        @Override
        public List<NeutronFirewallPolicy> value() {
            return firewallPolicies;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("firewallPolicies", firewallPolicies).toString();
        }
    }

    public static class FirewallPolicyConcreteBuilder implements FirewallPolicyBuilder {
        NeutronFirewallPolicy f;

        public FirewallPolicyConcreteBuilder() {
            this(new NeutronFirewallPolicy());
        }

        public FirewallPolicyConcreteBuilder(FirewallPolicy f) {
            this.f = (NeutronFirewallPolicy) f;
        }

        @Override
        public FirewallPolicy build() {
            return f;
        }

        @Override
        public FirewallPolicyBuilder from(FirewallPolicy in) {
            this.f = (NeutronFirewallPolicy) in;
            return this;
        }

        @Override
        public FirewallPolicyBuilder tenantId(String tenantId) {
            f.tenantId = tenantId;
            return this;
        }

        @Override
        public FirewallPolicyBuilder name(String name) {
            f.name = name;
            return this;
        }

        @Override
        public FirewallPolicyBuilder description(String description) {
            f.description = description;
            return this;
        }

        @Override
        public FirewallPolicyBuilder shared(Boolean shared) {
            f.shared = shared;
            return this;
        }

        @Override
        public FirewallPolicyBuilder audited(Boolean audited) {
            f.audited = audited;
            return this;
        }

        @Override
        public FirewallPolicyBuilder firewallRules(List<String> ruleIdList) {
            f.firewallRules = ruleIdList;
            return this;
        }
    }
}
