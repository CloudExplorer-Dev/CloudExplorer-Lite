package org.openstack4j.model.network.ext;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Operating status of a load balancer v2
 */
public enum LbOperatingStatus {
    ONLINE,
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
