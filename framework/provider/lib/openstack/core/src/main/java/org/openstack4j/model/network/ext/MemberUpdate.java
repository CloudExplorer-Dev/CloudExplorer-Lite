package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.MemberUpdateBuilder;

/**
 * An entity used to update a member of a pool
 *
 * @author liujunpeng
 */
public interface MemberUpdate extends ModelEntity, Buildable<MemberUpdateBuilder> {


    /**
     * @see Member#isAdminStateUp()
     */
    public boolean isAdminStateUp();

    /**
     * @see Member#getWeight()
     */
    public Integer getWeight();

    /**
     * @see Member#getPoolId()
     */
    public String getPoolId();
}
