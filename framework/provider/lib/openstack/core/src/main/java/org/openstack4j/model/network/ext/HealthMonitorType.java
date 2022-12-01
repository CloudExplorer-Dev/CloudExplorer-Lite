package org.openstack4j.model.network.ext;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * A Health Monitor Type
 *
 * @author liujunpeng
 */
public enum HealthMonitorType {
    PING, TCP, HTTP, HTTPS;

    @JsonCreator
    public static HealthMonitorType forValue(String value) {
        if (value != null) {
            for (HealthMonitorType s : HealthMonitorType.values()) {
                if (s.name().equalsIgnoreCase(value)) {
                    return s;
                }
            }
        }
        return HealthMonitorType.HTTP;
    }
}
