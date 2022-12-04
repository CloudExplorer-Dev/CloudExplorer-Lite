package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.Receiver;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinReceiver. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("receiver")
public class SenlinReceiver implements Receiver {
    private static final long serialVersionUID = -6208098906539289716L;

    @JsonProperty("action")
    private String action;
    @JsonProperty("cluster_id")
    private String clusterID;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("project")
    private String project;
    @JsonProperty("type")
    private String type;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("user")
    private String user;
    @JsonProperty("actor")
    private Map<String, Object> actor;
    @JsonProperty("channel")
    private Map<String, Object> channel;
    @JsonProperty("params")
    private Map<String, Object> params;

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public String getClusterID() {
        return clusterID;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getActor() {
        return actor;
    }

    @Override
    public Map<String, Object> getChannel() {
        return channel;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getWebHook() {
        return channel.get("alarm_url").toString();
    }

    @Override
    public String toString() {
        return "SenlinReceiver{" +
                "action='" + action + '\'' +
                ", cluster_id='" + clusterID + '\'' +
                ", created_at='" + createdAt + '\'' +
                ", domain='" + domain + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", project='" + project + '\'' +
                ", type='" + type + '\'' +
                ", updated_at='" + updatedAt + '\'' +
                ", user='" + user + '\'' +
                ", actor=" + actor +
                ", channel=" + channel +
                ", params=" + params +
                '}';
    }

    /**
     * An inner class for representing lists of senlinReceivers
     *
     * @author lion
     */
    public static class Receiver extends ListResult<SenlinReceiver> {
        private static final long serialVersionUID = 600661296207420793L;

        @JsonProperty("receivers")
        private List<SenlinReceiver> list;

        protected List<SenlinReceiver> value() {
            return list;
        }
    }
}
