package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.IPVersionType;
import org.openstack4j.model.gbp.NatPool;
import org.openstack4j.model.gbp.builder.NatPoolBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for nat pool
 *
 * @author vinod borole
 */
@JsonRootName("nat_pool")
public class GbpNatPool implements NatPool {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    private Boolean shared;
    @JsonProperty("external_segment_id")
    private String externalSegmentId;
    @JsonProperty("ip_pool")
    private String ipPool;
    @JsonProperty("ip_version")
    private String ipVersion;
    @JsonProperty("subnet_id")
    private String subnetId;

    public static NatPoolBuilder builder() {
        return new NatPoolConcreteBuilder();
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
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public String getExternalSegmentId() {
        return externalSegmentId;
    }

    @Override
    public String getIpPool() {
        return ipPool;
    }

    @Override
    public String getIpVersion() {
        return ipVersion;
    }

    @Override
    public String getSubnetId() {
        return subnetId;
    }

    @Override
    public NatPoolBuilder toBuilder() {
        return new NatPoolConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("externalSegmentId", externalSegmentId).add("ipPool", ipPool).add("ipVersion", ipVersion)
                .add("subnetId", subnetId).add("shared", shared).toString();
    }

    public static class NatPools extends ListResult<GbpNatPool> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("nat_pools")
        private List<GbpNatPool> natpools;

        @Override
        protected List<GbpNatPool> value() {
            return natpools;
        }

    }

    public static class NatPoolConcreteBuilder implements NatPoolBuilder {

        private GbpNatPool natPool;

        public NatPoolConcreteBuilder(GbpNatPool gbpNatPool) {
            this.natPool = gbpNatPool;
        }

        public NatPoolConcreteBuilder() {
            this(new GbpNatPool());
        }

        @Override
        public NatPool build() {
            return natPool;
        }

        @Override
        public NatPoolBuilder from(NatPool in) {
            this.natPool = (GbpNatPool) in;
            return this;
        }

        @Override
        public NatPoolBuilder name(String name) {
            this.natPool.name = name;
            return this;
        }

        @Override
        public NatPoolBuilder description(String description) {
            this.natPool.description = description;
            return this;
        }

        @Override
        public NatPoolBuilder ipVersion(IPVersionType ipVersion) {
            this.natPool.ipVersion = ipVersion.name();
            return this;
        }

        @Override
        public NatPoolBuilder cidr(String cidr) {
            this.natPool.ipPool = cidr;
            return this;
        }

        @Override
        public NatPoolBuilder isShared(boolean shared) {
            this.natPool.shared = shared;
            return this;
        }

        @Override
        public NatPoolBuilder externalSegmentId(String id) {
            this.natPool.externalSegmentId = id;
            return this;
        }

    }

}
