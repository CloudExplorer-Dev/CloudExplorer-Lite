package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.QuotaSet;

/**
 * Set of quotas for shares.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("quota_set")
public class ManilaQuotaSet implements QuotaSet {
    private String id;
    private Integer gigabytes;
    private Integer snapshots;
    private Integer shares;
    @JsonProperty("snapshot_gigabytes")
    private Integer snapshotGigabytes;
    @JsonProperty("share_networks")
    private Integer shareNetworks;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Integer getGigabytes() {
        return gigabytes;
    }

    @Override
    public Integer getSnapshots() {
        return snapshots;
    }

    @Override
    public Integer getShares() {
        return shares;
    }

    @Override
    public Integer getSnapshotGigabytes() {
        return snapshotGigabytes;
    }

    @Override
    public Integer getShareNetworks() {
        return shareNetworks;
    }
}
