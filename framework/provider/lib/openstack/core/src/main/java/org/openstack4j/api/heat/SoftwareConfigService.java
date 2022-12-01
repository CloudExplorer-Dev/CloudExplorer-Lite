package org.openstack4j.api.heat;

import org.openstack4j.api.Builders;
import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.heat.SoftwareConfig;

/**
 * Software Configuration Service for HEAT Orchestration
 *
 * @author Jeremy Unruh
 */
public interface SoftwareConfigService extends RestService {

    /**
     * Creates a new Software Config.  See {@link Builders#softwareConfig()} for creating the model
     *
     * @param sc the software config to create
     * @return the newly created SoftwareConfig
     */
    SoftwareConfig create(SoftwareConfig sc);

    /**
     * Fetches a Software Configuration by ID
     *
     * @param configId the configuration ID
     * @return SoftwareConfig
     */
    SoftwareConfig show(String configId);

    /**
     * Deletes a Software Config by ID
     *
     * @param configId the software config ID to delete
     * @return the action response
     */
    ActionResponse delete(String configId);

}
