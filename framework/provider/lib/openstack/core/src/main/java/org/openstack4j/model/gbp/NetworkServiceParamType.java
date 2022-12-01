package org.openstack4j.model.gbp;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by sumit gandhi on 7/8/2016.
 */

public enum NetworkServiceParamType {

    IP_SINGLE,
    IP_POOL,
    UNRECOGNIZED;

    @JsonCreator
    public static NetworkServiceParamType value(String paramType) {
        try {
            return valueOf(paramType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNRECOGNIZED;
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

}
