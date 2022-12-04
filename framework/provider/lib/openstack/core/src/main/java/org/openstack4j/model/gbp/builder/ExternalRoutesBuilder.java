package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.ExternalRoutes;

/**
 * A builder which produces a External Routes object
 *
 * @author vinod borole
 */
public interface ExternalRoutesBuilder extends Builder<ExternalRoutesBuilder, ExternalRoutes> {
    ExternalRoutesBuilder destination(String destination);

    ExternalRoutesBuilder nextHop(String nextHop);
}
