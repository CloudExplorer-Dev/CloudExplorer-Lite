package org.openstack4j.openstack.workflow.internal;

import org.openstack4j.api.workflow.WorkflowExecutionService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.workflow.WorkflowExecution;
import org.openstack4j.openstack.workflow.domain.MistralWorkflowExecution;
import org.openstack4j.openstack.workflow.domain.MistralWorkflowExecution.MistralWorkflowExecutions;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Workflow execution service implementation.
 *
 * @author Renat Akhmerov
 */
public class WorkflowExecutionServiceImpl extends BaseMistralService implements WorkflowExecutionService {

    @Override
    public List<? extends WorkflowExecution> list() {
        return get(MistralWorkflowExecutions.class, uri("/executions")).execute().getList();
    }

    @Override
    public WorkflowExecution create(WorkflowExecution wfExec) {
        checkNotNull(wfExec);

        Invocation<MistralWorkflowExecution> invocation = post(
                MistralWorkflowExecution.class,
                uri("/executions")
        );

        return invocation.entity(wfExec).execute();
    }

    @Override
    public WorkflowExecution get(String identifier) {
        return get(MistralWorkflowExecution.class, uri("/executions/%s", identifier)).execute();
    }

    @Override
    public ActionResponse delete(String identifier) {
        return deleteWithResponse(uri("/executions/%s", identifier)).execute();
    }

}
