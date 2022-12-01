package org.openstack4j.model.senlin;

import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * This interface describes the getter-methods (and thus components) of a Event.
 * All getters map to the possible return values of
 * <code> GET /v1/events/​{event_id}​</code>
 *
 * @author lion
 * @see http://developer.openstack.org/api-ref-clustering-v1.html
 */
public interface Event extends ModelEntity {

    /**
     * Returns the id of the Event
     *
     * @return the id of the Event
     */
    String getId();

    /**
     * Returns the action of the Event
     *
     * @return the action of the Event
     */
    String getAction();

    /**
     * Returns the cluster id of the Event
     *
     * @return the cluster id of the Event
     */
    String getClusterID();

    /**
     * Returns the level of the Event
     *
     * @return the level of the Event
     */
    Integer getLevel();

    /**
     * Returns the obj id of the Event
     *
     * @return the obj id of the Event
     */
    String getObjID();

    /**
     * Returns the obj name of the Event
     *
     * @return the obj name of the Event
     */
    String getObjName();

    /**
     * Returns the obj type of the Event
     *
     * @return the obj type of the Event
     */
    String getObjType();

    /**
     * Returns the project of the Event
     *
     * @return the project of the Event
     */
    String getProject();

    /**
     * Returns the status of the Event
     *
     * @return the status of the Event
     */
    String getStatus();

    /**
     * Returns the status reason of the Event
     *
     * @return the status reason of the Event
     */
    String getStatusReason();

    /**
     * Returns the timestamp of the Event
     *
     * @return the timestamp of the Event
     */
    Date getTimestamp();

    /**
     * Returns the user of the Event
     *
     * @return the user of the Event
     */
    String getUser();
}
