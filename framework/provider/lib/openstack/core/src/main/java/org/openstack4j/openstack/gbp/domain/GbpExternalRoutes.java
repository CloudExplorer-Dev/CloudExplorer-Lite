package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.ExternalRoutes;
import org.openstack4j.model.gbp.builder.ExternalRoutesBuilder;

/**
 * Model implementation for External Routes
 *
 * @author vinod borole
 */
@JsonRootName("external_routes")
public class GbpExternalRoutes implements ExternalRoutes {
    private static final long serialVersionUID = 1L;

    private String destination;
    private String nexthop;

    public GbpExternalRoutes() {
    }

    public GbpExternalRoutes(String destination, String nexthop) {
        this.destination = destination;
        this.nexthop = nexthop;
    }

    public static ExternalRoutesBuilder builder() {
        return new ExternalRoutesConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDestination() {
        return destination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNexthop() {
        return nexthop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("destination", destination).add("nexthop", nexthop).toString();
    }

    @Override
    public ExternalRoutesBuilder toBuilder() {
        return new ExternalRoutesConcreteBuilder(this);
    }

    public static class ExternalRoutesConcreteBuilder implements ExternalRoutesBuilder {
        private GbpExternalRoutes extRoutes;

        public ExternalRoutesConcreteBuilder(GbpExternalRoutes gbpExternalRoutes) {
            this.extRoutes = gbpExternalRoutes;
        }

        public ExternalRoutesConcreteBuilder() {
            this(new GbpExternalRoutes());
        }

        @Override
        public ExternalRoutes build() {
            return extRoutes;
        }

        @Override
        public ExternalRoutesBuilder from(ExternalRoutes in) {
            extRoutes = (GbpExternalRoutes) in;
            return this;
        }

        @Override
        public ExternalRoutesBuilder destination(String destination) {
            extRoutes.destination = destination;
            return this;
        }

        @Override
        public ExternalRoutesBuilder nextHop(String nextHop) {
            extRoutes.nexthop = nextHop;
            return this;
        }

    }
}
