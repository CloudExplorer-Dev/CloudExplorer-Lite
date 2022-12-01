package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.networking.*;
import org.openstack4j.api.networking.ext.*;

/**
 * OpenStack Networking Operations API
 *
 * @author Jeremy Unruh
 */
public class NetworkingServiceImpl implements NetworkingService {

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkService network() {
        return Apis.get(NetworkService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubnetService subnet() {
        return Apis.get(SubnetService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortService port() {
        return Apis.get(PortService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouterService router() {
        return Apis.get(RouterService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetFloatingIPService floatingip() {
        return Apis.get(NetFloatingIPService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecurityGroupService securitygroup() {
        return Apis.get(SecurityGroupService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NeutronResourceTagService resourceTags() {
        return Apis.get(NeutronResourceTagService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecurityGroupRuleService securityrule() {
        return Apis.get(SecurityGroupRuleService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetQuotaService quotas() {
        return Apis.get(NetQuotaService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadBalancerService loadbalancers() {
        return Apis.get(LoadBalancerService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbaasV2Service lbaasV2() {
        return Apis.get(LbaasV2Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FirewallAsService firewalls() {
        return Apis.get(FirewallAsService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AgentService agent() {
        return Apis.get(AgentService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AvailabilityZoneService availabilityzone() {
        return Apis.get(AvailabilityZoneService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkIPAvailabilityService networkIPAvailability() {
        return Apis.get(NetworkIPAvailabilityService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrunkService trunk() {
        return Apis.get(TrunkService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetQosPolicyService netQosPolicy() {
        return Apis.get(NetQosPolicyService.class);
    }

}
