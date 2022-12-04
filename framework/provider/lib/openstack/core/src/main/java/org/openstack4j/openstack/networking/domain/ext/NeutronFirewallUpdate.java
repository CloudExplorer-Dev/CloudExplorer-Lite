package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.FirewallUpdate;
import org.openstack4j.model.network.ext.builder.FirewallUpdateBuilder;

/**
 * An entity used to update Neutron Firewall (FwaaS).
 *
 * @author Vishvesh Deshmukh
 */
@JsonRootName("firewall")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronFirewallUpdate implements FirewallUpdate {

    private static final long serialVersionUID = 1L;

    private String name;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String description;

    private Boolean shared;

    @JsonProperty("admin_state_up")
    private Boolean adminStateUp;

    @JsonProperty("firewall_policy_id")
    private String policyId;

    /**
     * @return FirewallUpdateBuilder
     */
    public static FirewallUpdateBuilder builder() {
        return new FirewallUpdateConcreteBuilder();
    }

    /**
     * Wrap this Firewall to a builder
     *
     * @return FirewallUpdateBuilder
     */
    @Override
    public FirewallUpdateBuilder toBuilder() {
        return new FirewallUpdateConcreteBuilder(this);
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
    public Boolean isAdminStateUp() {
        return adminStateUp != null && adminStateUp;
    }

    @Override
    public Boolean isShared() {
        return shared != null && shared;
    }

    @JsonIgnore
    @Override
    public String getPolicy() {
        return policyId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("policyId", policyId)
                .add("shared", shared).add("adminStateUp", adminStateUp)
                .add("tenantId", tenantId).add("description", description)
                .toString();
    }

    public static class FirewallUpdateConcreteBuilder implements FirewallUpdateBuilder {
        NeutronFirewallUpdate f;

        public FirewallUpdateConcreteBuilder() {
            this(new NeutronFirewallUpdate());
        }

        public FirewallUpdateConcreteBuilder(NeutronFirewallUpdate f) {
            this.f = f;
        }

        @Override
        public FirewallUpdate build() {
            return f;
        }

        @Override
        public FirewallUpdateBuilder from(FirewallUpdate in) {
            this.f = (NeutronFirewallUpdate) in;
            return this;
        }

        @Override
        public FirewallUpdateBuilder tenantId(String tenantId) {
            f.tenantId = tenantId;
            return this;
        }

        @Override
        public FirewallUpdateBuilder name(String name) {
            f.name = name;
            return this;
        }

        @Override
        public FirewallUpdateBuilder description(String description) {
            f.description = description;
            return this;
        }

        @Override
        public FirewallUpdateBuilder adminStateUp(Boolean adminStateUp) {
            f.adminStateUp = adminStateUp;
            return this;
        }

        @Override
        public FirewallUpdateBuilder shared(Boolean shared) {
            f.shared = shared;
            return this;
        }

        @Override
        public FirewallUpdateBuilder policy(String policyId) {
            f.policyId = policyId;
            return this;
        }
    }

}
