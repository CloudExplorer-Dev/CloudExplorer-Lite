package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * <p>The state of a Tacker (VNF/NFV) entity.</p>
 *
 * <p>Indicates whether tacker nfv resource is currently operational.</p>
 *
 * @author Vishvesh Deshmukh
 * @date Aug 16, 2016
 */
public enum TackerVnfStatus {
    ACTIVE,
    INACTIVE,
    DEAD,
    DOWN,
    BUILD,
    ERROR,
    PENDING_CREATE,
    PENDING_UPDATE,
    PENDING_DELETE,
    PENDING_SCALE_IN,
    PENDING_SCALE_OUT,
    UNRECOGNIZED;

    @JsonCreator
    public static TackerVnfStatus forValue(String value) {
        if (value != null) {
            for (TackerVnfStatus s : TackerVnfStatus.values()) {
                if (s.name().equalsIgnoreCase(value))
                    return s;
            }
        }
        return TackerVnfStatus.UNRECOGNIZED;
    }
}
