package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.PolicyRuleSetBuilder;

import java.util.List;

/**
 * Policy rule set Model Entity
 *
 * @author vinod borole
 */
public interface PolicyRuleSet extends Resource, Buildable<PolicyRuleSetBuilder> {

    /**
     * Gets the list of Policy rules
     *
     * @return the list of Policy rules
     */
    List<String> getPolicyRules();

    /**
     * Gets the list of child policy rule sets
     *
     * @return the list of child policy rule sets
     */
    List<String> getChildPolicyRuleSets();

    /**
     * Gets the parent Id
     *
     * @return the parent Id
     */
    String getParentId();

    /**
     * Is Policy rule set shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

}
