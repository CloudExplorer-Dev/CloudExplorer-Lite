package org.openstack4j.api.senlin;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.senlin.ActionID;
import org.openstack4j.model.senlin.Cluster;
import org.openstack4j.model.senlin.ClusterActionCreate;
import org.openstack4j.model.senlin.ClusterCreate;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of Cluster
 *
 * @author lion
 */
public interface SenlinClusterService {

    /**
     * Gets a list of currently existing {@link Cluster}s.
     *
     * @return the list of {@link Cluster}s
     */
    List<? extends Cluster> list();

    /**
     * returns details of a {@link Cluster}.
     *
     * @param clusterID Id of {@link Cluster}
     * @return Cluster
     */
    Cluster get(String clusterID);

    /**
     * <code>POST /v1/clusters</code><br \>
     * <p>
     * Creates a new {@link Cluster} out of a {@link ClusterCreate} object
     *
     * @param newCluster {@link ClusterCreate} object out of which cluster is to be created
     * @return new {@link Cluster} as returned from the server
     */
    Cluster create(ClusterCreate newCluster);

    /**
     * Deletes the specified {@link Cluster} from the server.
     *
     * @param clusterID Id of {@link Cluster}
     * @return the action response
     */
    ActionResponse delete(String clusterID);

    /**
     * <code>PATCH /v1/clusters/​{cluster_id}​</code><br \>
     * <p>
     * Update a {@link Cluster} out of a {@link ClusterCreate} object
     *
     * @param clusterID  Id of {@link Cluster}
     * @param newCluster {@link ClusterCreate} object out of which stack is to be update
     * @return new {@link Cluster} as returned from the server
     */
    Cluster update(String clusterID, ClusterCreate newCluster);

    /**
     * Service implementation which provides methods for manipulation of action
     *
     * @return Action
     */
    ActionID action(String clusterID, ClusterActionCreate newClusterAction);
}
