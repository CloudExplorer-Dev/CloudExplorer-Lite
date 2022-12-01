package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.ServerAction;
import org.openstack4j.model.compute.ServerActionEvent;
import org.openstack4j.openstack.common.ListResult;
import org.openstack4j.openstack.compute.internal.NovaServerActionEvent;

import java.util.Date;
import java.util.List;

/**
 * This is a model of a serverevent.
 *
 * @author sujit sah
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("instanceAction")
public class ServerEvent implements ServerAction {

    private static final long serialVersionUID = 1L;
    @JsonProperty("action")
    private String action;
    @JsonProperty("events")
    private List<NovaServerActionEvent> events;
    @JsonProperty("instance_uuid")
    private String instanceUuid;
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("result")
    private String result;
    @JsonProperty("start_time")
    private Date start_time;
    @JsonProperty("user_id")
    private String userId;

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public List<? extends ServerActionEvent> getEvents() {
        return events;
    }

    @Override
    public String getInstaceUuid() {
        return instanceUuid;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public Date getStartTime() {
        return start_time;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * Inner class for representing the list of server resource
     *
     * @author sujit sah
     */
    public static class Events extends ListResult<ServerEvent> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("instanceActions")
        private List<ServerEvent> list;

        @Override
        protected List<ServerEvent> value() {
            return list;
        }
    }
}
