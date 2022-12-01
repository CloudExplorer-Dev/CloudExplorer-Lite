package org.openstack4j.openstack.storage.block.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.storage.*;
import org.openstack4j.api.storage.ext.BlockStorageServiceService;
import org.openstack4j.model.storage.block.BlockLimits;
import org.openstack4j.openstack.storage.block.domain.CinderBlockLimits;

/**
 * Block Storage (Cinder) Service Operation implementation
 *
 * @author Jeremy Unruh
 */
public class BlockStorageServiceImpl extends BaseBlockStorageServices implements BlockStorageService {

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockVolumeService volumes() {
        return Apis.get(BlockVolumeService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockVolumeSnapshotService snapshots() {
        return Apis.get(BlockVolumeSnapshotService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLimits getLimits() {
        return get(CinderBlockLimits.class, "/limits").execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockQuotaSetService quotaSets() {
        return Apis.get(BlockQuotaSetService.class);
    }

    @Override
    public CinderZoneService zones() {
        return Apis.get(CinderZoneService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchedulerStatsGetPoolService schedulerStatsPools() {
        return Apis.get(SchedulerStatsGetPoolService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockVolumeBackupService backups() {
        return Apis.get(BlockVolumeBackupService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockStorageServiceService services() {
        return Apis.get(BlockStorageServiceService.class);
    }

}
