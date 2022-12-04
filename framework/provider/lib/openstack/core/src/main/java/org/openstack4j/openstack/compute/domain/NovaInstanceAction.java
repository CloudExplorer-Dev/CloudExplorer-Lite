package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.InstanceAction;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

@JsonRootName("instanceAction")
public class NovaInstanceAction implements InstanceAction {

    @JsonProperty("action")
    private String action;
    @JsonProperty("instance_uuid")
    private String instanceUuid;
    @JsonProperty("message")
    private String message;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("start_time")
    private Date startTime;
    @JsonProperty("user_id")
    private String userId;

    public NovaInstanceAction() {

    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public String getInstanceUuid() {
        return instanceUuid;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("action", action).add("instance_uuid", instanceUuid)
                .add("message", message).add("project_id", projectId).add("request_id", requestId)
                .add("start_time", startTime).add("user_id", userId).toString();
    }

    public static class NovaInstanceActions extends ListResult<NovaInstanceAction> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("instanceActions")
        private List<NovaInstanceAction> actions;

        @Override
        protected List<NovaInstanceAction> value() {
            return actions;
        }

    }

}
