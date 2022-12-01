package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.Direction;
import org.openstack4j.model.gbp.PolicyClassifier;
import org.openstack4j.model.gbp.Protocol;

/**
 * A builder which produces a Policy Classifier object
 *
 * @author vinod borole
 */
public interface PolicyClassifierBuilder extends Builder<PolicyClassifierBuilder, PolicyClassifier> {
    PolicyClassifierBuilder name(String name);

    PolicyClassifierBuilder description(String description);

    PolicyClassifierBuilder portRangeMin(int min);

    PolicyClassifierBuilder portRangeMax(int max);

    PolicyClassifierBuilder direction(Direction direction);

    PolicyClassifierBuilder protocol(Protocol protocol);

    PolicyClassifierBuilder shared(boolean shared);
}
