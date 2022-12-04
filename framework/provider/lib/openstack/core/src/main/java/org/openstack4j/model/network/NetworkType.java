package org.openstack4j.model.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * The type of Network
 *
 * @author Jeremy Unruh
 */
public enum NetworkType {
    LOCAL,
    FLAT,
    VLAN,
    VXLAN,
    GRE;

    @JsonCreator
    public static NetworkType forValue(String value) {
        if (value != null) {
            for (NetworkType s : NetworkType.values()) {
                if (s.name().equalsIgnoreCase(value))
                    return s;
            }
        }
        return null;
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }

}
