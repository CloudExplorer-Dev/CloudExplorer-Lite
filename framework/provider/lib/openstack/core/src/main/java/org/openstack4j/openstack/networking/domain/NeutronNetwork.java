package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.api.Apis;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.model.network.State;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.model.network.builder.NetworkBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * An OpenStack (Neutron) network
 *
 * @author Jeremy Unruh
 */
@JsonRootName("network")
public class NeutronNetwork implements Network {

    private static final long serialVersionUID = 1L;

    private State status;
    @JsonProperty
    private List<String> subnets;
    private List<NeutronSubnet> neutronSubnets;
    private String name;
    @JsonProperty("provider:physical_network")
    private String providerPhyNet;
    @JsonProperty("admin_state_up")
    private Boolean adminStateUp;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("provider:network_type")
    private NetworkType networkType;
    @JsonProperty("router:external")
    private Boolean routerExternal;
    private String id;
    private Boolean shared;
    @JsonProperty("provider:segmentation_id")
    private String providerSegID;
    @JsonProperty("availability_zone_hints")
    private List<String> availabilityZoneHints;
    @JsonProperty("availability_zones")
    private List<String> availabilityZones;
    @JsonProperty("port_security_enabled")
    private Boolean portSecurityEnabled;
    @JsonProperty("created_at")
    private Date createdTime;
    @JsonProperty("updated_at")
    private Date updatedTime;

    /**
     * The maximum transmission unit (MTU) value to address fragmentation. Minimum value is 68 for IPv4, and 1280 for IPv6.
     */
    @JsonProperty("mtu")
    private Integer mtu;

    public static NetworkBuilder builder() {
        return new NetworkConcreteBuilder();
    }

    /**
     * Wraps this Network into a Builder
     *
     * @return the network builder
     */
    public NetworkBuilder toBuilder() {
        return new NetworkConcreteBuilder(this);
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
    public List<String> getSubnets() {
        return subnets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Subnet> getNeutronSubnets() {
        if (neutronSubnets == null && (subnets != null && subnets.size() > 0)) {
            neutronSubnets = new ArrayList<NeutronSubnet>();
            for (String subnetId : subnets) {
                NeutronSubnet sub = (NeutronSubnet) Apis.getNetworkingServices().subnet().get(subnetId);
                neutronSubnets.add(sub);
            }
        }
        return neutronSubnets;
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
    public String getProviderPhyNet() {
        return providerPhyNet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdminStateUp() {
        return adminStateUp != null && adminStateUp;
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
    public NetworkType getNetworkType() {
        return networkType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public boolean isRouterExternal() {
        return routerExternal != null && routerExternal;
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
    public boolean isShared() {
        return shared != null && shared;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProviderSegID() {
        return providerSegID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMTU() {
        return mtu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailabilityZoneHints() {
        return availabilityZoneHints;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailabilityZones() {
        return availabilityZones;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isPortSecurityEnabled() {
        return portSecurityEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("status", status).add("subnets", subnets).add("provider:physical_network", providerPhyNet)
                .add("adminStateUp", adminStateUp).add("tenantId", tenantId).add("provider:network_type", networkType).add("router:external", routerExternal)
                .add("id", id).add("shared", shared).add("provider:segmentation_id", providerSegID)
                .add("mtu", mtu).add("availabilityZoneHints", availabilityZoneHints).add("availabilityZones", availabilityZones)
                .add("portSecurityEnabled", portSecurityEnabled)
                .add("created_at", createdTime).add("updated_at", updatedTime)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, status, subnets,
                providerPhyNet, adminStateUp, tenantId, networkType,
                routerExternal, id, shared, providerSegID, availabilityZoneHints, availabilityZones, portSecurityEnabled,
                createdTime, updatedTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronNetwork) {
            NeutronNetwork that = (NeutronNetwork) obj;
            if (java.util.Objects.equals(name, that.name) &&
                    java.util.Objects.equals(status, that.status) &&
                    java.util.Objects.equals(subnets, that.subnets) &&
                    java.util.Objects.equals(providerPhyNet, that.providerPhyNet) &&
                    java.util.Objects.equals(adminStateUp, that.adminStateUp) &&
                    java.util.Objects.equals(tenantId, that.tenantId) &&
                    java.util.Objects.equals(networkType, that.networkType) &&
                    java.util.Objects.equals(routerExternal, that.routerExternal) &&
                    java.util.Objects.equals(id, that.id) &&
                    java.util.Objects.equals(shared, that.shared) &&
                    java.util.Objects.equals(providerSegID, that.providerSegID) &&
                    java.util.Objects.equals(availabilityZoneHints, that.availabilityZoneHints) &&
                    java.util.Objects.equals(availabilityZones, that.availabilityZones) &&
                    java.util.Objects.equals(portSecurityEnabled, that.portSecurityEnabled) &&
                    java.util.Objects.equals(createdTime, that.createdTime) &&
                    java.util.Objects.equals(updatedTime, that.updatedTime)) {
                return true;
            }
        }
        return false;
    }

    public static class Networks extends ListResult<NeutronNetwork> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("networks")
        private List<NeutronNetwork> networks;

        public List<NeutronNetwork> value() {
            return networks;
        }
    }

    public static class NetworkConcreteBuilder implements NetworkBuilder {

        private NeutronNetwork m;

        public NetworkConcreteBuilder() {
            this(new NeutronNetwork());
        }

        public NetworkConcreteBuilder(NeutronNetwork m) {
            this.m = m;
        }

        @Override
        public NetworkBuilder name(String name) {
            m.name = name;
            return this;
        }

        @Override
        public NetworkBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        @Override
        public NetworkBuilder networkType(NetworkType networkType) {
            m.networkType = networkType;
            return this;
        }

        @Override
        public NetworkBuilder physicalNetwork(String providerPhysicalNetwork) {
            m.providerPhyNet = providerPhysicalNetwork;
            return this;
        }

        @Override
        public NetworkBuilder segmentId(String providerSegmentationId) {
            m.providerSegID = providerSegmentationId;
            return this;
        }

        @Override
        public NetworkBuilder tenantId(String tenantId) {
            m.tenantId = tenantId;
            return this;
        }

        @Override
        public NetworkBuilder isShared(boolean shared) {
            m.shared = shared;
            return this;
        }

        @Override
        public NetworkBuilder isRouterExternal(boolean routerExternal) {
            m.routerExternal = routerExternal;
            return this;
        }

        @Override
        public Network build() {
            return m;
        }

        @Override
        public NetworkBuilder from(Network in) {
            m = (NeutronNetwork) in;
            return this;
        }

        @Override
        public NetworkBuilder addAvailabilityZoneHints(String availabilityZone) {
            if (m.availabilityZoneHints == null) {
                m.availabilityZoneHints = new ArrayList<>();
            }
            m.availabilityZoneHints.add(availabilityZone);
            return this;
        }

        @Override
        public NetworkBuilder isPortSecurityEnabled(Boolean portSecurityEnabled) {
            m.portSecurityEnabled = portSecurityEnabled;
            return this;
        }
    }
}
