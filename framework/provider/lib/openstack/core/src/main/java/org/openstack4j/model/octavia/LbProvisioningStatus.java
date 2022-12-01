package org.openstack4j.model.octavia;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Provisioning status of load balancer v2 object
 */
public enum LbProvisioningStatus {
    ACTIVE,
    DELETED,
    PENDING_CREATE,
    PENDING_UPDATE,
    PENDING_DELETE,
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
