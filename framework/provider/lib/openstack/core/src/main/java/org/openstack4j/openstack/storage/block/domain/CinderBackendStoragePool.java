package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Represents a back-end storage pool for Cinder.
 *
 * @author chenguofeng
 */
public class CinderBackendStoragePool implements VolumeBackendPool {
    private String name;
    private CinderCapabilities capabilities;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Capabilities getCapabilities() {
        return capabilities;
    }

    public static class CinderCapabilities implements Capabilities {
        @JsonProperty("pool_name")
        private String poolname;
        @JsonProperty("filter_function")
        private String filterfunction;
        @JsonProperty("goodness_function")
        private String goodnessfunction;
        @JsonProperty("total_volumes")
        private Integer totalvolumes;
        @JsonProperty("multiattach")
        private Boolean multiattach;
        @JsonProperty("provisioned_capacity_gb")
        private Long provisionedcapacitygb;
        @JsonProperty("timestamp")
        private String timestamp;
        @JsonProperty("allocated_capacity_gb")
        private Integer allocatedcapacitygb;
        @JsonProperty("volume_backend_name")
        private String volumeBackendName;
        @JsonProperty("thin_provisioning_support")
        private Boolean thinprovisioningsupport;
        @JsonProperty("free_capacity_gb")
        private Long freeCapacityGb;
        @JsonProperty("driver_version")
        private String driverVersion;
        @JsonProperty("location_info")
        private String locationinfo;
        @JsonProperty("total_capacity_gb")
        private Long totalCapacityGb;
        @JsonProperty("thick_provisioning_support")
        private Boolean thickprovisioningsupport;
        @JsonProperty("reserved_percentage")
        private Integer reservedPercentage;
        @JsonProperty("QoS_support")
        private Boolean qosSupport;
        @JsonProperty("max_over_subscription_ratio")
        private String maxoversubscription_ratio;
        @JsonProperty("vendor_name")
        private String vendorname;
        private String pools;
        @JsonProperty("storage_protocol")
        private String storageProtocol;


        @Override
        public String getPoolname() {
            return poolname;
        }

        @Override
        public String getGoodnessfunction() {
            return goodnessfunction;
        }

        @Override
        public Integer getTotalvolumes() {
            return totalvolumes;
        }

        @Override
        public Boolean getMultiattach() {
            return multiattach;
        }

        @Override
        public Long getProvisionedcapacitygb() {
            return provisionedcapacitygb;
        }

        @Override
        public String getTimestamp() {
            return timestamp;
        }

        @Override
        public Integer getAllocatedcapacitygb() {
            return allocatedcapacitygb;
        }

        @Override
        public Boolean getThinprovisioningsupport() {
            return thinprovisioningsupport;
        }

        @Override
        public String getLocationinfo() {
            return locationinfo;
        }

        @Override
        public Boolean getThickprovisioningsupport() {
            return thickprovisioningsupport;
        }

        @Override
        public String getMaxoversubscription_ratio() {
            return maxoversubscription_ratio;
        }

        @Override
        public String getvendorname() {
            return vendorname;
        }

        @Override
        public String getFilterfunction() {
            return filterfunction;
        }

        @Override
        public Boolean getQosSupport() {
            return qosSupport;
        }

        @Override
        public String getVolumeBackendName() {
            return volumeBackendName;
        }

        @Override
        public String getDriverVersion() {
            return driverVersion;
        }

        @Override
        public Long getTotalCapacityGb() {
            return totalCapacityGb;
        }

        @Override
        public Long getFreeCapacityGb() {
            return freeCapacityGb;
        }

        @Override
        public Integer getReservedPercentage() {
            return reservedPercentage;
        }

        @Override
        public String getStorageProtocol() {
            return storageProtocol;
        }
    }

    public static class VolumeBackendPools extends ListResult<CinderBackendStoragePool> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("pools")
        private List<CinderBackendStoragePool> pools;

        @Override
        protected List<CinderBackendStoragePool> value() {
            return pools;
        }
    }
}
