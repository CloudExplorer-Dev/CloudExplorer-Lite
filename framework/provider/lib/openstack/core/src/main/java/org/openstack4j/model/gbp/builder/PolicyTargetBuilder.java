package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.PolicyTarget;

/**
 * A builder which produces a Policy Target object
 *
 * @author vinod borole
 */
public interface PolicyTargetBuilder extends Builder<PolicyTargetBuilder, PolicyTarget> {
    PolicyTargetBuilder name(String name);

    PolicyTargetBuilder portId(String portId);

    PolicyTargetBuilder policyTargetGroupId(String policyTargetGroupId);

    PolicyTargetBuilder clusterId(String clusterId);

    PolicyTargetBuilder description(String description);
}
