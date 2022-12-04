package org.openstack4j.model.storage.block;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.storage.block.Volume.Status;
import org.openstack4j.model.storage.block.builder.VolumeSnapshotBuilder;

import java.util.Date;
import java.util.Map;

/**
 * An OpenStack Volume Snapshot which is a point-in-time copy of a volume
 *
 * @author Jeremy Unruh
 */
public interface VolumeSnapshot extends ModelEntity, Buildable<VolumeSnapshotBuilder> {

    /**
     * @return the snapshot identifier
     */
    String getId();

    /**
     * @return the name of the snapshot
     */
    String getName();

    /**
     * @return the display name of the snapshot
     */
    @Deprecated
    String getDisplayName();

    /**
     * @return the description of the snapshot
     */
    String getDescription();

    /**
     * @return the display description of the snapshot
     */
    @Deprecated
    String getDisplayDescription();

    /**
     * The volume identifier of an existing volume
     *
     * @return the volume identifier or null
     */
    String getVolumeId();

    /**
     * @return the status of the snapshot
     */
    Status getStatus();

    /**
     * Size in GBs
     *
     * @return the size of the snapshot in GB
     */
    int getSize();

    /**
     * @return the created data of the snapshot
     */
    Date getCreated();

    /**
     * @return extended meta data information. key value pair of String key, String value
     */
    Map<String, String> getMetaData();
}
