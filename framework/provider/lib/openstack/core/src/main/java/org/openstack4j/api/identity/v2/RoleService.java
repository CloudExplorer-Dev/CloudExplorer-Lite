package org.openstack4j.api.identity.v2;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v2.Role;

import java.util.List;

/**
 * Identity Role based Operations
 *
 * @author Jeremy Unruh
 */
public interface RoleService extends RestService {

    /**
     * Adds a global role to a user
     *
     * @param userId the user id
     * @param roleId the role id to add
     * @return the action response
     */
    ActionResponse addUserRole(String userId, String roleId);

    /**
     * Adds a tenant based role to a user
     *
     * @param tenantId the tenant id
     * @param userId   the user id
     * @param roleId   the role id
     * @return the action response
     */
    ActionResponse addUserRole(String tenantId, String userId, String roleId);

    /**
     * Removes a global role from a user
     *
     * @param userId the user id
     * @param roleId the role id
     * @return the action response
     */
    ActionResponse removeUserRole(String userId, String roleId);

    /**
     * Removes the user role from a user and the associated tenant
     *
     * @param tenantId the tenant id
     * @param userId   the user id
     * @param roleId   the role id
     * @return the action response
     */
    ActionResponse removeUserRole(String tenantId, String userId, String roleId);

    /**
     * Lists the global roles
     *
     * @return the list<? extends role>
     */
    List<? extends Role> list();

    /**
     * List roles for user.
     *
     * @param userId the user id
     * @return the list<? extends role>
     */
    List<? extends Role> listRolesForUser(String userId);

    /**
     * List roles for user.
     *
     * @param userId   the user id
     * @param tenantId the tenant id
     * @return the list<? extends role>
     */
    List<? extends Role> listRolesForUser(String userId, String tenantId);

    /**
     * Delete a role by it's ID
     *
     * @param roleId the role id
     * @param the    action response
     */
    ActionResponse delete(String roleId);

    /**
     * Gets a role by ID
     *
     * @param roleId the role id
     * @return the role
     */
    Role get(String roleId);

    /**
     * Gets a Role by Name
     *
     * @param name the name of the Role
     * @return the Role or null if not found
     */
    Role getByName(String name);

    /**
     * Creates a new Role
     *
     * @param name the name of the role
     * @return the role
     */
    Role create(String name);

}
