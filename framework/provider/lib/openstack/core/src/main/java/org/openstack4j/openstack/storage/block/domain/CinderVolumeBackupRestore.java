package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.storage.block.VolumeBackupRestore;

import javax.annotation.Nullable;

@JsonRootName("restore")
public class CinderVolumeBackupRestore implements VolumeBackupRestore {

    private static final long serialVersionUID = 1L;

    @JsonProperty("volume_name")
    private String name;
    @JsonProperty("volume_id")
    private String volumeId;

    @Nullable
    @JsonProperty("backup_id")
    private String backupId;

    public CinderVolumeBackupRestore() {

    }

    public CinderVolumeBackupRestore(String name, String volumeId) {
        this.name = name;
        this.volumeId = volumeId;
    }

    public CinderVolumeBackupRestore(String name, String volumeId, String backupId) {
        this.backupId = backupId;
        this.name = name;
        this.volumeId = volumeId;
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
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackupId() {
        return backupId;
    }
}
