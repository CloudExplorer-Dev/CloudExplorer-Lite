package org.openstack4j.model.senlin;

import org.openstack4j.model.ModelEntity;

/**
 * This interface describes the getter-methods (and thus components) of a ClusterPolicy.
 * All getters map to the possible return values of
 * <code> GET /v1/clusters/​{cluster_id}​/policies/​{policy_id}​</code>
 *
 * @author lion
 * @see http://developer.openstack.org/api-ref-clustering-v1.html
 */
public interface ClusterPolicy extends ModelEntity {

    /**
     * Returns the id of the ClusterPolicy
     *
     * @return the id of the ClusterPolicy
     */
    String getId();

    /**
     * Returns the policy type of the ClusterPolicy
     *
     * @return the policy type of the ClusterPolicy
     */
    String getPolicyType();

    /**
     * Returns the cluster id of the ClusterPolicy
     *
     * @return the cluster id of the ClusterPolicy
     */
    String getClusterID();

    /**
     * Returns the cluster name of the ClusterPolicy
     *
     * @return the cluster name of the ClusterPolicy
     */
    String getClusterName();

    /**
     * Returns the enabled or not of the ClusterPolicy
     *
     * @return the enabled or not of the ClusterPolicy
     */
    Boolean getEnabled();

    /**
     * Returns the policy id of the ClusterPolicy
     *
     * @return the policy id of the ClusterPolicy
     */
    String getPolicyID();

    /**
     * Returns the policy name of the ClusterPolicy
     *
     * @return the policy name of the ClusterPolicy
     */
    String getPolicyName();
}
