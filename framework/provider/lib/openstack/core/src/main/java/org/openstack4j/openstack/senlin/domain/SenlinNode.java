package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.Node;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinNode. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("node")
public class SenlinNode implements Node {
    private static final long serialVersionUID = 4953618741806882198L;

    @JsonProperty("cluster_id")
    private String clusterID;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("id")
    private String id;
    @JsonProperty("index")
    private Integer index;
    @JsonProperty("data")
    private Map<String, Object> data;
    @JsonProperty("details")
    private Map<String, Object> details;
    @JsonProperty("init_at")
    private Date initAt;
    @JsonProperty("metadata")
    private Map<String, Object> metadata;
    @JsonProperty("name")
    private String name;
    @JsonProperty("physical_id")
    private String physicalID;
    @JsonProperty("profile_id")
    private String profileID;
    @JsonProperty("profile_name")
    private String profileName;
    @JsonProperty("project")
    private String project;
    @JsonProperty("role")
    private String role;
    @JsonProperty("status")
    private String status;
    @JsonProperty("status_reason")
    private String statusReason;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("user")
    private String user;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
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
    public Integer getIndex() {
        return index;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public Map<String, Object> getDetails() {
        return details;
    }

    @Override
    public Date getInitAt() {
        return initAt;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public String getPhysicalID() {
        return physicalID;
    }

    @Override
    public String getProfileID() {
        return profileID;
    }

    @Override
    public String getProfileName() {
        return profileName;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public String getRole() {
        return role;
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
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "SenlinNode{" +
                "clusterID='" + clusterID + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", domain='" + domain + '\'' +
                ", id='" + id + '\'' +
                ", index='" + index + '\'' +
                ", data=" + data +
                ", details=" + details +
                ", initAt='" + initAt + '\'' +
                ", metadata=" + metadata +
                ", name='" + name + '\'' +
                ", physicalID='" + physicalID + '\'' +
                ", profileID='" + profileID + '\'' +
                ", profileName='" + profileName + '\'' +
                ", project='" + project + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    /**
     * An inner class for representing lists of senlinNode
     *
     * @author lion
     */
    public static class Node extends ListResult<SenlinNode> {
        private static final long serialVersionUID = 9043454000625258331L;

        @JsonProperty("nodes")
        private List<SenlinNode> list;

        protected List<SenlinNode> value() {
            return list;
        }
    }
}
