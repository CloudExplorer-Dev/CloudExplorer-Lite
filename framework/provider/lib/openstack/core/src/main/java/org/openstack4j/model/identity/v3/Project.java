package org.openstack4j.model.identity.v3;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v3.builder.ProjectBuilder;

import java.util.List;
import java.util.Map;

/**
 * Project Model class use to group/isolate resources and/or identity objects
 *
 * @see <a href="http://developer.openstack.org/api-ref-identity-v3.html#projects-v3">API reference</a>
 */
public interface Project extends ModelEntity, Buildable<ProjectBuilder> {

    /**
     * Globally unique within the owning domain.
     *
     * @return the Id of the project
     */
    String getId();

    /**
     * @return the DomainId of the project
     */
    String getDomainId();

    /**
     * @return the domain
     */
    Domain getDomain();

    /**
     * @return the Description of the project
     */
    String getDescription();

    /**
     * @return the Name of the project
     */
    String getName();

    /**
     * @return the links of the project
     */
    Map<String, String> getLinks();

    /**
     * @return the parentId of the project
     */
    String getParentId();

    /**
     * @return the subtree of the project
     */
    String getSubtree();

    /**
     * @return the parents of the project
     */
    String getParents();

    /**
     * @return if the project is enabled
     */
    boolean isEnabled();

    /**
     * @return value for the given key
     */
    String getExtra(String key);

    /**
     * Project options.
     */
    Map<String, String> getOptions();

    /**
     * @return list of tags
     */
    List<String> getTags();
}
