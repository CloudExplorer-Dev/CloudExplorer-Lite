package org.openstack4j.model.dns.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of a designate v2 zone
 */
public enum ZoneType {

    PRIMARY, SECONDARY;

    //default to PRIMARY
    @JsonCreator
    public static ZoneType value(String v) {
        if (v == null) return PRIMARY;
        try {
            return valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PRIMARY;
        }
    }

    @JsonValue
    public String value() {
        return name().toUpperCase();
    }

}
