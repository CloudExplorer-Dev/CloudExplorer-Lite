package org.openstack4j.openstack.workflow.internal;

import org.openstack4j.api.workflow.WorkbookDefinitionService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.payloads.InputStreamPayload;
import org.openstack4j.model.workflow.Scope;
import org.openstack4j.model.workflow.WorkbookDefinition;
import org.openstack4j.openstack.workflow.domain.MistralWorkbookDefinition;
import org.openstack4j.openstack.workflow.domain.MistralWorkbookDefinition.MistralWorkbookDefinitions;

import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Workbook definition service implementation.
 *
 * @author Renat Akhmerov
 */
public class WorkbookDefinitionServiceImpl extends BaseMistralService implements WorkbookDefinitionService {

    @Override
    public List<? extends WorkbookDefinition> list() {
        return get(MistralWorkbookDefinitions.class, uri("/workbooks")).execute().getList();
    }

    @Override
    public WorkbookDefinition create(InputStream wfText, Scope scope) {
        checkNotNull(wfText);
        checkNotNull(scope);

        Invocation<MistralWorkbookDefinition> invocation = post(
                MistralWorkbookDefinition.class,
                uri("/workbooks?scope=%s", scope.toString().toLowerCase())
        );

        return invocation.entity(new InputStreamPayload(wfText)).execute();
    }

    @Override
    public WorkbookDefinition get(String identifier) {
        return get(MistralWorkbookDefinition.class, uri("/workbooks/%s", identifier)).execute();
    }

    @Override
    public ActionResponse delete(String identifier) {
        return deleteWithResponse(uri("/workbooks/%s", identifier)).execute();
    }
}
