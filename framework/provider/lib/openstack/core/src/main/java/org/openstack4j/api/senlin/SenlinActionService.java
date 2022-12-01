package org.openstack4j.api.senlin;

import org.openstack4j.model.senlin.Action;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of Action
 *
 * @author lion
 */
public interface SenlinActionService {

    /**
     * Gets a list of currently existing {@link Action}s.
     *
     * @return the list of {@link Action}s
     */
    List<? extends Action> list();

    /**
     * returns details of a {@link Action}.
     *
     * @param actionID Id of {@link Action}
     * @return Action
     */
    Action get(String actionID);
}
