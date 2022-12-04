package org.openstack4j.api.murano.v1;

import org.openstack4j.common.RestService;

/**
 * (Murano) App catalog Operations API
 *
 * @author Nikolay Mahotkin
 */
public interface AppCatalogService extends RestService {

    /**
     * Environments Service API
     *
     * @return the environments service
     */
    MuranoEnvironmentService environments();

    /**
     * Murano Sessions API
     *
     * @return the sessions API
     */
    MuranoSessionService sessions();

    /**
     * Murano Services API
     *
     * @return the servces API
     */
    MuranoApplicationService services();

    /**
     * Murano deployments API
     *
     * @return the deployments API
     */
    MuranoDeploymentService deployments();

    /**
     * Murano actions API
     *
     * @return the actions API
     */
    MuranoActionService actions();
}
