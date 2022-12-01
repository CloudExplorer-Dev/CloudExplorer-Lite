package org.openstack4j.openstack.barbican.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.barbican.BarbicanService;
import org.openstack4j.api.barbican.ContainerService;
import org.openstack4j.api.barbican.SecretService;

/**
 * This class contains getters for all implementation of the available Barbican services
 */
public class BarbicanServiceImpl extends BaseBarbicanServices implements BarbicanService {

    @Override
    public ContainerService containers() {
        return Apis.get(ContainerService.class);
    }

    @Override
    public SecretService secrets() {
        return Apis.get(SecretService.class);
    }
}
