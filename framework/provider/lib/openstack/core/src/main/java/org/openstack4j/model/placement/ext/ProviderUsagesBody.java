package org.openstack4j.model.placement.ext;

/**
 * Provider usages body contains resource provider usages
 *
 * @author Jyothi Saroja
 */
public interface ProviderUsagesBody {

    /**
     * @return the resource provider generation contained in the unnamed body
     */
    String getGeneration();

    /**
     * @return the resource provider usages contained in the unnamed body
     */
    ResourceProviderUsages getUsages();
}
