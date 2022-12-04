package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.ListenerV2Service;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.ListenerV2;
import org.openstack4j.model.network.ext.ListenerV2Update;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.networking.domain.ext.NeutronListenerV2;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Lbaas listener imlementation
 *
 * @author emjburns
 */
public class ListenerV2ServiceImpl extends BaseNetworkingServices implements ListenerV2Service {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ListenerV2> list() {
        return get(NeutronListenerV2.Listeners.class, uri("/lbaas/listeners")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ListenerV2> list(Map<String, String> filteringParams) {
        Invocation<NeutronListenerV2.Listeners> req = get(NeutronListenerV2.Listeners.class, uri("/lbaas/listeners"));
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
        return get(NeutronListenerV2.class, uri("/lbaas/listeners/%s", listenerId)).execute();
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
        return post(NeutronListenerV2.class, uri("/lbaas/listeners")).entity(listener).execute();
    }

    @Override
    public ListenerV2 update(String listenerId, ListenerV2Update listener) {
        checkNotNull(listenerId);
        checkNotNull(listener);
        return put(NeutronListenerV2.class, uri("/lbaas/listeners/%s", listenerId)).entity(listener).execute();
    }
}
