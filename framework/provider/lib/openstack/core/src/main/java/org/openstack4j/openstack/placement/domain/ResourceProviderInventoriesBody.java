package org.openstack4j.openstack.placement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.placement.ext.ProviderInventoriesBody;
import org.openstack4j.model.placement.ext.ResourceProviderInventories;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProviderInventories;

/**
 * Resource Provider Inventories Body contains Resource provider inventories
 *
 * @author Jyothi Saroja
 */
public class ResourceProviderInventoriesBody implements ProviderInventoriesBody {

    public static final long serialVersionUID = 1L;

    @JsonProperty("resource_provider_generation")
    private String generation;

    @JsonProperty("inventories")
    private ExtResourceProviderInventories inventories;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGeneration() {
        return generation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceProviderInventories getInventories() {
        return inventories;
    }
}
