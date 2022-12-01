package org.openstack4j.model.placement.ext;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProviderInventories.DiskGb;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProviderInventories.MemoryMb;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProviderInventories.Pcpu;
import org.openstack4j.openstack.placement.domain.ext.ExtResourceProviderInventories.Vcpu;

/**
 * Represents a Resource Provider Inventories Entity used within the Resource Provider extensions API
 *
 * @author Jyothi Saroja
 */
public interface ResourceProviderInventories extends ModelEntity {

    /**
     * @return the disk inventory details of a resource provider
     */
    DiskGb getDiskGb();

    /**
     * @return the memory inventory details of a resource provider
     */
    MemoryMb getMemoryMb();

    /**
     * @return the pcpu inventory details of a resource provider
     */
    Pcpu getPcpu();

    /**
     * @return the vcpu inventory details of a resource provider
     */
    Vcpu getVcpu();
}
