package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.PolicyTargetBuilder;

/**
 * Policy Target Model Entity
 *
 * @author vinod borole
 */
public interface PolicyTarget extends Resource, Buildable<PolicyTargetBuilder> {

    /**
     * Gets the Port Id
     *
     * @return the Port Id
     */
    String getPortId();

    /**
     * Gets the Policy Target Group Id
     *
     * @return the Policy Target Group Id
     */
    String getPolicyTargetGroupId();

    /**
     * Gets the cluster Id
     *
     * @return the cluster Id
     */
    String getClusterId();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

}
