package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.ext.SubnetIPAvailability;
import org.openstack4j.model.network.ext.builder.SubnetIPAvailabilityBuilder;

import java.math.BigInteger;

/**
 * A subnet IP availabitiy that is bound to a network
 *
 * @author Xiangbin HAN
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronSubnetIPAvailability implements SubnetIPAvailability {

    private static final long serialVersionUID = 1L;

    @JsonProperty("used_ips")
    private BigInteger usedIps;

    @JsonProperty("total_ips")
    private BigInteger totalIps;

    @JsonProperty("subnet_id")
    private String subnetId;

    @JsonProperty("subnet_name")
    private String subnetName;

    @JsonProperty("ip_version")
    private IPVersionType ipVersion;

    @JsonProperty("cidr")
    private String cidr;

    public NeutronSubnetIPAvailability() {
    }

    public NeutronSubnetIPAvailability(BigInteger usedIps, BigInteger totalIps, String subnetId, String subnetName,
                                       IPVersionType ipVersion, String cidr) {
        this.usedIps = usedIps;
        this.totalIps = totalIps;
        this.subnetId = subnetId;
        this.subnetName = subnetName;
        this.ipVersion = ipVersion;
        this.cidr = cidr;
    }

    public static SubnetIPAvailabilityBuilder builder() {
        return new SubnetIPAvailabilityConcreteBuilder();
    }

    @Override
    public SubnetIPAvailabilityBuilder toBuilder() {
        return new SubnetIPAvailabilityConcreteBuilder(this);
    }

    @Override
    public BigInteger getUsedIps() {
        return usedIps;
    }

    @Override
    public BigInteger getTotalIps() {
        return totalIps;
    }

    @Override
    public String getSubnetId() {
        return subnetId;
    }

    @Override
    public String getSubnetName() {
        return subnetName;
    }

    @Override
    public IPVersionType getIpVersion() {
        return ipVersion;
    }

    @Override
    public String getCidr() {
        return cidr;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("usedIps", usedIps)
                .add("totalIps", totalIps)
                .add("subnetId", subnetId)
                .add("subnetName", subnetName)
                .add("ipVersion", ipVersion)
                .add("cidr", cidr)
                .toString();
    }

    public static class SubnetIPAvailabilityConcreteBuilder implements SubnetIPAvailabilityBuilder {

        NeutronSubnetIPAvailability model;

        public SubnetIPAvailabilityConcreteBuilder() {
            model = new NeutronSubnetIPAvailability();
        }

        public SubnetIPAvailabilityConcreteBuilder(NeutronSubnetIPAvailability model) {
            this.model = model;
        }

        @Override
        public SubnetIPAvailability build() {
            return model;
        }

        @Override
        public SubnetIPAvailabilityBuilder from(SubnetIPAvailability in) {
            return new SubnetIPAvailabilityConcreteBuilder((NeutronSubnetIPAvailability) in);
        }

        @Override
        public SubnetIPAvailabilityBuilder subnetName(String subnetName) {
            model.subnetName = subnetName;
            return this;
        }

        @Override
        public SubnetIPAvailabilityBuilder subnetId(String subnetId) {
            model.subnetId = subnetId;
            return this;
        }

        @Override
        public SubnetIPAvailabilityBuilder tenantId(IPVersionType ipVersion) {
            model.ipVersion = ipVersion;
            return this;
        }

        @Override
        public SubnetIPAvailabilityBuilder cidr(String cidr) {
            model.cidr = cidr;
            return this;
        }

        @Override
        public SubnetIPAvailabilityBuilder totalIps(BigInteger totalIps) {
            model.totalIps = totalIps;
            return this;
        }

        @Override
        public SubnetIPAvailabilityBuilder usedIps(BigInteger usedIps) {
            model.usedIps = usedIps;
            return this;
        }

    }
}
