package org.openstack4j.api.identity.v2;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.TenantUser;

import java.util.List;

/**
 * Identity Tenant based Operations
 *
 * @author Jeremy Unruh
 */
public interface TenantService extends RestService {

    /**
     * Lists tenants to which the specified token has access.
     *
     * @return List of Tenants
     */
    List<? extends Tenant> list();

    /**
     * Gets detailed information about a specified tenant by ID
     *
     * @param tenantId the tenant id
     * @return the tenant
     */
    Tenant get(String tenantId);

    /**
     * Gets detailed information about a specified tenant by name
     *
     * @param tenantName the tenant name
     * @return the tenant
     */
    Tenant getByName(String tenantName);

    /**
     * Creates a new Tenant
     *
     * @param tenant the tenant to create
     * @return the newly created tenant and it's assigned ID
     */
    Tenant create(Tenant tenant);

    /**
     * Deletes the specified tenant by ID
     *
     * @param tenantId the tenant id
     * @return the action response
     */
    ActionResponse delete(String tenantId);

    /**
     * Updates the tenant (ID must be set within the inbound tenant)
     *
     * @param tenant the tenant
     * @return the tenant
     */
    Tenant update(Tenant tenant);

    /**
     * Returns a list of users associated by the {@code tenantId}
     *
     * @param tenantId the tenant id to query users for
     * @return List of TenantUser
     */
    List<? extends TenantUser> listUsers(String tenantId);

}
