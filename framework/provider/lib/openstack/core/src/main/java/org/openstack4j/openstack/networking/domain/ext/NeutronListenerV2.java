package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.ListenerProtocol;
import org.openstack4j.model.network.ext.ListenerV2;
import org.openstack4j.model.network.ext.builder.ListenerV2Builder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * lbaas v2 listener
 *
 * @author emjburns
 */
@JsonRootName("listener")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronListenerV2 implements ListenerV2 {
    private static final long serialVersionUID = 1L;

    private String id;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String name;

    private String description;

    /**
     * The protocol of the VIP address. A valid value is TCP, HTTP, or HTTPS
     */
    private ListenerProtocol protocol;

    /**
     * The port on which to listen to client traffic that is associated with the
     * VIP address. A valid value is from 0 to 65535.
     */
    @JsonProperty("protocol_port")
    private Integer protocolPort;

    /**
     * The maximum number of connections allowed for the listener. Default is -1, meaning no limit.
     */
    @JsonProperty("connection_limit")
    private Integer connectionLimit;

    @JsonProperty("default_pool_id")
    private String defaultPoolId;

    @JsonProperty("loadbalancer_id")
    private String loadbalancerId;

    private List<ListItem> loadbalancers;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    @JsonProperty("default_tls_container_ref")
    private String defaultTlsContainerRef;

    @JsonProperty("sni_container_refs")
    private List<String> sniContainerRefs;

    public static ListenerV2Builder builder() {
        return new ListenerConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ListItem> getLoadBalancers() {
        return loadbalancers;
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
    public String getDefaultPoolId() {
        return defaultPoolId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getConnectionLimit() {
        return connectionLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getProtocolPort() {
        return protocolPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListenerProtocol getProtocol() {
        return protocol;
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
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultTlsContainerRef() {
        return defaultTlsContainerRef;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSniContainerRefs() {
        return sniContainerRefs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("adminStateUp", adminStateUp)
                .add("tenantId", tenantId)
                .add("description", description)
                .add("loadbalancers", loadbalancers)
                .add("name", name)
                .add("protocol", protocol)
                .add("protocolPort", protocolPort)
                .add("connectionLImit", connectionLimit)
                .add("defaultPoolId", defaultPoolId)
                .add("defaultTlsContainerRef", defaultTlsContainerRef)
                .add("sniContainerRefs", sniContainerRefs)
                .toString();
    }

    @Override
    public ListenerV2Builder toBuilder() {
        return new ListenerConcreteBuilder(this);
    }

    public static class ListenerConcreteBuilder implements ListenerV2Builder {
        private NeutronListenerV2 m;

        public ListenerConcreteBuilder() {
            this(new NeutronListenerV2());
        }

        public ListenerConcreteBuilder(NeutronListenerV2 m) {
            this.m = m;
        }

        @Override
        public ListenerV2 build() {
            return m;
        }

        @Override
        public ListenerV2Builder from(ListenerV2 in) {
            m = (NeutronListenerV2) in;
            return this;
        }

        @Override
        public ListenerV2Builder loadBalancerId(String loadbalancerId) {
            m.loadbalancerId = loadbalancerId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder tenantId(String tenantId) {
            m.tenantId = tenantId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder protocol(ListenerProtocol protocol) {
            m.protocol = protocol;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder protocolPort(Integer protocolPort) {
            m.protocolPort = protocolPort;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder name(String name) {
            m.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder description(String description) {
            m.description = description;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder connectionLimit(Integer connectionLimit) {
            m.connectionLimit = connectionLimit;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder sniContainerRefs(List<String> sniContainerRefs) {
            m.sniContainerRefs = sniContainerRefs;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ListenerV2Builder defaultTlsContainerRef(String tlsContainerRef) {
            m.defaultTlsContainerRef = tlsContainerRef;
            return this;
        }
    }

    public static class Listeners extends ListResult<NeutronListenerV2> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("listeners")
        List<NeutronListenerV2> listeners;

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<NeutronListenerV2> value() {
            return listeners;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("listeners", listeners)
                    .toString();
        }
    }
}
