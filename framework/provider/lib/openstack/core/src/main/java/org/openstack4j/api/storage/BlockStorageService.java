package org.openstack4j.api.storage;

import org.openstack4j.api.storage.ext.BlockStorageServiceService;
import org.openstack4j.common.RestService;
import org.openstack4j.model.storage.block.BlockLimits;

/**
 * Block Storage (Cinder) Service Operation API
 *
 * @author Jeremy Unruh
 */
public interface BlockStorageService extends RestService {

    /**
     * @return the Volume Service API
     */
    BlockVolumeService volumes();

    /**
     * @return the Volume Snapshot Service API
     */
    BlockVolumeSnapshotService snapshots();

    CinderZoneService zones();

    /**
     * Gets the Absolute limits used by this tenant
     *
     * @return the absolute limits
     */
    BlockLimits getLimits();

    /**
     * The block storage quota-set service.
     *
     * @return the quota-set service
     */
    BlockQuotaSetService quotaSets();

    /**
     * The block storage get_pools service.
     *
     * @return the scheduler stats service
     */
    SchedulerStatsGetPoolService schedulerStatsPools();

    /**
     * @return the Volume Service API
     */
    BlockVolumeBackupService backups();

    /**
     * The block storage service service
     *
     * @return ServiceService
     */
    BlockStorageServiceService services();
}
