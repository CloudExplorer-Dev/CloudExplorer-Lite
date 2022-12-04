package org.openstack4j.openstack.octavia.builder;

import org.openstack4j.model.octavia.builder.*;
import org.openstack4j.openstack.octavia.domain.*;

/**
 * The Octavia Builders
 */
public class OctaviaBuilders {

    public MemberV2Builder memberV2() {
        return OctaviaMemberV2.builder();
    }

    public MemberV2UpdateBuilder memberV2Update() {
        return OctaviaMemberV2Update.builder();
    }

    public LoadBalancerV2Builder loadBalancerV2() {
        return OctaviaLoadBalancerV2.builder();
    }

    public LoadBalancerV2UpdateBuilder loadBalancerV2Update() {
        return OctaviaLoadBalancerV2Update.builder();
    }

    public HealthMonitorV2Builder healthMonitorV2() {
        return OctaviaHealthMonitorV2.builder();
    }

    public HealthMonitorV2UpdateBuilder healthMonitorV2Update() {
        return OctaviaHealthMonitorV2Update.builder();
    }

    public SessionPersistenceBuilder sessionPersistence() {
        return OctaviaSessionPersistence.builder();
    }

    public LbPoolV2Builder lbPoolV2() {
        return OctaviaLbPoolV2.builder();
    }

    public LbPoolV2UpdateBuilder lbPoolV2Update() {
        return OctaviaLbPoolV2Update.builder();
    }

    public ListenerV2Builder listenerV2() {
        return OctaviaListenerV2.builder();
    }

    public ListenerV2UpdateBuilder listenerV2Update() {
        return OctaviaListenerV2Update.builder();
    }
}
