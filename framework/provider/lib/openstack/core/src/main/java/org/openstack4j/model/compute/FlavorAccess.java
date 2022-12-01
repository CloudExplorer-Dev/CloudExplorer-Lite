package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

/**
 * An OpenStack Flavor Access for tenants
 *
 * @author Moodpo
 */
public interface FlavorAccess extends ModelEntity {

    /**
     * @return the id for this flavor
     */
    String getFlavorId();

    /**
     * @return the id for this tenant
     */
    String getTenantId();

}
