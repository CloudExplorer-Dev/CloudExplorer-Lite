package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.NetworkIPAvailabilityBuilder;

import java.math.BigInteger;
import java.util.List;

/**
 * A network IP availability details for a network
 *
 * @author Xiangbin HAN
 */
public interface NetworkIPAvailability extends ModelEntity, Buildable<NetworkIPAvailabilityBuilder> {

    /**
     * @return the network name
     */
    String getNetworkName();

    /**
     * @return the network identifier
     */
    String getNetworkId();

    /**
     * @return the tenant identifier
     */
    String getTenantId();

    /**
     * @return the project identifier
     */
    String getProjectId();

    /**
     * Number of total IPs per network
     *
     * @return the number of IPs
     */
    BigInteger getTotalIps();

    /**
     * Number of used IPs per network
     *
     * @return the number of IPs
     */
    BigInteger getUsedIps();

    /**
     * List of subnet IP availability per network
     *
     * @return the list of SubnetIPAvailability
     */
    public List<? extends SubnetIPAvailability> getSubnetIPAvailabilities();
}
