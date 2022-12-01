package org.openstack4j.api.manila;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.ShareNetwork;
import org.openstack4j.model.manila.ShareNetworkCreate;
import org.openstack4j.model.manila.ShareNetworkUpdateOptions;
import org.openstack4j.model.manila.builder.ShareNetworkCreateBuilder;

import java.util.List;

/**
 * Share Networks Service for Manila Shared File Systems.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareNetworkService extends RestService {

    /**
     * Creates a share network.
     *
     * @param shareNetworkCreate the share network to create
     * @return the created share network
     */
    ShareNetwork create(ShareNetworkCreate shareNetworkCreate);

    /**
     * Lists all share networks.
     *
     * @return list of all share networks
     */
    List<? extends ShareNetwork> list();

    /**
     * Lists all share networks with details.
     *
     * @return list of all share networks with details
     */
    List<? extends ShareNetwork> listDetails();

    /**
     * Shows details for a share network.
     *
     * @param shareNetworkId the share network ID
     * @return the share network or null if not found
     */
    ShareNetwork get(String shareNetworkId);

    /**
     * Updates a share network.
     *
     * @param shareNetworkId            the share network ID
     * @param shareNetworkUpdateOptions the options to update on the share network
     * @return the updated share network
     */
    ShareNetwork update(String shareNetworkId, ShareNetworkUpdateOptions shareNetworkUpdateOptions);

    /**
     * Deletes a share network.
     *
     * @param shareNetworkId the share network ID
     * @return the action response
     */
    ActionResponse delete(String shareNetworkId);

    /**
     * Adds a security service to a share network.
     *
     * @param shareNetworkId    the share network ID
     * @param securityServiceId the security service ID
     * @return the share network the security service was added to
     */
    ShareNetwork addSecurityService(String shareNetworkId, String securityServiceId);

    /**
     * Removes a security service from a share network.
     *
     * @param shareNetworkId    the share network ID
     * @param securityServiceId the security service ID
     * @return the share network the security service was removed from
     */
    ShareNetwork removeSecurityService(String shareNetworkId, String securityServiceId);

    /**
     * @return a builder to create a share network
     */
    ShareNetworkCreateBuilder shareNetworkCreateBuilder();
}
