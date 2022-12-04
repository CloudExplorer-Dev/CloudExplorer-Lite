package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.common.builder.ResourceBuilder;
import org.openstack4j.model.network.ExternalGateway;
import org.openstack4j.model.network.HostRoute;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.State;
import org.openstack4j.model.network.builder.RouterBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A router is used to interconnect subnets and forward traffic among them. Another feature of the router is to NAT internal traffic to external networks.
 *
 * @author Jeremy Unruh
 */
@JsonRootName("router")
public class NeutronRouter implements Router {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private State status;

    @JsonProperty("external_gateway_info")
    private NeutronExternalGateway externalGatewayInfo;

    @JsonProperty("admin_state_up")
    private Boolean adminStateUp;

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("routes")
    private List<NeutronHostRoute> routes;

    @JsonProperty("distributed")
    private Boolean distributed;

    public static RouterBuilder builder() {
        return new RouterConcreteBuilder();
    }

    @Override
    public RouterBuilder toBuilder() {
        return new RouterConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(String id) {
        this.id = id;
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
    public void setName(String name) {
        this.name = name;
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
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends HostRoute> getRoutes() {
        return routes;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isAdminStateUp() {
        return (adminStateUp != null) ? adminStateUp : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalGateway getExternalGatewayInfo() {
        return externalGatewayInfo;
    }

    @Override
    public Boolean getDistributed() {
        return distributed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("tenantId", tenantId).add("admin_state_up", adminStateUp)
                .add("external_gateway_info", externalGatewayInfo).add("routes", routes)
                .add("distributed", distributed)
                .addValue("\n")
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, status, tenantId, adminStateUp,
                externalGatewayInfo, routes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronRouter) {
            NeutronRouter that = (NeutronRouter) obj;
            if (java.util.Objects.equals(id, that.id) &&
                    java.util.Objects.equals(name, that.name) &&
                    java.util.Objects.equals(status, that.status) &&
                    java.util.Objects.equals(tenantId, that.tenantId) &&
                    java.util.Objects.equals(adminStateUp, that.adminStateUp) &&
                    java.util.Objects.equals(externalGatewayInfo, that.externalGatewayInfo) &&
                    java.util.Objects.equals(routes, that.routes)) {
                return true;
            }
        }
        return false;
    }

    public static class Routers extends ListResult<NeutronRouter> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("routers")
        private List<NeutronRouter> routers;

        @Override
        protected List<NeutronRouter> value() {
            return routers;
        }
    }

    public static class RouterConcreteBuilder extends ResourceBuilder<Router, RouterConcreteBuilder> implements RouterBuilder {

        private NeutronRouter m;

        RouterConcreteBuilder() {
            this(new NeutronRouter());
        }

        RouterConcreteBuilder(NeutronRouter m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder adminStateUp(boolean isAdminStateUp) {
            m.adminStateUp = isAdminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder externalGateway(String networkId) {
            return externalGateway(networkId, null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder externalGateway(String networkId, Boolean enableSNAT) {
            m.externalGatewayInfo = new NeutronExternalGateway(networkId, enableSNAT);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder externalGateway(ExternalGateway externalGateway) {
            m.externalGatewayInfo = (NeutronExternalGateway) externalGateway;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder clearExternalGateway() {
            m.externalGatewayInfo = new NeutronExternalGateway();
            return this;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder route(String destination, String nexthop) {
            if (m.routes == null)
                m.routes = Lists.newArrayList();
            m.routes.add(new NeutronHostRoute(destination, nexthop));
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder noRoutes() {
            m.routes = Lists.newArrayList();
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Router reference() {
            return m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Router build() {
            return m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder from(Router in) {
            m = (NeutronRouter) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RouterBuilder distributed(Boolean distributed) {
            m.distributed = distributed;
            return this;
        }
    }
}
