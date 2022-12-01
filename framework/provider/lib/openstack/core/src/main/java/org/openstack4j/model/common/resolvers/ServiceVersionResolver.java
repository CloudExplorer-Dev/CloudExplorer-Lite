package org.openstack4j.model.common.resolvers;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v3.Service;

import java.util.SortedSet;

/**
 * Responsible for resolving a Service to a specific version.
 *
 * @author Jeremy Unruh
 */
public interface ServiceVersionResolver {

    /**
     * Resolves to a single identity v3 service for the given {@code ServiceType} and set of associated {@code services}.
     * <p>
     * For example if a deployment contains Compute/Nova version 2 and 2.1 then resolve would be invoked with the
     * params of {@link ServiceType#COMPUTE} and a set of 2 services (Compute 2 and Compute 2.1).  The resolver is
     * responsible for picking the appropriate version
     *
     * @param type     the type of {@code services}
     * @param services the sorted by version set of service(s)
     * @return a single service
     */
    Service resolveV3(ServiceType type, SortedSet<? extends Service> services);

    /**
     * Resolves to a single identity v2 service for the given {@code ServiceType} and set of associated {@code services}.
     * <p>
     * For example if a deployment contains Compute/Nova version 2 and 2.1 then resolve would be invoked with the
     * params of {@link ServiceType#COMPUTE} and a set of 2 services (Compute 2 and Compute 2.1).  The resolver is
     * responsible for picking the appropriate version
     *
     * @param type     type the type of {@code services}
     * @param services the sorted by version set of service(s)
     * @return a single service
     */
    Access.Service resolveV2(ServiceType type, SortedSet<? extends Access.Service> services);

}
