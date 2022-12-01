package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.NetworkServicePolicyBuilder;
import org.openstack4j.openstack.gbp.domain.GbpNetworkServiceParams;

import java.util.List;

/**
 * Created by sumit gandhi on 7/4/2016.
 */
public interface NetworkServicePolicy extends Resource, Buildable<NetworkServicePolicyBuilder> {

    /**
     * Is the network service policy shared
     *
     * @return boolean
     */
    boolean isShared();

    /**
     * Gets the network service parameters
     *
     * @return network service parameters
     */
    List<GbpNetworkServiceParams> getGbpNetworkServiceParamsList();

    /**
     * Gets the policy target groups associated with the service policy
     *
     * @return list of policy target groups
     */
    List<PolicyTargetGroup> getPolicyTargetGroupList();

    /**
     * Gets the description of the network service policy
     *
     * @return description
     */
    String getDescription();

}
