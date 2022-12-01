package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.SecurityGroupRule;
import org.openstack4j.model.network.builder.NetSecurityGroupRuleBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * An OpenStack Neutron Security Group Rule model.
 *
 * @author Nathan Anderson
 */
@JsonRootName("security_group_rule")
public class NeutronSecurityGroupRule implements SecurityGroupRule {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("security_group_id")
    private String securityGroupId;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("ethertype")
    private String etherType;

    @JsonProperty("port_range_max")
    private Integer portRangeMax;

    @JsonProperty("port_range_min")
    private Integer portRangeMin;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("remote_ip_prefix")
    private String remoteIpPrefix;

    @JsonProperty("remote_group_id")
    private String remoteGroupId;

    @JsonProperty("description")
    private String description;

    public static NetSecurityGroupRuleBuilder builder() {
        return new SecurityGroupRuleConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetSecurityGroupRuleBuilder toBuilder() {
        return new SecurityGroupRuleConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return this.tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDirection() {
        return this.direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEtherType() {
        return this.etherType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPortRangeMax() {
        return this.portRangeMax;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPortRangeMin() {
        return this.portRangeMin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRemoteGroupId() {
        return this.remoteGroupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRemoteIpPrefix() {
        return this.remoteIpPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSecurityGroupId() {
        return this.securityGroupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("tenantId", tenantId)
                .add("securityGroupId", securityGroupId)
                .add("direction", direction)
                .add("ethertype", etherType)
                .add("portRangeMin", portRangeMin)
                .add("portRangeMax", portRangeMax)
                .add("protocol", protocol)
                .add("remoteGroup", remoteGroupId)
                .add("remoteIpPrefix", remoteIpPrefix)
                .add("description", description)
                .addValue("\n")
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, tenantId, securityGroupId, direction, etherType,
                portRangeMin, portRangeMax, protocol, remoteGroupId, remoteIpPrefix, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronSecurityGroupRule) {
            NeutronSecurityGroupRule that = (NeutronSecurityGroupRule) obj;
            if (java.util.Objects.equals(id, that.id) &&
                    java.util.Objects.equals(tenantId, that.tenantId) &&
                    java.util.Objects.equals(securityGroupId, that.securityGroupId) &&
                    java.util.Objects.equals(direction, that.direction) &&
                    java.util.Objects.equals(etherType, that.etherType) &&
                    java.util.Objects.equals(portRangeMin, that.portRangeMin) &&
                    java.util.Objects.equals(portRangeMax, that.portRangeMax) &&
                    java.util.Objects.equals(protocol, that.protocol) &&
                    java.util.Objects.equals(remoteGroupId, that.remoteGroupId) &&
                    java.util.Objects.equals(remoteIpPrefix, that.remoteIpPrefix) &&
                    java.util.Objects.equals(description, that.description)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The Class SecurityGroupRules.
     *
     * @author Nathan Anderson
     */
    public static class SecurityGroupRules extends ListResult<NeutronSecurityGroupRule> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("security_group_rules")
        private List<NeutronSecurityGroupRule> rules;

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<NeutronSecurityGroupRule> value() {
            return rules;
        }
    }

    /**
     * The Class SecurityGroupRuleConcreteBuilder.
     *
     * @author Nathan Anderson
     */
    public static class SecurityGroupRuleConcreteBuilder implements NetSecurityGroupRuleBuilder {

        NeutronSecurityGroupRule r;

        /**
         * Instantiates a new security group rule concrete builder.
         */
        public SecurityGroupRuleConcreteBuilder() {
            r = new NeutronSecurityGroupRule();
        }

        /**
         * Instantiates a new security group rule concrete builder.
         *
         * @param rule the rule
         */
        public SecurityGroupRuleConcreteBuilder(SecurityGroupRule rule) {
            r = (NeutronSecurityGroupRule) rule;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecurityGroupRule build() {
            return r;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder from(SecurityGroupRule in) {
            r = (NeutronSecurityGroupRule) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder id(String id) {
            r.id = id;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder tenantId(String tenantId) {
            r.tenantId = tenantId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder securityGroupId(String groupId) {
            r.securityGroupId = groupId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder direction(String direction) {
            r.direction = direction;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder ethertype(String ethertype) {
            r.etherType = ethertype;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder portRangeMax(int max) {
            r.portRangeMax = max;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder portRangeMin(int min) {
            r.portRangeMin = min;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder protocol(String protocol) {
            r.protocol = protocol;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder remoteGroupId(String remoteGroupId) {
            r.remoteGroupId = remoteGroupId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder remoteIpPrefix(String prefix) {
            r.remoteIpPrefix = prefix;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetSecurityGroupRuleBuilder description(String description) {
            r.description = description;
            return this;
        }
    }
}
