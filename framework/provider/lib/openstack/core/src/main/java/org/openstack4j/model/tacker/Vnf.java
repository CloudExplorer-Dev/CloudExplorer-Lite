package org.openstack4j.model.tacker;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.tacker.builder.VnfBuilder;
import org.openstack4j.openstack.tacker.domain.TackerVnfStatus;
import org.openstack4j.openstack.tacker.domain.VnfAttributes;
import org.openstack4j.openstack.tacker.domain.VnfPlacementAttribute;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
public interface Vnf extends ModelEntity, Buildable<VnfBuilder> {
    /**
     * @return id : Unique identifier for the Vnf.
     */
    String getId();

    /**
     * @return name : Human readable name for the Vnf (255 characters limit). Does not have to be unique.
     */
    String getName();

    /**
     * @return tenantId : Owner of the Vnf. Only an administrative user can specify a tenant ID other than its own.
     */
    String getTenantId();

    /**
     * @return description : Human readable description for the Vnf (1024 characters limit).
     */
    String getDescription();

    /**
     * @return attributes
     */
    VnfAttributes getAttributes();

    /**
     * @return managementUrl
     */
    String getManagementUrl();

    /**
     * @return the vnfdId
     */
    String getVnfdId();

    /**
     * @return the errorReason
     */
    String getErrorReason();

    /**
     * @return the vimId
     */
    String getVimId();

    /**
     * @return the instanceId
     */
    String getInstanceId();

    /**
     * @return the status
     */
    TackerVnfStatus getStatus();

    /**
     * @return the placementAttribute
     */
    VnfPlacementAttribute getPlacementAttribute();

}
