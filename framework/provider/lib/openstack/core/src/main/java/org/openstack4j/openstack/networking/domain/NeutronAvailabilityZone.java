package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.AvailabilityZone;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * An availability zone groups network nodes that run services like DHCP, L3, FW, and others.
 *
 * @author Taemin
 */
@JsonRootName("availability_zones")
public class NeutronAvailabilityZone implements AvailabilityZone {

    private static final long serialVersionUID = 1L;

    private String state;
    private String resource;
    private String name;

    public NeutronAvailabilityZone() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResource() {
        return resource;
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
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("state", state).add("resource", resource).add("name", name).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(state, resource, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronAvailabilityZone) {
            NeutronAvailabilityZone that = (NeutronAvailabilityZone) obj;
            if (java.util.Objects.equals(state, that.state) &&
                    java.util.Objects.equals(resource, that.resource) &&
                    java.util.Objects.equals(name, that.name)) {
                return true;
            }
        }
        return false;
    }

    public static class AvailabilityZones extends ListResult<NeutronAvailabilityZone> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("availability_zones")
        private List<NeutronAvailabilityZone> availabilityZones;

        @Override
        protected List<NeutronAvailabilityZone> value() {
            return availabilityZones;
        }
    }
}


