package org.openstack4j.openstack.octavia.internal;

import org.openstack4j.api.octavia.ListenerV2Service;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.octavia.ListenerV2;
import org.openstack4j.model.octavia.ListenerV2Update;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.octavia.domain.OctaviaListenerV2;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Lbaas listener imlementation
 *
 * @author wei
 */
public class ListenerV2ServiceImpl extends BaseOctaviaServices implements ListenerV2Service {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ListenerV2> list() {
        return get(OctaviaListenerV2.Listeners.class, uri("/lbaas/listeners")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ListenerV2> list(Map<String, String> filteringParams) {
        Invocation<OctaviaListenerV2.Listeners> req = get(OctaviaListenerV2.Listeners.class, uri("/lbaas/listeners"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListenerV2 get(String listenerId) {
        checkNotNull(listenerId);
        return get(OctaviaListenerV2.class, uri("/lbaas/listeners/%s", listenerId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String listenerId) {
        checkNotNull(listenerId);
        return ToActionResponseFunction.INSTANCE.apply(
                delete(Void.class, uri("/lbaas/listeners/%s", listenerId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListenerV2 create(ListenerV2 listener) {
        checkNotNull(listener);
        return post(OctaviaListenerV2.class, uri("/lbaas/listeners")).entity(listener).execute();
    }

    @Override
    public ListenerV2 update(String listenerId, ListenerV2Update listener) {
        checkNotNull(listenerId);
        checkNotNull(listener);
        return put(OctaviaListenerV2.class, uri("/lbaas/listeners/%s", listenerId)).entity(listener).execute();
    }
}
