package org.openstack4j.model.gbp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * IP Version enum
 *
 * @author vinod borole
 */
public enum IPVersionType {
    V4(4),
    V6(6);
    private final int version;

    private IPVersionType(int version) {
        this.version = version;
    }

    @JsonCreator
    public static IPVersionType valueOf(int value) {
        for (IPVersionType v : IPVersionType.values()) {
            if (v.version == value) {
                return v;
            }
        }
        return V4;
    }

    /**
     * Gets the version in Integer form
     *
     * @return the version as int
     */
    @JsonValue
    public int getVersion() {
        return version;
    }
}
