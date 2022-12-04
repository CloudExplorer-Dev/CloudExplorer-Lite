package org.openstack4j.model.compute;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.builder.SecurityGroupRuleBuilder;

import java.util.List;

/**
 * A Security Group which is defined the the (os-security-groups) extension
 *
 * @author Jeremy Unruh
 */
@Deprecated
public interface SecGroupExtension extends ModelEntity {

    /**
     * Gets the identifier for the Security Group
     *
     * @return the identifier
     */
    String getId();

    /**
     * Gets the name of the Security Group
     *
     * @return the name of the Group
     */
    String getName();

    /**
     * Gets the description.
     *
     * @return the description of the Security Group
     */
    String getDescription();

    /**
     * Gets the tenant id associated with the group
     *
     * @return the tenant identifier
     */
    String getTenantId();

    /**
     * Gets the rules that make up this security group
     *
     * @return the list of rules
     */
    List<? extends Rule> getRules();

    /**
     * Gets the reference / external links
     *
     * @return external/reference list of links
     */
    List<? extends Link> getLinks();

    /**
     * Security Group Rule
     */
    @Deprecated
    public interface Rule extends ModelEntity, Buildable<SecurityGroupRuleBuilder> {

        /**
         * @return the IPProtocol for this rule
         */
        IPProtocol getIPProtocol();

        /**
         * @return the referenced security group which contains the name and tenant identifier
         */
        Group getGroup();

        /**
         * @return the IP Range
         */
        IpRange getRange();

        /**
         * @return the port at the start of the range
         */
        int getToPort();

        /**
         * @return the port at the end of the range
         */
        int getFromPort();

        /**
         * Gets the parent group id.
         *
         * @return the parent group id
         */
        String getParentGroupId();

        /**
         * @return the name of the rule
         */
        String getName();

        /**
         * @return the identifier for the rule
         */
        String getId();

        /**
         * Rule Group
         */
        @Deprecated
        public interface Group {

            /**
             * @return the name of the group
             */
            String getName();

            /**
             * Gets the tenant id.
             *
             * @return the tenant id
             */
            String getTenantId();
        }

        /**
         * Rule IP Range
         */
        @Deprecated
        public interface IpRange {

            /**
             * Gets the CIDR address range
             *
             * @return the CIDR address range
             */
            String getCidr();
        }
    }

}
