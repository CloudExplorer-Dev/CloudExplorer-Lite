package org.openstack4j.model.workflow.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.workflow.EventTrigger;

/**
 * Builder for a {@link EventTrigger} model class
 *
 * @author Renat Akhmerov
 */
public interface EventTriggerBuilder extends Builder<EventTriggerBuilder, EventTrigger> {

    /**
     * @see EventTrigger#getId()
     */
    EventTriggerBuilder id(String id);

    /**
     * @see EventTrigger#getName()
     */
    EventTriggerBuilder name(String name);

    // TODO(rakhmerov): add all methods
}
