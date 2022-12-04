package org.openstack4j.model.telemetry;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * A single measurement for Sample.
 *
 * @author Shital Patil
 */

public interface Sample extends ModelEntity {

    /**
     * @return the idof the meter
     */
    String getId();

    /**
     * @return Arbitrary metadata associated with the resource
     */
    Map<String, Object> getMetadata();

    /**
     * @return Arbitrary metadata associated with the resource
     */
    void setMetadata(Map<String, Object> metadata);

    /**
     * @return the meter
     */
    String getMeter();

    /**
     * @return the actual measured value
     */
    Float getVolume();

    /**
     * @return The ID of the source that identifies where the sample comes from
     */
    String getSource();

    /**
     * @return The ID of the source that identifies where the sample comes from
     */
    void setSource(String source);

    /**
     * @return The ID of the Resource for which the measurements are taken
     */
    String getResourceId();

    /**
     * @return The ID of the Resource for which the measurements are taken
     */
    void setResourceId(String resourceId);

    /**
     * @return UTC date and time when the measurement was made
     */
    String getTimestamp();

    /**
     * @return UTC date and time when the measurement was made
     */
    void setTimestamp(String timestamp);

    /**
     * @return UTC date and time when the sample was recorded
     */
    String getRecordedAt();

    /**
     * @return UTC date and time when the sample was recorded
     */
    void setRecordedAt(String date);

    /**
     * @return he ID of the project or tenant that owns the resource
     */
    String getProjectId();

    /**
     * @return he ID of the project or tenant that owns the resource
     */
    void setProjectId(String projectId);

    /**
     * @return the type of meter
     */
    Meter.Type getType();

    /**
     * @return The ID of the user who last triggered an update to the resource
     */
    String getUserId();

    /**
     * @return Returns unit
     */
    String getUnit();

}
