package org.openstack4j.openstack.workflow.internal;

import org.openstack4j.api.workflow.ActionExecutionService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.workflow.ActionExecution;
import org.openstack4j.openstack.workflow.domain.MistralActionExecution;
import org.openstack4j.openstack.workflow.domain.MistralActionExecution.MistralActionExecutions;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Action execution service implementation.
 *
 * @author Renat Akhmerov
 */
public class ActionExecutionServiceImpl extends BaseMistralService implements ActionExecutionService {

    @Override
    public List<? extends ActionExecution> list() {
        return get(MistralActionExecutions.class, uri("/action_executions")).execute().getList();
    }

    @Override
    public ActionExecution create(ActionExecution actionExec) {
        checkNotNull(actionExec);

        Invocation<MistralActionExecution> invocation = post(
                MistralActionExecution.class,
                uri("/executions")
        );

        return invocation.entity(actionExec).execute();
    }

    @Override
    public ActionExecution get(String id) {
        return get(MistralActionExecution.class, uri("/action_executions/%s", id)).execute();
    }

    @Override
    public ActionResponse delete(String id) {
        return deleteWithResponse(uri("/action_executions/%s", id)).execute();
    }
}
