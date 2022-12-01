package org.openstack4j.model.storage.block;

/**
 * Show absolute limits for a tenant
 *
 * @author Jeremy Unruh
 */
public interface BlockLimits {

    Absolute getAbsolute();

    public interface Absolute {

        /**
         * @return the total snapshots used
         */
        int getTotalSnapshotsUsed();

        /**
         * @return the maximum total backups
         */
        int getMaxTotalBackups();

        /**
         * @return the max total volume in GB
         */
        int getMaxTotalVolumeGigabytes();

        /**
         * @return the max total number of snapshots
         */
        int getMaxTotalSnapshots();

        /**
         * @return the max total backup in GB
         */
        int getMaxTotalBackupGigabytes();

        /**
         * @return the total backup GB used
         */
        int getTotalBackupGigabytesUsed();

        /**
         * @return the max total volumes
         */
        int getMaxTotalVolumes();

        /**
         * @return the total volumes used
         */
        int getTotalVolumesUsed();

        /**
         * @return total backups used
         */
        int getTotalBackupsUsed();

        /**
         * @return total GB used
         */
        int getTotalGigabytesUsed();
    }
}
