package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.Direction;
import org.openstack4j.model.gbp.PolicyClassifierUpdate;
import org.openstack4j.model.gbp.Protocol;

/**
 * A builder which produces a Policy Classifier Update object
 *
 * @author vinod borole
 */
public interface PolicyClassifierUpdateBuilder extends Builder<PolicyClassifierUpdateBuilder, PolicyClassifierUpdate> {
    PolicyClassifierUpdateBuilder name(String name);

    PolicyClassifierUpdateBuilder description(String description);

    PolicyClassifierUpdateBuilder shared(boolean shared);

    PolicyClassifierUpdateBuilder portRangeMin(int min);

    PolicyClassifierUpdateBuilder portRangeMax(int max);

    PolicyClassifierUpdateBuilder direction(Direction direction);

    PolicyClassifierUpdateBuilder protocol(Protocol protocol);
}
