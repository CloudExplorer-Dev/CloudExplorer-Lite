package org.openstack4j.model.telemetry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

/**
 * A Meter is a category of Measurement
 *
 * @author Jeremy Unruh
 */
public interface Meter extends ModelEntity {

    /**
     * @return the unique identifier for the meter
     */
    String getId();

    /**
     * @return the unique name of the meter
     */
    String getName();

    /**
     * @return the ID of the Resource for which the measurements are taken
     */
    String getResourceId();

    /**
     * @return the ID of the project/tenant that owns the resource
     */
    String getProjectId();

    /**
     * @return the meter type
     */
    Type getType();

    /**
     * @return the unit of measure
     */
    String getUnit();

    /**
     * @return The user id who last modified the resource
     */
    String getUserId();

    /**
     * The Meter Type
     */
    public enum Type {
        GAUGE, DELTA, CUMULATIVE, UNRECOGNIZED;

        @JsonCreator
        public static Type fromValue(String type) {
            try {
                return valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return value();
        }
    }

}
