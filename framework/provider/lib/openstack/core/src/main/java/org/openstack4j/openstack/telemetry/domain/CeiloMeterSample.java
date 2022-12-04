package org.openstack4j.openstack.telemetry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.telemetry.Meter.Type;
import org.openstack4j.model.telemetry.Sample;

import java.util.Map;

/**
 * A single measurement for sample.
 *
 * @author Shital Patil
 */

public class CeiloMeterSample implements Sample {

    private static final long serialVersionUID = 1L;

    private String id;

    private Type type;

    private String unit;

    private Float volume;

    private String source;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("resource_id")
    private String resourceId;

    private String timestamp;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("recorded_at")
    private String recordedAt;

    private String meter;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnit() {
        return unit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float getVolume() {
        return volume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProjectId() {
        return projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceId() {
        return resourceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordedAt() {
        return recordedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMeter() {
        return meter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("meter", meter).add("type", type).add("unit", unit).add("volume", volume).add("timestamp", timestamp)
                .add("source", source).add("project", projectId).add("user", userId).add("resource", resourceId).addValue("\n").add("metadata", metadata)
                .add("recorded_at", recordedAt).toString();
    }
}
