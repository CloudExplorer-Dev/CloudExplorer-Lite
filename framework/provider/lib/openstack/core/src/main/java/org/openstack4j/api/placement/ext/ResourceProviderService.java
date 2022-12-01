package org.openstack4j.api.placement.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.placement.ext.ResourceProvider;
import org.openstack4j.model.placement.ext.ResourceProviderInventories;
import org.openstack4j.model.placement.ext.ResourceProviderUsages;

import java.util.List;

/**
 * API which supports the "resource_providers" extension.
 *
 * @author Jyothi Saroja
 */
public interface ResourceProviderService extends RestService {

    /**
     * Get the specified resource provider by ID
     *
     * @param resourceProviderId -  the resource provider identifier
     * @return the ResourceProvider or null if not found
     */
    ResourceProvider get(String resourceProviderId);

    /**
     * The resource providers for this OpenStack deployment.
     * <p>
     * NOTE: This is an extension and not all deployments support resource_providers
     *
     * @return the available resource providers in detail
     */
    List<? extends ResourceProvider> list();

    /**
     * The cpu, memory and disk inventories of a specific resource provider
     *
     * @param resourceProviderId - the id of resource provider
     * @return - instance of ResourceProviderInventories
     */
    ResourceProviderInventories resourceProviderInventories(String resourceProviderId);

    /**
     * The cpu, memory and disk usages of a specific resource provider
     *
     * @param resourceProviderId - the id of resource provider
     * @return - instance of ResourceProviderUsages
     */
    ResourceProviderUsages resourceProviderUsages(String resourceProviderId);
}
