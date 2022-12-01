package org.openstack4j.model.common.resolvers;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v3.Service;

import java.util.SortedSet;

/**
 * Resolves each service to the lowest version which we consider most stable and
 * tested
 *
 * @author Jeremy Unruh
 */
public final class StableServiceVersionResolver implements ServiceVersionResolver {

    public static final StableServiceVersionResolver INSTANCE = new StableServiceVersionResolver();

    private StableServiceVersionResolver() {
    }

    @Override
    public Service resolveV3(ServiceType type, SortedSet<? extends Service> services) {
        return services.first();
    }

    @Override
    public Access.Service resolveV2(ServiceType type, SortedSet<? extends Access.Service> services) {
        return services.first();
    }

}
