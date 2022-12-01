package org.openstack4j.model.compute;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.Link;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An OpenStack image is a collection of files used to create a Server.  Users provide pre-built OS images by default and or custom
 * images can be built
 *
 * @author Jeremy Unruh
 */
@Deprecated
public interface Image extends ModelEntity {

    /**
     * @return the identifier of this image
     */
    String getId();

    /**
     * @return the descriptive name of the image
     */
    String getName();

    /**
     * @return the size in bytes
     */
    long getSize();

    /**
     * @return the minimum disk in bytes
     */
    int getMinDisk();

    /**
     * @return the minimum ram in bytes
     */
    int getMinRam();

    /**
     * @return the progress of the image during upload or setup
     */
    int getProgress();

    /**
     * @return the status of this image
     */
    Status getStatus();

    /**
     * @return the date the image was created
     */
    Date getCreated();

    /**
     * @return the date the image was last updated
     */
    Date getUpdated();

    /**
     * @return external reference links for the image
     */
    List<? extends Link> getLinks();

    /**
     * @return extra metadata/specs associated with the image
     */
    Map<String, Object> getMetaData();

    /**
     * Determines if this image is a snapshot
     *
     * @return true if this image is a snapshot
     */
    boolean isSnapshot();

    /**
     * Status can be used while an image is being saved.  It provides state of the progress indicator.  Images with ACTIVE status
     * are available for install.
     */
    @Deprecated
    enum Status {
        UNRECOGNIZED, UNKNOWN, ACTIVE, SAVING, ERROR, DELETED;

        @JsonCreator
        public static Status forValue(String value) {
            if (value != null) {
                for (Status s : Status.values()) {
                    if (s.name().equalsIgnoreCase(value))
                        return s;
                }
            }
            return Status.UNKNOWN;
        }
    }

}
