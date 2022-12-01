package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.storage.block.BlockLimits;

/**
 * Show absolute limits for a tenant
 *
 * @author Jeremy Unruh
 */
@JsonRootName("limits")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CinderBlockLimits implements BlockLimits {

    @JsonProperty
    private CinderAbsoluteLimit absolute;

    @Override
    public Absolute getAbsolute() {
        return absolute;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("absolute", absolute).toString();
    }

    public static class CinderAbsoluteLimit implements Absolute {

        private int totalSnapshotsUsed;
        private int maxTotalBackups;
        private int maxTotalVolumeGigabytes;
        private int maxTotalSnapshots;
        private int maxTotalBackupGigabytes;
        private int totalBackupGigabytesUsed;
        private int maxTotalVolumes;
        private int totalVolumesUsed;
        private int totalBackupsUsed;
        private int totalGigabytesUsed;

        @Override
        public int getTotalSnapshotsUsed() {
            return totalSnapshotsUsed;
        }

        @Override
        public int getMaxTotalBackups() {
            return maxTotalBackups;
        }

        @Override
        public int getMaxTotalVolumeGigabytes() {
            return maxTotalVolumeGigabytes;
        }

        @Override
        public int getMaxTotalSnapshots() {
            return maxTotalSnapshots;
        }

        @Override
        public int getMaxTotalBackupGigabytes() {
            return maxTotalBackupGigabytes;
        }

        @Override
        public int getTotalBackupGigabytesUsed() {
            return totalBackupGigabytesUsed;
        }

        @Override
        public int getMaxTotalVolumes() {
            return maxTotalVolumes;
        }

        @Override
        public int getTotalVolumesUsed() {
            return totalVolumesUsed;
        }

        @Override
        public int getTotalBackupsUsed() {
            return totalBackupsUsed;
        }

        @Override
        public int getTotalGigabytesUsed() {
            return totalGigabytesUsed;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("totalSnapshotsUsed", totalSnapshotsUsed).add("maxTotalBackups", maxTotalBackups)
                    .add("maxTotalVolumeGigabytes", maxTotalVolumeGigabytes).add("maxTotalSnapshots", maxTotalSnapshots)
                    .add("maxTotalBackupGigabytes", maxTotalBackupGigabytes).add("totalBackupGigabytesUsed", totalBackupGigabytesUsed)
                    .add("maxTotalVolumes", maxTotalVolumes).add("totalVolumesUsed", totalVolumesUsed)
                    .add("totalBackupsUsed", totalBackupsUsed).add("totalGigabytesUsed", totalGigabytesUsed)
                    .toString();
        }
    }
}
