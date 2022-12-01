package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.NetQuota;
import org.openstack4j.model.network.builder.NetQuotaBuilder;

import java.util.List;

/**
 * Networking (Neutron) Quota Extension API
 *
 * @author Jeremy Unruh
 */
public interface NetQuotaService extends RestService {

    /**
     * Lists quotas for tenants who have non-default quota values, and lists, updates, and resets quotas for a tenan
     *
     * @return the list of quotas
     */
    List<? extends NetQuota> get();

    /**
     * Fetches the network quotas for the specified tenant
     *
     * @param tenantId the tenant identifier
     * @return the tenants quota
     */
    NetQuota get(String tenantId);

    /**
     * Updates the network quotas for the current tenant
     *
     * @param netQuota the net quota to update
     * @return the updated network quota
     * @see NetQuotaBuilder
     */
    NetQuota update(NetQuota netQuota);

    /**
     * Updates the network quotas for the specified tenant
     *
     * @param tenantId the tenant identifier
     * @param netQuota the net quota to update
     * @return the updated network quota
     * @see NetQuotaBuilder
     */
    NetQuota updateForTenant(String tenantId, NetQuota netQuota);

    /**
     * Resets the current network quota for the current tenant back to defaults
     *
     * @return the action response
     */
    ActionResponse reset();

    /**
     * Resets the current network quota for the current tenant back to defaults
     *
     * @param netQuota the net quota to update
     * @return the action response
     */
    ActionResponse reset(String tenantId);
}
