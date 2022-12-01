package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.senlin.SenlinNodeService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.senlin.ActionID;
import org.openstack4j.model.senlin.Node;
import org.openstack4j.model.senlin.NodeActionCreate;
import org.openstack4j.model.senlin.NodeCreate;
import org.openstack4j.openstack.senlin.domain.SenlinActionID;
import org.openstack4j.openstack.senlin.domain.SenlinNode;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class contains getters for all implementation of the available node services
 *
 * @author lion
 */
public class SenlinNodeServiceImpl extends BaseSenlinServices implements SenlinNodeService {

    @Override
    public List<? extends Node> list() {
        return get(SenlinNode.Node.class, uri("/nodes")).execute().getList();
    }

    @Override
    public Node create(NodeCreate newNode) {
        checkNotNull(newNode);
        return post(SenlinNode.class, uri("/nodes")).entity(newNode).execute();
    }

    @Override
    public Node get(String nodeID) {
        return get(nodeID, false);    //false for backward compatibility
    }

    @Override
    public Node get(final String nodeID, final boolean showDetails) {
        checkNotNull(nodeID);
        //see at https://developer.openstack.org/api-ref/clustering/?expanded=show-node-details-detail
        return get(SenlinNode.class, uri("/nodes/%s", nodeID)).param("show_details", showDetails).execute();
    }

    @Override
    public ActionResponse delete(String nodeID) {
        checkNotNull(nodeID);
        return deleteWithResponse(uri("/nodes/%s", nodeID)).execute();
    }

    @Override
    public Node update(String nodeID, NodeCreate newNode) {
        checkNotNull(nodeID);
        checkNotNull(newNode);
        return patch(SenlinNode.class, uri("/nodes/%s", nodeID)).entity(newNode).execute();
    }

    @Override
    public ActionID action(String nodeID, NodeActionCreate newNodeAction) {
        checkNotNull(nodeID);
        checkNotNull(newNodeAction);
        return post(SenlinActionID.class, uri("/nodes/%s/actions", nodeID)).entity(newNodeAction).execute();
    }

}
