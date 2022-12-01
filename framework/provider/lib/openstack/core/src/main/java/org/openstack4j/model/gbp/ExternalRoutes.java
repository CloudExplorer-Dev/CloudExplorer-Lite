package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.gbp.builder.ExternalRoutesBuilder;

/**
 * External Routes Model Entity
 *
 * @author vinod borole
 */
public interface ExternalRoutes extends ModelEntity, Buildable<ExternalRoutesBuilder> {
    /**
     * Gets the destination for the route
     *
     * @return the destination host for this host route
     */
    String getDestination();

    /**
     * Gets the NextHop for this route
     *
     * @return the NextHop for this host route
     */
    String getNexthop();
}
