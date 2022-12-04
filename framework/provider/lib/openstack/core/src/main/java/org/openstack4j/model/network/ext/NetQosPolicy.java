package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.NetQosPolicyBuilder;

/**
 * Network qos policy that are bound to a Tenant
 *
 * @author bboyHan
 */
public interface NetQosPolicy extends ModelEntity, Buildable<NetQosPolicyBuilder> {

    /**
     * A human-readable description for the resource. Default is an empty string.
     *
     * @return Description
     */
    String getDescription();

    /**
     * The ID of the project that owns the resource.
     * Only administrative and users with advsvc role can specify a project ID other than their own.
     * You cannot change this value through authorization policies.
     *
     * @return tenantId
     */
    String getTenantId();

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

    /**
     * human-readable name
     *
     * @return name
     */
    String getName();

}
