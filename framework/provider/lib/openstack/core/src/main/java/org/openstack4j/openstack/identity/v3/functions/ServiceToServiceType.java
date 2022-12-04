package org.openstack4j.openstack.identity.v3.functions;

import com.google.common.base.Function;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v3.Service;

/**
 * A Function which takes a ServiceCatalog -> Service and returns the corresponding common ServiceType
 *
 * @author Jeremy Unruh
 */
public class ServiceToServiceType implements Function<Service, ServiceType> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceType apply(Service input) {
        ServiceType serviceType = ServiceType.forName(input.getType());
        if (serviceType == ServiceType.UNKNOWN)
            serviceType = ServiceType.forName(input.getName());
        return serviceType;
    }

}
