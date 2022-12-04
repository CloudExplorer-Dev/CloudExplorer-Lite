package org.openstack4j.model.heat;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.Link;

import java.util.Date;
import java.util.List;

/**
 * This interface describes the getter-methods (and thus components) of a heat source.
 * All getters map to the possible return values of
 * <code> Get /v1/{tenant_id}/stacks/{stack_name}/{stack_id}/resources/{resource_name}</code>
 *
 * @author Octopus Zhang
 * @see http://developer.openstack.org/api-ref-orchestration-v1.html
 */
public interface Resource extends ModelEntity {

    /**
     * Returns the link of the resource
     *
     * @return the link of the resource
     */
    List<? extends Link> getLinks();

    /**
     * Returns the update time of the resource
     *
     * @return the update time of the resource
     */
    Date getTime();

    /**
     * Returns the type of the resource
     *
     * @return the type of the resource
     */
    String getType();

    /**
     * Returns the type of the resource
     *
     * @return the type of the resource
     */
    String getReason();

    /**
     * Returns the name of the resource
     *
     * @return the name of the resource
     */
    String getResourceName();

    /**
     * Returns the local resource id of the resource
     *
     * @return the local resource id of the resource
     */
    String getLocalReourceId();

    /**
     * Returns the local resource id of the resource
     *
     * @return the local resource id of the resource
     */
    String getResourceStatus();

    /**
     * Returns the local physical resource id of the resource
     *
     * @return the local physical resource id of the resource
     */
    String getPhysicalResourceId();

    /**
     * Returns the resource name which need this resource
     *
     * @return the resource name which need this resource
     */
    List<String> getRequiredBy();

}
