package org.openstack4j.model.network.ext;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Provisioning status of load balancer v2 object
 */
public enum LbProvisioningStatus {
    ACTIVE,
    DOWN,
    CREATED,
    PENDING_CREATE,
    PENDING_UPDATE,
    PENDING_DELETE,
    INACTIVE,
    ERROR;

    @JsonCreator
    public static LbProvisioningStatus forValue(String value) {
        if (value != null) {
            for (LbProvisioningStatus s : LbProvisioningStatus.values()) {
                if (s.name().equalsIgnoreCase(value)) {
                    return s;
                }
            }
        }
        return LbProvisioningStatus.ERROR;
    }
}
