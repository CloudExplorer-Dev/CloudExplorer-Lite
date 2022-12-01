package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.SecurityGroup;

/**
 * Builder for a SecurityGroup model class
 *
 * @author Nathan Anderson
 */
public interface NetSecurityGroupBuilder extends Builder<NetSecurityGroupBuilder, SecurityGroup> {

    /**
     * @see SecurityGroup#getId()
     */
    NetSecurityGroupBuilder id(String id);

    /**
     * @see SecurityGroup#getName()
     */
    NetSecurityGroupBuilder name(String name);

    /**
     * @see SecurityGroup#getDescription()
     */
    NetSecurityGroupBuilder description(String description);

    /**
     * @see SecurityGroup#getTenantId()
     */
    NetSecurityGroupBuilder tenantId(String tenantId);

}
