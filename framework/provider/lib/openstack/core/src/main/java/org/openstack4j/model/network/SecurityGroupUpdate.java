package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.builder.NetSecurityGroupUpdateBuilder;

/**
 * The Interface SecurityGroupUpdate.
 * <p>
 * Created by Ayberk CAL on 17.03.2017.
 */
public interface SecurityGroupUpdate extends ModelEntity, Buildable<NetSecurityGroupUpdateBuilder> {

    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the spesific Id.
     *
     * @return the Id
     */
    String getId();

}
