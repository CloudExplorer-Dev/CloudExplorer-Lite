package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.builder.NetFloatingIPBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * An OpenStack Neutron Floating IP Model.
 *
 * @author Nathan Anderson
 */
@JsonRootName("floatingip")
public class NeutronFloatingIP implements NetFloatingIP {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("router_id")
    private String routerId;

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("floating_network_id")
    private String floatingNetworkId;

    @JsonProperty("floating_ip_address")
    private String floatingIpAddress;

    @JsonProperty("fixed_ip_address")
    private String fixedIpAddress;

    @JsonProperty("port_id")
    private String portId;

    @JsonProperty("qos_policy_id")
    private String qosPolicyId;

    private String status;

    private String description;

    private List<String> tags;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("revision_number")
    private Integer revisionNumber;

    /**
     * Builder.
     *
     * @return the net floating ip builder
     */
    public static NetFloatingIPBuilder builder() {
        return new FloatingIPConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetFloatingIPBuilder toBuilder() {
        return new FloatingIPConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRouterId() {
        return this.routerId;
    }

    /**
     * {@inheritDoc}
     */
    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return this.tenantId;
    }

    /**
     * {@inheritDoc}
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFloatingNetworkId() {
        return this.floatingNetworkId;
    }

    /**
     * {@inheritDoc}
     */
    public void setFloatingNetworkId(String floatingNetworkId) {
        this.floatingNetworkId = floatingNetworkId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFloatingIpAddress() {
        return this.floatingIpAddress;
    }

    /**
     * {@inheritDoc}
     */
    public void setFloatingIpAddress(String floatingIpAddress) {
        this.floatingIpAddress = floatingIpAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFixedIpAddress() {
        return this.fixedIpAddress;
    }

    /**
     * {@inheritDoc}
     */
    public void setFixedIpAddress(String fixedIpAddress) {
        this.fixedIpAddress = fixedIpAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPortId() {
        return this.portId;
    }

    /**
     * {@inheritDoc}
     */
    public void setPortId(String portId) {
        this.portId = portId;
    }

    @Override
    public String getQosPolicyId() {
        return qosPolicyId;
    }

    @Override
    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // Report tenantId iff it differs from projectId
        String distinctTenantId = Objects.equals(tenantId, projectId) ? null : tenantId;
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("routerId", routerId).add("floatingNetworkId", floatingNetworkId)
                .add("projectId", projectId).add("tenantId", distinctTenantId)
                .add("floatingIpAddress", floatingIpAddress).add("fixedIpAddress", fixedIpAddress).add("portId", portId).add("status", status)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, routerId, tenantId, projectId, floatingNetworkId, floatingIpAddress, fixedIpAddress, portId,
                qosPolicyId, status, description, tags, createdAt, updatedAt, revisionNumber
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeutronFloatingIP that = (NeutronFloatingIP) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(routerId, that.routerId) &&
                Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(floatingNetworkId, that.floatingNetworkId) &&
                Objects.equals(floatingIpAddress, that.floatingIpAddress) &&
                Objects.equals(fixedIpAddress, that.fixedIpAddress) &&
                Objects.equals(portId, that.portId) &&
                Objects.equals(qosPolicyId, that.qosPolicyId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(description, that.description) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(revisionNumber, that.revisionNumber);
    }

    /**
     * The Class FloatingIPs.
     *
     * @author Nathan Anderson
     */
    public static class FloatingIPs extends ListResult<NeutronFloatingIP> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("floatingips")
        private List<NeutronFloatingIP> floatingIps;

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<NeutronFloatingIP> value() {
            return floatingIps;
        }
    }

    /**
     * The Class FloatingIPConcreteBuilder.
     *
     * @author Nathan Anderson
     */
    public static class FloatingIPConcreteBuilder implements NetFloatingIPBuilder {

        private NeutronFloatingIP f = null;

        /**
         * Instantiates a new floating ip concrete builder.
         */
        public FloatingIPConcreteBuilder() {
            f = new NeutronFloatingIP();
        }

        /**
         * Instantiates a new floating ip concrete builder.
         *
         * @param in the in
         */
        public FloatingIPConcreteBuilder(NetFloatingIP in) {
            f = (NeutronFloatingIP) in;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetFloatingIP build() {
            return f;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetFloatingIPBuilder from(NetFloatingIP in) {
            f = (NeutronFloatingIP) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetFloatingIPBuilder floatingNetworkId(String networkId) {
            f.floatingNetworkId = networkId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NetFloatingIPBuilder portId(String portId) {
            f.portId = portId;
            return this;
        }

        @Override
        public NetFloatingIPBuilder floatingIpAddress(String address) {
            f.floatingIpAddress = address;
            return this;
        }

        @Override
        public NetFloatingIPBuilder fixedIpAddress(String address) {
            f.fixedIpAddress = address;
            return this;
        }

        @Override
        public NetFloatingIPBuilder description(String description) {
            f.description = description;
            return this;
        }
    }
}
