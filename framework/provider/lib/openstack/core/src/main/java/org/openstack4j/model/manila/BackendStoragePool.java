package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

import java.util.List;
import java.util.Map;

/**
 * Represents a back-end storage pool.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface BackendStoragePool extends ModelEntity {
    /**
     * @return the name of the back end
     */
    String getBackend();

    /**
     * @return the host name for the back end
     */
    String getHost();

    /**
     * @return the pool name for the back end
     */
    String getPool();

    /**
     * @return the name of the back end in this format: <code>host@backend#POOL</code>
     */
    String getName();

    /**
     * @return the capabilities for the storage back end
     */
    Capabilities getCapabilities();

    enum ConsistencyGroupSupport {
        POOL, HOST, FALSE;

        @JsonCreator
        public static ConsistencyGroupSupport value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    interface Capabilities {
        /**
         * @return the quality of service (QoS) support
         */
        Boolean getQosSupport();

        /**
         * @return the consistency group support
         */
        ConsistencyGroupSupport getConsistencyGroupSupport();

        /**
         * @return the date and time stamp when the API request was issued
         */
        String getTimestamp();

        /**
         * @return the name of the share back end
         */
        String getShareBackendName();

        /**
         * @return the mapping between servers and pools
         */
        Map<String, List<String>> getServerPoolsMapping();

        /**
         * @return an extra specification that defines the driver mode for share server,
         * or storage, life cycle management
         */
        Boolean getDriverHandlesShareServers();

        /**
         * @return the driver version
         */
        String getDriverVersion();

        /**
         * @return the total capacity for the back end, in GBs, or 'unkown'
         */
        String getTotalCapacityGb();

        /**
         * @return the amount of free capacity for the back end, in GBs, or 'unknown'
         */
        String getFreeCapacityGb();

        /**
         * @return the percentage of the total capacity that is reserved for the internal use by the back end
         */
        Integer getReservedPercentage();

        /**
         * @return the pools for the back end
         */
        String getPools();

        /**
         * @return the name of the vendor for the back end
         */
        String getVendorName();

        /**
         * @return the specification that filters back ends by whether they do or do not support share snapshots
         */
        Boolean getSnapshotSupport();

        /**
         * @return the storage protocol for the back end
         */
        String getStorageProtocol();
    }
}
