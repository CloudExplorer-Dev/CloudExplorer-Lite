package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.BasicResource;
import org.openstack4j.model.network.ext.builder.NetQosPolicyUpdateBuilder;

/**
 * Network qos policy for update that are bound to a Tenant
 *
 * @author bboyHan
 */
public interface NetQosPolicyUpdate extends BasicResource, Buildable<NetQosPolicyUpdateBuilder> {

    /**
     * A human-readable description for the resource. Default is an empty string.
     *
     * @return Description
     */
    String getDescription();

    /**
     * Set to true to share this policy with other projects. Default is false.
     *
     * @return true or false
     */
    boolean isShared();

    /**
     * if True, the QoS policy is the default policy.
     *
     * @return true or false
     */
    boolean isDefault();

}
