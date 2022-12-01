package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

/**
 * OS Host describes capabilities of each compute host where Nova servers are running on
 *
 * @author Qin An
 */
@Deprecated
public interface HostResource extends ModelEntity {
    /**
     * @return the number of CPUs of the compute host
     */
    public int getCpu();

    /**
     * @return the size of Disk the compute host has, in GB
     */
    public int getDiskInGb();

    /**
     * @return the Hostname of the compute host
     */
    public String getHost();

    /**
     * @return the size of Memory of the compute host has, in MB
     */
    public int getMemoryInMb();

    /**
     * @return the project id (or special name like total, used_now, used_max)
     */
    public String getProject();


    /**
     * @return service
     */
    String getService();

    /**
     * @return zone
     */
    String getZone();

    /**
     * @return host name
     */
    String getHostName();

}
