package org.openstack4j.model.placement.ext;

import org.openstack4j.model.ModelEntity;

/**
 * Represents a Resource Provider Usages Entity used within the Resource Provider extensions API
 *
 * @author Jyothi Saroja
 */
public interface ResourceProviderUsages extends ModelEntity {

    /**
     * @return the disk usage of a resource provider
     */
    int getDiskGb();

    /**
     * @return the memory usage of a resource provider
     */
    int getMemoryMb();

    /**
     * @return the pcpu usage of a resource provider
     */
    int getPcpu();

    /**
     * @return the vcpu usage of a resource provider
     */
    int getVcpu();
}
