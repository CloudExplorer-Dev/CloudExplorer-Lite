package org.openstack4j.model.octavia;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Operating status of a load balancer v2
 */
public enum LbOperatingStatus {
    ONLINE,
    DRAINING,
    DEGRADED,
    ERROR,
    NO_MONITOR,
    OFFLINE;

    @JsonCreator
    public static LbOperatingStatus forValue(String value) {
        if (value != null) {
            for (LbOperatingStatus s : LbOperatingStatus.values()) {
                if (s.name().equalsIgnoreCase(value)) {
                    return s;
                }
            }
        }
        return LbOperatingStatus.OFFLINE;
    }
}
