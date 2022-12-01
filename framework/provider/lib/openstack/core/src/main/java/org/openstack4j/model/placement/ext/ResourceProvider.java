package org.openstack4j.model.placement.ext;

import org.openstack4j.model.ModelEntity;

/**
 * Represents a Resource Provider details Entity used within the ResourceProvider extensions API
 *
 * @author Jyothi Saroja
 */
public interface ResourceProvider extends ModelEntity {

    /**
     * @return the unique identifier representing this resource provider
     */
    String getId();

    /**
     * @return the name of the resource provider host
     */
    String getName();
}
