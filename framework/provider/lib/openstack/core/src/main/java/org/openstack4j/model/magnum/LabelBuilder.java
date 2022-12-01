package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;

public interface LabelBuilder extends Builder<LabelBuilder, Label> {
    /**
     * @see Label#getKey
     */
    LabelBuilder key(String key);

}
