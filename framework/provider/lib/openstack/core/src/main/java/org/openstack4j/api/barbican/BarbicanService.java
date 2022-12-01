package org.openstack4j.api.barbican;

import org.openstack4j.common.RestService;

/**
 * Barbican (Key Management) Operations API
 */
public interface BarbicanService extends RestService {

    /**
     * @return the Container Service API
     */
    ContainerService containers();

    /**
     * @return the Secrets Service API
     */
    SecretService secrets();
}
