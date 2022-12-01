package org.openstack4j.model.storage.block.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.storage.block.VolumeSnapshot;

import java.util.Map;

/**
 * A Builder which creates a Volume Snapshot.
 * <p>
 * NOTE: The only <B>REQUIRED</B> field when creating a snapshot is
 * {@link #volume(String)} . All other fields are optional
 *
 * @author Jeremy Unruh
 */
public interface VolumeSnapshotBuilder extends Builder<VolumeSnapshotBuilder, VolumeSnapshot> {

    /**
     * Name of the snapshot
     *
     * @param name the name
     * @return the volume snapshot builder
     */
    VolumeSnapshotBuilder name(String name);

    /**
     * Description of the snapshot
     *
     * @param description the description
     * @return the volume snapshot builder
     */
    VolumeSnapshotBuilder description(String description);

    /**
     * The ID of of the existing Volume <B>(REQUIRED)</b>
     *
     * @param volumeId the volume id
     * @return the volume snapshot builder
     */
    VolumeSnapshotBuilder volume(String volumeId);

    /**
     * One or more metadata key and value pairs to associate with the volume snapshot. <b>Optional</b>
     *
     * @param metadata metadata to set
     * @return VolumeSnapshotBuilder
     */
    VolumeSnapshotBuilder metadata(Map<String, String> metadata);

    /**
     * [True/False] Indicate whether to snapshot, even if the volume is attached.
     *
     * @param force true to force an attached volume to be a snapshot
     * @return the volume snapshot builder
     */
    VolumeSnapshotBuilder force(boolean force);

}
