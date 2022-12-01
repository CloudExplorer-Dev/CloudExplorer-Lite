package org.openstack4j.openstack.heat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.heat.Resource;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * This is a model of a HeatResource. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author Octopus Zhang
 */
public class HeatResource implements Resource {
    private static final long serialVersionUID = 1L;

    private List<GenericLink> links;
    @JsonProperty("updated_time")
    private Date time;
    @JsonProperty("resource_type")
    private String type;
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
    @JsonProperty("required_by")
    private List<String> requiredBy;

    @Override
    public List<? extends Link> getLinks() {
        return links;
    }

    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public String getLocalReourceId() {
        return localReourceId;
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
    public List<String> getRequiredBy() {
        return requiredBy;
    }

    /**
     * An inner class for representing lists of Heat Resource
     *
     * @author Octopus Zhang
     */
    public static class Resources extends ListResult<HeatResource> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("resources")
        private List<HeatResource> list;

        protected List<HeatResource> value() {
            return list;
        }
    }
}
