package org.openstack4j.openstack.placement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.placement.ext.ProviderUsagesBody;
import org.openstack4j.model.placement.ext.ResourceProviderUsages;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProviderUsages;

/**
 * Resource Provider Usages Body contains Resource provider usages
 *
 * @author Jyothi Saroja
 */
public class ResourceProviderUsagesBody implements ProviderUsagesBody {

    public static final long serialVersionUID = 1L;

    @JsonProperty("resource_provider_generation")
    private String generation;

    @JsonProperty("usages")
    private ExtResourceProviderUsages usages;

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
    public ResourceProviderUsages getUsages() {
        return usages;
    }
}
