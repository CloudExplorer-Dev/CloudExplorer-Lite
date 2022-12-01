package org.openstack4j.api.identity.v3;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Project;

import java.util.List;

/**
 * Identity Project Service
 */
public interface ProjectService extends RestService {

    /**
     * Creates a new Project
     *
     * @param project the project to create
     * @return the new Project including it's id
     */
    Project create(Project project);

    /**
     * Creates a new Project
     *
     * @param domainId    the domain id
     * @param name        the name of the new project
     * @param description the description of the new project
     * @param enabled     the enabled status of the new project
     * @return the newly created project
     */
    Project create(String domainId, String name, String description, boolean enabled);

    /**
     * get detailed information on a project
     *
     * @param projectId the project id
     * @return the project
     */
    Project get(String projectId);

    /**
     * get detailed information about projects machting specified name across all domains
     *
     * @param projectName the project name
     * @return the list of projects matching the name across all domains
     */
    List<? extends Project> getByName(String projectName);

    /**
     * get detailed information about a project specified by project name and domain id
     *
     * @param projectName the project name
     * @param domainId    the domain id
     * @return the project or null if not found
     */
    Project getByName(String projectName, String domainId);

    /**
     * updates an existing project
     *
     * @param project the project set to update
     * @return the updated project
     */
    Project update(Project project);

    /**
     * delete a project by id
     *
     * @param projectId the project id
     * @return the ActionResponse
     */
    ActionResponse delete(String projectId);

    /**
     * list all projects the current token has access to
     *
     * @return list of projects
     */
    List<? extends Project> list();

}
