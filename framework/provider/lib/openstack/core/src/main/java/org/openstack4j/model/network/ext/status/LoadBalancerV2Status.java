package org.openstack4j.model.network.ext.status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.networking.domain.ext.LoadBalancerV2StatusTree.NeutronLoadBalancerV2Status;

import java.util.List;

/**
 * The status of an lbaas v2 loadbalancer
 *
 * @author emjburns
 */
@JsonDeserialize(as = NeutronLoadBalancerV2Status.class)
public interface LoadBalancerV2Status extends ModelEntity {
    /**
     * The id of the loadbalancer
     *
     * @return id
     */
    public String getId();

    /**
     * The name of the loadbalancer
     *
     * @return name
     */
    public String getName();

    /**
     * The operating status of the loadbalancer
     *
     * @return operating status
     */
    public String getOperatingStatus();

    /**
     * The provisioning status of the loadbalancer
     *
     * @return provisioning status
     */
    public String getProvisioningStatus();

    /**
     * The listeners associated with the loadbalancer
     *
     * @return list of listener statuses
     */
    public List<ListenerV2Status> getListenerStatuses();
}
