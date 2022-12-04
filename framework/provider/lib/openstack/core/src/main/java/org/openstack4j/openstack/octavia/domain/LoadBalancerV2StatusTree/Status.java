package org.openstack4j.openstack.octavia.domain.LoadBalancerV2StatusTree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract class for common status fields
 *
 * @author wei
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Status {
    public String id;

    @JsonProperty("operating_status")
    public String operatingStatus;

    @JsonProperty("provisioning_status")
    public String provisioningStatus;

    public String getId() {
        return id;
    }

    public String getOperatingStatus() {
        return operatingStatus;
    }

    public String getProvisioningStatus() {
        return provisioningStatus;
    }
}
