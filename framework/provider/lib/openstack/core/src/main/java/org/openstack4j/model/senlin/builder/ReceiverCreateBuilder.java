package org.openstack4j.model.senlin.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.senlin.ReceiverCreate;

import java.util.Map;

/**
 * This interface describes a builder for {@link ReceiverCreate} objects
 *
 * @author lion
 */
public interface ReceiverCreateBuilder extends Buildable.Builder<ReceiverCreateBuilder, ReceiverCreate> {

    /**
     * Add the action to initiate when the receiver is triggered.
     * A valid value should be the name of an action that can be applied on a cluster.
     *
     * @param action The action to initiate.
     * @return ReceiverCreateBuilder
     */
    ReceiverCreateBuilder action(String action);

    /**
     * Add the name, ID, or short ID of the object targeted by the receiver
     *
     * @param clusterID The Name, ID, or short ID of the object targeted by the receiver
     * @return ReceiverCreateBuilder
     */
    ReceiverCreateBuilder clusterID(String clusterID);

    /**
     * Add the name for the receiver
     *
     * @param name The name for the receiver
     * @return ReceiverCreateBuilder
     */
    ReceiverCreateBuilder name(String name);

    /**
     * Add the type of the receiver where the only valid value is webhook currently
     *
     * @param type The type of the receiver
     * @return ReceiverCreateBuilder
     */
    ReceiverCreateBuilder type(String type);

    /**
     * Add the map of key and value pairs to use for action creation.
     * Some actions might require certain input parameters
     *
     * @param params A map of key and value pairs to use for action creation.
     * @return ReceiverCreateBuilder
     */
    ReceiverCreateBuilder params(Map<String, String> params);

}
