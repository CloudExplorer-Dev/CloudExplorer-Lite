package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.manila.Share;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * A share is a remote, mountable file system.
 * You can mount a share to and access a share from several hosts by several users at a time.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share")
public class ManilaShare implements Share {
    private static final long serialVersionUID = 1L;

    private String id;
    private Status status;
    private List<GenericLink> links;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("share_proto")
    private Protocol shareProto;
    private Integer size;
    private String name;
    private String description;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("display_description")
    private String displayDescription;
    @JsonProperty("share_type")
    private String shareType;
    @JsonProperty("share_type_name")
    private String shareTypeName;
    @JsonProperty("volume_type")
    private String volumeType;
    @JsonProperty("snapshot_id")
    private String snapshotId;
    @JsonProperty("is_public")
    private Boolean isPublic;
    private Map<String, String> metadata;
    @JsonProperty("share_network_id")
    private String shareNetworkId;
    @JsonProperty("availability_zone")
    private String availabilityZone;
    @JsonProperty("export_location")
    private String exportLocation;
    @JsonProperty("export_locations")
    private List<String> exportLocations;
    private String host;
    @JsonProperty("task_state")
    private TaskState taskState;
    @JsonProperty("share_server_id")
    private String shareServerId;
    @JsonProperty("consistency_group_id")
    private String consistencyGroupId;
    @JsonProperty("snapshot_support")
    private Boolean snapshotSupport;
    @JsonProperty("source_cgsnapshot_member_id")
    private String sourceCgsnapshotMemberId;
    @JsonProperty("created_at")
    private String createdAt;

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
    public Status getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Link> getLinks() {
        return links;
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
    public Protocol getShareProto() {
        return shareProto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayDescription() {
        return displayDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShareType() {
        return shareType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShareTypeName() {
        return shareTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVolumeType() {
        return volumeType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSnapshotId() {
        return snapshotId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isPublic() {
        return isPublic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShareNetworkId() {
        return shareNetworkId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExportLocation() {
        return exportLocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getExportLocations() {
        return exportLocations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskState getTaskState() {
        return taskState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShareServerId() {
        return shareServerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConsistencyGroupId() {
        return consistencyGroupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getSnapshotSupport() {
        return snapshotSupport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSourceCgsnapshotMemberId() {
        return sourceCgsnapshotMemberId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    public static class Shares extends ListResult<ManilaShare> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("shares")
        private List<ManilaShare> shares;

        @Override
        protected List<ManilaShare> value() {
            return shares;
        }
    }
}
