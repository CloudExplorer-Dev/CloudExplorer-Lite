package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;
import org.openstack4j.model.storage.block.builder.VolumeBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An OpenStack Volume
 *
 * @author Jeremy Unruh
 */
@JsonRootName("volume")
public class CinderVolume implements Volume {

    private static final long serialVersionUID = 1L;

    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("display_description")
    private String displayDescription;
    private Status status;
    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("availability_zone")
    private String zone;
    @JsonProperty("created_at")
    private Date created;
    @JsonProperty("volume_type")
    private String volumeType;
    @JsonProperty("imageRef")
    private String imageRef;
    @JsonProperty("multiattach")
    private Boolean multiattach;
    @JsonProperty("source_volid")
    private String sourceVolid;
    @JsonProperty("snapshot_id")
    private String snapshotId;
    @JsonProperty("metadata")
    private Map<String, String> metadata;
    @JsonProperty("bootable")
    private Boolean bootable;
    @JsonProperty("attachments")
    private List<CinderVolumeAttachment> attachments;
    @JsonProperty("image_id")
    private String imageId;
    @JsonProperty("volume_image_metadata")
    private Map<String, Object> imageMetadata;
    @JsonProperty("os-vol-mig-status-attr:migstat")
    private MigrationStatus migrateStatus;
    @JsonProperty("os-vol-tenant-attr:tenant_id")
    private String tenantId;
    @JsonProperty("encrypted")
    private Boolean encrypted;
    @JsonProperty("os-vol-host-attr:host")
    private String host;

    /**
     * @return the Volume Builder
     */
    public static VolumeBuilder builder() {
        return new ConcreteVolumeBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VolumeBuilder toBuilder() {
        return new ConcreteVolumeBuilder(this);
    }

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
    public String getName() {
        return name;
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
    public String getDescription() {
        return description;
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
    public Status getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return (size == null) ? 0 : size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getZone() {
        return zone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreated() {
        return created;
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
    public MigrationStatus getMigrateStatus() {
        return migrateStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImageRef() {
        if (imageRef != null)
            return imageRef;

        // Depending on whether this is a Listing or a direct Get the information is different so we are smart
        // about returning the proper imageId if applicable
        if (imageId == null && imageMetadata != null && imageMetadata.containsKey("image_id"))
            imageId = String.valueOf(imageMetadata.get("image_id"));

        return imageId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean multiattach() {
        return multiattach;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSourceVolid() {
        return sourceVolid;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Map<String, String> getMetaData() {
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends VolumeAttachment> getAttachments() {
        return attachments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean bootable() {
        return bootable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean encrypted() {
        return encrypted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String host() {
        return host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("description", description)
                .add("status", status).add("size", size).add("zone", zone).add("created", created)
                .add("volumeType", volumeType).add("imageRef", getImageRef()).add("multiattach", multiattach)
                .add("sourceVolid", sourceVolid).add("snapshotId", snapshotId).add("metadata", metadata)
                .add("bootable", bootable)
                .toString();
    }

    public static class Volumes extends ListResult<CinderVolume> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("volumes")
        private List<CinderVolume> volumes;

        @Override
        protected List<CinderVolume> value() {
            return volumes;
        }
    }

    public static class ConcreteVolumeBuilder implements VolumeBuilder {

        private CinderVolume m;

        ConcreteVolumeBuilder() {
            this(new CinderVolume());
        }

        ConcreteVolumeBuilder(CinderVolume m) {
            this.m = m;
        }

        @Override
        public VolumeBuilder name(String name) {
            m.name = name;
            m.displayName = name;
            return this;
        }

        @Override
        public VolumeBuilder description(String description) {
            m.description = description;
            m.displayDescription = description;
            return this;
        }

        @Override
        public VolumeBuilder source_volid(String uuid) {
            m.sourceVolid = uuid;
            return this;
        }

        @Override
        public VolumeBuilder snapshot(String snapshotId) {
            m.snapshotId = snapshotId;
            return this;
        }

        @Override
        public VolumeBuilder imageRef(String imageRef) {
            m.imageRef = imageRef;
            return this;
        }

        @Override
        public VolumeBuilder multiattach(Boolean multiattach) {
            m.multiattach = multiattach;
            return this;
        }

        @Override
        public VolumeBuilder size(int size) {
            m.size = size;
            return this;
        }

        @Override
        public VolumeBuilder volumeType(String volumeType) {
            m.volumeType = volumeType;
            return this;
        }

        @Override
        public VolumeBuilder bootable(boolean isBootable) {
            m.bootable = isBootable;
            return this;
        }

        @Override
        public VolumeBuilder metadata(Map<String, String> metadata) {
            m.metadata = metadata;
            return this;
        }

        @Override
        public Volume build() {
            return m;
        }

        @Override
        public VolumeBuilder from(Volume in) {
            m = (CinderVolume) in;
            return this;
        }

        @Override
        public VolumeBuilder zone(String zone) {
            m.zone = zone;
            return this;
        }
    }
}
