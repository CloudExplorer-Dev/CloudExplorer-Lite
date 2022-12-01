package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.openstack4j.model.common.builder.ResourceBuilder;
import org.openstack4j.model.network.*;
import org.openstack4j.model.network.builder.PortBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.*;

/**
 * A Neutron Port
 *
 * @author Jeremy Unruh
 */
@JsonRootName("port")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronPort implements Port {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("device_owner")
    private String deviceOwner;

    @JsonProperty("fixed_ips")
    private Set<NeutronIP> fixedIps;

    @JsonProperty("allowed_address_pairs")
    private Set<NeutronAllowedAddressPair> allowedAddressPairs;

    @JsonProperty("mac_address")
    private String macAddress;

    @JsonProperty("network_id")
    private String networkId;

    @JsonProperty("status")
    private State state;

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("trunkport:type")
    private String trunkPortType;

    @JsonProperty("trunkport:parent_id")
    private String trunkPortParentId;

    @JsonProperty("trunkport:vid")
    private String trunkPortVlanId;

    @JsonProperty("security_groups")
    private List<String> securityGroups;

    @JsonProperty("extra_dhcp_opts")
    private List<NeutronExtraDhcpOptCreate> extraDhcpOptCreates;

    @JsonProperty("port_security_enabled")
    private Boolean portSecurityEnabled;

    @JsonProperty("binding:host_id")
    private String hostId;

    @JsonProperty("binding:vif_type")
    private String vifType;

    @JsonProperty("binding:vif_details")
    private Map<String, Object> vifDetails;

    @JsonProperty("binding:vnic_type")
    private String vNicType;

    @JsonProperty("binding:profile")
    private Map<String, Object> profile;

    @JsonProperty("created_at")
    private Date createdTime;

    @JsonProperty("updated_at")
    private Date updatedTime;

    public static PortBuilder builder() {
        return new PortConcreteBuilder();
    }

    @Override
    public PortBuilder toBuilder() {
        return new PortConcreteBuilder(this);
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
    public State getState() {
        return state;
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
    public String getNetworkId() {
        return networkId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeviceOwner() {
        return deviceOwner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<? extends IP> getFixedIps() {
        return fixedIps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<? extends AllowedAddressPair> getAllowedAddressPairs() {
        return allowedAddressPairs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSecurityGroups() {
        return securityGroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVifType() {
        return vifType;
    }

    public void setVifType(String vifType) {
        this.vifType = vifType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getVifDetails() {
        return vifDetails;
    }

    public void setVifDetails(Map<String, Object> vifDetails) {
        this.vifDetails = vifDetails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getvNicType() {
        return vNicType;
    }

    public void setvNicType(String vNicType) {
        this.vNicType = vNicType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getProfile() {
        return profile;
    }

    public void setProfile(Map<String, Object> profile) {
        this.profile = profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTrunkPortParentId() {
        return trunkPortParentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTrunkPortType() {
        return trunkPortType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTrunkPortVlanId() {
        return trunkPortVlanId;
    }

    @Override
    public Date getCreatedTime() {
        return createdTime;
    }

    @Override
    public Date getUpdatedTime() {
        return updatedTime;
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
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("adminStateUp", adminStateUp).add("deviceId", deviceId)
                .add("trunkPortType", trunkPortType).add("trunkPortParentId", trunkPortParentId).add("trunkPortVlanId", trunkPortVlanId)
                .add("deviceOwner", deviceOwner).add("fixedIps", fixedIps).add("macAddress", macAddress)
                .add("networkId", networkId).add("tenantId", tenantId).add("securityGroups", securityGroups)
                .add("allowed_address_pairs", allowedAddressPairs).add("port_security_enabled ", portSecurityEnabled)
                .add("binding:host_id", hostId).add("binding:vif_type", vifType).add("binding:vif_details", vifDetails)
                .add("binding:vnic_type", vNicType).add("binding:profile", profile)
                .add("created_at", createdTime).add("updated_at", updatedTime)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, adminStateUp, deviceId,
                deviceOwner, fixedIps, macAddress, networkId, tenantId,
                securityGroups, allowedAddressPairs, portSecurityEnabled, hostId,
                vifType, vifDetails, vNicType, profile, createdTime, updatedTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronPort) {
            NeutronPort that = (NeutronPort) obj;
            if (java.util.Objects.equals(id, that.id) &&
                    java.util.Objects.equals(name, that.name) &&
                    java.util.Objects.equals(adminStateUp, that.adminStateUp) &&
                    java.util.Objects.equals(deviceId, that.deviceId) &&
                    java.util.Objects.equals(deviceOwner, that.deviceOwner) &&
                    java.util.Objects.equals(fixedIps, that.fixedIps) &&
                    java.util.Objects.equals(macAddress, that.macAddress) &&
                    java.util.Objects.equals(networkId, that.networkId) &&
                    java.util.Objects.equals(tenantId, that.tenantId) &&
                    java.util.Objects.equals(securityGroups, that.securityGroups) &&
                    java.util.Objects.equals(allowedAddressPairs, that.allowedAddressPairs) &&
                    java.util.Objects.equals(portSecurityEnabled, that.portSecurityEnabled) &&
                    java.util.Objects.equals(hostId, that.hostId) &&
                    java.util.Objects.equals(vifType, that.vifType) &&
                    java.util.Objects.equals(vifDetails, that.vifDetails) &&
                    java.util.Objects.equals(vNicType, that.vNicType) &&
                    java.util.Objects.equals(profile, that.profile) &&
                    java.util.Objects.equals(createdTime, that.createdTime) &&
                    java.util.Objects.equals(updatedTime, that.updatedTime)) {
                return true;
            }
        }
        return false;
    }

    public static class Ports extends ListResult<NeutronPort> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("ports")
        private List<NeutronPort> ports;

        @Override
        protected List<NeutronPort> value() {
            return ports;
        }
    }

    public static class PortConcreteBuilder extends ResourceBuilder<Port, PortConcreteBuilder> implements PortBuilder {

        private NeutronPort m;

        PortConcreteBuilder() {
            this(new NeutronPort());
        }

        PortConcreteBuilder(NeutronPort port) {
            this.m = port;
        }

        @Override
        public PortBuilder networkId(String networkId) {
            m.networkId = networkId;
            return this;
        }

        @Override
        public PortBuilder deviceId(String deviceId) {
            m.deviceId = deviceId;
            return this;
        }

        @Override
        public PortBuilder deviceOwner(String deviceOwner) {
            m.deviceOwner = deviceOwner;
            return this;
        }

        @Override
        public PortBuilder macAddress(String macAddress) {
            m.macAddress = macAddress;
            return this;
        }

        @Override
        public PortBuilder fixedIp(String address, String subnetId) {
            if (m.fixedIps == null)
                m.fixedIps = Sets.newHashSet();

            m.fixedIps.add(new NeutronIP(address, subnetId));
            return this;
        }

        @Override
        public PortBuilder removeFixedIp(String address, String subnetId) {
            if (m.fixedIps == null)
                m.fixedIps = Sets.newHashSet();

            m.fixedIps.removeIf(fixedIP ->
                    fixedIP.getSubnetId() != null && fixedIP.getSubnetId().equals(subnetId) &&
                            fixedIP.getIpAddress() != null && fixedIP.getIpAddress().equals(address));

            return this;
        }

        @Override
        public PortBuilder allowedAddressPair(String ipAddress, String macAddress) {
            if (m.allowedAddressPairs == null)
                m.allowedAddressPairs = Sets.newHashSet();

            m.allowedAddressPairs.add(new NeutronAllowedAddressPair(ipAddress, macAddress));
            return this;
        }

        @Override
        public PortBuilder removeAddressPair(String ipAddress, String macAddress) {
            if (m.allowedAddressPairs == null)
                m.allowedAddressPairs = Sets.newHashSet();

            m.allowedAddressPairs.removeIf(allowedAddress ->
                    allowedAddress.getIpAddress() != null && allowedAddress.getIpAddress().equals(ipAddress) &&
                            allowedAddress.getMacAddress() != null && allowedAddress.getMacAddress().equals(macAddress));

            return this;
        }


        @Override
        public PortBuilder adminState(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        @Override
        public PortBuilder state(State state) {
            m.state = state;
            return this;
        }

        @Override
        public PortBuilder trunkPortParentId(String parentPortId) {
            m.trunkPortParentId = parentPortId;
            return this;
        }

        @Override
        public PortBuilder trunkPortType(String trunkPortType) {
            m.trunkPortType = trunkPortType;
            return this;
        }

        @Override
        public PortBuilder trunkPortVlanId(String vlanId) {
            m.trunkPortVlanId = vlanId;
            return this;
        }

        @Override
        public Port build() {
            return m;
        }

        @Override
        public PortBuilder from(Port in) {
            m = (NeutronPort) in;
            return this;
        }

        @Override
        protected Port reference() {
            return m;
        }

        @Override
        public PortBuilder extraDhcpOpt(ExtraDhcpOptCreate extraDhcpOptCreate) {
            if (m.extraDhcpOptCreates == null)
                m.extraDhcpOptCreates = Lists.newArrayList();
            m.extraDhcpOptCreates.add((NeutronExtraDhcpOptCreate) extraDhcpOptCreate);
            return this;
        }

        @Override
        public PortBuilder securityGroup(String groupName) {
            if (m.securityGroups == null) {
                m.securityGroups = new ArrayList<>();
            }
            m.securityGroups.add(groupName);
            return this;
        }

        @Override
        public PortBuilder portSecurityEnabled(Boolean portSecurityEnabled) {
            m.portSecurityEnabled = portSecurityEnabled;
            return this;
        }

        @Override
        public PortBuilder hostId(String hostId) {
            m.hostId = hostId;
            return this;
        }

        @Override
        public PortBuilder vifType(String vifType) {
            m.vifType = vifType;
            return this;
        }

        @Override
        public PortBuilder vifDetails(Map<String, Object> vifDetails) {
            m.vifDetails = vifDetails;
            return this;
        }

        @Override
        public PortBuilder vNicType(String vNicType) {
            m.vNicType = vNicType;
            return this;
        }

        @Override
        public PortBuilder profile(Map<String, Object> profile) {
            m.profile = profile;
            return this;
        }

    }

}
