package org.openstack4j.model.senlin;

import org.openstack4j.model.ModelEntity;

import java.util.List;
import java.util.Map;

/**
 * This interface describes the getter-methods (and thus components) of the version of senlin.
 * All getters map to the possible return values of
 * <code> GET /</code>
 *
 * @author lion
 * @see http://developer.openstack.org/api-ref-clustering-v1.html
 */
public interface Version extends ModelEntity {

    /**
     * Returns the status of the senlin version
     *
     * @return the status of the senlin version
     */
    String getStatus();

    /**
     * Returns the id of the senlin version
     *
     * @return the id of the senlin version
     */
    String getId();

    /**
     * Returns the links of the senlin version
     *
     * @return the links of the senlin version
     */
    List<Map<String, String>> getLinks();
}
