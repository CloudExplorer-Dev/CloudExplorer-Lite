package org.openstack4j.model.network.builder;

import org.openstack4j.model.network.ext.builder.*;

/**
 * The Network builders
 */
public interface NetworkBuilders {

    /**
     * The builder to create a Network
     *
     * @return the network builder
     */
    public NetworkBuilder network();

    /**
     * The builder to update a network
     *
     * @return the NetworkUpdateBuilder
     */
    public NetworkUpdateBuilder networkUpdate();

    public ExtraDhcpOptBuilder extraDhcpOpt();

    /**
     * The builder to create a Subnet
     *
     * @return the subnet builder
     */
    public SubnetBuilder subnet();

    /**
     * The builder to create a Port
     *
     * @return the port builder
     */
    public PortBuilder port();

    /**
     * The builder to create a Router
     *
     * @return the router builder
     */
    public RouterBuilder router();

    /**
     * The builder to create a Neutron Security Group
     *
     * @return the security group builder
     */
    public NetSecurityGroupBuilder securityGroup();

    /**
     * The builder to create a Neutron Security Group Rule
     *
     * @return the security group builder
     */
    public NetSecurityGroupRuleBuilder securityGroupRule();

    /**
     * The builder to create a Neutron Floating IP Address
     *
     * @return the floating ip builder
     */
    public NetFloatingIPBuilder netFloatingIP();

    /**
     * The builder to create NetQuota entities
     *
     * @return the NetQuota builder
     */
    public NetQuotaBuilder netQuota();

    /**
     * The builder to create a lb member
     *
     * @return the Member Builder
     */
    public MemberBuilder member();

    /**
     * The builder to update a lb member
     *
     * @return the MemberUpdate Builder
     */
    public MemberUpdateBuilder memberUpdate();

    /**
     * The builder to create and update a sessionPersistence
     *
     * @return SessionPersistenceBuilder
     */
    public SessionPersistenceBuilder sessionPersistence();

    /**
     * The builder to create a vip.
     *
     * @return VipBuilder the vip builder
     */
    public VipBuilder vip();

    /**
     * The builder to update a vip.
     *
     * @return VipUpdateBuilder
     */
    public VipUpdateBuilder vipUpdate();

    /**
     * The builder to create a healthMonitor
     *
     * @return HealthMonitorBuilder
     */
    public HealthMonitorBuilder healthMonitor();

    /**
     * The builder to update a healthMonitor
     *
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder healthMonitorUpdate();

    /**
     * The builder to create a firewall
     *
     * @return FirewallBuilder
     */
    public FirewallBuilder firewall();

    /**
     * The builder to update a healthMonitor
     *
     * @return FirewallUpdateBuilder
     */
    public FirewallUpdateBuilder firewallUpdate();

    /**
     * The builder to create a firewallRule
     *
     * @return FirewallRuleBuilder
     */
    public FirewallRuleBuilder firewallRule();

    /**
     * The builder to update a firewallRule
     *
     * @return FirewallUpdateBuilder
     */
    public FirewallRuleUpdateBuilder firewallRuleUpdate();

    /**
     * The builder to create a firewallPolicy
     *
     * @return FirewallPolicyBuilder
     */
    public FirewallPolicyBuilder firewallPolicy();

    /**
     * The builder to update a firewallPolicy
     *
     * @return FirewallPolicyUpdateBuilder
     */
    public FirewallPolicyUpdateBuilder firewallPolicyUpdate();

    /**
     * The builder to create a lbPool
     *
     * @return LbPoolBuilder
     */
    public LbPoolBuilder lbPool();

    /**
     * The builder to update a lbPool
     *
     * @return LbPoolUpdateBuilder
     */
    public LbPoolUpdateBuilder lbPoolUpdate();

    /**
     * The builder to create a lbPool
     *
     * @return HealthMonitorAssociateBuilder
     */
    public HealthMonitorAssociateBuilder lbPoolAssociateHealthMonitor();

    /**
     * The builder to create a lb v2 member
     *
     * @return the Member Builder v2
     */
    public MemberV2Builder memberV2();

    /**
     * The builder to update a lbaas v2 member
     *
     * @return MemberV2UpdateBuilder
     */
    public MemberV2UpdateBuilder memberV2Update();

    /**
     * The builder to create a v2 loadbalancer.
     *
     * @return LoadBalancerV2Builder the loadbalancer v2 builder
     */
    public LoadBalancerV2Builder lbV2();

    /**
     * The builder to update a lbaas v2 loadbalancer
     *
     * @return LoadBalancerV2UpdateBuilder
     */
    public LoadBalancerV2UpdateBuilder loadBalancerV2Update();

    /**
     * The builder to create a v2 lbPool
     *
     * @return LbPoolV2Builder
     */
    public LbPoolV2Builder lbPoolV2();

    /**
     * The builder to update a lbaas v2 lbPool
     *
     * @return LbPoolV2UpdateBuilder
     */
    public LbPoolV2UpdateBuilder lbPoolV2Update();

    /**
     * The builder to createa v2 lbaas listener
     *
     * @return ListenerV2Builder
     */
    public ListenerV2Builder listenerV2();

    /**
     * The builder to update a lbaas v2 lbaas listener
     *
     * @return ListenerV2UpdateBuilder
     */
    public ListenerV2UpdateBuilder listenerV2Update();

    /**
     * The builder to create a v2 healthMonitor
     *
     * @return HealthMonitorV2Builder
     */
    public HealthMonitorV2Builder healthMonitorV2();

    /**
     * The builder to update a lbaas v2 health monitor
     *
     * @return HealthMonitorV2UpdateBuilder
     */
    public HealthMonitorV2UpdateBuilder healthMonitorV2Update();

    /**
     * The builder to create a network IP availability entity
     *
     * @return the NetworkIPAvailability builder
     */
    public NetworkIPAvailabilityBuilder networkIPAvailability();

    /**
     * The builder to create a Trunk
     *
     * @return TrunkBuilder
     */
    public TrunkBuilder trunk();

    /**
     * The builder to create a floating IP port forwarding
     *
     * @return PortForwardingBuilder
     */
    PortForwardingBuilder portForwarding();
}
