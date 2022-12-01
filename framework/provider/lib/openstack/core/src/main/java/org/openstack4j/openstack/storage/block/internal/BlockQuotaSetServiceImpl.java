package org.openstack4j.openstack.storage.block.internal;

import org.openstack4j.api.storage.BlockQuotaSetService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.BlockQuotaSet;
import org.openstack4j.model.storage.block.BlockQuotaSetUsage;
import org.openstack4j.openstack.storage.block.domain.CinderBlockQuotaSet;
import org.openstack4j.openstack.storage.block.domain.CinderBlockQuotaSetUsage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Quota-Set Extension API for Block Storage
 *
 * @author Jeremy Unruh
 */
public class BlockQuotaSetServiceImpl extends BaseBlockStorageServices implements BlockQuotaSetService {

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockQuotaSet getDefaults(String tenantId) {
        checkNotNull(tenantId, "Tenant cannot be null");
        return get(CinderBlockQuotaSet.class, uri("/os-quota-sets/%s/defaults", tenantId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockQuotaSet get(String tenantId) {
        checkNotNull(tenantId, "Tenant cannot be null");
        return get(CinderBlockQuotaSet.class, uri("/os-quota-sets/%s", tenantId)).param("usage", false).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockQuotaSet updateForTenant(String tenantId, BlockQuotaSet quota) {
        checkNotNull(tenantId, "Tenant cannot be null");
        checkNotNull(quota, "Quota cannot be null");
        return put(CinderBlockQuotaSet.class, uri("/os-quota-sets/%s", tenantId)).entity(quota).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String tenantId) {
        checkNotNull(tenantId, "Tenant cannot be null");
        return delete(ActionResponse.class, uri("/os-quota-sets/%s", tenantId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockQuotaSetUsage usageForTenant(String tenantId) {
        checkNotNull(tenantId, "Tenant cannot be null");
        return get(CinderBlockQuotaSetUsage.class, uri("/os-quota-sets/%s", tenantId)).param("usage", true).execute();
    }

    @Override
    public BlockQuotaSetUsage usageForUser(String tenantId, String userId) {
        checkNotNull(tenantId, "Tenant cannot be null");
        checkNotNull(userId, "User cannot be null");
        return get(CinderBlockQuotaSetUsage.class, uri("/os-quota-sets/%s", tenantId))
                .param("user_id", userId)
                .param("usage", true)
                .execute();
    }

}
