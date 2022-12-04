package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.builder.NetQuotaBuilder;

/**
 * Network quotas that are bound to a Tenant
 *
 * @author Jeremy Unruh
 */
public interface NetQuota extends ModelEntity, Buildable<NetQuotaBuilder> {

    /**
     * Number of subnets allowed per tenant
     *
     * @return number of subnets
     */
    int getSubnet();

    /**
     * Number of routers allowed per tenant
     *
     * @return number of routers
     */
    int getRouter();

    /**
     * Number of ports allowed per tenant
     *
     * @return number of ports
     */
    int getPort();

    /**
     * Number of networks allowed per tenant
     *
     * @return number of networks
     */
    int getNetwork();

    /**
     * Number of floating IpAddresses allowed per tenant
     *
     * @return number of float IpAddresses
     */
    int getFloatingIP();

    /**
     * Number of security groups per tenant
     *
     * @return number of security groups
     */
    int getSecurityGroup();

    /**
     * Number of security groups rules per security group per tenant
     *
     * @return number of security groups rules
     */
    int getSecurityGroupRule();

    /**
     * The number of subnet pools allowed for each project. A value of -1 means no limit
     *
     * @return number of subnet pools
     */
    int getSubnetpool();

    /**
     * The number of role-based access control (RBAC) policies for each project. A value of -1 means no limit
     *
     * @return number of RBAC policies
     */
    int getRbacPolicy();
}
