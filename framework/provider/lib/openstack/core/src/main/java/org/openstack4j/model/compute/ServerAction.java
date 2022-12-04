package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

import java.util.Date;
import java.util.List;

/**
 * This interface describes the getter-methods (and thus components) of a {@link ServerAction}.
 * All getters map to the possible return values of
 * <code> GET /v2.1/servers/{server_id}/os-instance-actions​</code>
 * <code> GET /v2.1/servers/{server_id}/os-instance-actions/{request_id}​</code>
 *
 * @author sujit sah
 * @see https://docs.openstack.org/api-ref/compute/index.html
 */
public interface ServerAction extends ModelEntity {
    /**
     * Returns the action performed on the server
     *
     * @return the action performed on the server
     */
    public String getAction();

    /**
     * Returns the list of server actions
     *
     * @return the list of server actions
     */
    public List<? extends ServerActionEvent> getEvents();

    /**
     * Returns the id of the server
     *
     * @return the id of the server
     */
    public String getInstaceUuid();

    /**
     * Returns the request id of the server action
     *
     * @return the request id of the server action
     */
    public String getRequestId();

    /**
     * Returns the start_time of the server action
     *
     * @return the start_time of the server action
     */
    public Date getStartTime();

    /**
     * Returns the id of the user
     *
     * @return the id of user
     */
    public String getUserId();
}
