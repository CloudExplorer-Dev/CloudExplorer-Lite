package org.openstack4j.openstack.heat.internal;

import org.openstack4j.api.heat.EventsService;
import org.openstack4j.model.heat.Event;
import org.openstack4j.openstack.heat.domain.HeatEvent;
import org.openstack4j.openstack.heat.domain.HeatEvent.Events;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class implements some methods for manipulation of {@link HeatEvent} objects. The
 * non-exhaustive list of methods is oriented along
 * http://developer.openstack.org/api-ref-orchestration-v1.html#stacks
 *
 * @author Octopus Zhang
 */
public class EventsServiceImpl extends BaseHeatServices implements EventsService {

    @Override
    public List<? extends Event> list(String stackName, String stackId) {
        checkNotNull(stackId);
        checkNotNull(stackName);
        return get(Events.class, uri("/stacks/%s/%s/events", stackName, stackId)).execute().getList();
    }

    @Override
    public List<? extends Event> list(String stackName, String stackId, String resourceName) {
        checkNotNull(stackId);
        checkNotNull(stackName);
        checkNotNull(resourceName);
        return get(Events.class, uri("/stacks/%s/%s/resources/%s/events", stackName, stackId, resourceName)).execute().getList();
    }

    @Override
    public Event show(String stackName, String stackId, String resourceName, String eventId) {
        checkNotNull(stackId);
        checkNotNull(stackName);
        checkNotNull(resourceName);
        checkNotNull(eventId);
        return get(HeatEvent.class, uri("/stacks/%s/%s/resources/%s/events/%s", stackName, stackId, resourceName, eventId)).execute();
    }

}
