package org.openstack4j.model.compute;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents a Port State
 *
 * @author Jeremy Unruh
 */
public enum PortState {
    ACTIVE,
    DOWN,
    BUILD,
    ERROR,
    UNRECOGNIZED;

    @JsonCreator
    public static PortState forValue(String value) {
        if (value != null) {
            for (PortState s : PortState.values()) {
                if (s.name().equalsIgnoreCase(value))
                    return s;
            }
        }
        return PortState.UNRECOGNIZED;
    }

}
