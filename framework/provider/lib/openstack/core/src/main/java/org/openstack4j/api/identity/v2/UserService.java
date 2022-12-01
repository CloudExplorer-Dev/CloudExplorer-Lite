package org.openstack4j.api.identity.v2;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v2.Role;
import org.openstack4j.model.identity.v2.User;

import java.util.List;

/**
 * Identity User based Operations
 *
 * @author Jeremy Unruh
 */
public interface UserService {

    /**
     * Gets the detailed User information by ID
     *
     * @param userId the user id
     * @return the user
     */
    User get(String userId);

    /**
     * API added by @ Sandeep Kumar Singh
     * Gets detailed information about a specified user by name
     *
     * @param userName the user name
     * @return the user
     */
    User getByName(String userName);

    /**
     * Lists current users
     *
     * @return List of User
     */
    List<? extends User> list();

    /**
     * List users who are associated with the given tenant identifier
     *
     * @param tenantId the tenant id
     * @return List of User
     */
    List<? extends User> listTenantUsers(String tenantId);

    /**
     * Creates a new User
     *
     * @param tenantId the tenant id
     * @param name     the name of the user
     * @param password the password for the user
     * @param email    the email address of the user
     * @param enabled  if true the user will be immediately enabled
     * @return the newly created user
     */
    User create(String tenantId, String name, String password, String email, boolean enabled);

    /**
     * Creates a User
     *
     * @param user the user to create
     * @return the newly created user
     */
    User create(User user);

    /**
     * Deletes a user by ID
     *
     * @param userId the user id
     * @return the action response
     */
    ActionResponse delete(String userId);

    /**
     * Enables/Disables a user by ID
     *
     * @param userId  the user id
     * @param enabled true to enable the user
     * @return the updated User
     */
    User enableUser(String userId, boolean enabled);

    /**
     * Updates a User
     *
     * @param user the user
     * @return the updated user
     */
    User update(User user);

    /**
     * Changes a password for the specified user by ID
     *
     * @param userId   the user id
     * @param password the new password
     * @return the action response
     */
    ActionResponse changePassword(String userId, String password);

    /**
     * Lists global roles for a specified user. Excludes tenant roles
     *
     * @param userId the user id
     * @return List of Role
     */
    List<? extends Role> listRoles(String userId);

    /**
     * Lists global roles for a specified user. Excludes tenant roles.
     *
     * @param user the user
     * @return List of Role
     */
    List<? extends Role> listRoles(User user);

    /**
     * Lists the tenant roles for a specified user.
     *
     * @param userId   the user id
     * @param tenantId the tenant id
     * @return List of Role
     */
    List<? extends Role> listRolesOnTenant(String userId, String tenantId);

    /**
     * List roles on current tenant (default tenant for the given user)
     *
     * @param user the user
     * @return List of Role
     */
    List<? extends Role> listRolesOnCurrentTenant(User user);

}
