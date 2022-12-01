package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.networking.NetworkService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.NetworkUpdate;
import org.openstack4j.openstack.networking.domain.NeutronNetwork;
import org.openstack4j.openstack.networking.domain.NeutronNetwork.Networks;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Neutron) Network based Operations
 *
 * @author Jeremy Unruh
 */
public class NetworkServiceImpl extends BaseNetworkingServices implements NetworkService {

    private Invocation<Networks> buildInvocation(Map<String, String> filteringParams) {
        Invocation<Networks> invocation = get(Networks.class, "/networks");
        if (filteringParams == null) {
            return invocation;
        } else {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                invocation = invocation.param(entry.getKey(), entry.getValue());
            }
        }
        return invocation;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Network> list(Map<String, String> filteringParams) {
        Invocation<Networks> invocation = buildInvocation(filteringParams);
        return invocation.execute().getList();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Network> list() {
        return get(Networks.class, uri("/networks")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String networkId) {
        checkNotNull(networkId);
        return deleteWithResponse(uri("/networks/%s", networkId)).execute();
    }

    @Override
    public Network get(String networkId) {
        checkNotNull(networkId);
        return get(NeutronNetwork.class, uri("/networks/%s", networkId)).execute();
    }

    @Override
    public Network create(Network network) {
        checkNotNull(network);
        return post(NeutronNetwork.class, uri("/networks")).entity(network).execute();
    }

    @Override
    public Network update(String networkId, NetworkUpdate network) {
        checkNotNull(networkId);
        checkNotNull(network, "network");
        return put(NeutronNetwork.class, uri("/networks/%s", networkId)).entity(network).execute();
    }

}
