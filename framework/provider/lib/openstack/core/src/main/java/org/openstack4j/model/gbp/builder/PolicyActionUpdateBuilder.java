package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.PolicyActionUpdate;

/**
 * A builder which produces a Policy Action Update object
 *
 * @author vinod borole
 */
public interface PolicyActionUpdateBuilder extends Builder<PolicyActionUpdateBuilder, PolicyActionUpdate> {
    PolicyActionUpdateBuilder name(String name);

    PolicyActionUpdateBuilder description(String description);

    PolicyActionUpdateBuilder shared(boolean shared);
}
