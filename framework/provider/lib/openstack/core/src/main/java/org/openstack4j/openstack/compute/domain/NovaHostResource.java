package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.HostResource;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Nova Host Resource describes the capacity and capability of a compute host that provides compute service
 *
 * @author Qin An
 * @author Elina Meier
 */
@JsonRootName("resource")
@Deprecated
public class NovaHostResource implements HostResource {

    public static final long serialVersionUID = 1L;
    @JsonProperty("host_name")
    public String hostName;
    public String service;
    public String zone;
    private int cpu;
    @JsonProperty("disk_gb")
    private int diskGb;
    private String host;
    @JsonProperty("memory_mb")
    private int memoryMb;
    private String project;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCpu() {
        return cpu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDiskInGb() {
        return diskGb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMemoryInMb() {
        return memoryMb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProject() {
        return project;
    }

    /**
     * <p>Author:Wang Ting/王婷</p>
     *
     * @Title: getService
     */
    @Override
    public String getService() {
        return service;
    }

    /**
     * <p>Author:Wang Ting/王婷</p>
     *
     * @Title: getZone
     */
    @Override
    public String getZone() {
        return zone;
    }

    /**
     * <p>Author:Wang Ting/王婷</p>
     *
     * @Title: getHostName
     */
    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("cpu", cpu)
                .add("diskGb", diskGb)
                .add("host", host)
                .add("memoryMb", memoryMb)
                .add("project", project)
                .add("hostName", hostName)
                .add("service", service)
                .add("zone", zone).toString();
    }

    /**
     * host aggregates
     *
     * @author Wang Ting/王婷
     */
    @Deprecated
    public static class NovaHostResources extends ListResult<NovaHostResource> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("hosts")
        private List<NovaHostResource> hosts;

        /**
         * NovaHosts
         */
        public List<NovaHostResource> value() {
            return hosts;
        }

        @Override
        public String toString() {
            return "NovaHostResources [hosts=" + hosts + "]";
        }

    }

}
