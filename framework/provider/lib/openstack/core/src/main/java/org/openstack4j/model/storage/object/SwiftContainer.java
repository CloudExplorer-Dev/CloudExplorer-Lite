package org.openstack4j.model.storage.object;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * Represents an OpenStack Swift Container which holds Objects
 *
 * @author Jeremy Unruh
 */
public interface SwiftContainer extends ModelEntity {

    /**
     * The name of the Container
     *
     * @return the container name
     */
    String getName();

    /**
     * The current object count for this container
     *
     * @return the number of objects
     */
    int getObjectCount();

    /**
     * The total size of all the objects within this contain in bytes
     *
     * @return total size in bytes
     */
    long getTotalSize();

    /**
     * The metadata for the current container.  NOTE: This is a lazy call to the server and will invoke each time this
     * method is called.
     *
     * @return The metadata for this container
     */
    Map<String, String> getMetadata();
}
