package org.openstack4j.api.senlin;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.senlin.ActionID;
import org.openstack4j.model.senlin.Node;
import org.openstack4j.model.senlin.NodeActionCreate;
import org.openstack4j.model.senlin.NodeCreate;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of Node
 *
 * @author lion
 */
public interface SenlinNodeService {

    /**
     * Gets a list of currently existing {@link Node}s.
     *
     * @return the list of {@link Node}s
     */
    List<? extends Node> list();

    /**
     * <code>POST /v1/nodes</code><br \>
     * <p>
     * Creates a new {@link Node} out of a {@link NodeCreate} object
     *
     * @param newNode {@link NodeCreate} object out of which node is to be created
     * @return new {@link Node} as returned from the server
     */
    Node create(NodeCreate newNode);

    /**
     * returns details of a {@link Node}.
     *
     * @param nodeID Id of {@link Node}
     * @return Node
     */
    Node get(String nodeID);

    /**
     * returns details of a {@link Node}
     *
     * @param nodeID      Id of {@link Node}
     * @param showDetails {@literal true} to retrieve the properties about the physical object that backs the node
     * @return Node
     */
    Node get(String nodeID, boolean showDetails);

    /**
     * Deletes the specified {@link Node} from the server.
     *
     * @param nodeID Id of {@link Node}
     * @return the action response
     */
    ActionResponse delete(String nodeID);

    /**
     * <code>PATCH /v1/nodes/​{node_id}​</code><br \>
     * <p>
     * Update a {@link Node} out of a {@link NodeCreate} object
     *
     * @param nodeID  Id of {@link Node}
     * @param newNode {@link NodeCreate} object out of which stack is to be update
     * @return new {@link Node} as returned from the server
     */
    Node update(String nodeID, NodeCreate newNode);

    /**
     * Service implementation which provides methods for manipulation of action
     *
     * @return Action
     */
    ActionID action(String nodeID, NodeActionCreate newNodeAction);
}
