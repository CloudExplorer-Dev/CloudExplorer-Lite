package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.builder.FloatingIPBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * The Class NovaFloatingIP.
 *
 * @author Nathan Anderson
 */
@JsonRootName("floating_ip")
@Deprecated
public class NovaFloatingIP implements FloatingIP {

    private static final long serialVersionUID = -4441740897994315920L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("instance_id")
    private String instanceId;

    @JsonProperty("ip")
    private String floatingIpAddress;

    @JsonProperty("fixed_ip")
    private String fixedIpAddress;

    @JsonProperty("pool")
    private String pool;

    /**
     * @return the Floating IP Builder
     */
    public static FloatingIPBuilder builder() {
        return new FloatingIPConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    public FloatingIPBuilder toBuilder() {
        return new FloatingIPConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInstanceId() {
        return this.instanceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFloatingIpAddress() {
        return this.floatingIpAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFixedIpAddress() {
        return this.fixedIpAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPool() {
        return this.pool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("instanceId", instanceId)
                .add("floatingIpAddress", floatingIpAddress)
                .add("fixedIpAddress", fixedIpAddress)
                .add("pool", pool)
                .addValue("\n")
                .toString();
    }

    /**
     * The Class NovaFloatingIPs.
     *
     * @author Nathan Anderson
     */
    @Deprecated
    public static class NovaFloatingIPs extends ListResult<NovaFloatingIP> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("floating_ips")
        private List<NovaFloatingIP> floatingIps;

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<NovaFloatingIP> value() {
            return floatingIps;
        }
    }

    /**
     * The Class FloatingIPConcreteBuilder.
     *
     * @author Nathan Anderson
     */
    @Deprecated
    public static class FloatingIPConcreteBuilder implements FloatingIPBuilder {

        NovaFloatingIP m = null;

        /**
         * Instantiates a new floating ip concrete builder.
         */
        public FloatingIPConcreteBuilder() {
            this.m = new NovaFloatingIP();
        }

        /**
         * Instantiates a new floating ip concrete builder.
         *
         * @param floatingIp the floating ip
         */
        public FloatingIPConcreteBuilder(FloatingIP floatingIp) {
            this.m = (NovaFloatingIP) floatingIp;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FloatingIP build() {
            return m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FloatingIPBuilder from(FloatingIP in) {
            m = (NovaFloatingIP) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FloatingIPBuilder id(String id) {
            m.id = id;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FloatingIPBuilder fixedIpAddress(String fixedIp) {
            m.fixedIpAddress = fixedIp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FloatingIPBuilder floatingIpAddress(String floatingIpAddress) {
            m.floatingIpAddress = floatingIpAddress;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FloatingIPBuilder instanceId(String instanceId) {
            m.instanceId = instanceId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FloatingIPBuilder pool(String pool) {
            m.pool = pool;
            return this;
        }
    }

}
