package org.openstack4j.openstack.octavia.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.octavia.*;

/**
 * OpenStack Networking Operations API
 *
 * @author wei
 */
public class OctaviaServiceImpl implements OctaviaService {

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerV2Service loadBalancerV2() {
        return Apis.get(LoadBalancerV2Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListenerV2Service listenerV2() {
        return Apis.get(ListenerV2Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolV2Service lbPoolV2() {
        return Apis.get(LbPoolV2Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorV2Service healthMonitorV2() {
        return Apis.get(HealthMonitorV2Service.class);
    }

}
