package org.openstack4j.model.octavia;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.octavia.builder.ListenerV2UpdateBuilder;

/**
 * An entity used to update an lbaas v2 listener
 *
 * @author wei
 */
public interface ListenerV2Update extends ModelEntity, Buildable<ListenerV2UpdateBuilder> {
    /**
     * Optional
     *
     * @see ListenerV2#isAdminStateUp()
     */
    public boolean isAdminStateUp();

    /**
     * Optional
     *
     * @see ListenerV2#getDescription()
     */
    public String getDescription();

    /**
     * Optional
     *
     * @see ListenerV2#getName()
     */
    public String getName();

    /**
     * Optional
     *
     * @see ListenerV2#getConnectionLimit()
     */
    public Integer getConnectionLimit();

    /**
     * Optional
     *
     * @see ListenerV2#getDefaultTlsContainerRef()
     */
    public String getDefaultTlsContainerRef();
}
