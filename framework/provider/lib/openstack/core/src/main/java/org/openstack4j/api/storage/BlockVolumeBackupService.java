package org.openstack4j.api.storage;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.VolumeBackup;
import org.openstack4j.model.storage.block.VolumeBackupCreate;
import org.openstack4j.model.storage.block.VolumeBackupRestore;

import java.util.List;
import java.util.Map;


/**
 * A backup is a full copy of a volume stored in an external service. A backup
 * can subsequently be restored from the external service to either the same
 * volume that the backup was originally taken from or to a new volume.
 * {@link http://developer.openstack.org/api-ref/block-storage/v2/}
 *
 * @author Huang Yi
 */
public interface BlockVolumeBackupService extends RestService {
    /**
     * Lists detailed information for all Block Storage backups that the tenant who submits the request can access.
     *
     * @return List of VolumeBackup
     */
    List<? extends VolumeBackup> list();

    /**
     * Returns list of Block Storage backups filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends VolumeBackup> list(Map<String, String> filteringParams);

    /**
     * Shows information for a specified backup.
     *
     * @param backupId the backup identifier
     * @return the volume backup or null
     */
    VolumeBackup get(String backupId);

    /**
     * Delete a specified volume backup
     *
     * @param backupId the backup identifier
     * @return the action response
     */
    ActionResponse delete(String backupId);

    /**
     * Create volume backup
     *
     * @param vbc the input for backup creation
     * @return the volume backup created.
     */
    VolumeBackup create(VolumeBackupCreate vbc);

    /**
     * Restore volume from a a specified backup.
     * If volumeId is provided, the backup will be restored to existng volume specified by the volume id.
     * Otherwise, a new volume will be created from the backup with assigned volume name.
     *
     * @param backupId the backup identifier
     * @param name     the volume name
     * @param volumeId the identified of existing volume
     */
    VolumeBackupRestore restore(String backupId, String name, String volumeId);


}
