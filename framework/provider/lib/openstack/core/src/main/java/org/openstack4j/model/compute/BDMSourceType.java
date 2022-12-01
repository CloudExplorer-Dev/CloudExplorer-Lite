package org.openstack4j.model.compute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Block Device Mapping Source Type
 *
 * @author Jeremy Unruh
 * @see http://docs.openstack.org/developer/nova/block_device_mapping.html
 */
public enum BDMSourceType {
    BLANK,
    IMAGE,
    SNAPSHOT,
    VOLUME;

    @JsonCreator
    public static BDMSourceType value(String v) {
        if (v == null)
            return VOLUME;
        try {
            return valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            return VOLUME;
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

}
