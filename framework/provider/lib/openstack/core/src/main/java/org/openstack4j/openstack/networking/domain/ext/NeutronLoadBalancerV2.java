package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.LbOperatingStatus;
import org.openstack4j.model.network.ext.LbProvisioningStatus;
import org.openstack4j.model.network.ext.LoadBalancerV2;
import org.openstack4j.model.network.ext.builder.LoadBalancerV2Builder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * lbaas v2 loadbalancer
 *
 * @author emjburns
 */
@JsonRootName("loadbalancer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronLoadBalancerV2 implements LoadBalancerV2 {

    private String id;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String name;

    private String description;

    /**
     * The ID of the subnet on which to allocate the VIP address.
     */
    @JsonProperty("vip_subnet_id")
    private String vipSubnetId;

    /**
     * The IP address of the VIP.
     */
    @JsonProperty("vip_address")
    private String vipAddress;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    @JsonProperty("provisioning_status")
    private LbProvisioningStatus provisioningStatus;

    @JsonProperty("operating_status")
    private LbOperatingStatus operatingStatus;

    private List<ListItem> listeners;

    @JsonProperty("vip_port_id")
    private String vipPortId;

    private String provider;

    public static LoadBalancerV2Builder builder() {
        return new LoadBalancerV2ConcreteBuilder();
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
    @JsonProperty("vip_subnet_id")
    @Override
    public String getVipSubnetId() {
        return vipSubnetId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVipAddress() {
        return vipAddress;
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
    public LbProvisioningStatus getProvisioningStatus() {
        return provisioningStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbOperatingStatus getOperatingStatus() {
        return operatingStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVipPortId() {
        return vipPortId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProvider() {
        return provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2Builder toBuilder() {
        return new LoadBalancerV2ConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("tenantId", tenantId)
                .add("name", name)
                .add("description", description)
                .add("vipSubnetId", vipSubnetId)
                .add("vipAddress", vipAddress)
                .add("adminStateUp", adminStateUp)
                .add("provisioningStatus", provisioningStatus)
                .add("operatingStatus", operatingStatus)
                .add("listeners", listeners)
                .add("vipPortId", vipPortId)
                .add("provider", provider)
                .toString();
    }

    public static class LoadBalancersV2 extends ListResult<NeutronLoadBalancerV2> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("loadbalancers")
        List<NeutronLoadBalancerV2> loadbalancers;

        @Override
        public List<NeutronLoadBalancerV2> value() {
            return loadbalancers;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("loadbalancers", loadbalancers).toString();
        }
    }

    public static class LoadBalancerV2ConcreteBuilder implements LoadBalancerV2Builder {
        private NeutronLoadBalancerV2 m;

        public LoadBalancerV2ConcreteBuilder() {
            this(new NeutronLoadBalancerV2());
        }

        public LoadBalancerV2ConcreteBuilder(NeutronLoadBalancerV2 m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2 build() {
            return m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Builder from(LoadBalancerV2 in) {
            m = (NeutronLoadBalancerV2) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Builder tenantId(String tenantId) {
            m.tenantId = tenantId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Builder name(String name) {
            m.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Builder description(String description) {
            m.description = description;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Builder subnetId(String vipSubnetId) {
            m.vipSubnetId = vipSubnetId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Builder address(String vipAddress) {
            m.vipAddress = vipAddress;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LoadBalancerV2Builder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        @Override
        public LoadBalancerV2Builder provider(String provider) {
            m.provider = provider;
            return this;
        }
    }
}

