package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.PortPairService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.PortPair;
import org.openstack4j.openstack.networking.domain.ext.NeutronPortPair;
import org.openstack4j.openstack.networking.domain.ext.NeutronPortPair.PortPairs;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@inheritDoc}
 */
public class PortPairServiceImpl extends BaseNetworkingServices implements PortPairService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PortPair> list() {
        return get(PortPairs.class, uri("/sfc/port_pairs")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortPair create(PortPair portPair) {
        checkNotNull(portPair);
        return post(NeutronPortPair.class, uri("/sfc/port_pairs")).entity(portPair).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String portPairId) {
        checkNotNull(portPairId);
        return deleteWithResponse(uri("/sfc/port_pairs/%s", portPairId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortPair get(String portPairId) {
        checkNotNull(portPairId);
        return get(NeutronPortPair.class, uri("/sfc/port_pairs/%s", portPairId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortPair update(String portPairId, PortPair portPair) {
        checkNotNull(portPairId);
        return put(NeutronPortPair.class, uri("/sfc/port_pairs/%s", portPairId)).entity(portPair).execute();
    }
}
