package org.openstack4j.model.senlin;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.senlin.builder.NodeActionCreateBuilder;

/**
 * This interface describes the model of a {@link Node}, before it is sent to
 * the server for creation
 *
 * @author Matthias Reisser
 */
public interface NodeActionCreate extends ModelEntity, Buildable<NodeActionCreateBuilder> {

}
