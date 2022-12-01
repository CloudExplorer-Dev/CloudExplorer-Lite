package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.IPProtocol;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.compute.builder.SecurityGroupRuleBuilder;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A Security Group which is defined the the (os-security-groups) extension
 *
 * @author Jeremy Unruh
 */
@JsonRootName("security_group")
@Deprecated
public class NovaSecGroupExtension implements SecGroupExtension {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    @JsonProperty("tenant_id")
    private String tenantId;
    private List<SecurityGroupRule> rules;
    private List<GenericLink> links;

    /**
     * Security Groups only need two fields populated when Created so instead of a builder the API can
     * leverage this call to create a new Security Group
     *
     * @param name        name of the security group
     * @param description description of the security group
     */
    public static NovaSecGroupExtension create(String name, String description) {
        NovaSecGroupExtension sg = new NovaSecGroupExtension();
        sg.name = name;
        sg.description = description;
        return sg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Rule> getRules() {
        return rules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Link> getLinks() {
        return links;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("description", description)
                .add("tenantId", tenantId).add("rules", rules).add("links", links)
                .toString();
    }

    @Deprecated
    public static class SecurityGroups extends ListResult<NovaSecGroupExtension> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("security_groups")
        private List<NovaSecGroupExtension> groups;

        @Override
        protected List<NovaSecGroupExtension> value() {
            return groups;
        }
    }

    /**
     * Security Group Rule
     */
    @JsonRootName("security_group_rule")
    @Deprecated
    public static class SecurityGroupRule implements SecGroupExtension.Rule {

        private static final long serialVersionUID = 1L;
        // Used for Create Only
        @JsonProperty("cidr")
        String cidr;
        @JsonProperty("group_id")
        String groupId;
        private String id;
        private String name;
        @JsonProperty("parent_group_id")
        private String parentGroupId;
        @JsonProperty("from_port")
        private Integer fromPort;
        @JsonProperty("to_port")
        private Integer toPort;
        @JsonProperty("ip_protocol")
        private IPProtocol ipProtocol;
        @JsonProperty("ip_range")
        private RuleIpRange ipRange;
        private RuleGroup group;

        public static SecurityGroupRuleBuilder builder() {
            return new RuleConcreteBuilder();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IPProtocol getIPProtocol() {
            return (ipProtocol != null) ? ipProtocol : IPProtocol.UNRECOGNIZED;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Group getGroup() {
            return group;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IpRange getRange() {
            return ipRange;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getToPort() {
            return (toPort != null) ? toPort : 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getFromPort() {
            return (fromPort != null) ? fromPort : 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getParentGroupId() {
            return parentGroupId;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getId() {
            return id;
        }

        public String toString() {
            return MoreObjects.toStringHelper("Rule").omitNullValues()
                    .add("id", id).add("name", name).add("parentGroupId", parentGroupId)
                    .add("fromPort", fromPort).add("toPort", toPort).add("ipProtocol", ipProtocol)
                    .add("range", ipRange).add("group", group)
                    .toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecurityGroupRuleBuilder toBuilder() {
            return new RuleConcreteBuilder(this);
        }

        @Deprecated
        public static class RuleConcreteBuilder implements SecurityGroupRuleBuilder {

            private SecurityGroupRule m;

            RuleConcreteBuilder() {
                this(new SecurityGroupRule());
            }

            RuleConcreteBuilder(SecurityGroupRule m) {
                this.m = m;
            }

            @Override
            public SecurityGroupRuleBuilder protocol(IPProtocol protocol) {
                m.ipProtocol = protocol;
                return this;
            }

            @Override
            public SecurityGroupRuleBuilder range(int fromPort, int toPort) {
                m.fromPort = fromPort;
                m.toPort = toPort;
                return this;
            }

            @Override
            public SecurityGroupRuleBuilder cidr(String cidr) {
                m.cidr = cidr;
                return this;
            }

            @Override
            public SecurityGroupRuleBuilder groupId(String groupId) {
                m.groupId = groupId;
                return this;
            }

            @Override
            public SecurityGroupRuleBuilder parentGroupId(String parentGroupId) {
                m.parentGroupId = parentGroupId;
                return this;
            }

            @Override
            public Rule build() {
                return m;
            }

            @Override
            public SecurityGroupRuleBuilder from(Rule in) {
                m = (SecurityGroupRule) in;
                return this;
            }

        }

        /**
         * Security Group Rule -> Group
         */
        @Deprecated
        public static class RuleGroup implements Group {

            private String name;

            @JsonProperty("tenant_id")
            private String tenantId;

            /**
             * {@inheritDoc}
             */
            @Override
            public String getName() {
                return name;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String getTenantId() {
                return tenantId;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this).omitNullValues().add("name", name).add("tenantId", tenantId).toString();
            }

        }

        /**
         * Security Group Rule -> IP Range
         */
        @Deprecated
        public static class RuleIpRange implements IpRange {

            private String cidr;

            /**
             * {@inheritDoc}
             */
            @Override
            public String getCidr() {
                return cidr;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this).add("cidr", cidr).toString();
            }

        }
    }

}
