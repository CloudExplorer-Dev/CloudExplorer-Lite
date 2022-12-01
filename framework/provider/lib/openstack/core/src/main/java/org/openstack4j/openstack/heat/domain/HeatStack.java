package org.openstack4j.openstack.heat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.heat.Stack;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * This is a model of a heatstack. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author Matthias Reisser
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("stack")
public class HeatStack implements Stack {
    private static final long serialVersionUID = 2151261616427716429L;

    @JsonProperty("id")
    private String id;
    @JsonProperty("stack_name")
    private String name;
    @JsonProperty("stack_status")
    private String status;
    @JsonProperty("stack_status_reason")
    private String stackStatusReason;
    @JsonProperty("description")
    private String description;
    @JsonProperty("template_description")
    private String templateDescription;
    @JsonProperty("timeout_mins")
    private Long timeoutMins;
    @JsonProperty("outputs")
    private List<Map<String, Object>> outputs;
    @JsonProperty("parameters")
    private Map<String, String> parameters;
    @JsonProperty("creation_time")
    private String creationTime;
    @JsonProperty("links")
    private List<GenericLink> links;
    @JsonProperty("updated_time")
    private String updatedTime;
    @JsonProperty("tags")
    private List<String> tags;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTemplateDescription() {
        return templateDescription;
    }

    @Override
    public Long getTimeoutMins() {
        return timeoutMins;
    }

    @Override
    public List<Map<String, Object>> getOutputs() {
        return outputs;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String getCreationTime() {
        return creationTime;
    }

    @Override
    public List<GenericLink> getLinks() {
        return links;
    }

    @Override
    public String getUpdatedTime() {
        return updatedTime;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public String getStackStatusReason() {
        return stackStatusReason;
    }

    @Override
    public String toString() {
        return "HeatStack [id=" + id + ", name=" + name + ", status=" + status
                + ", description=" + description + ", templateDescription="
                + templateDescription + ", timeoutMins=" + timeoutMins
                + ", outputs=" + outputs + ", parameters=" + parameters
                + ", creationTime=" + creationTime + ", links=" + links
                + ", updatedTime=" + updatedTime + ", tags=" + tags + "]";
    }

    /**
     * An inner class for representing lists of HeatStacks
     *
     * @author Matthias Reisser
     */
    public static class Stacks extends ListResult<HeatStack> {
        private static final long serialVersionUID = 600661296207420793L;

        @JsonProperty("stacks")
        private List<HeatStack> list;

        protected List<HeatStack> value() {
            return list;
        }
    }
}
