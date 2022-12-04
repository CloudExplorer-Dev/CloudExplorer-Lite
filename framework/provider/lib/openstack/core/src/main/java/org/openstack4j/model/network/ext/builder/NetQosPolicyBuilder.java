package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.NetQosPolicy;

/**
 * A Builder which creates a NetQosPolicy entity
 *
 * @author bboyHan
 */
public interface NetQosPolicyBuilder extends Builder<NetQosPolicyBuilder, NetQosPolicy> {

    /**
     * See {@link NetQosPolicy#getDescription()} for details
     *
     * @param description qos description
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyBuilder description(String description);

    /**
     * See {@link NetQosPolicy#getTenantId()} for details
     *
     * @param tenantId tenantId
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyBuilder tenantId(String tenantId);

    /**
     * See {@link NetQosPolicy#isShared()} for details
     *
     * @param shared true or false
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyBuilder shared(boolean shared);

    /**
     * See {@link NetQosPolicy#isDefault()} for details
     *
     * @param isDefault if True, the QoS policy is the default policy.
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyBuilder isDefault(boolean isDefault);

    /**
     * See {@link NetQosPolicy#getName()} for details
     *
     * @param name human-readable name
     * @return NetQosPolicyBuilder
     */
    NetQosPolicyBuilder name(String name);

}
