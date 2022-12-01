package org.openstack4j.model.storage.block;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.storage.block.builder.VolumeBackupCreateBuilder;

/**
 * Represents entity which is used for creating a volume backup
 *
 * @author Huang Yi
 */
public interface VolumeBackupCreate extends ModelEntity, Buildable<VolumeBackupCreateBuilder> {

    /**
     * @return the name of the Volume Transfer.
     */
    String getName();

    /**
     * @return the description of the volume backup
     */
    String getDescription();

    /**
     * @return The UUID of the volume.
     */
    String getVolumeId();

    /**
     * @return The container name or null.
     */
    String getContainer();

    /**
     * @return The backup mode. A valid value is true for incremental backup mode or false for full backup mode
     */
    boolean isIncremental();

    /**
     * @return Force mode. True to do backup while a volume is attached. Default is false.
     */
    boolean isForce();

    /**
     * @return Force mode. True to do backup while a volume is attached. Default is false.
     */
    String getSnapshotId();


}
