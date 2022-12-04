package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.manila.BackendStoragePool;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * Represents a back-end storage pool.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ManilaBackendStoragePool implements BackendStoragePool {
    private String backend;
    private String host;
    private String pool;
    private String name;
    private ManilaCapabilities capabilities;

    @Override
    public String getBackend() {
        return backend;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPool() {
        return pool;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Capabilities getCapabilities() {
        return capabilities;
    }

    public static class ManilaCapabilities implements Capabilities {
        @JsonProperty("QoS_support")
        private Boolean qosSupport;
        @JsonProperty("consistency_group_support")
        private ConsistencyGroupSupport consistencyGroupSupport;
        private String timestamp;
        @JsonProperty("share_backend_name")
        private String shareBackendName;
        @JsonProperty("server_pools_mapping")
        private Map<String, List<String>> serverPoolsMapping;
        @JsonProperty("driver_handles_share_servers")
        private Boolean driverHandlesShareServers;
        @JsonProperty("driver_version")
        private String driverVersion;
        @JsonProperty("total_capacity_gb")
        private String totalCapacityGb;
        @JsonProperty("free_capacity_gb")
        private String freeCapacityGb;
        @JsonProperty("reserved_percentage")
        private Integer reservedPercentage;
        private String pools;
        @JsonProperty("vendor_name")
        private String vendorName;
        @JsonProperty("snapshot_support")
        private Boolean snapshotSupport;
        @JsonProperty("storage_protocol")
        private String storageProtocol;

        @Override
        public Boolean getQosSupport() {
            return qosSupport;
        }

        @Override
        public ConsistencyGroupSupport getConsistencyGroupSupport() {
            return consistencyGroupSupport;
        }

        @Override
        public String getTimestamp() {
            return timestamp;
        }

        @Override
        public String getShareBackendName() {
            return shareBackendName;
        }

        @Override
        public Map<String, List<String>> getServerPoolsMapping() {
            return serverPoolsMapping;
        }

        @Override
        public Boolean getDriverHandlesShareServers() {
            return driverHandlesShareServers;
        }

        @Override
        public String getDriverVersion() {
            return driverVersion;
        }

        @Override
        public String getTotalCapacityGb() {
            return totalCapacityGb;
        }

        @Override
        public String getFreeCapacityGb() {
            return freeCapacityGb;
        }

        @Override
        public Integer getReservedPercentage() {
            return reservedPercentage;
        }

        @Override
        public String getPools() {
            return pools;
        }

        @Override
        public String getVendorName() {
            return vendorName;
        }

        @Override
        public Boolean getSnapshotSupport() {
            return snapshotSupport;
        }

        @Override
        public String getStorageProtocol() {
            return storageProtocol;
        }
    }

    public static class BackendStoragePools extends ListResult<ManilaBackendStoragePool> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("pools")
        private List<ManilaBackendStoragePool> pools;

        @Override
        protected List<ManilaBackendStoragePool> value() {
            return pools;
        }
    }
}
