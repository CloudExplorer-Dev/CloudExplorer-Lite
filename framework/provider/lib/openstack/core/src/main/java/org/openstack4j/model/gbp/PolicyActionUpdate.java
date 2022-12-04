package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.gbp.builder.PolicyActionUpdateBuilder;

/**
 * Policy Action Model Entity
 *
 * @author vinod borole
 */
public interface PolicyActionUpdate extends ModelEntity, Buildable<PolicyActionUpdateBuilder> {

    /**
     * Is Policy Action shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the name
     *
     * @return the name
     */
    String getName();

}
