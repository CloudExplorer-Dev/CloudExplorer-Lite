package org.openstack4j.model.common;

import org.openstack4j.model.ModelEntity;

/**
 * Details for a specific Quota with a Quota-Set
 *
 * @author Jeremy Unruh
 */
public interface QuotaDetails extends ModelEntity {

    /**
     * The number of items in use for this tenant
     *
     * @return number of items in use
     */
    int getInUse();

    /**
     * The number of items permitted for this tenant.
     *
     * @return number of items permitted
     */
    int getLimit();

    /**
     * The number of reserved items for this tenant
     *
     * @return the number of reserved items
     */
    int getReserved();
}
