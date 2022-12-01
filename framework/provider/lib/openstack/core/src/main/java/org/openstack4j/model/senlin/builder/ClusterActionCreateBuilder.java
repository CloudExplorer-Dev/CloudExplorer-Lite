package org.openstack4j.model.senlin.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.senlin.ClusterActionCreate;

import java.util.ArrayList;
import java.util.Map;

/**
 * This interface describes a builder for {@link ClusterActionCreate} objects
 *
 * @author lion
 */
public interface ClusterActionCreateBuilder extends Buildable.Builder<ClusterActionCreateBuilder, ClusterActionCreate> {

    /**
     * Add one or more nodes, as a list, to a cluster
     *
     * @param addNodes the list of node
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder addNodes(Map<String, ArrayList<String>> addNodes);

    /**
     * Delete one or more nodes, as a list, from a cluster
     *
     * @param delNodes the list of node
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder delNodes(Map<String, ArrayList<String>> delNodes);

    /**
     * Enlarge the cluster by count number of nodes
     *
     * @param scaleOut count number of nodes
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder scaleOut(Map<String, String> scaleOut);

    /**
     * Shrink the cluster by count number of nodes
     *
     * @param scaleIn count number of nodes
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder scaleIn(Map<String, String> scaleIn);

    /**
     * Change the size of the cluster
     *
     * @param resize size of the cluster
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder resize(Map<String, String> resize);

    /**
     * Check the health status of a cluster
     *
     * @param check check info
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder check(Map<String, String> check);

    /**
     * Recover a cluster from its current unhealthy status
     *
     * @param recover its current unhealthy status
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder recover(Map<String, String> recover);

    /**
     * Attach a policy to a cluster
     *
     * @param policyAttach parameters for the policy attachment
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder policyAttach(Map<String, String> policyAttach);

    /**
     * Detach a policy from a cluster
     *
     * @param policyDetach parameters for the policy attachment
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder policyDetach(Map<String, String> policyDetach);

    /**
     * Update the policy attachment
     *
     * @param policyUpdate property settings
     * @return ClusterActionCreateBuilder
     */
    ClusterActionCreateBuilder policyUpdate(Map<String, String> policyUpdate);

}
