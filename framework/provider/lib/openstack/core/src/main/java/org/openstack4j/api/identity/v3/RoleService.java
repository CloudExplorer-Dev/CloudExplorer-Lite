package org.openstack4j.api.identity.v3;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Role;
import org.openstack4j.model.identity.v3.RoleAssignment;

import java.util.List;

/**
 * Identity Role based Operations
 */
public interface RoleService extends RestService {

    /**
     * Create a new role
     *
     * @param role the role
     * @return the newly created role
     */
    Role create(Role role);

    /**
     * Create a new role
     *
     * @param name the role name
     * @return the newly created role
     */
    Role create(String name);

    /**
     * Update a role
     *
     * @param role the role set to update
     * @return the updated role
     */
    Role update(Role role);

    /**
     * Delete a role
     *
     * @param roleId the role id
     * @return the ActionResponse
     */
    ActionResponse delete(String roleId);

    /**
     * Lists the global roles
     *
     * @return the list<? extends role>
     */
    List<? extends Role> list();

    /**
     * Get details for a role
     *
     * @param roleId the role id
     * @return the role
     */
    Role get(String roleId);

    /**
     * Get Role(s) filtering by Name
     *
     * @param name the name of the Role to filter by
     * @return the list<? extends Role>
     */
    List<? extends Role> getByName(String name);

    /**
     * list a role assignment list in project context
     *
     * @param projectId the project id
     * @return the list<? extends RoleAssignment>
     */
    List<? extends RoleAssignment> listRoleAssignments(String projectId);


    /**
     * grants a role to a specified user in project context
     *
     * @param projectId the project id
     * @param userId    the user id
     * @param roleId    the role id
     * @return the action response
     */
    ActionResponse grantProjectUserRole(String projectId, String userId, String roleId);

    /**
     * revokes a role to a specified user in project context
     *
     * @param projectId the project id
     * @param userId    the user id
     * @param roleId    the role id
     * @return the action response
     */
    ActionResponse revokeProjectUserRole(String projectId, String userId, String roleId);

    /**
     * checks if a user has a specific role in a given project-context
     *
     * @param projectId the project id
     * @param userId    the user id
     * @param roleId    the role id
     * @return the ActionResponse
     */
    ActionResponse checkProjectUserRole(String projectId, String userId, String roleId);

    /**
     * grants a role to a specified user in domain context
     *
     * @param domainId the domain id
     * @param userId   the user id
     * @param roleId   the role id
     * @return the action response
     */
    ActionResponse grantDomainUserRole(String domainId, String userId, String roleId);

    /**
     * revokes a role to a specified user in domain context
     *
     * @param domainId the domain id
     * @param userId   the user id
     * @param roleId   the role id
     * @return the action response
     */
    ActionResponse revokeDomainUserRole(String domainId, String userId, String roleId);

    /**
     * checks if a user has a specific role in a given domain-context
     *
     * @param domainId the domain id
     * @param userId   the user id
     * @param roleId   the role id
     * @return the ActionResponse
     */
    ActionResponse checkDomainUserRole(String domainId, String userId, String roleId);

    /**
     * grants a role to a specified group in project context
     *
     * @param projectId the project id
     * @param groupId   the group id
     * @param roleId    the role id
     * @return the ActionResponse
     */
    ActionResponse grantProjectGroupRole(String projectId, String groupId, String roleId);

    /**
     * revokes a role from a specified group in project context
     *
     * @param projectId the project id
     * @param groupId   the group id
     * @param roleId    the role id
     * @return the ActionResponse
     */
    ActionResponse revokeProjectGroupRole(String projectId, String groupId, String roleId);

    /**
     * check if a group has a specific role in a given project
     *
     * @param projectId the project id
     * @param groupId   the group id
     * @param roleId    the role id
     * @return the ActionResponse
     */
    ActionResponse checkProjectGroupRole(String projectId, String groupId, String roleId);

    /**
     * grant a role to a specified group in domain context
     *
     * @param domainId the domain id
     * @param groupId  the group id
     * @param roleId   the role id
     * @return the ActionResponse
     */
    ActionResponse grantDomainGroupRole(String domainId, String groupId, String roleId);

    /**
     * revoke a role from a specified group in domain context
     *
     * @param domainId the domain id
     * @param groupId  the group id
     * @param roleId   the role id
     * @return the ActionResponse
     */
    ActionResponse revokeDomainGroupRole(String domainId, String groupId, String roleId);

    /**
     * checks if a group has a specific role in a given domain
     *
     * @param domainId the domain id
     * @param groupId  the group id
     * @param roleId   the role id
     * @return the ActionResponse
     */
    ActionResponse checkDomainGroupRole(String domainId, String groupId, String roleId);
}
