package org.openstack4j.openstack.heat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.heat.Event;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * This is a model of a heatevent. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author Octopus Zhang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("event")
public class HeatEvent implements Event {
    private static final long serialVersionUID = 1L;

    private String id;
    private List<GenericLink> links;
    @JsonProperty("event_time")
    private Date time;
    @JsonProperty("resource_status_reason")
    private String reason;
    @JsonProperty("resource_name")
    private String resourceName;
    @JsonProperty("logical_resource_id")
    private String localReourceId;
    @JsonProperty("resource_status")
    private String resourceStatus;
    @JsonProperty("physical_resource_id")
    private String physicalResourceId;
    @JsonProperty("resource_type")
    private String resourceType;
    @JsonProperty("resource_properties")
    private Object resourceProperties;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public String getLocalReourceId() {
        return localReourceId;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getResourceStatus() {
        return resourceStatus;
    }

    @Override
    public String getPhysicalResourceId() {
        return physicalResourceId;
    }

    @Override
    public List<? extends Link> getLinks() {
        return links;
    }

    @Override
    public String getResourceType() {
        return resourceType;
    }

    @Override
    public Object getResourceProperties() {
        return resourceProperties;
    }


    /**
     * An inner class for representing lists of Heat Resource
     *
     * @author Octopus Zhang
     */
    public static class Events extends ListResult<HeatEvent> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("events")
        private List<HeatEvent> list;

        protected List<HeatEvent> value() {
            return list;
        }
    }
}
