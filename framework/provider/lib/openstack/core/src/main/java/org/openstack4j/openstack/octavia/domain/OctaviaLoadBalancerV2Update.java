package org.openstack4j.openstack.octavia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.octavia.LoadBalancerV2Update;
import org.openstack4j.model.octavia.builder.LoadBalancerV2UpdateBuilder;

/**
 * Entity used to update lbaas v2 loadbalancer
 *
 * @author wei
 */
@JsonRootName("loadbalancer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OctaviaLoadBalancerV2Update implements LoadBalancerV2Update {

    private String name;

    private String description;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    public static LoadBalancerV2UpdateBuilder builder() {
        return new LoadBalancerV2UpdateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
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
    public boolean isAdminStateUp() {
        return adminStateUp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("adminStateUp", adminStateUp)
                .add("description", description)
                .add("name", name)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2UpdateBuilder toBuilder() {
        return new LoadBalancerV2UpdateConcreteBuilder();
    }

    public static class LoadBalancerV2UpdateConcreteBuilder implements LoadBalancerV2UpdateBuilder {

        private OctaviaLoadBalancerV2Update m;

        public LoadBalancerV2UpdateConcreteBuilder() {
            this(new OctaviaLoadBalancerV2Update());
        }

        public LoadBalancerV2UpdateConcreteBuilder(OctaviaLoadBalancerV2Update m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Update build() {
            return m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2UpdateBuilder from(LoadBalancerV2Update in) {
            m = (OctaviaLoadBalancerV2Update) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2UpdateBuilder description(String description) {
            m.description = description;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2UpdateBuilder name(String name) {
            m.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2UpdateBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

    }
}
