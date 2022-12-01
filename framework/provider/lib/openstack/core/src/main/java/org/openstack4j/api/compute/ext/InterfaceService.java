package org.openstack4j.api.compute.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.InterfaceAttachment;

import java.util.List;

/**
 * API to Create, list, get details for, and delete port interfaces on a Server Instance
 *
 * @author Jeremy Unruh
 */
public interface InterfaceService extends RestService {

    /**
     * Creates and uses a port interface to attach the port to a server instance.
     *
     * @param serverId the server id
     * @param portId   the port id to attach
     * @return the attached interface
     */
    InterfaceAttachment create(String serverId, String portId);

    /**
     * List the port interfaces for the specified {@code serverId}
     *
     * @param serverId the server id
     * @return List of interface attachments
     */
    List<? extends InterfaceAttachment> list(String serverId);

    /**
     * Shows information about a specified port interface
     *
     * @param serverId     the server id
     * @param attachmentId the attachment identifier
     * @return the interface attachment
     */
    InterfaceAttachment get(String serverId, String attachmentId);

    /**
     * Detaches a specified port interface
     *
     * @param serverId     the server id
     * @param attachmentId the attachment identifier
     * @return the action response indicating success or failure
     */
    ActionResponse detach(String serverId, String attachmentId);

}
