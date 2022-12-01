package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * The JSON object used to update an existing aggregate
 *
 * @author liujunpeng
 */
@JsonRootName("aggregate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaHostAggregateUpdate implements ModelEntity {

    private static final long serialVersionUID = 1L;
    @JsonProperty("availability_zone")
    public String availabilityZone;
    public String name;

    public NovaHostAggregateUpdate(String name, String availabilityZone) {
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
        return "NovaHostAggregateUpdate [availabilityZone=" + availabilityZone
                + ", name=" + name + "]";
    }

}
