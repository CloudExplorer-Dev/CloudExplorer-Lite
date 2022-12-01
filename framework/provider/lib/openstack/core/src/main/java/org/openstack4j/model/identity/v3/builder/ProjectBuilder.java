package org.openstack4j.model.identity.v3.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;

import java.util.List;
import java.util.Map;

/**
 * A Builder which creates a identity v3 project
 */
public interface ProjectBuilder extends Builder<ProjectBuilder, Project> {

    /**
     * @see Project#getId()
     */
    ProjectBuilder id(String id);

    /**
     * @see Project#getDomainId()
     */
    ProjectBuilder domainId(String domainId);

    /**
     * Accepts an existing domain and uses its id
     *
     * @see Project#getDomainId()
     */
    ProjectBuilder domain(Domain domain);

    /**
     * @see Project#getDescription
     */
    ProjectBuilder description(String description);

    /**
     * @see Project#isEnabled()
     */
    ProjectBuilder enabled(boolean enabled);

    /**
     * @see Project#getName()
     */
    ProjectBuilder name(String name);

    ProjectBuilder options(Map<String, String> options);

    /**
     * @see Project#getLinks()
     */
    ProjectBuilder links(Map<String, String> links);

    /**
     * @see Project#getParentId()
     */
    ProjectBuilder parentId(String parentId);

    /**
     * @see Project#getSubtree()
     */
    ProjectBuilder subtree(String subtree);

    /**
     * @see Project#getParents()
     */
    ProjectBuilder parents(String parents);

    /**
     * @see Project#getExtra(String)
     */
    ProjectBuilder setExtra(String name, String value);

    /**
     * @see Project#getTags()
     */
    ProjectBuilder setTags(List<String> tags);
}
