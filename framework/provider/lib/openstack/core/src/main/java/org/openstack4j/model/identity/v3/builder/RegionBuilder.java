package org.openstack4j.model.identity.v3.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v3.Region;

public interface RegionBuilder extends Builder<RegionBuilder, Region> {

    /**
     * @see Region#getId()
     */
    RegionBuilder id(String id);

    /**
     * @see Region#getDescription()
     */
    RegionBuilder description(String description);

    /**
     * @see Region#getParentRegionId()
     */
    RegionBuilder parentRegionId(String parentRegionId);

}
