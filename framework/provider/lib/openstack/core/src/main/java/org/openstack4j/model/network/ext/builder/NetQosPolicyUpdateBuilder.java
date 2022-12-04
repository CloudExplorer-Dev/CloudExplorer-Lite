package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.NetQosPolicy;
import org.openstack4j.model.network.ext.NetQosPolicyUpdate;

/**
 * A Builder which creates a NetQosPolicy entity
 *
 * @author bboyHan
 */
public interface NetQosPolicyUpdateBuilder extends Builder<NetQosPolicyUpdateBuilder, NetQosPolicyUpdate> {

    NetQosPolicyUpdateBuilder id(String id);

    /**
     * See {@link NetQosPolicy#getDescription()} for details
     *
     * @param description qos description
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyUpdateBuilder description(String description);

    /**
     * See {@link NetQosPolicy#isShared()} for details
     *
     * @param shared true or false
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyUpdateBuilder shared(boolean shared);

    /**
     * See {@link NetQosPolicy#isDefault()} for details
     *
     * @param isDefault if True, the QoS policy is the default policy.
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyUpdateBuilder isDefault(boolean isDefault);

    /**
     * See {@link NetQosPolicy#getName()} for details
     *
     * @param name human-readable name
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyUpdateBuilder name(String name);

}
