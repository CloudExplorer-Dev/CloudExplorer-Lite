package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.networking.PortService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.options.PortListOptions;
import org.openstack4j.openstack.networking.domain.NeutronPort;
import org.openstack4j.openstack.networking.domain.NeutronPort.Ports;
import org.openstack4j.openstack.networking.domain.NeutronPortCreate;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Neutron) Port based Operations Implementation
 *
 * @author Jeremy Unruh
 */
public class PortServiceImpl extends BaseNetworkingServices implements PortService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Port> list() {
        return get(Ports.class, uri("/ports")).execute().getList();
    }

    @Override
    public List<? extends Port> list(PortListOptions options) {
        if (options == null)
            return list();

        return get(Ports.class, uri("/ports")).params(options.getOptions()).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Port get(String portId) {
        checkNotNull(portId);
        return get(NeutronPort.class, uri("/ports/%s", portId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String portId) {
        checkNotNull(portId);
        return deleteWithResponse(uri("/ports/%s", portId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Port create(Port port) {
        checkNotNull(port);
        checkNotNull(port.getNetworkId(), "NetworkId is a required field");
        return post(NeutronPort.class, uri("/ports")).entity(NeutronPortCreate.fromPort(port)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Port> create(List<? extends Port> ports) {
        checkNotNull(ports);
        for (Port port : ports) {
            checkNotNull(port.getNetworkId(), "NetworkId is a required field");
        }
        return post(Ports.class, uri("/ports")).entity(NeutronPortCreate.NeutronPortsCreate.fromPorts(ports))
                .execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Port update(Port port) {
        checkNotNull(port);
        checkNotNull(port.getId());
        Port p = port.toBuilder().networkId(null).state(null).tenantId(null).macAddress(null)
                .vifType(null).vifDetails(null)
                .build();
        return put(NeutronPort.class, uri("/ports/%s", getAndClearIdentifier(p))).entity(p).execute();
    }

    private String getAndClearIdentifier(Port port) {
        String portId = port.getId();
        port.setId(null);
        return portId;
    }
}
