package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.network.ext.NetworkIPAvailability;
import org.openstack4j.model.network.ext.SubnetIPAvailability;
import org.openstack4j.model.network.ext.builder.NetworkIPAvailabilityBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.math.BigInteger;
import java.util.List;

/**
 * A network IP availability is used to list and show the network IP usage stats of a specified network
 *
 * @author Xiangbin HAN
 */
@JsonRootName("network_ip_availability")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronNetworkIPAvailability implements NetworkIPAvailability {

    private static final long serialVersionUID = 1L;

    @JsonProperty("network_name")
    private String networkName;

    @JsonProperty("network_id")
    private String networkId;

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("used_ips")
    private BigInteger usedIps;

    @JsonProperty("total_ips")
    private BigInteger totalIps;

    @JsonProperty("subnet_ip_availability")
    private List<NeutronSubnetIPAvailability> subnetIPAvailabilities;

    public NeutronNetworkIPAvailability() {
    }

    public NeutronNetworkIPAvailability(String networkName, String networkId, String tenantId, String projectId, BigInteger usedIps,
                                        BigInteger totalIps, List<NeutronSubnetIPAvailability> subnetIPAvailabilities) {
        this.networkName = networkName;
        this.networkId = networkId;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.usedIps = usedIps;
        this.totalIps = totalIps;
        this.subnetIPAvailabilities = subnetIPAvailabilities;
    }

    public static NetworkIPAvailabilityBuilder builder() {
        return new NetworkIPAvailabilityConcreteBuilder();
    }

    @Override
    public NetworkIPAvailabilityBuilder toBuilder() {
        return new NetworkIPAvailabilityConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNetworkName() {
        return networkName;
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
    public String getProjectId() {
        return projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getTotalIps() {
        return totalIps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getUsedIps() {
        return usedIps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends SubnetIPAvailability> getSubnetIPAvailabilities() {
        return subnetIPAvailabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("networkName", networkName)
                .add("networkId", networkId)
                .add("tenantId", tenantId)
                .add("projectId", projectId)
                .add("usedIps", usedIps)
                .add("totalIps", totalIps)
                .add("subnetIPAvailabilities", subnetIPAvailabilities)
                .toString();
    }

    public static class NetworkIPAvailabilityConcreteBuilder implements NetworkIPAvailabilityBuilder {

        private NeutronNetworkIPAvailability model;

        public NetworkIPAvailabilityConcreteBuilder() {
            model = new NeutronNetworkIPAvailability();
        }

        public NetworkIPAvailabilityConcreteBuilder(NeutronNetworkIPAvailability model) {
            this.model = model;
        }

        @Override
        public NetworkIPAvailability build() {
            return model;
        }

        @Override
        public NetworkIPAvailabilityBuilder from(NetworkIPAvailability in) {
            model = (NeutronNetworkIPAvailability) in;
            return this;
        }

        @Override
        public NetworkIPAvailabilityBuilder networkName(String networkName) {
            model.networkName = networkName;
            return this;
        }

        @Override
        public NetworkIPAvailabilityBuilder networkId(String networkId) {
            model.networkId = networkId;
            return this;
        }

        @Override
        public NetworkIPAvailabilityBuilder tenantId(String tenantId) {
            model.tenantId = tenantId;
            return this;
        }

        @Override
        public NetworkIPAvailabilityBuilder projectId(String projectId) {
            model.projectId = projectId;
            return this;
        }

        @Override
        public NetworkIPAvailabilityBuilder totalIps(BigInteger totalIps) {
            model.totalIps = totalIps;
            return this;
        }

        @Override
        public NetworkIPAvailabilityBuilder usedIps(BigInteger usedIps) {
            model.usedIps = usedIps;
            return this;
        }

        @Override
        public NetworkIPAvailabilityBuilder subnetIPAvailability(SubnetIPAvailability subnetIPAvailability) {
            if (model.subnetIPAvailabilities == null)
                model.subnetIPAvailabilities = Lists.newArrayList();
            model.subnetIPAvailabilities.add((NeutronSubnetIPAvailability) subnetIPAvailability);
            return this;
        }

    }

    public static class NeutronNetworkIPAvailabilities extends ListResult<NeutronNetworkIPAvailability> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("network_ip_availabilities")
        private List<NeutronNetworkIPAvailability> networkIPAvailabilities;

        @Override
        protected List<NeutronNetworkIPAvailability> value() {
            return networkIPAvailabilities;
        }
    }

}
