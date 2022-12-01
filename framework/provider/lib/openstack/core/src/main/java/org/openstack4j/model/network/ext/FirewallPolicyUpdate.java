package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.FirewallPolicyUpdateBuilder;

import java.util.List;

/**
 * <p>A Builder to Update Firewall Policy of FwaaS</p>
 *
 * <p>Represents an ordered collection of firewall rules. A firewall policy can be shared across tenants.
 * Thus it can also be made part of an audit workflow wherein the firewall_policy can be audited by the
 * relevant entity that is authorized (and can be different from the tenants which create or use the firewall policy).
 * </p>
 *
 * @author Vishvesh Deshmukh
 */
public interface FirewallPolicyUpdate extends ModelEntity, Buildable<FirewallPolicyUpdateBuilder> {

    /**
     * @return name : Human readable name for the FirewallPolicyUpdate (255 characters limit). Does not have to be unique.
     */
    public String getName();

    /**
     * @return description : Human readable description for the FirewallPolicyUpdate (1024 characters limit).
     */
    public String getDescription();

    /**
     * @return shared :  When set to True makes this FirewallPolicyUpdate visible to tenants other
     * than its owner, and can be used in FirewallPolicyUpdate not owned by its tenant.
     */
    public Boolean isShared();

    /**
     * @return audited : When set to True by the policy owner indicates that the firewall policy has been audited.
     * This attribute is meant to aid in the firewall policy audit workflows.
     * Each time the firewall policy or the associated firewall rules are changed,
     * this attribute will be set to False and will have to be explicitly set
     * to True through an update operation.
     */
    public Boolean isAudited();

    /**
     * @return firewallRules(UUID)List : This is an ordered list of firewall rule uuids.
     * The firewall applies the rules in the order in which they appear in this list.
     */
    public List<String> getFirewallRuleIds();
}
