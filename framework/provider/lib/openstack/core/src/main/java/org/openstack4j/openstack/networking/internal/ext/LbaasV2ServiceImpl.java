package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.Apis;
import org.openstack4j.api.networking.ext.*;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

/**
 * OpenStack (Neutron) lbaas v2 service implementation
 *
 * @author emjburns
 */
public class LbaasV2ServiceImpl extends BaseNetworkingServices implements LbaasV2Service {
    /**
     * {@inheritDoc}
     */
    @Override
    public ListenerV2Service listener() {
        return Apis.get(ListenerV2Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolV2Service lbPool() {
        return Apis.get(LbPoolV2Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorV2Service healthMonitor() {
        return Apis.get(HealthMonitorV2Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2Service loadbalancer() {
        return Apis.get(LoadBalancerV2Service.class);
    }
}
