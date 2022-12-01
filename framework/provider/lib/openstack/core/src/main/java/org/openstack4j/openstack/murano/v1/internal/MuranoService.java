package org.openstack4j.openstack.murano.v1.internal;


import org.openstack4j.api.Apis;
import org.openstack4j.api.murano.v1.*;

/**
 * This class contains getters for all implementation of the available Murano services
 *
 * @author Nikolay Mahotkin
 */
public class MuranoService extends BaseMuranoServices implements AppCatalogService {

    @Override
    public MuranoEnvironmentService environments() {
        return Apis.get(MuranoEnvironmentService.class);
    }

    @Override
    public MuranoSessionService sessions() {
        return Apis.get(MuranoSessionService.class);
    }

    @Override
    public MuranoApplicationService services() {
        return Apis.get(MuranoApplicationService.class);
    }

    @Override
    public MuranoDeploymentService deployments() {
        return Apis.get(MuranoDeploymentService.class);
    }

    @Override
    public MuranoActionService actions() {
        return Apis.get(MuranoActionService.class);
    }
}
