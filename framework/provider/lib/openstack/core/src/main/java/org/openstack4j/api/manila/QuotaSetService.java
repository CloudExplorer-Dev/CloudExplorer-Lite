package org.openstack4j.api.manila;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.QuotaSet;
import org.openstack4j.model.manila.QuotaSetUpdateOptions;

/**
 * Quota Set Service for Manila Shared Filesystems.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface QuotaSetService extends RestService {
    /**
     * Shows quotas for a tenant.
     *
     * @param tenantId the UUID for the tenant for which you want to show quotas
     * @return the quota set
     */
    QuotaSet get(String tenantId);

    /**
     * Shows quotas for a user in a tenant.
     *
     * @param tenantId the UUID for the tenant for which you want to show quotas
     * @param userId   the UUID of the user
     * @return the quota set
     */
    QuotaSet get(String tenantId, String userId);

    /**
     * Updates quotas for a tenant.
     *
     * @param tenantId the UUID for the tenant for which you want to update quotas
     * @param options  the options to update
     * @return the updated quota set
     */
    QuotaSet update(String tenantId, QuotaSetUpdateOptions options);

    /**
     * Updates quotas for a user in a tenant.
     *
     * @param tenantId the UUID for the tenant for which you want to update quotas
     * @param userId   the UUID of the user
     * @param options  the options to update
     * @return the updated quota set
     */
    QuotaSet update(String tenantId, String userId, QuotaSetUpdateOptions options);

    /**
     * Deletes quotas for a tenant. The quota reverts to the default quota.
     *
     * @param tenantId the UUID for the tenant for which you want to delete quotas
     * @return the action response
     */
    ActionResponse delete(String tenantId);

    /**
     * Deletes quotas for a user in a tenant. The quota reverts to the default quota.
     *
     * @param tenantId the UUID for the tenant for which you want to delete quotas
     * @param userId   the UUID of the user
     * @return the action response
     */
    ActionResponse delete(String tenantId, String userId);

    /**
     * Shows default quotas for a tenant.
     *
     * @param tenantId the UUID for the tenant for which you want to show default quotas
     * @return the default quota set
     */
    QuotaSet getDefault(String tenantId);
}
