package org.openstack4j.model.murano.v1.domain;

import org.openstack4j.model.ModelEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Nikolay Mahotkin.
 */
public interface EnvironmentDescription extends ModelEntity {
    /**
     * @return services list in given environment.
     */
    List<? extends Application> getServices();

    /**
     * @return the environment name.
     */
    String getName();

    /**
     * @return defaultNetworks info.
     */
    Map<String, Object> getDefaultNetworks();

    /**
     * @return Map object containing env id and type.
     */
    Map<String, Object> getEnvIdentities();
}
