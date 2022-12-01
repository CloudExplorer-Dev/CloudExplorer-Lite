package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

/**
 * Represents a Manila service and their binary.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface Service extends ModelEntity {
    /**
     * @return the service ID
     */
    Integer getId();

    /**
     * @return the service status, which is <code>enabled</code> or <code>disabled</code>
     */
    Status getStatus();

    /**
     * @return the service binary name
     */
    String getBinary();

    /**
     * @return the availability zone
     */
    String getZone();

    /**
     * @return the host name
     */
    String getHost();

    /**
     * @return the current state of the service
     */
    State getState();

    /**
     * @return the date and time stamp when the service was updated
     */
    String getUpdatedAt();

    enum Status {
        ENABLED,
        DISABLED;

        @JsonCreator
        public static Status value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    enum State {
        UP,
        DOWN;

        @JsonCreator
        public static State value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }
}
