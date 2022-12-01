package org.openstack4j.openstack.placement.domain.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.placement.ext.ResourceProviderInventories;

/**
 * The resource provider inventories instance
 *
 * @author Jyothi Saroja
 */
@JsonRootName("inventories")
public class ExtResourceProviderInventories implements ResourceProviderInventories {

    private static final long serialVersionUID = 1L;

    @JsonProperty("VCPU")
    private ResourceProviderVcpu vcpu;

    @JsonProperty("PCPU")
    private ResourceProviderPcpu pcpu;

    @JsonProperty("MEMORY_MB")
    private ResourceProviderMemoryMb memoryMb;

    @JsonProperty("DISK_GB")
    private ResourceProviderDiskGb diskGb;

    /**
     * {@inheritDoc}
     */
    @Override
    public DiskGb getDiskGb() {
        return diskGb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemoryMb getMemoryMb() {
        return memoryMb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pcpu getPcpu() {
        return pcpu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vcpu getVcpu() {
        return vcpu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("vcpu", vcpu).add("pcpu", pcpu)
                .add("memoryMb", memoryMb).add("diskGb", diskGb).toString();
    }

    @JsonRootName("DISK_GB")
    static class ResourceProviderDiskGb implements DiskGb {

        private static final long serialVersionUID = 1L;
        @JsonProperty("reserved")
        private int reserved;
        @JsonProperty("min_unit")
        private int minUnit;
        @JsonProperty("max_unit")
        private int maxUnit;
        @JsonProperty("step_size")
        private int stepSize;
        @JsonProperty("total")
        private int total;
        @JsonProperty("allocation_ratio")
        private float allocationRatio;

        /**
         * {@inheritDoc}
         */
        @Override
        public float getAllocationRatio() {
            return allocationRatio;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMaxUnit() {
            return maxUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMinUnit() {
            return minUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getReserved() {
            return reserved;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStepSize() {
            return stepSize;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getTotal() {
            return total;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("total", total).add("reserved", reserved)
                    .add("min_unit", minUnit).add("max_unit", maxUnit).add("step_size", stepSize)
                    .add("allocation_ratio", allocationRatio).toString();
        }
    }

    @JsonRootName("MEMORY_MB")
    static class ResourceProviderMemoryMb implements MemoryMb {

        private static final long serialVersionUID = 1L;
        @JsonProperty("reserved")
        private int reserved;
        @JsonProperty("min_unit")
        private int minUnit;
        @JsonProperty("max_unit")
        private int maxUnit;
        @JsonProperty("step_size")
        private int stepSize;
        @JsonProperty("total")
        private int total;
        @JsonProperty("allocation_ratio")
        private float allocationRatio;

        /**
         * {@inheritDoc}
         */
        @Override
        public float getAllocationRatio() {
            return allocationRatio;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMaxUnit() {
            return maxUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMinUnit() {
            return minUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getReserved() {
            return reserved;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStepSize() {
            return stepSize;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getTotal() {
            return total;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("total", total).add("reserved", reserved)
                    .add("min_unit", minUnit).add("max_unit", maxUnit).add("step_size", stepSize)
                    .add("allocation_ratio", allocationRatio).toString();
        }
    }

    @JsonRootName("PCPU")
    static class ResourceProviderPcpu implements Pcpu {

        private static final long serialVersionUID = 1L;
        @JsonProperty("reserved")
        private int reserved;
        @JsonProperty("min_unit")
        private int minUnit;
        @JsonProperty("max_unit")
        private int maxUnit;
        @JsonProperty("step_size")
        private int stepSize;
        @JsonProperty("total")
        private int total;
        @JsonProperty("allocation_ratio")
        private float allocationRatio;

        /**
         * {@inheritDoc}
         */
        @Override
        public float getAllocationRatio() {
            return allocationRatio;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMaxUnit() {
            return maxUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMinUnit() {
            return minUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getReserved() {
            return reserved;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStepSize() {
            return stepSize;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getTotal() {
            return total;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("total", total).add("reserved", reserved)
                    .add("min_unit", minUnit).add("max_unit", maxUnit).add("step_size", stepSize)
                    .add("allocation_ratio", allocationRatio).toString();
        }
    }

    @JsonRootName("VCPU")
    static class ResourceProviderVcpu implements Vcpu {

        private static final long serialVersionUID = 1L;
        @JsonProperty("reserved")
        private int reserved;
        @JsonProperty("min_unit")
        private int minUnit;
        @JsonProperty("max_unit")
        private int maxUnit;
        @JsonProperty("step_size")
        private int stepSize;
        @JsonProperty("total")
        private int total;
        @JsonProperty("allocation_ratio")
        private float allocationRatio;

        /**
         * {@inheritDoc}
         */
        @Override
        public float getAllocationRatio() {
            return allocationRatio;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMaxUnit() {
            return maxUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getMinUnit() {
            return minUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getReserved() {
            return reserved;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStepSize() {
            return stepSize;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getTotal() {
            return total;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("total", total).add("reserved", reserved)
                    .add("min_unit", minUnit).add("max_unit", maxUnit).add("step_size", stepSize)
                    .add("allocation_ratio", allocationRatio).toString();
        }
    }

    public interface DiskGb extends ModelEntity {

        /**
         * @return the allocation ratio of disk
         */
        float getAllocationRatio();

        /**
         * @return the max unit of disk
         */
        int getMaxUnit();

        /**
         * @return the min unit of disk
         */
        int getMinUnit();

        /**
         * @return the reserved disk
         */
        int getReserved();

        /**
         * @return the step size of disk
         */
        int getStepSize();

        /**
         * @return the total disk
         */
        int getTotal();

    }

    public interface MemoryMb extends ModelEntity {

        /**
         * @return the allocation ratio of memory
         */
        float getAllocationRatio();

        /**
         * @return the max unit of memory
         */
        int getMaxUnit();

        /**
         * @return the min unit of memory
         */
        int getMinUnit();

        /**
         * @return the reserved memory
         */
        int getReserved();

        /**
         * @return the step size of memory
         */
        int getStepSize();

        /**
         * @return the total memory
         */
        int getTotal();

    }

    public interface Pcpu extends ModelEntity {

        /**
         * @return the allocation ratio of pcpus
         */
        float getAllocationRatio();

        /**
         * @return the max unit of pcpus
         */
        int getMaxUnit();

        /**
         * @return the min unit of pcpus
         */
        int getMinUnit();

        /**
         * @return the reserved pcpus
         */
        int getReserved();

        /**
         * @return the step size of pcpus
         */
        int getStepSize();

        /**
         * @return the total pcpus
         */
        int getTotal();

    }

    public interface Vcpu extends ModelEntity {

        /**
         * @return the allocation ratio of vcpus
         */
        float getAllocationRatio();

        /**
         * @return the max unit of vcpus
         */
        int getMaxUnit();

        /**
         * @return the min unit of vcpus
         */
        int getMinUnit();

        /**
         * @return the reserved vcpus
         */
        int getReserved();

        /**
         * @return the step size of vcpus
         */
        int getStepSize();

        /**
         * @return the total vcpus
         */
        int getTotal();

    }
}
