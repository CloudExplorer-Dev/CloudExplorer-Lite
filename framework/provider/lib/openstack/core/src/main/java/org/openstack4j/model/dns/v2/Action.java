package org.openstack4j.model.dns.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of a designate v2 recordset action
 */
public enum Action {

    NONE, CREATE, DELETE, UPDATE;

    //default to PRIMARY
    @JsonCreator
    public static Action value(String v) {
        if (v == null) return NONE;
        try {
            return valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }

    @JsonValue
    public String value() {
        return name().toUpperCase();
    }

}
