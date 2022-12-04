package org.openstack4j.openstack.telemetry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.telemetry.Trait;

/**
 * The event’s Traits contain most of the details of the event.
 * Traits are typed, and can be strings, ints, floats, or datetime
 *
 * @author Miroslav Lacina
 */
public class CeilometerTrait implements Trait {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private String value;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
