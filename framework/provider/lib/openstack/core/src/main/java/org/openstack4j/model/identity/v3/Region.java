package org.openstack4j.model.identity.v3;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v3.builder.RegionBuilder;

public interface Region extends ModelEntity, Buildable<RegionBuilder> {

    /**
     * @return the user-defined id of the region
     */
    String getId();

    /**
     * @return the description of the region
     */
    String getDescription();

    /**
     * @return the id of the parent region
     */
    String getParentRegionId();

}
