package org.openstack4j.openstack.compute.domain.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.ext.HypervisorStatistics;

@JsonRootName("hypervisor_statistics")
public class ExtHypervisorStatistics implements HypervisorStatistics {

    private static final long serialVersionUID = 1L;

    @JsonProperty("count")
    int count;
    @JsonProperty("current_workload")
    int currentWorkload;
    @JsonProperty("disk_available_least")
    int leastDiskAvail;
    @JsonProperty("free_disk_gb")
    int freeDisk;
    @JsonProperty("free_ram_mb")
    int freeRam;
    @JsonProperty("local_gb")
    int local;
    @JsonProperty("local_gb_used")
    int localUsed;
    @JsonProperty("memory_mb")
    int memory;
    @JsonProperty("memory_mb_used")
    int memoryUsed;
    @JsonProperty("running_vms")
    int running;
    @JsonProperty("vcpus")
    int vcpus;
    @JsonProperty("vcpus_used")
    int vcpusUsed;

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getCurrentWorkload() {
        return currentWorkload;
    }

    @Override
    public int getLeastAvailableDisk() {
        return leastDiskAvail;
    }

    @Override
    public int getFreeDisk() {
        return freeDisk;
    }

    @Override
    public int getFreeRam() {
        return freeRam;
    }

    @Override
    public int getLocal() {
        return local;
    }

    @Override
    public int getLocalUsed() {
        return localUsed;
    }

    @Override
    public int getMemory() {
        return memory;
    }

    @Override
    public int getMemoryUsed() {
        return memoryUsed;
    }

    @Override
    public int getRunningVM() {
        return running;
    }

    @Override
    public int getVirtualCPU() {
        return vcpus;
    }

    @Override
    public int getVirtualUsedCPU() {
        return vcpusUsed;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(HypervisorStatistics.class)
                .add("count", count).add("current_workload", currentWorkload).add("least_disk_avail", leastDiskAvail)
                .add("freeRam", freeRam).add("freeDisk", freeDisk).add("local", local).add("local_used", localUsed)
                .add("memory", memory).add("memory_used", memoryUsed).add("running_vms", running).add("vcspus", vcpus)
                .add("vcpus_used", vcpusUsed)
                .toString();
    }

}
