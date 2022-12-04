package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;

public interface Label extends ModelEntity, Buildable<LabelBuilder> {
    /**
     * Gets key
     *
     * @return key
     */
    String getKey();

}
