package org.openstack4j.api.murano.v1;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.murano.v1.domain.Environment;

import java.util.List;

public interface MuranoEnvironmentService extends RestService {

    /**
     * List all environments
     *
     * @return list of environments or empty list
     */
    List<? extends Environment> list();

    /**
     * Creates a new environment
     *
     * @param env the environment to create
     * @return the created environment
     */
    Environment create(Environment env);

    /**
     * Gets an environment by ID
     *
     * @param id the environment identifier
     * @return the cluster or null if not found
     */
    Environment get(String id);

    /**
     * Deletes the specified environment
     *
     * @param id the environment identifier
     * @return the action response
     */
    ActionResponse delete(String id);

    /**
     * Renames the specified environment
     *
     * @param id the environment identifier
     * @return the renamed environment
     */
    Environment rename(String id, String name);

}
