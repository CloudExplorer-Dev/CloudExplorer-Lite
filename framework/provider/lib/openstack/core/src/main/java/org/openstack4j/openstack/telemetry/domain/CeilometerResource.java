package org.openstack4j.openstack.telemetry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.telemetry.Resource;

import java.util.Date;
import java.util.Map;

/**
 * A single measurement for resource.
 *
 * @author Shital Patil
 */

public class CeilometerResource implements Resource {

    private static final long serialVersionUID = 1L;

    @JsonProperty("resource_id")
    String id;
    @JsonProperty("metadata")
    Map<String, Object> metaData;
    @JsonProperty("user_id")
    private String userId;
    private String source;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("first_sample_timestamp")
    private Date firstSampleTimestamp;
    @JsonProperty("last_sample_timestamp")
    private Date lastSampleTimeStamp;

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
    public String getUserId() {
        return userId;
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
    public Date getFirstSampleTimestamp() {
        return firstSampleTimestamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastSampleTimestamp() {
        return lastSampleTimeStamp;
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
    public Map<String, Object> getMeataData() {
        return metaData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("userId", userId).add("projectId", projectId).add("source", source).add("firstSanpleTimestamp", firstSampleTimestamp)
                .add("lastSanpleTimestamp", lastSampleTimeStamp).toString();
    }

}
