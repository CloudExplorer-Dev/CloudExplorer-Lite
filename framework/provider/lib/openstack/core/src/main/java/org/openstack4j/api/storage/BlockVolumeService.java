package org.openstack4j.api.storage;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeType;
import org.openstack4j.model.storage.block.VolumeTypeEncryption;
import org.openstack4j.model.storage.block.VolumeUploadImage;
import org.openstack4j.model.storage.block.options.UploadImageData;

import java.util.List;
import java.util.Map;

/**
 * Manages Volumes and Volume Type based operations against Block Storage (Cinder)
 *
 * @author Jeremy Unruh
 */
public interface BlockVolumeService extends RestService {

    /**
     * The volume type defines the characteristics of a volume
     *
     * @return List of VolumeType entities
     */
    List<? extends VolumeType> listVolumeTypes();

    /**
     * Deletes the specified VolumeType
     *
     * @param volumeTypeId the volume type identifier
     */
    void deleteVolumeType(String volumeTypeId);

    /**
     * Creates a new volume type with the specified name
     *
     * @param volumeType the volumeType for create
     * @return the created volume type
     */
    VolumeType createVolumeType(VolumeType volumeType);

    /**
     * Creates a new encryption with the specified instance for the specified volume type
     *
     * @param volumeTypeId         the volume type identifier
     * @param volumeTypeEncryption the encryption to create
     * @return the created volume type encryption
     */
    VolumeTypeEncryption createVolumeTypeEncryption(String volumeTypeId, VolumeTypeEncryption volumeTypeEncryption);

    /**
     * Retrieves the encryption for the specified volume type
     *
     * @param volumeTypeId the volume type identifier
     * @return the encryption
     */
    VolumeTypeEncryption getVolumeTypeEncryption(String volumeTypeId);

    /**
     * Deletes the specified volume type encryption for the specified volume type
     *
     * @param volumeTypeId the volume type identifier
     * @param encryptionId the encryption identifier
     */
    void deleteVolumeTypeEncryption(String volumeTypeId, String encryptionId);

    /**
     * Lists summary information for all Block Storage volumes that the tenant who submits the request can access.
     *
     * @return List of Volumes
     */
    List<? extends Volume> list();

    /**
     * Returns list of Block Storage volumes filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends Volume> list(Map<String, String> filteringParams);

    /**
     * Lists all Block Storage volumes for all tenants.
     *
     * @return List of all Volumes
     */
    List<? extends Volume> listAll();

    /**
     * Gets a Block Storage volume by ID
     *
     * @param volumeId the volume identifier
     * @return the volume or null if not found
     */
    Volume get(String volumeId);

    /**
     * Deletes the specified volume
     *
     * @param volumeId the volume identifier
     * @return the action response
     */
    ActionResponse delete(String volumeId);

    /**
     * Attempt forced removal of volume, regardless of the state.
     * It's dangerous but useful. It's not 100% success.
     *
     * @param volumeId the volume id
     * @return the action response
     */
    ActionResponse forceDelete(String volumeId);

    /**
     * Resets the specified volume status.
     *
     * @param volumeId the volume id
     * @param status   new volume status
     * @return the action response
     */
    ActionResponse resetState(String volumeId, Volume.Status status);

    /**
     * Extends the specified volume size.
     *
     * @param volumeId the volume id
     * @param newSize  new volume size
     * @return the action response
     */
    ActionResponse extend(String volumeId, Integer newSize);

    /**
     * Creates a new Block Storage Volume
     *
     * @param volume the volume for create
     * @return the created volume
     */
    Volume create(Volume volume);

    /**
     * Uploads a volume to the image service
     *
     * @param volumeId the volume identifier to upload
     * @param data     the data about the volume being uploaded (required)
     * @return the volume upload image containing the current status
     */
    VolumeUploadImage uploadToImage(String volumeId, UploadImageData data);

    /**
     * OpenStack only allows name or description to be updated. This call enforces that based on the API docs.
     *
     * @param volumeId    the volume id
     * @param name        the name to update (null indicates no name update)
     * @param description the description to update (null indicates no description update)
     * @return the action response
     */
    ActionResponse update(String volumeId, String name, String description);

    /**
     * migrate a volume to another host and service
     *
     * @param volumeId    the volume id
     * @param hostService the destination host and service ,like kvmnode002021.cnsuning.com@lvmdriver
     * @return the action response
     */
    ActionResponse migrate(String volumeId, String hostService, boolean forceHostCopy);

    /**
     * Returns the API used to transfer a Volume from one tenant/project to another
     *
     * @return the volume transfer service
     */
    BlockVolumeTransferService transfer();

    /**
     * Updates volume read-only access-mode flag
     *
     * @param volumeId ID of volume to update
     * @param readonly enables or disables update of volume to read-only access mode
     * @return the action response
     */
    ActionResponse readOnlyModeUpdate(String volumeId, boolean readonly);

    /**
     * <br/>Description:Attaches a volume to a server.
     * You should set instance_uuid or host_name.
     * Volume status must be available.
     * <p>Author:Wang Ting/王婷</p>
     */
    ActionResponse attach(String volumeId, String instanceId, String mountpoint, String hostName);

    /**
     * <br/>Description:Forces a volume to detach.
     * <p>Author:Wang Ting/王婷</p>
     */
    ActionResponse forceDetach(String volumeId, String initiator, String attachmentId);

    /**
     * Detach volume from server
     *
     * @author capitek-xuning（首信科技-徐宁）
     */
    ActionResponse detach(String volumeId, String attachmentId);

    /**
     * Update volume bootable status.
     *
     * @param volumeId the volume id
     * @param bootable Enables or disables the bootable attribute
     * @return the action response
     */
    ActionResponse bootable(String volumeId, Boolean bootable);
}
