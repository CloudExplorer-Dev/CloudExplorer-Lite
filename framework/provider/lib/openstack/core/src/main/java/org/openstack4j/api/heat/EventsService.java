package org.openstack4j.api.heat;

import org.openstack4j.model.heat.Event;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of events
 *
 * @author Octopus Zhang
 */
public interface EventsService {

    /**
     * Gets a list of currently existing {@link Event}s  for a specified stack.
     *
     * @param stackId   The unique identifier for a stack
     * @param stackName The name of a stack
     * @return the list of {@link Event}s
     */
    List<? extends Event> list(String stackName, String stackId);

    /**
     * Gets a list of currently existing {@link Event}s  for a specified stack resource.
     *
     * @param stackId      The unique identifier for a stack
     * @param stackName    The name of a stack
     * @param resourceName The name of a resource in the stack
     * @return the list of {@link Event}s
     */
    List<? extends Event> list(String stackName, String stackId, String resourceName);


    /**
     * Shows details for a specified event.
     *
     * @param stackId      The unique identifier for a stack
     * @param stackName    The name of a stack
     * @param resourceName The name of a resource in the stack
     * @param eventId      The unique identifier of an event related to the resource in the stack
     * @return event details
     */
    Event show(String stackName, String stackId, String resourceName, String eventId);
}
