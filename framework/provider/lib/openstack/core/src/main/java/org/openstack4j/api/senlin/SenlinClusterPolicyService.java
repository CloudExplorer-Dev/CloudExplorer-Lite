package org.openstack4j.api.senlin;

import org.openstack4j.model.senlin.ClusterPolicy;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of ClusterPolicy
 *
 * @author lion
 */
public interface SenlinClusterPolicyService {

    /**
     * Gets a list of currently existing {@link ClusterPolicy}s.
     *
     * @return the list of {@link ClusterPolicy}s
     */
    List<? extends ClusterPolicy> list(String clusterID);

    /**
     * returns details of a {@link ClusterPolicy}.
     *
     * @param clusterID Id of Cluster
     * @param policyID  Id of {@link ClusterPolicy}
     * @return ClusterPolicy
     */
    ClusterPolicy get(String clusterID, String policyID);
}
