package org.openstack4j.model.manila;

import org.openstack4j.model.ModelEntity;

/**
 * Set of quotas for shares.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface QuotaSet extends ModelEntity {
    /**
     * @return the UUID of the tenant for which you manage quotas
     */
    String getId();

    /**
     * @return the number of gigabytes allowed for each tenant
     */
    Integer getGigabytes();

    /**
     * @return the number of snapshots allowed for each tenant
     */
    Integer getSnapshots();

    /**
     * @return the number of shares allowed for each tenant
     */
    Integer getShares();

    /**
     * @return the number of gigabytes for the snapshots allowed for each tenant
     */
    Integer getSnapshotGigabytes();

    /**
     * @return the number of share networks allowed for each tenant
     */
    Integer getShareNetworks();
}
