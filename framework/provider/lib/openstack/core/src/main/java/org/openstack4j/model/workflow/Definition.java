/*
 *
 */
package org.openstack4j.model.workflow;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.workflow.builder.DefinitionBuilder;

import java.util.Date;
import java.util.List;


/**
 * Base interface for all definition objects.
 *
 * @author Renat Akhmerov
 */
public interface Definition extends ModelEntity, Buildable<DefinitionBuilder> {

    /**
     * @return The id of this definition.
     */
    String getId();

    /**
     * @return The name of this definition.
     */
    String getName();

    /**
     * @return The text of this definition.
     */
    String getDefinition();

    /**
     * @return the createdAt
     */
    Date getCreatedAt();

    /**
     * @return the updatedAt
     */
    Date getUpdatedAt();

    /**
     * @return {@code True} if the definition is system (not createdAt by user).
     */
    Boolean isSystem();

    /**
     * @return User tags.
     */
    List<String> getTags();

    /**
     * @return Definition scope.
     */
    Scope getScope();

    /**
     * @return Definition project ID.
     */
    String getProjectId();
}
