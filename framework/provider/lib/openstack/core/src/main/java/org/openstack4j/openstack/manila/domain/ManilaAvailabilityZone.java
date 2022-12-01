package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.manila.AvailabilityZone;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Represents an availability zone.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ManilaAvailabilityZone implements AvailabilityZone {
    private String name;
    private String id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    public static class AvailabilityZones extends ListResult<ManilaAvailabilityZone> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("availability_zones")
        private List<ManilaAvailabilityZone> availabilityZones;

        @Override
        protected List<ManilaAvailabilityZone> value() {
            return availabilityZones;
        }
    }
}
