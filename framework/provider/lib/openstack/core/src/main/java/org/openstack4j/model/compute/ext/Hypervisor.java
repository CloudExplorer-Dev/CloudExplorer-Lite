package org.openstack4j.model.compute.ext;

import org.openstack4j.model.ModelEntity;

import java.util.List;

/**
 * Represents a Hypervisor details Entity used within the OSHypervisor extensions API
 *
 * @author Jeremy Unruh
 */
public interface Hypervisor extends ModelEntity {

    /**
     * @return the unique identifier representing this hypervisor
     */
    String getId();

    /**
     * Gets the current workload.
     *
     * @return the current workload
     */
    int getCurrentWorkload();

    /**
     * Gets the least disk available.
     *
     * @return the least disk available
     */
    int getLeastDiskAvailable();

    /**
     * Gets free disk space in GB
     *
     * @return the free disk in GB
     */
    int getFreeDisk();

    /**
     * Gets the free RAM in MB
     *
     * @return the free RAM in MB
     */
    int getFreeRam();

    /**
     * Gets the hostname of the hypervisor node.
     *
     * @return the DNS hostname of the hypervisor
     */
    String getHypervisorHostname();

    /**
     * Gets the host IP address
     *
     * @return the host IP address
     */
    String getHostIP();

    /**
     * Gets the type of the hypervisor.  For example "QEMU"
     *
     * @return the hypervisor implementation type
     */
    String getType();

    /**
     * Gets the version of the hypervisor
     *
     * @return the version
     */
    int getVersion();

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

    /**
     * Gets the local disk.
     *
     * @return the local disk
     */
    int getLocalDisk();

    /**
     * Gets the local disk used.
     *
     * @return the local disk used
     */
    int getLocalDiskUsed();

    /**
     * Gets the local memory.
     *
     * @return the local memory
     */
    int getLocalMemory();

    /**
     * Gets the local memory used.
     *
     * @return the local memory used
     */
    int getLocalMemoryUsed();

    /**
     * Gets the service.
     *
     * @return the service
     */
    Service getService();


    /**
     * Gets the cpu info.
     *
     * @return CPUInfo
     */
    CPUInfo getCPUInfo();

    /**
     * <br/>Description:Gets the host status
     * <p>Author:Wang Ting/王婷</p>
     *
     * @return the host status
     */
    String getStatus();

    /**
     * Gets the host state
     * <p>Author:Wang Ting/王婷</p>
     *
     * @return the host state
     */
    String getState();

    /**
     * The Hypervisor Services Detail
     */
    public interface Service extends ModelEntity {

        /**
         * Gets the host.
         *
         * @return the host
         */
        String getHost();

        /**
         * Gets the id.
         *
         * @return the id
         */
        String getId();

    }

    public interface CPUInfo extends ModelEntity {

        /**
         * @return cpu vendor
         */
        List<Object> getVendor();

        /**
         * @return cpu model
         */
        List<Object> getModel();

        /**
         * @return chipset architecture
         */
        String getArch();

        /**
         * @return cpu feature set
         */
        List<String> getFeatures();


        /**
         * @return cpu topology
         */
        CPUTopology getTopology();
    }

    public interface CPUTopology extends ModelEntity {

        /**
         * @return core count
         */
        int getCores();


        /**
         * @return thread count
         */
        int getThreads();

        /**
         * @return socket count
         */
        int getSockets();
    }

    int getCpuAllocationRatio();

    int getRamAllocationRatio();
}
