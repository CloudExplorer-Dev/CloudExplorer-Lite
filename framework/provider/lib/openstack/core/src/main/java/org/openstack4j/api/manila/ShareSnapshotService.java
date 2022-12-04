package org.openstack4j.api.manila;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.ShareSnapshot;
import org.openstack4j.model.manila.ShareSnapshotCreate;
import org.openstack4j.model.manila.ShareSnapshotUpdateOptions;

import java.util.List;

/**
 * Share Snapshot Service for Manila SHared Filesystems.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareSnapshotService extends RestService {
    /**
     * Creates a snapshot from a share.
     *
     * @param snapshotCreate the snapshot to create
     * @return the snapshot
     */
    ShareSnapshot create(ShareSnapshotCreate snapshotCreate);

    /**
     * Lists all share snapshots.
     *
     * @return a list of all snapshots
     */
    List<? extends ShareSnapshot> list();

    /**
     * Lists all share snapshots with details.
     *
     * @return a list of all snapshots with details
     */
    List<? extends ShareSnapshot> listDetails();

    /**
     * Shows details for a share snapshot.
     *
     * @param snapshotId the snapshot ID
     * @return the snapshot
     */
    ShareSnapshot get(String snapshotId);

    /**
     * Updates a share snapshot.
     *
     * @param snapshotId            the snapshot ID
     * @param snapshotUpdateOptions the options to update on the snapshot
     * @return the snapshot
     */
    ShareSnapshot update(String snapshotId, ShareSnapshotUpdateOptions snapshotUpdateOptions);

    /**
     * Deletes a share snapshot.
     *
     * @param snapshotId the snapshot ID
     * @return the action response
     */
    ActionResponse delete(String snapshotId);

    /**
     * Administrator only. Explicitly updates the state of a share snapshot.
     *
     * @param snapshotId the snapshot ID
     * @param status     the status to set
     * @return the action response
     */
    ActionResponse resetState(String snapshotId, ShareSnapshot.Status status);

    /**
     * Administrator only. Force-deletes a share snapshot in any state.
     *
     * @param snapshotId the snapshot ID
     * @return the action response
     */
    ActionResponse forceDelete(String snapshotId);
}
