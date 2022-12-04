package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.PolicyTargetGroupBuilder;

import java.util.List;
import java.util.Map;

/**
 * Policy Target group Model Entity
 *
 * @author vinod borole
 */
public interface PolicyTargetGroupCreate extends Buildable<PolicyTargetGroupBuilder>, Resource {

    /**
     * Gets the subnets
     *
     * @return the subnets
     */
    List<String> getSubnets();

    /**
     * Is Policy Target Group shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Is service management
     *
     * @return True or False
     */
    boolean isServiceManagement();

    /**
     * Gets the list of policy targets
     *
     * @return the list of policy targets
     */
    List<String> getPolicyTargets();

    /**
     * Gets the network service policy id
     *
     * @return the network service policy id
     */
    String getNetworkServicePolicyId();

    /**
     * Gets the L2 Policy id
     *
     * @return the L2 Policy id
     */
    String getL2PolicyId();

    /**
     * Gets the provided policy rule sets Ids
     *
     * @return the provided policy rule sets ids
     */
    Map<String, String> getProvidedPolicyRuleSets();

    /**
     * Gets the consumed policy rule sets Ids
     *
     * @return the consumed policy rule sets ids
     */
    Map<String, String> getConsumedPolicyRuleSets();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

}
