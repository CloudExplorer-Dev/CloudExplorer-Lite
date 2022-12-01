package org.openstack4j.api.compute.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.compute.InstanceAction;

import java.util.List;

/**
 * API to list executed instance actions.
 *
 * @author Christian Banse
 */
public interface InstanceActionsService extends RestService {

    /**
     * List the executed actions on the specified {@code serverId}
     *
     * @param serverId the server id
     * @return List of instance actions
     */
    List<? extends InstanceAction> list(String serverId);

    /**
     * Shows information about a specified instance action
     *
     * @param serverId  the server id
     * @param requestId the request identifier
     * @return the instance action
     */
    InstanceAction get(String serverId, String requestId);

}
