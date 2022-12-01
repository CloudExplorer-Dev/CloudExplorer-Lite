package org.openstack4j.api.senlin;

import org.openstack4j.model.senlin.ActionID;

/**
 * This interface defines all methods for the manipulation of WebHook
 *
 * @author lion
 */
public interface SenlinWebHookService {

    /**
     * Service implementation which provides methods for manipulation of action
     *
     * @return Action
     */
    ActionID action(String webHookUrl);
}
