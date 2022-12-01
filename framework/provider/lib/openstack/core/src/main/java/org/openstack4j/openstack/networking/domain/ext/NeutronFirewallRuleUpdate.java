package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.ext.FirewallRuleUpdate;
import org.openstack4j.model.network.ext.builder.FirewallRuleUpdateBuilder;
import org.openstack4j.openstack.networking.domain.ext.NeutronFirewallRule.FirewallRuleAction;
import org.openstack4j.openstack.networking.domain.ext.NeutronFirewallRule.IPProtocol;

/**
 * An entity used to update Neutron Firewall Rule (FwaaS).
 *
 * @author Vishvesh Deshmukh
 */
@JsonRootName("firewall_rule")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronFirewallRuleUpdate implements FirewallRuleUpdate {

    private static final long serialVersionUID = 1L;

    private String name;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String description;

    private Boolean enabled;

    private Boolean shared;

    private FirewallRuleAction action;

    @JsonProperty("source_ip_address")
    private String sourceIpAddress;

    @JsonProperty("destination_ip_address")
    private String destinationIpAddress;

    private IPProtocol protocol;

    @JsonProperty("ip_version")
    private IPVersionType ipVersion;

    @JsonProperty("source_port")
    private String sourcePort;

    @JsonProperty("destination_port")
    private String destinationPort;

    /**
     * @return FirewallRuleUpdateBuilder
     */
    public static FirewallRuleUpdateBuilder builder() {
        return new FirewallRuleUpdateConcreteBuilder();
    }

    /**
     * Wrap this FirewallRuleUpdate to a builder
     *
     * @return FirewallRuleUpdateBuilder
     */
    @Override
    public FirewallRuleUpdateBuilder toBuilder() {
        return new FirewallRuleUpdateConcreteBuilder(this);
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
                .add("name", name).add("action", action).add("ipVersion", ipVersion)
                .add("enabled", enabled).add("shared", shared).add("tenantId", tenantId)
                .add("sourceIpAddress", sourceIpAddress)
                .add("destinationIpAddress", destinationIpAddress)
                .add("sourcePort", sourcePort).add("destinationPort", destinationPort)
                .add("description", description).add("protocol", protocol)
                .toString();
    }

    public static class FirewallRuleUpdateConcreteBuilder implements FirewallRuleUpdateBuilder {
        NeutronFirewallRuleUpdate f;

        public FirewallRuleUpdateConcreteBuilder() {
            this(new NeutronFirewallRuleUpdate());
        }

        public FirewallRuleUpdateConcreteBuilder(NeutronFirewallRuleUpdate f) {
            this.f = f;
        }

        @Override
        public FirewallRuleUpdate build() {
            return f;
        }

        @Override
        public FirewallRuleUpdateBuilder from(FirewallRuleUpdate in) {
            this.f = (NeutronFirewallRuleUpdate) in;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder tenantId(String tenantId) {
            f.tenantId = tenantId;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder name(String name) {
            f.name = name;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder description(String description) {
            f.description = description;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder shared(Boolean shared) {
            f.shared = shared;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder protocol(IPProtocol protocol) {
            f.protocol = protocol;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder ipVersion(IPVersionType ipVersion) {
            f.ipVersion = ipVersion;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder sourceIpAddress(String sourceIpAddress) {
            f.sourceIpAddress = sourceIpAddress;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder destinationIpAddress(String destinationIpAddress) {
            f.destinationIpAddress = destinationIpAddress;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder sourcePort(String sourcePort) {
            f.sourcePort = sourcePort;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder destinationPort(String destinationPort) {
            f.destinationPort = destinationPort;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder action(FirewallRuleAction action) {
            f.action = action;
            return this;
        }

        @Override
        public FirewallRuleUpdateBuilder enabled(Boolean enabled) {
            f.enabled = enabled;
            return this;
        }
    }
}
