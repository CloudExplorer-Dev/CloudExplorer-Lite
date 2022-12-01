package org.openstack4j.model;

/**
 * Model class which is used for Service Operations that is backed by a unique identifier and a name
 *
 * @author Jeremy Unruh
 */
public interface ResourceEntity extends ModelEntity {

    /**
     * @return the id of this entity
     */
    String getId();

    /**
     * @return the name of this entity
     */
    String getName();

}
