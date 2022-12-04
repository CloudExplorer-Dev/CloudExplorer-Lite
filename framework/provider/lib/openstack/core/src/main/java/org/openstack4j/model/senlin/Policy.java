package org.openstack4j.model.senlin;

import org.openstack4j.model.ResourceEntity;

import java.util.Date;
import java.util.Map;

/**
 * This interface describes the getter-methods (and thus components) of a Policy.
 * All getters map to the possible return values of
 * <code> GET /v1/policies/​{policy_id}​</code>
 *
 * @author lion
 * @see http://developer.openstack.org/api-ref-clustering-v1.html
 */
public interface Policy extends ResourceEntity {

    /**
     * Returns the domain of the policy
     *
     * @return the domain of the policy
     */
    String getDomain();

    /**
     * Returns the project of the policy
     *
     * @return the project of the policy
     */
    String getProject();

    /**
     * Returns the user of the policy
     *
     * @return the user of the policy
     */
    String getUser();

    /**
     * Returns the data of the policy
     *
     * @return the data of the policy
     */
    Map<String, Object> getData();

    /**
     * Returns the spec of the policy
     *
     * @return the spec of the policy
     */
    Map<String, Object> getSpec();

    /**
     * Returns the type of the policy
     *
     * @return the type of the policy
     */
    String getType();

    /**
     * Returns the created at time of the policy
     *
     * @return the created at time of the policy
     */
    Date getCreatedAt();

    /**
     * Returns the updated at time of the policy
     *
     * @return the updated at time of the policy
     */
    Date getUpdatedAt();
}
