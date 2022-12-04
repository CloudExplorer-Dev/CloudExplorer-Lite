package org.openstack4j.openstack.compute.domain.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.core.transport.ObjectMapperSingleton;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.openstack.common.ListResult;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExtHypervisor implements Hypervisor {

    private static final long serialVersionUID = 1L;

    private String id;

    @JsonProperty("current_workload")
    private int currentWorkload;
    @JsonProperty("disk_available_least")
    private int leastDiskAvailable;
    @JsonProperty("free_disk_gb")
    private int freeDisk;
    @JsonProperty("free_ram_mb")
    private int freeRam;
    @JsonProperty("hypervisor_type")
    private String type;
    @JsonProperty("hypervisor_hostname")
    private String hypervisorHostname;
    @JsonProperty("host_ip")
    private String hostIP;
    private int version;
    @JsonProperty("running_vms")
    private int runningVM;
    @JsonProperty("vcpus")
    private int virtualCPU;
    @JsonProperty("vcpus_used")
    private int virtualUsedCPU;
    @JsonProperty("local_gb")
    private int localDisk;
    @JsonProperty("local_gb_used")
    private int localDiskUsed;
    @JsonProperty("memory_mb")
    private int localMemory;
    @JsonProperty("memory_mb_used")
    private int localMemoryUsed;
    @JsonProperty("service")
    private HypervisorService service;

    @JsonProperty("cpu_info")
    private HypervisorCPUInfo cpuInfo;
    /**
     * The status of the hypervisor. One of enabled or disabled.
     * Author:Wang Ting/王婷
     */
    private String status;

    /**
     * The state of the hypervisor. One of up or down
     * Author:Wang Ting/王婷
     */
    private String state;

    @JsonProperty("cpu_allocation_ratio")
    private int cpuAllocationRatio;
    @JsonProperty("ram_allocation_ratio")
    private int ramAllocationRatio;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getCurrentWorkload() {
        return currentWorkload;
    }

    @Override
    public int getLeastDiskAvailable() {
        return leastDiskAvailable;
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
    public String getHypervisorHostname() {
        return hypervisorHostname;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public int getRunningVM() {
        return runningVM;
    }

    @Override
    public int getVirtualCPU() {
        return virtualCPU;
    }

    @Override
    public int getVirtualUsedCPU() {
        return virtualUsedCPU;
    }

    @Override
    public int getLocalDisk() {
        return localDisk;
    }

    @Override
    public int getLocalDiskUsed() {
        return localDiskUsed;
    }

    @Override
    public int getLocalMemory() {
        return localMemory;
    }

    @Override
    public int getLocalMemoryUsed() {
        return localMemoryUsed;
    }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public String getHostIP() {
        return hostIP;
    }


    @Override
    public CPUInfo getCPUInfo() {
        return cpuInfo;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public int getCpuAllocationRatio() {
        return cpuAllocationRatio;
    }

    @Override
    public int getRamAllocationRatio() {
        return ramAllocationRatio;
    }

    public void setCpuAllocationRatio(int cpuAllocationRatio) {
        this.cpuAllocationRatio = cpuAllocationRatio;
    }

    public void setRamAllocationRatio(int ramAllocationRatio) {
        this.ramAllocationRatio = ramAllocationRatio;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("hypervisor_hostname", hypervisorHostname).add("version", version).add("type", type)
                .add("host_ip", hostIP).add("running", runningVM).add("freeDisk", freeDisk).add("freeRam", freeRam)
                .add("vcpus", virtualCPU).add("usedVcpu", virtualUsedCPU).add("localDisk", localDisk).add("localDiskUsed", localDiskUsed)
                .add("localMemory", localMemory).add("localMemoryUsed", localMemoryUsed).add("currentWorkload", currentWorkload)
                .add("leastDiskAvail", leastDiskAvailable).add("running_vms", runningVM).add("service", service)
                .add("cpuInfo", cpuInfo)
                .add("status", status)
                .add("state", state)
                .toString();
    }

    @JsonRootName("service")
    static class HypervisorService implements Service {

        private static final long serialVersionUID = 1L;
        private String host;
        private String id;

        @Override
        public String getHost() {
            return host;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("host", host).toString();
        }
    }

    public static class Hypervisors extends ListResult<ExtHypervisor> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("hypervisors")
        List<ExtHypervisor> hypervisors;

        @Override
        protected List<ExtHypervisor> value() {
            return hypervisors;
        }

    }

    static class HypervisorCPUInfo implements CPUInfo {

        private static final long serialVersionUID = 1L;

        private List<Object> vendor;
        private List<Object> model;
        private String arch;
        private List<String> features;
        private HypervisorCPUTopology topology;

        @JsonCreator
        public static HypervisorCPUInfo value(String json) {
            if (json != null && json.length() > 0) {
                try {
                    return ObjectMapperSingleton.getContext(HypervisorCPUInfo.class)
                            .reader(HypervisorCPUInfo.class).readValue(json);
                } catch (Exception e) {
                    LoggerFactory.getLogger(HypervisorCPUInfo.class).error(e.getMessage(), e);
                }
            }
            return null;
        }

        @Override
        public List<Object> getVendor() {
            return vendor;
        }

        @Override
        public List<Object> getModel() {
            return model;
        }

        @Override
        public String getArch() {
            return arch;
        }

        @Override
        public List<String> getFeatures() {
            return features;
        }

        @Override
        public CPUTopology getTopology() {
            return topology;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("vendor", vendor)
                    .add("model", model)
                    .add("arch", arch)
                    .add("features", features)
                    .add("topology", topology)
                    .toString();
        }
    }

    @JsonRootName("topology")
    static class HypervisorCPUTopology implements CPUTopology {

        private static final long serialVersionUID = 1L;

        int cores;
        int threads;
        int sockets;

        @Override
        public int getCores() {
            return cores;
        }

        @Override
        public int getThreads() {
            return threads;
        }

        @Override
        public int getSockets() {
            return sockets;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("cores", cores)
                    .add("threads", threads)
                    .add("sockets", sockets)
                    .toString();
        }
    }
}
