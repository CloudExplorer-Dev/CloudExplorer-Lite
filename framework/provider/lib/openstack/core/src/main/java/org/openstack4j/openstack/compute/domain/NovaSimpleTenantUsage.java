package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.SimpleTenantUsage;
import org.openstack4j.openstack.common.ListResult;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Provides simple usage reporting for tenants
 *
 * @author Jeremy Unruh
 */
@JsonRootName("tenant_usage")
public class NovaSimpleTenantUsage implements SimpleTenantUsage {

    private static final long serialVersionUID = 1L;
    @JsonProperty("total_memory_mb_usage")
    private BigDecimal totalMemoryMbUsage;
    @JsonProperty("total_vcpus_usage")
    private BigDecimal totalVcpusUsage;
    @JsonProperty("total_local_gb_usage")
    private BigDecimal totalLocalGbUsage;
    private Date start;
    private Date stop;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("total_hours")
    private String totalHours;
    @JsonProperty("server_usages")
    private List<NovaServerUsage> serverUsages;

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getTotalMemoryMbUsage() {
        return totalMemoryMbUsage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getTotalVcpusUsage() {
        return totalVcpusUsage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getTotalLocalGbUsage() {
        return totalLocalGbUsage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getStop() {
        return stop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTotalHours() {
        return totalHours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ServerUsage> getServerUsages() {
        return serverUsages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("tenantId", tenantId).add("totalMemoryMbUsage", totalMemoryMbUsage).add("totalVcpusUsage", totalVcpusUsage).add("totalLocalGbUsage", totalLocalGbUsage)
                .add("start", start).add("stop", stop).add("totalHours", totalHours).addValue("\n").add("serverUsages", serverUsages)
                .toString();
    }

    /**
     * Tenant usages for all tenants wrapper
     */
    public static class NovaSimpleTenantUsages extends ListResult<NovaSimpleTenantUsage> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("tenant_usages")
        private List<NovaSimpleTenantUsage> tenantUsages;

        public List<NovaSimpleTenantUsage> value() {
            return tenantUsages;
        }
    }

    public static class NovaServerUsage implements ServerUsage {

        private static final long serialVersionUID = 1L;
        @JsonProperty("instance_id")
        private String instanceId;
        private int uptime;
        @JsonProperty("started_at")
        private Date startedAt;
        @JsonProperty("ended_at")
        private Date endedAt;
        @JsonProperty("memory_mb")
        private int memoryMb;
        @JsonProperty("tenant_id")
        private String tenantId;
        private Status state;
        private double hours;
        private int vcpus;
        private String flavor;
        @JsonProperty("local_gb")
        private int localDiskSize;
        private String name;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getInstanceId() {
            return instanceId;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getUptime() {
            return uptime;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Date getStartedAt() {
            return startedAt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Date getEndedAt() {
            return endedAt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMemoryMb() {
            return memoryMb;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTenantId() {
            return tenantId;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Status getState() {
            return state;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getHours() {
            return hours;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getVcpus() {
            return vcpus;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getFlavor() {
            return flavor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getLocalDiskSize() {
            return localDiskSize;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("instanceId", instanceId).add("name", name).add("uptime", uptime).add("startedAt", startedAt)
                    .add("endedAt", endedAt).add("memoryMb", memoryMb).add("tenantId", tenantId).add("state", state)
                    .add("hours", hours).add("vcpus", vcpus).add("flavor", flavor).add("localDiskSize", localDiskSize)
                    .addValue("\n")
                    .toString();
        }
    }
}
