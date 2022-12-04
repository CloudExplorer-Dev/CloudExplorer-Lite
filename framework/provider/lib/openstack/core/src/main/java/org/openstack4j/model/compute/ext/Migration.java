package org.openstack4j.model.compute.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * A model class which enables an administrative user to fetch in-progress migrations for a region or specified cell in a region
 *
 * @author Jeremy Unruh
 */
public interface Migration extends ModelEntity {

    /**
     * @return the migration ID
     */
    String getId();

    /**
     * @return the ID of the instance being migrated
     */
    String getInstanceUuid();

    /**
     * @return the migration status
     */
    Status getStatus();

    /**
     * @return the created timestamp
     */
    Date getCreatedAt();

    /**
     * @return the last updated timestamp
     */
    Date getUpdatedAt();

    /**
     * @return the destination compute node
     */
    String getDestCompute();

    /**
     * @return the destination host
     */
    String getDestHost();

    /**
     * @return the destination node
     */
    String getDestNode();

    /**
     * @return the source compute node
     */
    String getSourceCompute();

    /**
     * @return the source node
     */
    String getSourceNode();

    /**
     * @return the ID of the new instance type
     */
    String getNewInstanceTypeId();

    /**
     * @return the ID of the old instance type
     */
    String getOldInstanceTypeId();

    public enum Status {
        MIGRATING,
        ERROR,
        DONE,
        FINISHED,
        CONFIRMED;

        @JsonCreator
        public static Status forValue(String value) {
            if (value != null) {
                for (Status s : Status.values()) {
                    if (s.name().equalsIgnoreCase(value))
                        return s;
                }
            }
            return Status.MIGRATING;
        }
    }
}

