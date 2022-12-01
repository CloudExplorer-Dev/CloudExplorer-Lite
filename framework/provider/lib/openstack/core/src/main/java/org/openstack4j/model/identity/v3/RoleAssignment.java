package org.openstack4j.model.identity.v3;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * identity v3 role assignment class
 *
 * @see <a href="http://developer.openstack.org/api-ref-identity-v3.html#roles-v3">API reference</a>
 */
public interface RoleAssignment extends ModelEntity {

    /**
     * Get role id for role assignment
     *
     * @return the id of the role
     */
    String getRoleId();

    /**
     * Get user id for role assignment
     *
     * @return the id of the user
     */
    String getUserId();

    /**
     * Get group id for role assignment
     *
     * @return the id of the group
     */
    String getGroupId();

    /**
     * Get scoped domain id for role assignment
     *
     * @return the id of the domain
     */
    String getDomainId();

    /**
     * Get scoped project id for role assignment
     *
     * @return the id of the project
     */
    String getProjectId();

    /**
     * @return the links of the role assignment
     */
    Map<String, String> getLinks();

}
