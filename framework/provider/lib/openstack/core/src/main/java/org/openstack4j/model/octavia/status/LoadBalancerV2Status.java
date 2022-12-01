package org.openstack4j.model.octavia.status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.octavia.domain.LoadBalancerV2StatusTree.OctaviaLoadBalancerV2Status;

import java.util.List;

/**
 * The status of an lbaas v2 loadbalancer
 *
 * @author wei
 */
@JsonDeserialize(as = OctaviaLoadBalancerV2Status.class)
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
