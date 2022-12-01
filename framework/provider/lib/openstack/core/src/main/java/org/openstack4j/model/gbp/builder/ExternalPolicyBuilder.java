package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.ExternalPolicyCreate;

import java.util.List;

/**
 * A builder which produces a External Policy object
 *
 * @author vinod borole
 */
public interface ExternalPolicyBuilder extends Builder<ExternalPolicyBuilder, ExternalPolicyCreate> {

    ExternalPolicyBuilder name(String string);

    ExternalPolicyBuilder description(String description);

    ExternalPolicyBuilder isShared(boolean shared);

    ExternalPolicyBuilder consumedPolicyRuleSets(List<String> policyRuleSet);

    ExternalPolicyBuilder providedPolicyRuleSets(List<String> policyRuleSet);

    ExternalPolicyBuilder externalSegments(List<String> externalSegmentIds);

}
