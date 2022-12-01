package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.PortForwardingService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.PortForwarding;
import org.openstack4j.openstack.networking.domain.ext.FloatingIPPortForwarding;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 */
public class PortForwardingServiceImpl extends BaseNetworkingServices implements PortForwardingService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PortForwarding> list(String floatingIpId) {
        return get(FloatingIPPortForwarding.PortForwardings.class, uri("/floatingips/%s/port_forwardings", floatingIpId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends PortForwarding> list(String floatingIpId, Map<String, String> filteringParams) {
        Invocation<FloatingIPPortForwarding.PortForwardings> req = get(FloatingIPPortForwarding.PortForwardings.class, uri("/floatingips/%s/port_forwardings", floatingIpId));
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
    public PortForwarding get(String floatingIpId, String id) {
        return get(FloatingIPPortForwarding.class, uri("/floatingips/%s/port_forwardings/%s", floatingIpId, id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String floatingIpId, String id) {
        return deleteWithResponse(uri("/floatingips/%s/port_forwardings/%s", floatingIpId, id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortForwarding create(String floatingIpId, PortForwarding portForwarding) {
        return post(FloatingIPPortForwarding.class, uri("/floatingips/%s/port_forwardings", floatingIpId)).entity(portForwarding).execute();
    }
}
