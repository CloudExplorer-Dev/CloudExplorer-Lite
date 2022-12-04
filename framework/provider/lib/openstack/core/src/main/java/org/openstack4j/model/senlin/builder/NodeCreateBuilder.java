package org.openstack4j.model.senlin.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.senlin.NodeCreate;

import java.util.Map;

/**
 * This interface describes a builder for {@link NodeCreate} objects
 *
 * @author lion
 */
public interface NodeCreateBuilder extends Buildable.Builder<NodeCreateBuilder, NodeCreate> {

    /**
     * Add the ID or shortID or name of the cluster the node lives in.
     * If not specified, the node created will be an orphaned node.
     *
     * @param clusterID The ID or shortID or name of the cluster
     * @return NodeCreateBuilder
     */
    NodeCreateBuilder clusterID(String clusterID);

    /**
     * Add a set of key and value pairs to associate with the node.
     *
     * @param metadata A set of key and value pairs
     * @return NodeCreateBuilder
     */
    NodeCreateBuilder metadata(Map<String, String> metadata);

    /**
     * Add the name of the node to be created.
     *
     * @param name The name of the node
     * @return NodeCreateBuilder
     */
    NodeCreateBuilder name(String name);

    /**
     * Add the ID or shortID or name of the profile for the node.
     *
     * @param profileID The ID or shortID or name of the profile
     * @return NodeCreateBuilder
     */
    NodeCreateBuilder profileID(String profileID);

    /**
     * Add a string indicating the role this node plays in a cluster.
     *
     * @param role a string indicating the role this node plays
     * @return NodeCreateBuilder
     */
    NodeCreateBuilder role(String role);
}
