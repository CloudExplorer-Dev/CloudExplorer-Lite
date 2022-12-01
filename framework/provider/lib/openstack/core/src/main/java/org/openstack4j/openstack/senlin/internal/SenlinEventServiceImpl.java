package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.senlin.SenlinEventService;
import org.openstack4j.model.senlin.Event;
import org.openstack4j.openstack.senlin.domain.SenlinEvent;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class contains getters for all implementation of the available event services
 *
 * @author lion
 */
public class SenlinEventServiceImpl extends BaseSenlinServices implements SenlinEventService {

    @Override
    public List<? extends Event> list() {
        return get(SenlinEvent.Event.class, uri("/events")).execute().getList();
    }

    @Override
    public Event get(String eventID) {
        checkNotNull(eventID);
        return get(SenlinEvent.class, uri("/events/%s", eventID)).execute();
    }
}
