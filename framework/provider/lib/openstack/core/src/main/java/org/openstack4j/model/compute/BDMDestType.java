package org.openstack4j.model.compute;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Block Device Mapping Destination Type
 *
 * @author Jeremy Unruh
 * @see http://docs.openstack.org/developer/nova/block_device_mapping.html
 */
public enum BDMDestType {

    /**
     * Will either mean an ephemeral blank disk on hypervisor local storage, or a swap disk
     **/
    LOCAL,
    /**
     * Creates a blank Cinder volume and attaches it. This will also require the volume size to be set
     **/
    VOLUME;

    @JsonCreator
    public static BDMDestType value(String v) {
        if (v == null)
            return LOCAL;
        try {
            return valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LOCAL;
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

}
