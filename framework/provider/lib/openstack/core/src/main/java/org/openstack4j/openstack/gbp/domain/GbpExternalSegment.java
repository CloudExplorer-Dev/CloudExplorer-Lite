package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.gbp.ExternalRoutes;
import org.openstack4j.model.gbp.ExternalSegment;
import org.openstack4j.model.gbp.builder.ExternalSegmentBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for External Segments
 *
 * @author vinod borole
 */
@JsonRootName("external_segment")
public class GbpExternalSegment implements ExternalSegment {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    @JsonProperty("external_policies")
    private List<String> externalPolicies;
    @JsonProperty("l3_policies")
    private List<String> l3Policies;
    @JsonProperty("nat_pools")
    private List<String> natpools;
    @JsonProperty("ip_version")
    private int ipVersion;
    @JsonProperty("cidr")
    private String cidr;
    private String description;
    private Boolean shared;
    @JsonProperty("subnet_id")
    private String subnetId;
    @JsonProperty("port_address_translation")
    private boolean portAddressTranslation;
    @JsonProperty("external_routes")
    private List<GbpExternalRoutes> externalRoutes;

    public static ExternalSegmentBuilder builder() {
        return new ExternalSegmentConcreteBuilder();
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<String> getExternalPolicies() {
        return externalPolicies;
    }

    @Override
    public int getIpVersion() {
        return ipVersion;
    }

    @Override
    public String getCidr() {
        return cidr;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public String getSubnetId() {
        return subnetId;
    }

    @Override
    public List<String> getL3Policies() {
        return l3Policies;
    }

    @Override
    public List<String> getNatpools() {
        return natpools;
    }

    @Override
    public boolean isPortAddressTranslation() {
        return portAddressTranslation;
    }

    @Override
    public List<GbpExternalRoutes> getExternalRoutes() {
        return externalRoutes;
    }

    @Override
    public ExternalSegmentBuilder toBuilder() {
        return new ExternalSegmentConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description).add("tenantId", tenantId).add("externalPolicies", externalPolicies)
                .add("l3Policies", l3Policies).add("natpools", natpools).add("ipVersion", ipVersion).add("cidr", cidr).add("shared", shared)
                .add("subnetId", subnetId).add("portAddressTranslation", portAddressTranslation).add("externalRoutes", externalRoutes).toString();
    }

    public static class ExternalSegments extends ListResult<GbpExternalSegment> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("external_segments")
        private List<GbpExternalSegment> externalSegments;

        @Override
        public List<GbpExternalSegment> value() {
            return externalSegments;
        }
    }

    public static class ExternalSegmentConcreteBuilder implements ExternalSegmentBuilder {

        private GbpExternalSegment extSegment;

        public ExternalSegmentConcreteBuilder() {
            this(new GbpExternalSegment());
        }

        public ExternalSegmentConcreteBuilder(GbpExternalSegment gbpExternalSegment) {
            this.extSegment = gbpExternalSegment;
        }

        @Override
        public ExternalSegment build() {
            return extSegment;
        }

        @Override
        public ExternalSegmentBuilder from(ExternalSegment in) {
            extSegment = (GbpExternalSegment) in;
            return this;
        }

        @Override
        public ExternalSegmentBuilder name(String name) {
            extSegment.name = name;
            return this;
        }

        @Override
        public ExternalSegmentBuilder description(String description) {
            this.extSegment.description = description;
            return this;
        }

        @Override
        public ExternalSegmentBuilder externalPolicies(List<String> extPolicyIds) {
            this.extSegment.externalPolicies = extPolicyIds;
            return this;
        }

        @Override
        public ExternalSegmentBuilder ipVersion(int ipVersion) {
            this.extSegment.ipVersion = ipVersion;
            return this;
        }

        @Override
        public ExternalSegmentBuilder cidr(String cidr) {
            this.extSegment.cidr = cidr;
            return this;
        }

        @Override
        public ExternalSegmentBuilder isShared(boolean shared) {
            this.extSegment.shared = shared;
            return this;
        }

        @Override
        public ExternalSegmentBuilder externalRoutes(List<ExternalRoutes> extRoutes) {
            this.extSegment.externalRoutes = Lists.newArrayList();
            for (ExternalRoutes externalRoute : extRoutes) {
                this.extSegment.externalRoutes.add((GbpExternalRoutes) externalRoute);
            }
            return this;
        }

        @Override
        public ExternalSegmentBuilder subnetId(String subnetId) {
            this.extSegment.subnetId = subnetId;
            return this;
        }

        @Override
        public ExternalSegmentBuilder isPortAddressTranslation(boolean isPortAddressTranslation) {
            this.extSegment.portAddressTranslation = isPortAddressTranslation;
            return this;
        }

    }

}
