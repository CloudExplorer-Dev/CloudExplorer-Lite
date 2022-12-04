package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.openstack4j.model.common.builder.ResourceBuilder;
import org.openstack4j.model.network.*;
import org.openstack4j.model.network.builder.SubnetBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * A Subnet is a network with Pools and network based settings
 *
 * @author Jeremy Unruh
 */
@JsonRootName("subnet")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronSubnet implements Subnet {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    @JsonProperty("enable_dhcp")
    private boolean enableDHCP;
    @JsonProperty("network_id")
    private String networkId;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("dns_nameservers")
    private List<String> dnsNames;
    @JsonProperty("allocation_pools")
    private List<NeutronPool> pools;
    @JsonProperty("host_routes")
    private List<NeutronHostRoute> hostRoutes;
    @JsonProperty("ip_version")
    private IPVersionType ipVersion;
    @JsonProperty("gateway_ip")
    private String gateway;
    private String cidr;
    @JsonProperty("ipv6_address_mode")
    private Ipv6AddressMode ipv6AddressMode;
    @JsonProperty("ipv6_ra_mode")
    private Ipv6RaMode ipv6RaMode;
    @JsonProperty("created_at")
    private Date createdTime;
    @JsonProperty("updated_at")
    private Date updatedTime;

    public NeutronSubnet() {
    }

    public NeutronSubnet(String id, String name, boolean enableDHCP, String networkId, String tenantId, List<String> dnsNames,
                         List<NeutronPool> pools, List<NeutronHostRoute> hostRoutes, IPVersionType ipVersion,
                         String gateway, String cidr, Ipv6AddressMode ipv6AddressMode, Ipv6RaMode ipv6RaMode) {
        this.id = id;
        this.name = name;
        this.enableDHCP = enableDHCP;
        this.networkId = networkId;
        this.tenantId = tenantId;
        this.dnsNames = dnsNames;
        this.pools = pools;
        this.hostRoutes = hostRoutes;
        this.ipVersion = ipVersion;
        this.gateway = gateway;
        this.cidr = cidr;
        this.ipv6AddressMode = ipv6AddressMode;
        this.ipv6RaMode = ipv6RaMode;
    }

    public static SubnetBuilder builder() {
        return new SubnetConcreteBuilder();
    }

    @Override
    public SubnetBuilder toBuilder() {
        return new SubnetConcreteBuilder(this);
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
    @JsonIgnore
    public boolean isDHCPEnabled() {
        return enableDHCP;
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
    public List<String> getDnsNames() {
        return dnsNames;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<? extends Pool> getAllocationPools() {
        return pools;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends HostRoute> getHostRoutes() {
        return hostRoutes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPVersionType getIpVersion() {
        return ipVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGateway() {
        return gateway;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCidr() {
        return cidr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ipv6AddressMode getIpv6AddressMode() {
        return ipv6AddressMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ipv6RaMode getIpv6RaMode() {
        return ipv6RaMode;
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
                .add("id", id).add("name", name).add("enableDHCP", enableDHCP).add("network-id", networkId)
                .add("tenant_id", tenantId).add("dns_nameservers", dnsNames).add("allocation_pools", pools)
                .add("host_routes", hostRoutes).add("ip_version", ipVersion).add("gateway_ip", gateway).add("cidr", cidr)
                .add("ipv6AddressMode", ipv6AddressMode).add("ipv6RaMode", ipv6RaMode)
                .add("created_at", createdTime).add("updated_at", updatedTime)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, enableDHCP, networkId,
                tenantId, dnsNames, pools, hostRoutes, ipVersion, gateway,
                cidr, ipv6AddressMode, ipv6RaMode, createdTime, updatedTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronSubnet) {
            NeutronSubnet that = (NeutronSubnet) obj;
            if (java.util.Objects.equals(id, that.id) &&
                    java.util.Objects.equals(name, that.name) &&
                    java.util.Objects.equals(enableDHCP, that.enableDHCP) &&
                    java.util.Objects.equals(networkId, that.networkId) &&
                    java.util.Objects.equals(tenantId, that.tenantId) &&
                    java.util.Objects.equals(dnsNames, that.dnsNames) &&
                    java.util.Objects.equals(pools, that.pools) &&
                    java.util.Objects.equals(hostRoutes, that.hostRoutes) &&
                    java.util.Objects.equals(ipVersion, that.ipVersion) &&
                    java.util.Objects.equals(gateway, that.gateway) &&
                    java.util.Objects.equals(cidr, that.cidr) &&
                    java.util.Objects.equals(ipv6AddressMode, that.ipv6AddressMode) &&
                    java.util.Objects.equals(ipv6RaMode, that.ipv6RaMode) &&
                    java.util.Objects.equals(createdTime, that.createdTime) &&
                    java.util.Objects.equals(updatedTime, that.updatedTime)) {
                return true;
            }
        }
        return false;
    }

    @JsonRootName("subnet")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NeutronSubnetNoGateway extends NeutronSubnet {

        @JsonProperty("gateway_ip")
        @JsonInclude
        private String gateway;

        public NeutronSubnetNoGateway(String id, String name, boolean enableDHCP, String networkId, String tenantId,
                                      List<String> dnsNames, List<NeutronPool> pools, List<NeutronHostRoute> hostRoutes,
                                      IPVersionType ipVersion, String cidr, Ipv6AddressMode ipv6AddressMode, Ipv6RaMode ipv6RaMode) {
            super(id, name, enableDHCP, networkId, tenantId, dnsNames, pools, hostRoutes, ipVersion, null, cidr, ipv6AddressMode, ipv6RaMode);
            this.gateway = null;
        }
    }

    public static class Subnets extends ListResult<NeutronSubnet> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("subnets")
        private List<NeutronSubnet> subnets;

        @Override
        protected List<NeutronSubnet> value() {
            return subnets;
        }
    }

    public static class SubnetConcreteBuilder extends ResourceBuilder<Subnet, SubnetConcreteBuilder> implements SubnetBuilder {

        private NeutronSubnet m;
        private boolean isNoGateway = false;

        SubnetConcreteBuilder() {
            this(new NeutronSubnet());
        }

        SubnetConcreteBuilder(NeutronSubnet m) {
            this.m = m;
        }

        @Override
        public SubnetBuilder networkId(String networkId) {
            m.networkId = networkId;
            return this;
        }

        @Override
        public SubnetBuilder network(Network network) {
            m.networkId = network.getId();
            return this;
        }

        @Override
        public SubnetBuilder ipVersion(IPVersionType ipVersion) {
            m.ipVersion = ipVersion;
            return this;
        }

        @Override
        public SubnetBuilder cidr(String cidr) {
            m.cidr = cidr;
            return this;
        }

        @Override
        public SubnetBuilder gateway(String gateway) {
            m.gateway = gateway;
            return this;
        }

        @Override
        public SubnetBuilder addPool(String start, String end) {
            if (m.pools == null)
                m.pools = Lists.newArrayList();
            m.pools.add(new NeutronPool(start, end));
            return this;
        }

        @Override
        public SubnetBuilder enableDHCP(boolean enable) {
            m.enableDHCP = enable;
            return this;
        }

        @Override
        public SubnetBuilder noGateway() {
            isNoGateway = true;
            return this;
        }

        @Override
        public SubnetBuilder ipv6AddressMode(Ipv6AddressMode ipv6AddressMode) {
            m.ipv6AddressMode = ipv6AddressMode;
            return this;
        }

        @Override
        public SubnetBuilder ipv6RaMode(Ipv6RaMode ipv6RaMode) {
            m.ipv6RaMode = ipv6RaMode;
            return this;
        }

        @Override
        public Subnet build() {
            if (isNoGateway) {
                return new NeutronSubnetNoGateway(m.id, m.name, m.enableDHCP, m.networkId,
                        m.tenantId, m.dnsNames, m.pools, m.hostRoutes, m.ipVersion, m.cidr, m.ipv6AddressMode, m.ipv6RaMode);
            }
            return m;
        }

        @Override
        public SubnetBuilder from(Subnet in) {
            return this;
        }

        @Override
        protected Subnet reference() {
            return m;
        }

        @Override
        public SubnetBuilder addDNSNameServer(String host) {
            if (Strings.isNullOrEmpty(host))
                return this;

            if (m.dnsNames == null)
                m.dnsNames = Lists.newArrayList();

            m.dnsNames.add(host);
            return this;
        }

        @Override
        public SubnetBuilder addHostRoute(String destination, String nexthop) {
            Preconditions.checkArgument(nexthop != null && destination != null, "NextHop and Destination must have a value");
            if (m.hostRoutes == null)
                m.hostRoutes = Lists.newArrayList();

            m.hostRoutes.add(new NeutronHostRoute(destination, nexthop));
            return this;
        }
    }
}
