package org.openstack4j.model.identity.v2.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v2.Service;

/**
 * A Builder which creates an Identity Service
 *
 * @author Jeremy Unruh
 */
public interface ServiceBuilder extends Builder<ServiceBuilder, Service> {

    /**
     * @see Service#getName()
     */
    ServiceBuilder name(String name);

    /**
     * @see Service#getType()
     */
    ServiceBuilder type(String type);

    /**
     * @see Service#getDescription()
     */
    ServiceBuilder description(String description);

}
