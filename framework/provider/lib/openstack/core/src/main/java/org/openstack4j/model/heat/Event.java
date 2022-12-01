package org.openstack4j.model.heat;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.Link;

import java.util.Date;
import java.util.List;

/**
 * This interface describes the getter-methods (and thus components) of a event.
 * All getters map to the possible return values of
 * <code> Get /v1/{tenant_id}/stacks/{stack_name}/{stack_id}/resources/{resource_name}/events/{event_id}</code>
 *
 * @author Octopus Zhang
 * @see http://developer.openstack.org/api-ref-orchestration-v1.html
 */
public interface Event extends ModelEntity {

    /**
     * Returns the id of the event
     *
     * @return the id of the event
     */
    String getId();

    /**
     * Returns the resource_name of the event
     *
     * @return the resource_name of the event
     */
    String getResourceName();

    /**
     * Returns the event_time of the event
     *
     * @return the event_time of the event
     */
    Date getTime();

    /**
     * Returns the logical_resource_id of the event
     *
     * @return the logical_resource_id of the event
     */
    String getLocalReourceId();

    /**
     * Returns the resource_status_reason of the event
     *
     * @return the resource_status_reason of the event
     */
    String getReason();

    /**
     * Returns the resource_status of the event
     *
     * @return the resource_status of the event
     */
    String getResourceStatus();

    /**
     * Returns the physical_resource_id of the event
     *
     * @return the physical_resource_id of the event
     */
    String getPhysicalResourceId();

    /**
     * Returns the links of the event
     *
     * @return the links of the event
     */
    List<? extends Link> getLinks();

    /**
     * Returns the resource type of the event
     *
     * @return the resource type of the event
     */
    String getResourceType();

    /**
     * Returns the resource properties of the event
     *
     * @return the resource properties of the event
     */
    Object getResourceProperties();

}
