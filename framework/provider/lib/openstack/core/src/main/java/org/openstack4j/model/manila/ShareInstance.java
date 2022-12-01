package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

import java.util.List;

/**
 * Representation of a share instance.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareInstance extends ModelEntity {
    /**
     * @return the share instance status
     */
    Status getStatus();

    /**
     * @return the UUID of the share from which the share instance was created
     */
    String getShareId();

    /**
     * @return the availability zone
     */
    String getAvailabilityZone();

    /**
     * @return the date and time stamp when the share instance was created
     */
    String getCreatedAt();

    /**
     * @return the share instance export location
     */
    String getExportLocation();

    /**
     * @return a list of share instance export locations
     */
    List<String> getExportLocations();

    /**
     * @return the share network ID
     */
    String getShareNetworkId();

    /**
     * @return the share server ID
     */
    String getShareServerId();

    /**
     * @return the share instance host name
     */
    String getHost();

    /**
     * @return the share instance ID
     */
    String getId();

    enum Status {
        AVAILABLE, ERROR, CREATING, DELETING, ERROR_DELETING;

        @JsonCreator
        public static Status value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }
}
