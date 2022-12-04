package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.VipUpdateBuilder;

/**
 * An entity used to update a vip
 *
 * @author liujunpeng
 */
public interface VipUpdate extends ModelEntity, Buildable<VipUpdateBuilder> {
    /**
     * Optional
     *
     * @see Vip#isAdminStateUp()
     */
    public boolean isAdminStateUp();

    /**
     * Optional
     *
     * @see Vip#getConnectionLimit()
     */
    public Integer getConnectionLimit();

    /**
     * Optional
     *
     * @see Vip#getDescription()
     */
    public String getDescription();

    /**
     * Optional
     *
     * @see Vip#getName()
     */
    public String getName();

    /**
     * Optional
     *
     * @see Vip#getPoolId()
     */
    public String getPoolId();

    /**
     * Optional
     *
     * @see Vip#getSessionPersistence()
     */
    public SessionPersistence getSessionPersistence();

}
