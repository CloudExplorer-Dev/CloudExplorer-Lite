package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.ext.builder.FirewallRuleBuilder;
import org.openstack4j.openstack.networking.domain.ext.NeutronFirewallRule;

/**
 * <p>Networking (Neutron) FwaaS Firewall Rule Extension API</p>
 *
 * <p>Represents a collection of attributes like ports, ip addresses which define match
 * criteria and action (allow, or deny) that needs to be taken on the matched data traffic.</p>
 *
 * @author Vishvesh Deshmukh
 */
public interface FirewallRule extends ModelEntity, Buildable<FirewallRuleBuilder> {
    /**
     * @return id : Unique identifier for the firewall rule object.
     */
    public String getId();

    /**
     * @return name : Human readable name for the firewall rule (255 characters limit). Does not have to be unique.
     */
    public String getName();

    /**
     * @return tenantId : Owner of the Firewall Rule. Only an administrative user can
     * specify a tenant ID other than its own.
     */
    public String getTenantId();

    /**
     * @return description : Human readable description for the firewall rule (1024 characters limit).
     */
    public String getDescription();

    /**
     * @return policyid : This is a <strong>read-only</strong> attribute which gets populated with the uuid of the firewall policy when this
     * firewall rule is associated with a firewall policy. A firewall rule can be associated with one firewall policy at a time.
     * The association can however be updated to a different firewall policy. This attribute can be <code>null</code> if the rule is not
     * associated with any firewall policy.
     */
    public String getPolicy();

    /**
     * @return shared : When set to True makes this firewall rule visible to tenants other than its owner,
     * and can be used in firewall policies not owned by its tenant.
     */
    public Boolean isShared();

    /**
     * @return protocol : IP Protocol : Possible values are ICMP/TCP/UDP/NONE(ANY).
     * @see NeutronFirewallRule.IPProtocol
     */
    public NeutronFirewallRule.IPProtocol getProtocol();

    /**
     * @return ipVersion : IP Protocol Version : Possible values are 4/6.
     * @see IPVersionType
     */
    public IPVersionType getIpVersion();

    /**
     * @return sourceIpAddress or CIDR : Valid IP address (v4 or v6), or CIDR.
     */
    public String getSourceIpAddress();

    /**
     * @return destinationIpAddress or CIDR : Valid IP address (v4 or v6), or CIDR.
     */
    public String getDestinationIpAddress();

    /**
     * @return sourcePort : Valid port number (integer or string), or port range in the format of a ':' separated range).
     * In the case of port range, both ends of the range are included.
     */
    public String getSourcePort();

    /**
     * @return destinationPort : Valid port number (integer or string), or port range in the format of a ':' separated range).
     * In the case of port range, both ends of the range are included.
     */
    public String getDestinationPort();

    /**
     * @return position : This is a <strong>read-only</strong> attribute that gets assigned to this rule when the rule is associated with a firewall policy.
     * It indicates the position of this rule in that firewall policy. This position number starts at 1.
     * The position can be <code>null</code> if the firewall rule is not associated with any policy.
     */
    public Integer getPosition();

    /**
     * @return action : Action to be performed on the traffic matching the rule (allow, deny).
     * @see NeutronFirewallRule.FirewallRuleAction
     */
    public NeutronFirewallRule.FirewallRuleAction getAction();

    /**
     * @return enabled : When set to False will disable this rule in the firewall policy. Facilitates selectively turning off
     * rules without having to disassociate the rule from the firewall policy.
     */
    public Boolean isEnabled();
}
