package org.openstack4j.model.storage.block;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.storage.block.builder.BlockQuotaSetBuilder;

import java.util.Map;

/**
 * An OpenStack Quota-Set
 *
 * @author Jeremy Unruh
 */
public interface BlockQuotaSet extends ModelEntity, Buildable<BlockQuotaSetBuilder> {

    /**
     * @return the identifier
     */
    String getId();

    /**
     * @return the Snapshots.
     */
    int getSnapshots();

    /**
     * @return the Volumes
     */
    int getVolumes();

    /**
     * @return the gigabytes
     */
    int getGigabytes();

    Map<String, Integer> getVolumeTypesQuotas();
}
