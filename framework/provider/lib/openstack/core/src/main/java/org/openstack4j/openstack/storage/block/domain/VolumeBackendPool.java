package org.openstack4j.openstack.storage.block.domain;

import org.openstack4j.model.ModelEntity;


/**
 * Represents a back-end storage pool of Cinder Volume.
 *
 * @author chenguofeng
 */
public interface VolumeBackendPool extends ModelEntity {

    /**
     * @return the name of the back end in this format: <code>host@backend#POOL</code>
     */
    String getName();

    /**
     * @return the capabilities for the storage back end
     */
    Capabilities getCapabilities();

    interface Capabilities {
        /**
         * @return the pool name
         */
        String getPoolname();

        /**
         * @return the goodness function
         */
        String getGoodnessfunction();

        /**
         * @return the total number of volumes
         */
        Integer getTotalvolumes();

        /**
         * @return the multi attach info
         */
        Boolean getMultiattach();

        /**
         * @return the provisioned capacity in GB
         */
        Long getProvisionedcapacitygb();

        /**
         * @return the timestamp of creation
         */
        String getTimestamp();

        /**
         * @return the allocated capacity in GB
         */
        Integer getAllocatedcapacitygb();

        /**
         * @return the support of thin provisioning
         */
        Boolean getThinprovisioningsupport();

        /**
         * @return the location info
         */
        String getLocationinfo();

        /**
         * @return the support of thick provisioning
         */
        Boolean getThickprovisioningsupport();

        /**
         * @return the ratio of Max over subscription
         */
        String getMaxoversubscription_ratio();

        /**
         * @return the vendor name
         */
        String getvendorname();

        /**
         * @return the filter function
         */
        String getFilterfunction();


        /**
         * @return the quality of service (QoS) support
         */
        Boolean getQosSupport();

        /**
         * @return the name of the share back end
         */
        String getVolumeBackendName();

        /**
         * @return the driver version
         */
        String getDriverVersion();

        /**
         * @return the total capacity for the back end, in GBs, or 'unkown'
         */
        Long getTotalCapacityGb();

        /**
         * @return the amount of free capacity for the back end, in GBs, or 'unknown'
         */
        Long getFreeCapacityGb();

        /**
         * @return the percentage of the total capacity that is reserved for the internal use by the back end
         */
        Integer getReservedPercentage();

        /**
         * @return the storage protocol for the back end
         */
        String getStorageProtocol();
    }
}
