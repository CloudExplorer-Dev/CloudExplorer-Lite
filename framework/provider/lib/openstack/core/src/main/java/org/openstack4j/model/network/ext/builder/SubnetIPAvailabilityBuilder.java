package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.ext.SubnetIPAvailability;

import java.math.BigInteger;

/**
 * A builder which creates subnet IP availabitiy that is bound to a network
 *
 * @author Xiangbin HAN
 */
public interface SubnetIPAvailabilityBuilder extends Builder<SubnetIPAvailabilityBuilder, SubnetIPAvailability> {

    public SubnetIPAvailabilityBuilder subnetName(String subnetName);

    public SubnetIPAvailabilityBuilder subnetId(String subnetId);

    public SubnetIPAvailabilityBuilder tenantId(IPVersionType ipVersion);

    public SubnetIPAvailabilityBuilder cidr(String cidr);

    public SubnetIPAvailabilityBuilder totalIps(BigInteger totalIps);

    public SubnetIPAvailabilityBuilder usedIps(BigInteger usedIps);

}
