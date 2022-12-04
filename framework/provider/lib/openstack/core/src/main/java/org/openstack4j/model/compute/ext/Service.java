package org.openstack4j.model.compute.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * A Service represents a Nova compute service
 *
 * @author Stephan Latour
 */
public interface Service extends ModelEntity {
    /**
     * @return the binary for this service
     */
    String getBinary();

    /**
     * @return the reason for disabled status of this service
     */
    String getDisabledReason();

    /**
     * @return the host for this service
     */
    String getHost();

    /**
     * @return the id for this service
     */
    String getId();

    /**
     * @return the status of the service
     */
    State getState();

    /**
     * @return the status of the service
     */
    Status getStatus();

    /**
     * @return last updated time
     */
    Date getUpdatedAt();

    /**
     * @return the zone for this service
     */
    String getZone();

    /**
     * @return Whether or not this service was forced down manually by an administrator
     */
    String getForcedDown();

    /**
     * The status of a Nova service entity
     */
    public enum Status {
        DISABLED, ENABLED, UNRECOGNIZED;

        @JsonCreator
        public static Status forValue(String value) {
            if (value != null) {
                for (Status s : Status.values()) {
                    if (s.name().equalsIgnoreCase(value)) {
                        return s;
                    }
                }
            }
            return Status.UNRECOGNIZED;
        }
    }

    /**
     * The state of a Nova service entity
     */
    public enum State {
        DOWN, UNRECOGNIZED, UP;

        @JsonCreator
        public static State forValue(String value) {
            if (value != null) {
                for (State s : State.values()) {
                    if (s.name().equalsIgnoreCase(value)) {
                        return s;
                    }
                }
            }
            return State.UNRECOGNIZED;
        }
    }
}
