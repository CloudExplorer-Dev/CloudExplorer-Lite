package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.SecurityGroupUpdate;

/**
 * Builder for a Security Group Update model class
 * <p>
 * Created by Ayberk CAL on 17.03.2017.
 */
public interface NetSecurityGroupUpdateBuilder extends Builder<NetSecurityGroupUpdateBuilder, SecurityGroupUpdate> {

    /**
     * @see SecurityGroupUpdate#getName()
     */
    NetSecurityGroupUpdateBuilder name(String name);

    /**
     * @see SecurityGroupUpdate#getDescription()
     */
    NetSecurityGroupUpdateBuilder description(String description);
}
