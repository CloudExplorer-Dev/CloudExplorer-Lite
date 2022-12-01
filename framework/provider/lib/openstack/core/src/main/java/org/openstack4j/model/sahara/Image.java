package org.openstack4j.model.sahara;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openstack4j.model.ModelEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A Sahara image
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface Image extends ModelEntity {

    /**
     * @return the status of this image
     */
    Status getStatus();

    /**
     * @return the username of this image
     */
    String getUsername();

    /**
     * @return the date the image was last updated
     */
    Date getUpdated();

    /**
     * @return the size in bytes
     */
    long getSize();

    /**
     * @return the descriptive name of the image
     */
    String getName();

    /**
     * @return the date the image was created
     */
    Date getCreated();

    /**
     * @return the tags associated with this image
     */
    List<String> getTags();

    /**
     * @return the minimum disk in bytes
     */
    int getMinDisk();

    /**
     * @return the progress of the image during upload or setup
     */
    int getProgress();

    /**
     * @return the minimum ram in bytes
     */
    int getMinRam();

    /**
     * @return extra metadata/specs associated with the image
     */
    Map<String, Object> getMetaData();

    /**
     * @return the identifier of this image
     */
    String getId();

    /**
     * @return the description of this image
     */
    String getDescription();

    enum Status {
        UNRECOGNIZED, ACTIVE, SAVING, QUEUED, KILLED, PENDING_DELETE, DELETED, ERROR; // Use Glance Image Status

        @JsonCreator
        public static Status forValue(String v) {
            if (v == null) return UNRECOGNIZED;
            try {
                return valueOf(v.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }
    }

}
