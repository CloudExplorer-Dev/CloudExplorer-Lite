package org.openstack4j.model.gbp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by sumit gandhi on 7/8/2016.
 */
public enum NetworkServiceParamValue {

    SELF_SUBNET,
    NAT_POOL,
    UNRECOGNIZED;

    @JsonCreator
    public static NetworkServiceParamValue value(String paramValue) {
        try {
            return valueOf(paramValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNRECOGNIZED;
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

}
