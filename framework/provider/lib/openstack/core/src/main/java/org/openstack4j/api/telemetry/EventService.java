package org.openstack4j.api.telemetry;

import org.openstack4j.common.RestService;
import org.openstack4j.model.telemetry.Event;
import org.openstack4j.model.telemetry.EventCriteria;
import org.openstack4j.model.telemetry.Trait;
import org.openstack4j.model.telemetry.TraitDescription;

import java.util.List;

/**
 * OpenStack (Ceilometer) Event based Operations
 *
 * @author Miroslav Lacina
 */
public interface EventService extends RestService {

    /**
     * Return all Events matching the query filters
     *
     * @param eventCriteria the event query criteria for filtering results
     * @return list of Events
     */
    List<? extends Event> list(EventCriteria eventCriteria);

    /**
     * Return all Events matching the query filters
     *
     * @param eventCriteria the event query criteria for filtering results
     * @param limit         maximum number of samples to be returned
     * @return list of Events
     */
    List<? extends Event> list(EventCriteria eventCriteria, int limit);

    /**
     * Return a single Event with the given message id
     *
     * @param messageId message ID of the Event to be returned
     * @return single Event
     */
    Event get(String messageId);

    /**
     * Get all event types
     *
     * @return list of event types
     */
    List<String> listEventTypes();

    /**
     * Return all trait names for an event type
     *
     * @param eventType event type to filter traits by
     * @return list of TraitDescriptions
     */
    List<? extends TraitDescription> listTraitDescriptions(String eventType);

    /**
     * Return all instances of a trait for an event type
     *
     * @param eventType event type to filter traits by
     * @param traitName trait to return values for
     * @return list of Traits
     */
    List<? extends Trait> listTraits(String eventType, String traitName);

}
