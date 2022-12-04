package org.openstack4j.model.telemetry;

import org.openstack4j.model.ModelEntity;

/**
 * A description of a trait, with no associated value.
 *
 * @author Miroslav Lacina
 */
public interface TraitDescription extends ModelEntity {

    /**
     * @return name of Trait
     */
    String getName();

    /**
     * @return data type of Trait
     */
    String getType();

}
