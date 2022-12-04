package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.common.QuotaDetails;
import org.openstack4j.model.storage.block.BlockQuotaSetUsage;
import org.openstack4j.openstack.common.QuotaDetailsEntity;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Block Quota-Set Usage Details
 *
 * @author Jeremy Unruh
 */
@JsonRootName("quota_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CinderBlockQuotaSetUsage implements BlockQuotaSetUsage {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private QuotaDetailsEntity snapshots;
    @JsonProperty
    private QuotaDetailsEntity volumes;
    @JsonProperty
    private QuotaDetailsEntity gigabytes;


    @Override
    public QuotaDetails getSnapshots() {
        return snapshots;
    }

    @Override
    public QuotaDetails getVolumes() {
        return volumes;
    }

    @Override
    public QuotaDetails getGigabytes() {
        return gigabytes;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("snapshots", snapshots).add("volumes", volumes).add("gigabytes", gigabytes).toString();
    }

}
