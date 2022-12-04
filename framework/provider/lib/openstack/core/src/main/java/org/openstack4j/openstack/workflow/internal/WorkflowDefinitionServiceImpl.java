package org.openstack4j.openstack.workflow.internal;

import org.openstack4j.api.workflow.WorkflowDefinitionService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.payloads.InputStreamPayload;
import org.openstack4j.model.workflow.Scope;
import org.openstack4j.model.workflow.WorkflowDefinition;
import org.openstack4j.openstack.workflow.domain.MistralWorkflowDefinition;
import org.openstack4j.openstack.workflow.domain.MistralWorkflowDefinition.MistralWorkflowDefinitions;

import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Workflow definition service implementation.
 *
 * @author Renat Akhmerov
 */
public class WorkflowDefinitionServiceImpl extends BaseMistralService implements WorkflowDefinitionService {

    @Override
    public List<? extends WorkflowDefinition> list() {
        return get(MistralWorkflowDefinitions.class, uri("/workflows")).execute().getList();
    }

    @Override
    public List<? extends WorkflowDefinition> create(InputStream wfText, Scope scope) {
        checkNotNull(wfText);
        checkNotNull(scope);

        Invocation<MistralWorkflowDefinitions> invocation = post(
                MistralWorkflowDefinitions.class,
                uri("/workflows?scope=%s", scope.toString().toLowerCase())
        );

        return invocation.entity(new InputStreamPayload(wfText)).execute().getList();
    }

    @Override
    public WorkflowDefinition get(String identifier) {
        return get(MistralWorkflowDefinition.class, uri("/workflows/%s", identifier)).execute();
    }

    @Override
    public ActionResponse delete(String identifier) {
        return deleteWithResponse(uri("/workflows/%s", identifier)).execute();
    }

}
