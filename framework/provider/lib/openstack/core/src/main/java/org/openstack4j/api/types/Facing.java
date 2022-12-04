package org.openstack4j.api.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the URL perspective in which the API is accessing data from within an OpenStack deployment.  For example: admin, public, internal
 *
 * @author Jeremy Unruh
 */
public enum Facing {

    INTERNAL,
    ADMIN,
    PUBLIC;

    @JsonCreator
    public static Facing value(String facing) {
        if (facing == null || facing.isEmpty()) return PUBLIC;
        try {
            return valueOf(facing.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PUBLIC;
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

}
