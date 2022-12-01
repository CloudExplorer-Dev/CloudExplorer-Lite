package org.openstack4j.model.manila;

/**
 * Options used to update a quota set.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class QuotaSetUpdateOptions {
    private Integer gigabytes;
    private Integer snapshots;
    private Integer shares;
    private Integer snapshotGigabytes;
    private Integer shareNetworks;
    private Boolean force;

    public static QuotaSetUpdateOptions create() {
        return new QuotaSetUpdateOptions();
    }

    public QuotaSetUpdateOptions force(Boolean force) {
        this.force = force;
        return this;
    }

    public QuotaSetUpdateOptions gigabytes(Integer gigabytes) {
        this.gigabytes = gigabytes;
        return this;
    }

    public QuotaSetUpdateOptions snapshots(Integer snapshots) {
        this.snapshots = snapshots;
        return this;
    }

    public QuotaSetUpdateOptions shares(Integer shares) {
        this.shares = shares;
        return this;
    }

    public QuotaSetUpdateOptions snapshotGigabytes(Integer snapshotGigabytes) {
        this.snapshotGigabytes = snapshotGigabytes;
        return this;
    }

    public QuotaSetUpdateOptions shareNetworks(Integer shareNetworks) {
        this.shareNetworks = shareNetworks;
        return this;
    }

    public Integer getGigabytes() {
        return gigabytes;
    }

    public Integer getSnapshots() {
        return snapshots;
    }

    public Integer getShares() {
        return shares;
    }

    public Integer getSnapshotGigabytes() {
        return snapshotGigabytes;
    }

    public Integer getShareNetworks() {
        return shareNetworks;
    }

    public Boolean getForce() {
        return force;
    }
}
