package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * <p>The state of a Tacker (VIM) entity.</p>
 *
 * <p>Indicates whether tacker vim resource is currently operational.</p>
 *
 * @author Vishvesh Deshmukh
 * @date Aug 18, 2016
 */
public enum TackerVimStatus {
    REGISTERING,
    REACHABLE,
    UNREACHABLE,
    PENDING,
    ERROR,
    UNRECOGNIZED;

    @JsonCreator
    public static TackerVimStatus forValue(String value) {
        if (value != null) {
            for (TackerVimStatus s : TackerVimStatus.values()) {
                if (s.name().equalsIgnoreCase(value))
                    return s;
            }
        }
        return TackerVimStatus.UNRECOGNIZED;
    }
}
