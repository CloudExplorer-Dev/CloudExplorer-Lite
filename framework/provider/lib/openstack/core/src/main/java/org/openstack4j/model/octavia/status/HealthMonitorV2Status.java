package org.openstack4j.model.octavia.status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.octavia.HealthMonitorType;
import org.openstack4j.openstack.octavia.domain.LoadBalancerV2StatusTree.OctaviaHealthMonitorV2Status;

/**
 * The status of an lbaas v2 heathmonitor
 *
 * @author wei
 */
@JsonDeserialize(as = OctaviaHealthMonitorV2Status.class)
public interface HealthMonitorV2Status extends ModelEntity {
    /**
     * The id of the healthmonitor
     *
     * @return id
     */
    public String getId();

    /**
     * The health monitor type
     *
     * @return HealthMonitorType
     */
    public HealthMonitorType getType();

    /**
     * Provisioning status of the health monitor
     *
     * @return provisioningStatus
     */
    public String getProvisioningStatus();
}
