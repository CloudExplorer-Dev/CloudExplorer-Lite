package org.openstack4j.model.storage.block.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.storage.block.VolumeBackupCreate;

public interface VolumeBackupCreateBuilder extends Builder<VolumeBackupCreateBuilder, VolumeBackupCreate> {


    VolumeBackupCreateBuilder name(String name);

    VolumeBackupCreateBuilder description(String description);

    VolumeBackupCreateBuilder volumeId(String volumeId);

    VolumeBackupCreateBuilder container(String container);

    VolumeBackupCreateBuilder incremental(boolean incremental);

    VolumeBackupCreateBuilder force(boolean force);

    VolumeBackupCreateBuilder snapshotId(String snapshotId);

}
