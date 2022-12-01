package org.openstack4j.model.dns.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status of a designate v2 recordset
 */
public enum Status {

    ERROR, PENDING, ACTIVE;

    //default to PRIMARY
    @JsonCreator
    public static Status value(String v) {
        if (v == null) return ERROR;
        try {
            return valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ERROR;
        }
    }

    @JsonValue
    public String value() {
        return name().toUpperCase();
    }

}
