package org.openstack4j.model.octavia.status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.octavia.domain.LoadBalancerV2StatusTree.OctaviaLbPoolV2Status;

import java.util.List;

/**
 * The status of an lbaas v2 loadbalancer pool
 *
 * @author wei
 */
@JsonDeserialize(as = OctaviaLbPoolV2Status.class)
public interface LbPoolV2Status extends ModelEntity {
    /**
     * The id of the loadbalancer pool
     *
     * @return id
     */
    public String getId();

    /**
     * The name of the loadbalancer pool
     *
     * @return name
     */
    public String getName();

    /**
     * The operating status of the loadbalancer pool
     *
     * @return operating status
     */
    public String getOperatingStatus();

    /**
     * The provisioning status of the loadbalancer pool
     *
     * @return provisioning status
     */
    public String getProvisioningStatus();

    /**
     * The status of the healthmonitor associated with this pool
     *
     * @return HealthMonitorV2Status
     */
    public HealthMonitorV2Status getHeathMonitorStatus();

    /**
     * The status of the members of the loadbalancer pool
     *
     * @return list of MemberV2Status
     */
    public List<MemberV2Status> getMemberStatuses();
}
