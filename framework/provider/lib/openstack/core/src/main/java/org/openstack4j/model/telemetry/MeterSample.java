package org.openstack4j.model.telemetry;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * A single measurement for a given meter and resource.
 *
 * @author Jeremy Unruh
 */
public interface MeterSample extends ModelEntity {

    /**
     * @return the name of the meter
     */
    String getCounterName();

    /**
     * @return the name of the meter
     */
    void setCounterName(String counterName);

    /**
     * @return the type of meter
     */
    Meter.Type getCounterType();

    /**
     * @return the type of meter
     */
    void setCounterType(Meter.Type meterType);

    /**
     * @return The unit of measure for the value in the counter volume
     */
    String getCounterUnit();

    /**
     * @return The unit of measure for the value in the counter volume
     */
    void setCounterUnit(String counterUnit);

    /**
     * @return the actual measured value
     */
    Float getCounterVolume();

    /**
     * @return the actual measured value
     */
    void setCounterVolume(Float counterVolume);

    /**
     * @return The ID of the source that identifies where the sample comes from
     */
    String getSource();

    /**
     * @return The ID of the source that identifies where the sample comes from
     */
    void setSource(String source);

    /**
     * @return he ID of the project or tenant that owns the resource
     */
    String getProjectId();

    /**
     * @return he ID of the project or tenant that owns the resource
     */
    void setProjectId(String projectId);

    /**
     * @return The ID of the user who last triggered an update to the resource
     */
    String getUserId();

    /**
     * @return The ID of the user who last triggered an update to the resource
     */
    void setUserId(String userId);

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
     * @return A unique identifier for the sample
     */
    String getMessageId();

    /**
     * @return A unique identifier for the sample
     */
    void setMessageId(String messageId);

    /**
     * @return Arbitrary metadata associated with the resource
     */
    Map<String, Object> getMetadata();

    /**
     * @return Arbitrary metadata associated with the resource
     */
    void setMetadata(Map<String, Object> metadata);
}
