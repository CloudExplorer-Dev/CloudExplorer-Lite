package org.openstack4j.model.senlin;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * This interface describes the getter-methods (and thus components) of a PolicyType.
 * All getters map to the possible return values of
 * <code> GET /v1/policy-types/​{policy_type}​</code>
 *
 * @author lion
 * @see http://developer.openstack.org/api-ref-clustering-v1.html
 */
public interface PolicyType extends ModelEntity {

    /**
     * Returns the name of the PolicyType
     *
     * @return the name of the PolicyType
     */
    String getName();

    /**
     * Returns the schema of the PolicyType
     *
     * @return the schema of the PolicyType
     */
    Map<String, Object> getSchema();
}
