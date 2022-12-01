package org.openstack4j.api.storage;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.VolumeTransfer;

import java.util.List;

/**
 * Related services for managing volume transfers (os-volume-transfer).  This service is used to transfer a volume from one tenant
 * to another.  In OpenStack4j you would create the request authenticated against one tenant and accept the request from the receiving
 * tenant
 *
 * @author Jeremy Unruh
 */
public interface BlockVolumeTransferService extends RestService {

    /**
     * Gets ALL volume transfers in detailed form
     *
     * @return List of Volume Transfers
     */
    List<? extends VolumeTransfer> list();

    /**
     * Gets ALL volume transfers
     *
     * @param detailed if true all fields are populated otherwise just id, volumeId and name
     * @return List of Volume Transfers
     */
    List<? extends VolumeTransfer> list(boolean detailed);

    /**
     * Gets details about a specific transfer
     *
     * @param transferId the transfer identifier
     * @return the transfer details or null
     */
    VolumeTransfer get(String transferId);

    /**
     * Deletes a specific transfer
     *
     * @param transferId the transfer identifier to delete
     * @return the action response
     */
    ActionResponse delete(String transferId);


    /**
     * Creates a new Volume Transfer
     *
     * @param volumeId the identifier of the volume to transfer
     * @return the created volume transfer containing the authorization key
     */
    VolumeTransfer create(String volumeId);

    /**
     * Creates a new Volume Transfer
     *
     * @param volumeId the identifier of the volume to transfer
     * @param name     the name of the transfer (optional)
     * @return the created volume transfer containing the authorization key
     */
    VolumeTransfer create(String volumeId, String name);

    /**
     * Accepts a pending volume transfer
     *
     * @param transferId the identifier of the transfer to accept
     * @param authKey    the auth key of the transfer
     * @return VolumeTransfer detailing the name and volume identifier that was transfer
     */
    VolumeTransfer accept(String transferId, String authKey);
}
