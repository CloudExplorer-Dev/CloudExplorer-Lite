package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.NetworkIPAvailability;
import org.openstack4j.model.network.ext.SubnetIPAvailability;

import java.math.BigInteger;

/**
 * A Builder which creates a NetworkIPAvailability entity
 *
 * @author Xiangbin HAN
 */
public interface NetworkIPAvailabilityBuilder extends Builder<NetworkIPAvailabilityBuilder, NetworkIPAvailability> {

    public NetworkIPAvailabilityBuilder networkName(String networkName);

    public NetworkIPAvailabilityBuilder networkId(String networkId);

    public NetworkIPAvailabilityBuilder tenantId(String tenantId);

    public NetworkIPAvailabilityBuilder projectId(String projectId);

    public NetworkIPAvailabilityBuilder totalIps(BigInteger totalIps);

    public NetworkIPAvailabilityBuilder usedIps(BigInteger usedIps);

    public NetworkIPAvailabilityBuilder subnetIPAvailability(SubnetIPAvailability subnetIPAvailability);

}
