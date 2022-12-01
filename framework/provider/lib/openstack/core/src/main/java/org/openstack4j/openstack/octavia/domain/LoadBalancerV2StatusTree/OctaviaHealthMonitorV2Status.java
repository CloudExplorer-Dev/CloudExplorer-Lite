package org.openstack4j.openstack.octavia.domain.LoadBalancerV2StatusTree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.octavia.HealthMonitorType;
import org.openstack4j.model.octavia.status.HealthMonitorV2Status;

/**
 * An object to hold status of lbaas v2 healthmonitor
 *
 * @author wei
 */
@JsonRootName("healthmonitor")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OctaviaHealthMonitorV2Status extends Status implements HealthMonitorV2Status {

    @JsonProperty("type")
    private HealthMonitorType type;

    public HealthMonitorType getType() {
        return type;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("provisioningStatus", provisioningStatus)
                .toString();
    }
}
