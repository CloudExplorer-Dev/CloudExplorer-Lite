package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.ext.FirewallRule;
import org.openstack4j.model.network.ext.builder.FirewallRuleBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A Neutron Firewall (FwaaS) : Firewall Rule Entity.
 *
 * @author Vishvesh Deshmukh
 */
@JsonRootName("firewall_rule")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronFirewallRule implements FirewallRule {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String description;
    private Boolean enabled;
    private Boolean shared;
    @JsonProperty("firewall_policy_id")
    private String policyId;
    private FirewallRuleAction action;
    @JsonProperty("source_ip_address")
    private String sourceIpAddress;
    @JsonProperty("destination_ip_address")
    private String destinationIpAddress;
    private Integer position;
    private IPProtocol protocol;
    @JsonProperty("ip_version")
    private IPVersionType ipVersion;
    @JsonProperty("source_port")
    private String sourcePort;
    @JsonProperty("destination_port")
    private String destinationPort;

    /**
     * @return FirewallRuleBuilder
     */
    public static FirewallRuleBuilder builder() {
        return new FirewallRuleConcreteBuilder();
    }

    /**
     * Wrap this FirewallRule to a builder
     *
     * @return FirewallRuleBuilder
     */
    @Override
    public FirewallRuleBuilder toBuilder() {
        return new FirewallRuleConcreteBuilder(this);
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
    public String getPolicy() {
        return policyId;
    }

    @Override
    public IPProtocol getProtocol() {
        return protocol;
    }

    @Override
    public IPVersionType getIpVersion() {
        return ipVersion;
    }

    @Override
    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    @Override
    public String getDestinationIpAddress() {
        return destinationIpAddress;
    }

    @Override
    public String getSourcePort() {
        return sourcePort;
    }

    @Override
    public String getDestinationPort() {
        return destinationPort;
    }

    @Override
    public Integer getPosition() {
        return position;
    }

    @Override
    public FirewallRuleAction getAction() {
        return action;
    }

    @Override
    public Boolean isEnabled() {
        return enabled != null && enabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("position", position)
                .add("action", action).add("ipVersion", ipVersion)
                .add("policyId", policyId).add("enabled", enabled)
                .add("shared", shared).add("tenantId", tenantId)
                .add("sourceIpAddress", sourceIpAddress)
                .add("destinationIpAddress", destinationIpAddress)
                .add("sourcePort", sourcePort).add("destinationPort", destinationPort)
                .add("description", description).add("protocol", protocol)
                .toString();
    }

    /**
     * <p>Action of a Neutron (Firewall Rule - FwaaS) entity.</p>
     *
     * <p>Indicates whether firewall rule resource has action ALLOW/DENY.</p>
     *
     * @author Vishvesh Deshmukh
     */
    public enum FirewallRuleAction {
        ALLOW,
        DENY,
        UNRECOGNIZED;

        @JsonCreator
        public static FirewallRuleAction value(String v) {
            if (v == null) return UNRECOGNIZED;
            try {
                return valueOf(v.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    /**
     * <p>IPProtocolType of a Neutron (Firewall Rule - FwaaS) entity.</p>
     *
     * <p>Represents an IPProtocolType of a Neutron (Firewall Rule - FwaaS) entity.</p>
     *
     * @author Vishvesh Deshmukh
     */
    public enum IPProtocol {
        TCP,
        UDP,
        ICMP,
        UNRECOGNIZED;

        @JsonCreator
        public static IPProtocol value(String v) {
            if (v == null) return UNRECOGNIZED;
            try {
                return valueOf(v.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    public static class FirewallRules extends ListResult<NeutronFirewallRule> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("firewall_rules")
        List<NeutronFirewallRule> firewallRules;

        @Override
        public List<NeutronFirewallRule> value() {
            return firewallRules;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("firewall_rules", firewallRules).toString();
        }
    }

    public static class FirewallRuleConcreteBuilder implements FirewallRuleBuilder {
        NeutronFirewallRule f;

        public FirewallRuleConcreteBuilder() {
            this(new NeutronFirewallRule());
        }

        public FirewallRuleConcreteBuilder(NeutronFirewallRule f) {
            this.f = f;
        }

        @Override
        public FirewallRule build() {
            return f;
        }

        @Override
        public FirewallRuleBuilder from(FirewallRule in) {
            this.f = (NeutronFirewallRule) in;
            return this;
        }

        @Override
        public FirewallRuleBuilder tenantId(String tenantId) {
            f.tenantId = tenantId;
            return this;
        }

        @Override
        public FirewallRuleBuilder name(String name) {
            f.name = name;
            return this;
        }

        @Override
        public FirewallRuleBuilder description(String description) {
            f.description = description;
            return this;
        }

        @Override
        public FirewallRuleBuilder shared(Boolean shared) {
            f.shared = shared;
            return this;
        }

        @Override
        public FirewallRuleBuilder protocol(IPProtocol protocol) {
            f.protocol = protocol;
            return this;
        }

        @Override
        public FirewallRuleBuilder ipVersion(IPVersionType ipVersion) {
            f.ipVersion = ipVersion;
            return this;
        }

        @Override
        public FirewallRuleBuilder sourceIpAddress(String sourceIpAddress) {
            f.sourceIpAddress = sourceIpAddress;
            return this;
        }

        @Override
        public FirewallRuleBuilder destinationIpAddress(String destinationIpAddress) {
            f.destinationIpAddress = destinationIpAddress;
            return this;
        }

        @Override
        public FirewallRuleBuilder sourcePort(String sourcePort) {
            f.sourcePort = sourcePort;
            return this;
        }

        @Override
        public FirewallRuleBuilder destinationPort(String destinationPort) {
            f.destinationPort = destinationPort;
            return this;
        }

        @Override
        public FirewallRuleBuilder action(FirewallRuleAction action) {
            f.action = action;
            return this;
        }

        @Override
        public FirewallRuleBuilder enabled(Boolean enabled) {
            f.enabled = enabled;
            return this;
        }
    }
}
