package org.openstack4j.model.octavia;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Protocol options for lbaas v2 listener
 *
 * @author wei
 */
public enum ListenerProtocol {
    HTTP,
    HTTPS,
    TERMINATED_HTTPS,
    TCP;

    @JsonCreator
    public static ListenerProtocol forValue(String value) {
        if (value != null) {
            for (ListenerProtocol s : ListenerProtocol.values()) {
                if (s.name().equalsIgnoreCase(value)) {
                    return s;
                }
            }
        }
        return ListenerProtocol.HTTP;
    }
}
