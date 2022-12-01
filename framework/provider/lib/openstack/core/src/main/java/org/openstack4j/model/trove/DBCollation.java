package org.openstack4j.model.trove;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by sumit gandhi on 9/3/2016.
 */
public enum DBCollation {

    UTF8_GENERAL_CI,
    UTF16_GENERAL_CI,
    UTF32_GENERAL_CI,
    UNRECOGNIZED;

    @JsonCreator
    public static DBCollation value(String paramType) {
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
