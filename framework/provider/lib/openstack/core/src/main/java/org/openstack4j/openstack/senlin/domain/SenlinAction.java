package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.Action;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinAction. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("action")
public class SenlinAction implements Action {
    private static final long serialVersionUID = -1125919503657762374L;

    @JsonProperty("action")
    private String action;
    @JsonProperty("cause")
    private String cause;
    @JsonProperty("context")
    private Map<String, Object> context;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("depended_by")
    private List<String> dependedBy;
    @JsonProperty("depended_on")
    private List<String> dependedOn;
    @JsonProperty("end_time")
    private Float endTime;
    @JsonProperty("id")
    private String id;
    @JsonProperty("inputs")
    private Map<String, Object> inputs;
    @JsonProperty("interval")
    private Integer interval;
    @JsonProperty("name")
    private String name;
    @JsonProperty("outputs")
    private Map<String, Object> outputs;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("start_time")
    private Float startTime;
    @JsonProperty("status")
    private String status;
    @JsonProperty("status_reason")
    private String statusReason;
    @JsonProperty("target")
    private String target;
    @JsonProperty("timeout")
    private Integer timeout;
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getContext() {
        return context;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getCause() {
        return cause;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public List<String> getDependedBy() {
        return dependedBy;
    }

    @Override
    public List<String> getDependedOn() {
        return dependedOn;
    }

    @Override
    public Float getEndTime() {
        return endTime;
    }

    @Override
    public Map<String, Object> getInputs() {
        return inputs;
    }

    @Override
    public Integer getInterval() {
        return interval;
    }

    @Override
    public Map<String, Object> getOutputs() {
        return outputs;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public Float getStartTime() {
        return startTime;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getStatusReason() {
        return statusReason;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public Integer getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "SenlinAction{" +
                "action='" + action + '\'' +
                ", cause='" + cause + '\'' +
                ", context=" + context +
                ", createdAt=" + createdAt +
                ", dependedBy=" + dependedBy +
                ", dependedOn=" + dependedOn +
                ", endTime=" + endTime +
                ", id='" + id + '\'' +
                ", inputs=" + inputs +
                ", interval=" + interval +
                ", name='" + name + '\'' +
                ", outputs=" + outputs +
                ", owner='" + owner + '\'' +
                ", startTime=" + startTime +
                ", status='" + status + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", target='" + target + '\'' +
                ", timeout=" + timeout +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * An inner class for representing lists of senlinAction
     *
     * @author lion
     */
    public static class Action extends ListResult<SenlinAction> {
        private static final long serialVersionUID = 298037693459165073L;
        @JsonProperty("actions")
        private List<SenlinAction> list;

        protected List<SenlinAction> value() {
            return list;
        }
    }
}
