package org.openstack4j.api.workflow;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.workflow.Scope;
import org.openstack4j.model.workflow.WorkflowDefinition;

import java.io.InputStream;
import java.util.List;

/**
 * Service that provides CRUD operations for workflow definitions.
 *
 * @author Renat Akhmerov
 */
public interface WorkflowDefinitionService extends RestService {

    /**
     * List all workflow definitions with details.
     *
     * @return List of workflow definitions.
     */
    List<? extends WorkflowDefinition> list();

    /**
     * Create one or more workflow definitions.
     *
     * @param wfText Text in YAML format (Mistral language) with one or more workflow definitions.
     * @param scope  Scope of newly created workflows.
     * @return Created workflow definitions.
     */
    List<? extends WorkflowDefinition> create(InputStream wfText, Scope scope);

    /**
     * Get workflow definition by its identifier.
     *
     * @param identifier Workflow definition identifier (either ID or name).
     * @return Workflow definition.
     */
    WorkflowDefinition get(String identifier);

    /**
     * Delete workflow definition by its identifier.
     *
     * @param identifier Workflow definition identifier (either ID or name).
     * @return Action response from the server.
     */
    ActionResponse delete(String identifier);
}
