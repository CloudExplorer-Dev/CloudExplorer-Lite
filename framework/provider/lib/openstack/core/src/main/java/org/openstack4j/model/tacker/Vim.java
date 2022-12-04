package org.openstack4j.model.tacker;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.tacker.builder.VimBuilder;
import org.openstack4j.openstack.tacker.domain.AuthCredentials;
import org.openstack4j.openstack.tacker.domain.TackerVimStatus;
import org.openstack4j.openstack.tacker.domain.VimPlacementAttribute;
import org.openstack4j.openstack.tacker.domain.VimProject;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 18, 2016
 */
public interface Vim extends ModelEntity, Buildable<VimBuilder> {

    /**
     * @return id : Unique identifier for the Vim.
     */
    String getId();

    /**
     * @return name : Human readable name for the Vim (255 characters limit). Does not have to be unique.
     */
    String getName();

    /**
     * @return tenantId : Owner of the Vim. Only an administrative user can specify a tenant ID other than its own.
     */
    String getTenantId();

    /**
     * @return description : Human readable description for the Vim (1024 characters limit).
     */
    String getDescription();

    /**
     * @return type
     */
    String getType();

    /**
     * @return isDefault
     */
    Boolean isDefault();

    /**
     * @return authUrl
     */
    String getAuthUrl();

    /**
     * @return authCredentials
     */
    AuthCredentials getAuthCredentials();

    /**
     * @return vimProject
     */
    VimProject getVimProject();

    /**
     * @return the status
     */
    TackerVimStatus getStatus();

    /**
     * @return the placementAttribute
     */
    VimPlacementAttribute getPlacementAttribute();

}
