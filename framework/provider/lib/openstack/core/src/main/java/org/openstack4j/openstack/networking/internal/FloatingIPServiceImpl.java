package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.networking.NetFloatingIPService;
import org.openstack4j.api.networking.ext.PortForwardingService;
import org.openstack4j.core.transport.ExecutionOptions;
import org.openstack4j.core.transport.propagation.PropagateOnStatus;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.openstack.networking.domain.NeutronFloatingIP;
import org.openstack4j.openstack.networking.domain.NeutronFloatingIP.FloatingIPs;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * FloatingIPService implementation that provides Neutron Floating-IP based Service Operations.
 *
 * @author Nathan Anderson
 */
public class FloatingIPServiceImpl extends BaseNetworkingServices implements NetFloatingIPService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends NetFloatingIP> list() {
        return get(FloatingIPs.class, uri("/floatingips")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends NetFloatingIP> list(Map<String, String> filteringParams) {
        Invocation<FloatingIPs> fIPsInvocation = get(FloatingIPs.class, "/floatingips");
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                fIPsInvocation = fIPsInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return fIPsInvocation.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetFloatingIP get(String fipId) {
        checkNotNull(fipId);
        return get(NeutronFloatingIP.class, uri("/floatingips/%s", fipId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String fipId) {
        checkNotNull(fipId);
        return deleteWithResponse(uri("/floatingips/%s", fipId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetFloatingIP create(NetFloatingIP floatingIp) {
        checkNotNull(floatingIp);
        checkNotNull(floatingIp.getFloatingNetworkId());
        return post(NeutronFloatingIP.class, uri("/floatingips")).entity(floatingIp)
                .execute(ExecutionOptions.create(PropagateOnStatus.on(404)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetFloatingIP associateToPort(String fipId, String portId) {
        checkNotNull(fipId);
        checkNotNull(portId);
        String inner = String.format("{ \"port_id\":\"%s\" }", portId);
        String json = String.format("{ \"%s\": %s }", "floatingip", inner);
        return put(NeutronFloatingIP.class, uri("/floatingips/%s", fipId)).json(json)
                .execute(ExecutionOptions.create(PropagateOnStatus.on(404)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetFloatingIP disassociateFromPort(String fipId) {
        checkNotNull(fipId);
        String json = String.format("{ \"%s\": %s }", "floatingip", "{ \"port_id\":null }");
        return put(NeutronFloatingIP.class, uri("/floatingips/%s", fipId)).json(json)
                .execute(ExecutionOptions.create(PropagateOnStatus.on(404)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortForwardingService portForwarding() {
        return Apis.get(PortForwardingService.class);
    }
}
