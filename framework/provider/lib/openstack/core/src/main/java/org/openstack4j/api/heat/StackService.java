package org.openstack4j.api.heat;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.heat.AdoptStackData;
import org.openstack4j.model.heat.Stack;
import org.openstack4j.model.heat.StackCreate;
import org.openstack4j.model.heat.StackUpdate;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of stacks
 *
 * @author Matthias Reisser
 */
public interface StackService {
    /**
     * <code>POST /v1/{tenant_id}/stacks</code><br \>
     * <p>
     * Creates a new {@link Stack} out of a {@link StackCreate} object
     *
     * @param newStack {@link StackCreate} object out of which stack is to be created
     * @return new {@link Stack} as returned from the server
     */
    Stack create(StackCreate newStack);

    /**
     * Updates an existing Stack
     *
     * @param stackName   the stack name
     * @param stackId     the specific stack identifier
     * @param stackUpdate the stack update options
     * @return the action response
     */
    ActionResponse update(String stackName, String stackId, StackUpdate stackUpdate);

    /**
     * <code> POST /v1/{tenant_id}/stacks </code> <br\>
     * Creates a new {@link StackCreate} Object and returns a new {@link Stack} as sent from the
     * server.
     *
     * @param name            Name of Stack
     * @param template        Template in Json-Format or YAML format
     * @param parameters      Map of parameters
     * @param disableRollback boolean to enable or disable rollback
     * @param timeOutMins     timeout in minutes
     * @return new {@link Stack} as returned from the server
     */
    Stack create(String name, String template, Map<String, String> parameters,
                 boolean disableRollback, Long timeOutMins);

    /**
     * returns details of a {@link Stack}.
     *
     * @param stackName Name of {@link Stack}
     * @return {@link Stack}
     */
    Stack getStackByName(String name);

    /**
     * Gets a list of currently existing {@link Stack}s.
     *
     * @return the list of {@link Stack}s
     */
    List<? extends Stack> list();

    /**
     * Gets a list of currently existing {@link Stack} objects, filtered by parameters.
     *
     * @param filteringParams The parameters used to filter the stacks returned.
     * @return the list of {@link Stack} objects.
     */
    public List<? extends Stack> list(Map<String, String> filteringParams);

    /**
     * returns details of a {@link Stack}.
     *
     * @param stackName Name of {@link Stack}
     * @param stackId   Id of {@link Stack}
     */
    Stack getDetails(String stackName, String stackId);

    /**
     * Deletes the specified {@link Stack} from the server.
     *
     * @param stackName Name of {@link Stack}
     * @param stackId   Id of {@link Stack}
     * @return the action response
     */
    ActionResponse delete(String stackName, String stackId);

    /**
     * Deletes a stack but leaves its resources intact, and returns data that describes the stack and its resources.
     *
     * @param stackName Name of {@link Stack}
     * @param stackId   Id of {@link Stack}
     * @return <code>adopt_stack_data</code> element representing by {@link AdoptStackData}
     */
    AdoptStackData abandon(String stackName, String stackId);

    /**
     * Creates a stack from existing resources.
     *
     * @param adoptStackData  Structure {@link AdoptStackData}, representing existing resources
     * @param parameters      Map of parameters
     * @param disableRollback Enable or disable rollback
     * @param timeOutMins     Timeout in minutes
     * @param template        Template in Json-Format or YAML format. It is optional, used just in case there will be new resources (not included in adoptStackData)
     */
    Stack adopt(AdoptStackData adoptStackData, Map<String, String> parameters,
                boolean disableRollback, Long timeOutMins, String template);
}
