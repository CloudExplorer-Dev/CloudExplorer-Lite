package org.openstack4j.openstack.networking.builder;

import org.openstack4j.model.network.builder.*;
import org.openstack4j.model.network.ext.builder.*;
import org.openstack4j.openstack.networking.domain.*;
import org.openstack4j.openstack.networking.domain.ext.*;

/**
 * The Network V3 Builders
 */
public class NeutronBuilders implements NetworkBuilders {

    @Override
    public NetworkBuilder network() {
        return NeutronNetwork.builder();
    }

    @Override
    public NetworkUpdateBuilder networkUpdate() {
        return NeutronNetworkUpdate.builder();
    }

    @Override
    public ExtraDhcpOptBuilder extraDhcpOpt() {
        return NeutronExtraDhcpOptCreate.builder();
    }

    @Override
    public SubnetBuilder subnet() {
        return NeutronSubnet.builder();
    }

    @Override
    public PortBuilder port() {
        return NeutronPort.builder();
    }

    @Override
    public TrunkBuilder trunk() {
        return NeutronTrunk.builder();
    }

    @Override
    public PortForwardingBuilder portForwarding() {
        return FloatingIPPortForwarding.builder();
    }

    @Override
    public RouterBuilder router() {
        return NeutronRouter.builder();
    }

    @Override
    public NetSecurityGroupBuilder securityGroup() {
        return NeutronSecurityGroup.builder();
    }

    @Override
    public NetSecurityGroupRuleBuilder securityGroupRule() {
        return NeutronSecurityGroupRule.builder();
    }

    @Override
    public NetFloatingIPBuilder netFloatingIP() {
        return NeutronFloatingIP.builder();
    }

    @Override
    public NetQuotaBuilder netQuota() {
        return NeutronNetQuota.builder();
    }

    @Override
    public MemberBuilder member() {
        return NeutronMember.builder();
    }

    @Override
    public MemberUpdateBuilder memberUpdate() {
        return NeutronMemberUpdate.builder();
    }

    @Override
    public SessionPersistenceBuilder sessionPersistence() {
        return NeutronSessionPersistence.builder();
    }

    @Override
    public VipBuilder vip() {
        return NeutronVip.builder();
    }

    @Override
    public VipUpdateBuilder vipUpdate() {
        return NeutronVipUpdate.builder();
    }

    @Override
    public HealthMonitorBuilder healthMonitor() {
        return NeutronHealthMonitor.builder();
    }

    @Override
    public HealthMonitorUpdateBuilder healthMonitorUpdate() {
        return NeutronHealthMonitorUpdate.builder();
    }

    @Override
    public FirewallBuilder firewall() {
        return NeutronFirewall.builder();
    }

    @Override
    public FirewallUpdateBuilder firewallUpdate() {
        return NeutronFirewallUpdate.builder();
    }

    @Override
    public FirewallRuleBuilder firewallRule() {
        return NeutronFirewallRule.builder();
    }

    @Override
    public FirewallRuleUpdateBuilder firewallRuleUpdate() {
        return NeutronFirewallRuleUpdate.builder();
    }

    @Override
    public FirewallPolicyBuilder firewallPolicy() {
        return NeutronFirewallPolicy.builder();
    }

    @Override
    public FirewallPolicyUpdateBuilder firewallPolicyUpdate() {
        return NeutronFirewallPolicyUpdate.builder();
    }

    @Override
    public LbPoolBuilder lbPool() {
        return NeutronLbPool.builder();
    }

    @Override
    public LbPoolUpdateBuilder lbPoolUpdate() {
        return NeutronLbPoolUpdate.builder();
    }

    @Override
    public HealthMonitorAssociateBuilder lbPoolAssociateHealthMonitor() {
        return NeutronHealthMonitorAssociate.builder();
    }

    @Override
    public MemberV2Builder memberV2() {
        return NeutronMemberV2.builder();
    }

    @Override
    public MemberV2UpdateBuilder memberV2Update() {
        return NeutronMemberV2Update.builder();
    }

    @Override
    public LoadBalancerV2Builder lbV2() {
        return NeutronLoadBalancerV2.builder();
    }

    @Override
    public LoadBalancerV2UpdateBuilder loadBalancerV2Update() {
        return NeutronLoadBalancerV2Update.builder();
    }

    @Override
    public HealthMonitorV2Builder healthMonitorV2() {
        return NeutronHealthMonitorV2.builder();
    }

    @Override
    public HealthMonitorV2UpdateBuilder healthMonitorV2Update() {
        return NeutronHealthMonitorV2Update.builder();
    }

    @Override
    public LbPoolV2Builder lbPoolV2() {
        return NeutronLbPoolV2.builder();
    }

    @Override
    public LbPoolV2UpdateBuilder lbPoolV2Update() {
        return NeutronLbPoolV2Update.builder();
    }

    @Override
    public ListenerV2Builder listenerV2() {
        return NeutronListenerV2.builder();
    }

    @Override
    public ListenerV2UpdateBuilder listenerV2Update() {
        return NeutronListenerV2Update.builder();
    }

    @Override
    public NetworkIPAvailabilityBuilder networkIPAvailability() {
        return NeutronNetworkIPAvailability.builder();
    }
}
