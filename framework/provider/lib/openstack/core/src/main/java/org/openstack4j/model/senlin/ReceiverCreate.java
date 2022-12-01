package org.openstack4j.model.senlin;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.senlin.builder.ReceiverCreateBuilder;

/**
 * This interface describes the model of a {@link Receiver}, before it is sent to
 * the server for creation
 *
 * @author lion
 */
public interface ReceiverCreate extends ModelEntity, Buildable<ReceiverCreateBuilder> {

}
