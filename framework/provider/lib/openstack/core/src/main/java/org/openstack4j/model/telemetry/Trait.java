package org.openstack4j.model.telemetry;

import org.openstack4j.model.ModelEntity;

/**
 * The event’s Traits contain most of the details of the event.
 * Traits are typed, and can be strings, ints, floats, or datetime
 *
 * @author Miroslav Lacina
 */
public interface Trait extends ModelEntity {

    /**
     * @return name of Trait
     */
    String getName();

    /**
     * @return data type of Trait
     */
    String getType();

    /**
     * @return value of Trait
     */
    String getValue();

}
