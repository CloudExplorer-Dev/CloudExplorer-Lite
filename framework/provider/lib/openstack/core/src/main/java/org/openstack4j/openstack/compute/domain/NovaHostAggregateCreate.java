package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * The JSON object used to create an host aggregate
 *
 * @author chenyan
 */
@JsonRootName("aggregate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaHostAggregateCreate implements ModelEntity {

    private static final long serialVersionUID = 1L;
    @JsonProperty("availability_zone")
    public String availabilityZone;
    public String name;

    public NovaHostAggregateCreate(String name, String availabilityZone) {
        this.availabilityZone = availabilityZone;
        this.name = name;

    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NovaHostAggregateCreate [availabilityZone=" + availabilityZone
                + ", name=" + name + "]";
    }

}
