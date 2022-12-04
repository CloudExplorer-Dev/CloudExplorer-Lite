package org.openstack4j.model.trove;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by sumit gandhi on 9/3/2016.
 */
public enum DBCharacterSet {

    UTF8,
    UTF16,
    UTF32,
    UNRECOGNIZED;

    @JsonCreator
    public static DBCharacterSet value(String paramType) {
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
