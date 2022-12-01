package org.openstack4j.model.common;

/**
 * A Resource is a common model which contains an Id, TenantId and Name
 *
 * @author Jeremy Unruh
 */
public interface Resource extends BasicResource {

    /**
     * @return the tenant identifier for this resource
     */
    String getTenantId();

    /**
     * Sets the tenant identifier
     *
     * @param tenantId the tenant id to set
     */
    void setTenantId(String tenantId);

}
