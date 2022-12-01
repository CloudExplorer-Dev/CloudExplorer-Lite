package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.L3PolicyBuilder;

import java.util.List;
import java.util.Map;

/**
 * L3 Policy Model Entity
 *
 * @author vinod borole
 */
public interface L3Policy extends Resource, Buildable<L3PolicyBuilder> {

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the external segment
     *
     * @return the external segment
     */
    Map<String, List<String>> getExternalSegments();

    /**
     * Gets the Ip Pool
     *
     * @return the Ip Pool
     */
    String getIpPool();

    /**
     * Gets the Ip version
     *
     * @return the Ip version
     */
    int getIpVersion();

    /**
     * Gets the list of L2 Policies
     *
     * @return the list of L2 Policies
     */
    List<String> getL2Policies();

    /**
     * Gets the list of routers
     *
     * @return the list of routers
     */
    List<String> getRouters();

    /**
     * Is L3 Policy shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Gets the subnet prefix length
     *
     * @return the subnet prefix length
     */
    String getSubnetPrefixLength();

}
