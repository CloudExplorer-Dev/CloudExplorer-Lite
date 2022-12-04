package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.Cluster;
import org.openstack4j.model.sahara.NodeGroup;

import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author Ekasit Kijsipongse
 */
public interface ClusterService extends RestService {

    /**
     * List all clusters
     *
     * @return list of images or empty
     */
    List<? extends Cluster> list();

    /**
     * Get a cluster by ID
     *
     * @param clusterId the cluster identifier
     * @return the cluster or null if not found
     */
    Cluster get(String clusterId);


    /**
     * Create a new cluster
     *
     * @param cluster the cluster to create
     * @return the created cluster
     */
    Cluster create(Cluster cluster);

    /**
     * Delete the specified cluster
     *
     * @param clusterId the cluster identifier
     * @return the action response
     */
    ActionResponse delete(String clusterId);

    /**
     * Resize a node group in the specified cluster
     *
     * @param clusterId the cluster identifier
     * @param groupName the name of the node group
     * @param count     the number of instances in the node group
     * @return the updated cluster
     */
    Cluster resizeNodeGroup(String clusterId, String groupName, int count);

    /**
     * Add a new node group in the specified cluster
     *
     * @param clusterId the cluster identifier
     * @param nodeGroup the new node group
     * @return the updated cluster
     */
    Cluster addNodeGroup(String clusterId, NodeGroup nodeGroup);

}
