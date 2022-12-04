package org.openstack4j.model.storage.block;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.QuotaDetails;

/**
 * Block Quota-Set Usage Details
 *
 * @author Jeremy Unruh
 */
public interface BlockQuotaSetUsage extends ModelEntity {

    /**
     * Usage details for Snapshots
     *
     * @return usage for snapshots
     */
    QuotaDetails getSnapshots();

    /**
     * Usage details for Volumes
     *
     * @return usage for volumes
     */
    QuotaDetails getVolumes();

    /**
     * Usage details for gigabytes used
     *
     * @return usage for gigabytes
     */
    QuotaDetails getGigabytes();

}
