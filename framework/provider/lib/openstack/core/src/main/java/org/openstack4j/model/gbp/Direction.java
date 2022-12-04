package org.openstack4j.model.gbp;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Direction enum
 *
 * @author vinod borole
 */
public enum Direction {
    IN,
    OUT,
    BI,
    UNRECOGNIZED;

    @JsonCreator
    public static Direction forValue(String value) {
        if (value != null) {
            for (Direction s : Direction.values()) {
                if (s.name().equalsIgnoreCase(value))
                    return s;
            }
        }
        return Direction.UNRECOGNIZED;
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }
}
