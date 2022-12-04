package org.openstack4j.model.identity.v2;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v2.builder.TenantBuilder;

/**
 * Tenant Model class use to group/isolate resources and/or identity objects
 *
 * @author Jeremy Unruh
 * @see <a href="http://docs.openstack.org/api/openstack-identity-service/2.0/content/GET_listTenants_v2.0_tenants_Tenant_Operations.html#GET_listTenants_v2.0_tenants_Tenant_Operations-Response"
 */
public interface Tenant extends ModelEntity, Buildable<TenantBuilder> {

    /**
     * By providing an ID it is assumed this object will be mapped to an existing Tenant.
     *
     * @return the id of the tenant
     */
    String getId();

    /**
     * @return the name of the tenant
     */
    String getName();

    /**
     * @return the description of the tenant
     */
    String getDescription();

    /**
     * @return if the tenant is enabled
     */
    boolean isEnabled();

    void delete();

    void update();

    void addUser(String userId, String roleId);

    void removeUser(String userId, String roleId);

}
