package org.openstack4j.model.storage.block.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.storage.block.BlockQuotaSet;

import java.util.Map;

/**
 * Builder for a QuotaSet model class.
 *
 * @author Jeremy Unruh
 */
public interface BlockQuotaSetBuilder extends Builder<BlockQuotaSetBuilder, BlockQuotaSet> {

    /**
     * Volumes Quota for Block Storage
     *
     * @return volumes consumed in the Block Storage.
     */
    BlockQuotaSetBuilder volumes(int volumes);

    /**
     * Snapshots present in Block Storage
     *
     * @return snapshots present in the Block Storage.
     */
    BlockQuotaSetBuilder snapshots(int snapshots);

    /**
     * Space consumed in gigabytes for Block Storage
     *
     * @return space consumed in the Block Storage.
     */
    BlockQuotaSetBuilder gigabytes(int gigabytes);

    /**
     * Quotas limits for each volume type
     *
     * @return volume types quota limits configured in the Block Storage.
     */
    BlockQuotaSetBuilder volumeTypesQuotas(Map<String, Integer> volumeTypesQuotas);
}
