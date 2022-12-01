package org.openstack4j.model.heat;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.heat.builder.StackCreateBuilder;

/**
 * This interface describes the model of a {@link Stack}, before it is sent to
 * the server for creation
 *
 * @author Matthias Reisser
 */
public interface StackCreate extends BaseStackCreateUpdate, Buildable<StackCreateBuilder> {

    boolean getDisableRollback();

    /**
     * Returns the name of the stack to create
     *
     * @return the name of the stack to create
     */
    String getName();
}
