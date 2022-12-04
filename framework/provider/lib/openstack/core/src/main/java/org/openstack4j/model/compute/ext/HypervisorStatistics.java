package org.openstack4j.model.compute.ext;

import org.openstack4j.model.ModelEntity;

/**
 * Represents a Hypervisor Statistics Entity used within the OSHypervisor extensions API
 *
 * @author Jeremy Unruh
 */
public interface HypervisorStatistics extends ModelEntity {

    /**
     * Gets the count.
     *
     * @return the count
     */
    int getCount();

    /**
     * Gets the current workload.
     *
     * @return the current workload
     */
    int getCurrentWorkload();

    /**
     * Gets the least available disk.
     *
     * @return the least available disk
     */
    int getLeastAvailableDisk();

    /**
     * Gets the free disk.
     *
     * @return the free disk
     */
    int getFreeDisk();

    /**
     * Gets the free ram.
     *
     * @return the free ram
     */
    int getFreeRam();

    /**
     * Gets the local.
     *
     * @return the local
     */
    int getLocal();

    /**
     * Gets the local used.
     *
     * @return the local used
     */
    int getLocalUsed();

    /**
     * Gets the memory.
     *
     * @return the memory
     */
    int getMemory();

    /**
     * Gets the memory used.
     *
     * @return the memory used
     */
    int getMemoryUsed();

    /**
     * Gets the running vm.
     *
     * @return the running vm
     */
    int getRunningVM();

    /**
     * Gets the virtual cpu.
     *
     * @return the virtual cpu
     */
    int getVirtualCPU();

    /**
     * Gets the virtual used cpu.
     *
     * @return the virtual used cpu
     */
    int getVirtualUsedCPU();
}
