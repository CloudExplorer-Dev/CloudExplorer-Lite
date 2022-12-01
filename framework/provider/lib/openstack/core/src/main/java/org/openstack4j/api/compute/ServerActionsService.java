package org.openstack4j.api.compute;

import org.openstack4j.common.RestService;
import org.openstack4j.model.compute.ServerAction;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of server actions
 *
 * @author sujit sah
 */
public interface ServerActionsService extends RestService {
    /**
     * Gets a list of currently existing {@link ServerAction}s for a specified server.
     *
     * @param serverId the id of server
     * @return the list of {@link ServerAction}s
     */
    List<? extends ServerAction> list(String serverId);

    /**
     * Gets the server action associated with specific request id for the specified server
     *
     * @param serverId the id of server
     * @param request  id of specific action for the specified server
     * @return the server action detail
     */
    ServerAction show(String serverId, String requestId);

}
