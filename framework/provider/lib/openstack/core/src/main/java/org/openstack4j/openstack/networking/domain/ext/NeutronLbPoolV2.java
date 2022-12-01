package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.LbMethod;
import org.openstack4j.model.network.ext.LbPoolV2;
import org.openstack4j.model.network.ext.Protocol;
import org.openstack4j.model.network.ext.SessionPersistence;
import org.openstack4j.model.network.ext.builder.LbPoolV2Builder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Lbaas V2 load balancer pool
 *
 * @author emjburns
 */
@JsonRootName("pool")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronLbPoolV2 implements LbPoolV2 {

    private String id;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String name;

    private String description;

    private Protocol protocol;

    @JsonProperty("lb_algorithm")
    private LbMethod lbMethod;

    @JsonProperty("session_persistence")
    @JsonDeserialize(as = NeutronSessionPersistence.class)
    private SessionPersistence sessionPersistence;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    @JsonProperty("listener_id")
    private String listenerId;

    @JsonProperty("loadbalancer_id")
    private String loadBalancerId;

    private List<ListItem> listeners;

    private List<ListItem> members;

    @JsonProperty("healthmonitor_id")
    private String healthMonitorId;

    public static LbPoolV2Builder builder() {
        return new LbPoolV2ConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolV2Builder toBuilder() {
        return new LbPoolV2ConcreteBuilder(this);
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
    public String getTenantId() {
        return tenantId;
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
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbMethod getLbMethod() {
        return lbMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdminStateUp() {
        return adminStateUp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ListItem> getListeners() {
        return listeners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ListItem> getMembers() {
        return members;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHealthMonitorId() {
        return healthMonitorId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("tenantId", tenantId)
                .add("name", name)
                .add("description", description)
                .add("protocol", protocol)
                .add("lbMethod", lbMethod)
                .add("sessionPersistence", sessionPersistence)
                .add("adminStateUp", adminStateUp)
                .add("listenerId", listenerId)
                .add("listeners", listeners)
                .add("loadBalancerId", loadBalancerId)
                .add("members", members)
                .add("healthMonitorId", healthMonitorId)
                .toString();
    }

    public static class LbPoolV2ConcreteBuilder implements LbPoolV2Builder {

        private NeutronLbPoolV2 m;

        public LbPoolV2ConcreteBuilder() {
            this(new NeutronLbPoolV2());
        }

        public LbPoolV2ConcreteBuilder(NeutronLbPoolV2 m) {
            this.m = m;
        }

        @Override
        public LbPoolV2 build() {
            return m;
        }

        @Override
        public LbPoolV2Builder from(LbPoolV2 in) {
            m = (NeutronLbPoolV2) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder tenantId(String tenantId) {
            m.tenantId = tenantId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder name(String name) {
            m.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder description(String description) {
            m.description = description;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder protocol(Protocol protocol) {
            m.protocol = protocol;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder lbMethod(LbMethod lbMethod) {
            m.lbMethod = lbMethod;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder sessionPersistence(SessionPersistence sessionPersistence) {
            m.sessionPersistence = sessionPersistence;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder listenerId(String listenerId) {
            m.listenerId = listenerId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2Builder loadBalancerId(String loadBalancerId) {
            m.loadBalancerId = loadBalancerId;
            return this;
        }
    }

    public static class LbPoolsV2 extends ListResult<NeutronLbPoolV2> {
        @JsonProperty("pools")
        List<NeutronLbPoolV2> lbPools;

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<NeutronLbPoolV2> value() {
            return lbPools;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("lbPools", lbPools)
                    .toString();
        }
    }
}
