package org.openstack4j.api.manila;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.ExtraSpecs;
import org.openstack4j.model.manila.ShareType;
import org.openstack4j.model.manila.ShareTypeAccess;
import org.openstack4j.model.manila.ShareTypeCreate;

import java.util.List;

/**
 * Share Types Service for Manila Shared Filesystems.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareTypeService extends RestService {
    /**
     * Creates a share type.
     *
     * @param shareTypeCreate the share type to create
     * @return the created share type
     */
    ShareType create(ShareTypeCreate shareTypeCreate);

    /**
     * Lists all share types.
     *
     * @return a list of all share types
     */
    List<? extends ShareType> list();

    /**
     * Lists default share types.
     *
     * @return the default share types
     */
    ShareType listDefaults();

    /**
     * Deletes a share type.
     *
     * @param shareTypeId the share type ID
     * @return the action response
     */
    ActionResponse delete(String shareTypeId);

    /**
     * Lists the extra specifications for a share type.
     *
     * @param shareTypeId the share type ID
     * @return a list of the extra specifications of a share type
     */
    ExtraSpecs listExtraSpecs(String shareTypeId);

    /**
     * Sets an extra specification for the share type.
     *
     * @param shareTypeId the share type ID
     * @param extraSpecs  the extra specifications to set
     * @return the extra specifications of the share type
     */
    ExtraSpecs setExtraSpec(String shareTypeId, ExtraSpecs extraSpecs);

    /**
     * Unsets an extra specification for the share type.
     *
     * @param shareTypeId  the share type ID
     * @param extraSpecKey the key of the extra specification to unset
     * @return the action response
     */
    ActionResponse unsetExtraSpec(String shareTypeId, String extraSpecKey);

    /**
     * Adds share type access for a project.
     *
     * @param shareTypeId the share type ID
     * @param projectId   the project ID
     * @return the action response
     */
    ActionResponse addShareTypeAccess(String shareTypeId, String projectId);

    /**
     * Removes share type access from a project.
     *
     * @param shareTypeId the share type ID
     * @param projectId   the project ID
     * @return the action response
     */
    ActionResponse removeShareTypeAccess(String shareTypeId, String projectId);

    /**
     * Shows access details for a share type.
     *
     * @param shareTypeId the share type ID
     * @return a list of all access etails of a share type
     */
    List<? extends ShareTypeAccess> shareTypeAccessDetails(String shareTypeId);
}
