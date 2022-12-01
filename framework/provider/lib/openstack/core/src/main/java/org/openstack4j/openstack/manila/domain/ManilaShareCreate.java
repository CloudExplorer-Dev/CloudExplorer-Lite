package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Maps;
import org.openstack4j.model.manila.Share;
import org.openstack4j.model.manila.ShareCreate;
import org.openstack4j.model.manila.builder.ShareCreateBuilder;

import java.util.Map;

/**
 * Object used to create new shares.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share")
public class ManilaShareCreate implements ShareCreate {
    private static final long serialVersionUID = 1L;

    @JsonProperty("share_proto")
    private Share.Protocol shareProto;
    private Integer size;
    private String name;
    private String description;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("display_description")
    private String displayDescription;
    @JsonProperty("share_type")
    private String shareType;
    @JsonProperty("volume_type")
    private String volumeType;
    @JsonProperty("snapshot_id")
    private String snapshotId;
    @JsonProperty("is_public")
    private Boolean isPublic;
    private Map<String, String> metadata;
    @JsonProperty("share_network_id")
    private String shareNetworkId;
    @JsonProperty("consistency_group_id")
    private String consistencyGroupId;
    @JsonProperty("availability_zone")
    private String availabilityZone;

    public static ShareCreateBuilder builder() {
        return new ShareCreateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Share.Protocol getShareProto() {
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
    public String getConsistencyGroupId() {
        return consistencyGroupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    @Override
    public ShareCreateBuilder toBuilder() {
        return new ShareCreateConcreteBuilder(this);
    }

    public static class ShareCreateConcreteBuilder implements ShareCreateBuilder {
        ManilaShareCreate shareCreate;

        public ShareCreateConcreteBuilder() {
            this(new ManilaShareCreate());
        }

        public ShareCreateConcreteBuilder(ManilaShareCreate shareCreate) {
            this.shareCreate = shareCreate;
        }

        @Override
        public ShareCreateBuilder shareProto(Share.Protocol shareProto) {
            shareCreate.shareProto = shareProto;
            return this;
        }

        @Override
        public ShareCreateBuilder size(Integer size) {
            shareCreate.size = size;
            return this;
        }

        @Override
        public ShareCreateBuilder name(String name) {
            shareCreate.name = name;
            return this;
        }

        @Override
        public ShareCreateBuilder description(String description) {
            shareCreate.description = description;
            return this;
        }

        @Override
        public ShareCreateBuilder displayName(String displayName) {
            shareCreate.displayName = displayName;
            return this;
        }

        @Override
        public ShareCreateBuilder displayDescription(String displayDescription) {
            shareCreate.displayDescription = displayDescription;
            return this;
        }

        @Override
        public ShareCreateBuilder shareType(String shareType) {
            shareCreate.shareType = shareType;
            return this;
        }

        @Override
        public ShareCreateBuilder volumeType(String volumeType) {
            shareCreate.volumeType = volumeType;
            return this;
        }

        @Override
        public ShareCreateBuilder snapshotId(String snapshotId) {
            shareCreate.snapshotId = snapshotId;
            return this;
        }

        @Override
        public ShareCreateBuilder isPublic(Boolean isPublic) {
            shareCreate.isPublic = isPublic;
            return this;
        }

        @Override
        public ShareCreateBuilder addMetadataItem(String key, String value) {
            if (shareCreate.metadata == null)
                shareCreate.metadata = Maps.newHashMap();

            shareCreate.metadata.put(key, value);
            return this;
        }

        @Override
        public ShareCreateBuilder metadata(Map<String, String> metadata) {
            shareCreate.metadata = metadata;
            return this;
        }

        @Override
        public ShareCreateBuilder shareNetworkId(String shareNetworkId) {
            shareCreate.shareNetworkId = shareNetworkId;
            return this;
        }

        @Override
        public ShareCreateBuilder consistencyGroupId(String consistencyGroupId) {
            shareCreate.consistencyGroupId = consistencyGroupId;
            return this;
        }

        @Override
        public ShareCreateBuilder availabilityZone(String availabilityZone) {
            shareCreate.availabilityZone = availabilityZone;
            return this;
        }

        @Override
        public ShareCreate build() {
            return shareCreate;
        }

        @Override
        public ShareCreateBuilder from(ShareCreate in) {
            shareCreate = (ManilaShareCreate) in;
            return this;
        }
    }
}
