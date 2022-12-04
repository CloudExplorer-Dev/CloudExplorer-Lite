package org.openstack4j.openstack.manila.domain;

import org.openstack4j.model.manila.AbsoluteLimit;

/**
 * Absolute limits contain information about:
 * <ul>
 * <li>Total maximum share memory, in GBs.</li>
 * <li>Number of share-networks.</li>
 * <li>Number of share-snapshots.</li>
 * <li>Number of shares.</li>
 * <li>Shares and total used memory, in GBs.</li>
 * <li>Snapshots and total used memory, in GBs.</li>
 * </ul>
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ManilaAbsoluteLimit implements AbsoluteLimit {
    private static final long serialVersionUID = 1L;

    private int maxTotalShareGigabytes;
    private int maxTotalSnapshotGigabytes;
    private int maxTotalShares;
    private int maxTotalShareSnapshots;
    private int maxTotalShareNetworks;
    private int totalSharesUsed;
    private int totalShareSnapshotsUsed;
    private int totalShareNetworksUsed;
    private int totalShareGigabytesUsed;
    private int totalSnapshotGigabytesUsed;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTotalShareGigabytes() {
        return maxTotalShareGigabytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTotalSnapshotGigabytes() {
        return maxTotalSnapshotGigabytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTotalShares() {
        return maxTotalShares;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTotalShareSnapshots() {
        return maxTotalShareSnapshots;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTotalShareNetworks() {
        return maxTotalShareNetworks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalSharesUsed() {
        return totalSharesUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalShareSnapshotsUsed() {
        return totalShareSnapshotsUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalShareNetworksUsed() {
        return totalShareNetworksUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalShareGigabytesUsed() {
        return totalShareGigabytesUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalSnapshotGigabytesUsed() {
        return totalSnapshotGigabytesUsed;
    }
}
