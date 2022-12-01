package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.NetQuota;

/**
 * A Builder which creates a NetQuota entity
 *
 * @author Jeremy Unruh
 */
public interface NetQuotaBuilder extends Builder<NetQuotaBuilder, NetQuota> {

    /**
     * See {@link NetQuota#getSubnet()} for details
     *
     * @param subnet the max subnets allowed
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder subnet(int subnet);

    /**
     * See {@link NetQuota#getRouter()} for details
     *
     * @param router the max routers allowed
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder router(int router);

    /**
     * See {@link NetQuota#getPort()} for details
     *
     * @param port the max ports allowed
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder port(int port);

    /**
     * See {@link NetQuota#getNetwork()} for details
     *
     * @param network the max networks allowed
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder network(int network);

    /**
     * See {@link NetQuota#getFloatingIP()} for details
     *
     * @param floatingIP the max floating IPAddresses allowed
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder floatingIP(int floatingIP);

    /**
     * See {@link NetQuota#getSecurityGroup()} for details
     *
     * @param securityGroup the max security group allowed
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder securityGroup(int securityGroup);

    /**
     * See {@link NetQuota#getSecurityGroupRule()} for details
     *
     * @param securityGroupRule the max security group rules allowed
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder securityGroupRule(int securityGroupRule);


    /**
     * See {@link NetQuota#getRbacPolicy()} for details
     *
     * @param rbacPolicy
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder rbacPolicy(int rbacPolicy);

    /**
     * See {@link NetQuota#getSubnetPool()} for details
     *
     * @param subnetPool
     * @return NetQuotaBuilder
     */
    NetQuotaBuilder subnetpool(int subnetpool);
}
