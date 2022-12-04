package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

/**
 * Represents the access to a a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface Access extends ModelEntity {
    /**
     * @return the access rule ID
     */
    String getId();

    /**
     * @return the UUID of the share to which you are granted or denied access
     */
    String getShareId();

    /**
     * @return the access rule state
     */
    State getState();

    /**
     * @return the date and time stamp when the access rule was created
     */
    String getCreatedAt();

    /**
     * @return the date and time stamp when the access rule was updated
     */
    String getUpdatedAt();

    /**
     * @return the rule access type
     */
    Type getAccessType();

    /**
     * @return the access that the back end grants or denies
     */
    String getAccessTo();

    /**
     * @return the share access level
     */
    Level getAccessLevel();

    enum Level {
        RW, RO;

        @JsonCreator
        public static Level value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    public enum Type {
        IP, CERT, USER;

        @JsonCreator
        public static Type value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    enum State {
        NEW, ACTIVE, ERROR;

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
