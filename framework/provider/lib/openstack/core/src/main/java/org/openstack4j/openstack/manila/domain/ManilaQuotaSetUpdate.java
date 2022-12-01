package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.QuotaSetUpdateOptions;

/**
 * Object used to update quotas.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("quota_set")
public class ManilaQuotaSetUpdate implements ModelEntity {
    private Integer gigabytes;
    private Integer snapshots;
    private Integer shares;
    @JsonProperty("snapshot_gigabytes")
    private Integer snapshotGigabytes;
    @JsonProperty("share_networks")
    private Integer shareNetworks;
    private Boolean force;

    private ManilaQuotaSetUpdate() {
    }

    /**
     * Creates a quota set update from options.
     *
     * @param options the quota set update options
     * @return a quota set update with the given options
     */
    public static ManilaQuotaSetUpdate fromOptions(QuotaSetUpdateOptions options) {
        ManilaQuotaSetUpdate quotaSetUpdate = new ManilaQuotaSetUpdate();
        quotaSetUpdate.gigabytes = options.getGigabytes();
        quotaSetUpdate.snapshots = options.getSnapshots();
        quotaSetUpdate.shares = options.getShares();
        quotaSetUpdate.snapshotGigabytes = options.getSnapshotGigabytes();
        quotaSetUpdate.shareNetworks = options.getShareNetworks();
        quotaSetUpdate.force = options.getForce();

        return quotaSetUpdate;
    }
}
