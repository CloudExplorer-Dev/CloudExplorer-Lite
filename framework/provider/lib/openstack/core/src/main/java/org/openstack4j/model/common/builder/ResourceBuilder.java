package org.openstack4j.model.common.builder;

import org.openstack4j.model.common.Resource;
import org.openstack4j.model.identity.v3.Tenant;

/**
 * Abstract Resource Builder
 *
 * @param <M> the model type
 * @param <T> the builder type
 */
public abstract class ResourceBuilder<M extends Resource, T extends ResourceBuilder<M, T>> extends BasicResourceBuilder<M, T> {

    /**
     * Set the Tenant id.
     *
     * @param tenantId the tenant id
     * @return the builder
     */
    public T tenantId(String tenantId) {
        reference().setTenantId(tenantId);
        return self();
    }

    /**
     * Sets the Tenant.
     *
     * @param tenant the tenant
     * @return the builder
     */
    public T tenant(Tenant tenant) {
        reference().setTenantId(tenant.getId());
        return self();
    }

}
