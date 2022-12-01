package org.openstack4j.model.senlin;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * This interface describes the getter-methods (and thus components) of a BuildInfo.
 * All getters map to the possible return values of
 * <code> GET /v1/build-info</code>
 *
 * @author lion
 * @see http://developer.openstack.org/api-ref-clustering-v1.html
 */
public interface BuildInfo extends ModelEntity {

    /**
     * Returns the api of the build info
     *
     * @return the api of the build info
     */
    Map<String, String> getApi();

    /**
     * Returns the engine of the build info
     *
     * @return the engine of the build info
     */
    Map<String, String> getEngine();
}
