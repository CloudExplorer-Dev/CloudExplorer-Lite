package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.storage.block.VolumeBackup;
import org.openstack4j.openstack.common.ListResult;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

@JsonRootName("backup")
public class CinderVolumeBackup implements VolumeBackup {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    @Nullable
    private Status status;
    private String description;
    private int size;
    private String container;

    @JsonProperty("volume_id")
    private String volumeId;

    @JsonProperty("availability_zone")
    @Nullable
    private String zone;

    @JsonProperty("created_at")
    private Date created;

    @JsonProperty("fail_reason")
    private String failReason;

    @JsonProperty("object_count")
    private int objectCount;

    @JsonProperty("has_dependent_backups")
    @Nullable
    private Boolean hasDependent;

    @JsonProperty("is_incremental")
    @Nullable
    private Boolean incremental;

    @JsonProperty("snapshot_id")
    @Nullable
    private String snapshotId;

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
    public String getVolumeId() {
        return volumeId;
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
    public Status getStatus() {
        return status;
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
    public Boolean isIncremental() {
        return incremental;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return size;
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
    public String getContainer() {
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFailReason() {
        return failReason;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getObjectCount() {
        return objectCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean hasDependent() {
        return hasDependent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSnapshotId() {
        return snapshotId;
    }


    public static class VolumeBackups extends ListResult<CinderVolumeBackup> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("backups")
        private List<CinderVolumeBackup> backups;

        @Override
        protected List<CinderVolumeBackup> value() {
            return backups;
        }
    }


}
