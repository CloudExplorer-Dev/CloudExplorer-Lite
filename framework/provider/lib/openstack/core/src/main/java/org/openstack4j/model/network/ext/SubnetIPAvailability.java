package org.openstack4j.model.network.ext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.ext.builder.SubnetIPAvailabilityBuilder;
import org.openstack4j.openstack.networking.domain.ext.NeutronSubnetIPAvailability;

import java.math.BigInteger;

/**
 * A subnet IP availability details associated to a network
 *
 * @author Xiangbin HAN
 */
@JsonDeserialize(as = NeutronSubnetIPAvailability.class)
public interface SubnetIPAvailability extends ModelEntity, Buildable<SubnetIPAvailabilityBuilder> {

    /**
     * Number of used IPs per subnet
     *
     * @return the number of IPs
     */
    BigInteger getUsedIps();

    /**
     * Number of total IPs per subnet
     *
     * @return the number of IPs
     */
    BigInteger getTotalIps();

    /**
     * @return the subnet identifier
     */
    String getSubnetId();

    /**
     * @return the subnet name
     */
    String getSubnetName();

    /**
     * @return the IP version type of subnet
     */
    IPVersionType getIpVersion();

    /**
     * @return the subnet CIDR
     */
    String getCidr();
}
