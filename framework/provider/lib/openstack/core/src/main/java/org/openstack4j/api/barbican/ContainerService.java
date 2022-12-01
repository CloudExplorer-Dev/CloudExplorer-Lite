package org.openstack4j.api.barbican;

import org.openstack4j.model.barbican.Container;
import org.openstack4j.model.common.ActionResponse;

import java.util.List;
import java.util.Map;

/**
 * Container service provides CRUD capabilities for Containers(s).
 */
public interface ContainerService {

    /**
     * Returns list of containers filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return list of containers filtered by filteringParams
     */
    List<? extends Container> list(final Map<String, String> filteringParams);

    /**
     * Gets a list of currently existing {@link Container}s for a specified container.
     *
     * @return the list of {@link Container}s
     */
    List<? extends Container> list(final String name);

    /**
     * Get the specified container by ID
     */
    Container get(final String containerId);

    /**
     * Delete the specified container by ID
     */
    ActionResponse delete(final String containerId);

    /**
     * Create a container.
     */
    Container create(final Container container);
}
