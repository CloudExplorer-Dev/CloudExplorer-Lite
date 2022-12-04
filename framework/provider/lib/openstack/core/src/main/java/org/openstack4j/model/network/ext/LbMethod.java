package org.openstack4j.model.network.ext;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Load Balancer Algorithm
 *
 * @author liujunpeng
 */
public enum LbMethod {
    ROUND_ROBIN,
    LEAST_CONNECTIONS,
    SOURCE_IP;

    @JsonCreator
    public static LbMethod forValue(String value) {
        if (value != null) {
            for (LbMethod s : LbMethod.values()) {
                if (s.name().equalsIgnoreCase(value)) {
                    return s;
                }
            }
        }
        return LbMethod.ROUND_ROBIN;
    }
}
