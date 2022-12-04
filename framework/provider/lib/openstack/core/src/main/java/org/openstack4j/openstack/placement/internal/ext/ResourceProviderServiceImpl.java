package org.openstack4j.openstack.placement.internal.ext;

import org.openstack4j.api.placement.ext.ResourceProviderService;
import org.openstack4j.model.placement.ext.ResourceProvider;
import org.openstack4j.model.placement.ext.ResourceProviderInventories;
import org.openstack4j.model.placement.ext.ResourceProviderUsages;
import org.openstack4j.openstack.placement.domain.ResourceProviderInventoriesBody;
import org.openstack4j.openstack.placement.domain.ResourceProviderUsagesBody;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProvider;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProvider.ResourceProviders;
import org.openstack4j.openstack.placement.internal.BasePlacementServices;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation for the Resource providers.
 *
 * @author Jyothi Saroja
 */
public class ResourceProviderServiceImpl extends BasePlacementServices implements ResourceProviderService {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceProvider get(String resourceProviderId) {
        checkNotNull(resourceProviderId);
        return get(ExtResourceProvider.class, uri("/resource_providers/%s", resourceProviderId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ResourceProvider> list() {
        return get(ResourceProviders.class, "/resource_providers").execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceProviderInventories resourceProviderInventories(String resourceProviderId) {
        checkNotNull(resourceProviderId);
        return get(ResourceProviderInventoriesBody.class, uri("/resource_providers/%s/inventories", resourceProviderId))
                .execute().getInventories();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceProviderUsages resourceProviderUsages(String resourceProviderId) {
        checkNotNull(resourceProviderId);
        return get(ResourceProviderUsagesBody.class, uri("/resource_providers/%s/usages", resourceProviderId)).execute()
                .getUsages();
    }
}
