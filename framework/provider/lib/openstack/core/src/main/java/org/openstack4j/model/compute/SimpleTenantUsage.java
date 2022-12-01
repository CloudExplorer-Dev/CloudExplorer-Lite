package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.Server.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Provides simple usage reporting for tenants
 *
 * @author Jeremy Unruh
 */
public interface SimpleTenantUsage extends ModelEntity {

    /**
     * @return the total memory usage in MB
     */
    BigDecimal getTotalMemoryMbUsage();

    /**
     * @return the total Virtual CPU Usage
     */
    BigDecimal getTotalVcpusUsage();

    /**
     * @return the total local GB of data used
     */
    BigDecimal getTotalLocalGbUsage();

    /**
     * @return the start date/time
     */
    Date getStart();

    /**
     * @return the stop date/time
     */
    Date getStop();

    /**
     * @return the tenant the usage is reporting againt
     */
    String getTenantId();

    /**
     * @return the total hours of usage
     */
    String getTotalHours();

    /**
     * @return the list of usages per tenant server (Null when query was against all tenants)
     */
    List<? extends ServerUsage> getServerUsages();


    public interface ServerUsage extends Serializable {

        /**
         * @return the instance id of the server
         */
        String getInstanceId();

        /**
         * @return the uptime of the server
         */
        int getUptime();

        /**
         * @return the date the server was started or null
         */
        Date getStartedAt();

        /**
         * @return the date the server was last stopped or null
         */
        Date getEndedAt();

        /**
         * @return the memory used for the server in MB
         */
        int getMemoryMb();

        /**
         * @return the tenant assigned to the server
         */
        String getTenantId();

        /**
         * @return the state
         */
        Status getState();

        /**
         * @return the hours of usage
         */
        double getHours();

        /**
         * @return the Virtual CPUs used
         */
        int getVcpus();

        /**
         * @return the flavor assigned to the server
         */
        String getFlavor();

        /**
         * @return the local disk size in GB
         */
        int getLocalDiskSize();

        /**
         * @return the name of the server
         */
        String getName();
    }

}
