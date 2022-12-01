package org.openstack4j.openstack.identity.v2.functions;

import com.google.common.base.Function;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v2.Access.Service;

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
        return input.getServiceType();
    }

}
