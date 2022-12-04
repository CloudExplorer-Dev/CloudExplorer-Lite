package org.openstack4j.model.manila;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.builder.ShareSnapshotCreateBuilder;

/**
 * Object used to create new share snapshots
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareSnapshotCreate extends ModelEntity, Buildable<ShareSnapshotCreateBuilder> {
    /**
     * @return the UUID of the share from which to create a snapshot
     */
    String getShareId();

    /**
     * @return whether snapshot creation is enabled when a share is busy
     */
    Boolean getForce();

    /**
     * @return the snapshot name
     */
    String getName();

    /**
     * @return the snapshot name
     */
    String getDisplayName();

    /**
     * @return the snapshot description
     */
    String getDescription();

    /**
     * @return the snapshot description
     */
    String getDisplayDescription();
}
