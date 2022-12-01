package org.openstack4j.api.senlin;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.senlin.Receiver;
import org.openstack4j.model.senlin.ReceiverCreate;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of Receiver
 *
 * @author lion
 */
public interface SenlinReceiverService {

    /**
     * Gets a list of currently existing {@link Receiver}s.
     *
     * @return the list of {@link Receiver}s
     */
    List<? extends Receiver> list();

    /**
     * <code>POST /v1/receivers</code><br \>
     * <p>
     * Creates a new {@link Receiver} out of a {@link ReceiverCreate} object
     *
     * @param newReceiver {@link ReceiverCreate} object out of which Receiver is to be created
     * @return new {@link Receiver} as returned from the server
     */
    Receiver create(ReceiverCreate newReceiver);

    /**
     * returns details of a {@link Receiver}.
     *
     * @param receiverID Id of {@link Receiver}
     */
    Receiver get(String receiverID);

    /**
     * Deletes the specified {@link Receiver} from the server.
     *
     * @param receiverID Id of {@link Receiver}
     * @return the action response
     */
    ActionResponse delete(String receiverID);
}
