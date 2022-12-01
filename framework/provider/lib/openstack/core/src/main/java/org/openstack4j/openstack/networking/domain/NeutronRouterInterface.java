package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.RouterInterface;

/**
 * An interface data model which is returned during interface association with a router
 *
 * @author Jeremy Unruh
 */
public class NeutronRouterInterface implements RouterInterface {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("subnet_id")
    private String subnetId;

    @JsonProperty("port_id")
    private String portId;

    public NeutronRouterInterface() {
    }

    public NeutronRouterInterface(String subnetId, String portId) {
        this.subnetId = subnetId;
        this.portId = portId;
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
    public String getSubnetId() {
        return subnetId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPortId() {
        return portId;
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
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("subnetId", subnetId).add("portId", portId).add("tenantId", tenantId).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, subnetId, portId, tenantId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronRouterInterface) {
            NeutronRouterInterface that = (NeutronRouterInterface) obj;
            if (java.util.Objects.equals(id, that.id) &&
                    java.util.Objects.equals(subnetId, that.subnetId) &&
                    java.util.Objects.equals(portId, that.portId) &&
                    java.util.Objects.equals(tenantId, that.tenantId)) {
                return true;
            }
        }
        return false;
    }

}
