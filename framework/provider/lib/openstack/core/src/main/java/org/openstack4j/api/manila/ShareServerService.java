package org.openstack4j.api.manila;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.ShareServer;

import java.util.List;

/**
 * Share Server Service for Manila Shared Filesystems.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareServerService extends RestService {
    /**
     * Lists all share servers.
     *
     * @return a list containing all share servers
     */
    List<? extends ShareServer> list();

    /**
     * Shows details for a share server.
     *
     * @param shareServerId the share server ID
     * @return the share server or null if not found
     */
    ShareServer get(String shareServerId);

    /**
     * Deletes a share server.
     *
     * @param shareServerId the share server ID
     * @return the action response
     */
    ActionResponse delete(String shareServerId);
}
