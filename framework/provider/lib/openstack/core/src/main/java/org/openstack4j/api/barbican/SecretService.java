package org.openstack4j.api.barbican;

import org.openstack4j.model.barbican.Secret;
import org.openstack4j.model.common.ActionResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by reneschollmeyer on 02.08.17.
 * <p>
 * Secret service provides CRUD capabilities for Secret(s).
 */
public interface SecretService {

    /**
     * Returns a list of secrets filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return list of secrets filtered by filteringParameters
     */
    List<? extends Secret> list(final Map<String, String> filteringParams);

    /**
     * Get a list of currently existing {@link Secret}s.
     *
     * @return this list of {@link Secret}s.
     */
    List<? extends Secret> list(final String name);

    /**
     * Get a specified secret by its ID.
     */
    Secret get(final String secretId);

    /**
     * Delete a specified secret by its ID.
     */
    ActionResponse delete(final String secretId);

    /**
     * Create a secret.
     */
    Secret create(final Secret secret);
}
