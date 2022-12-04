package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.L2PolicyBuilder;

import java.util.List;

/**
 * L2 Policy Model Entity
 *
 * @author vinod borole
 */
public interface L2Policy extends Resource, Buildable<L2PolicyBuilder> {

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the network Id
     *
     * @return the network Id
     */
    String getNetworkId();

    /**
     * Gets the L3 Policy Id
     *
     * @return the L3 Policy Id
     */
    String getL3PolicyId();

    /**
     * Is L2 Policy shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Gets the list of policy Target groups
     *
     * @return the policy target group list
     */
    List<String> getPolicyTargetGroups();

}
