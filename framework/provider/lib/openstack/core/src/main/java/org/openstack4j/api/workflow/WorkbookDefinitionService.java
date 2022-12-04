package org.openstack4j.api.workflow;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.workflow.Scope;
import org.openstack4j.model.workflow.WorkbookDefinition;

import java.io.InputStream;
import java.util.List;

/**
 * Service that provides CRUD operations for workbook definitions.
 *
 * @author Renat Akhmerov
 */
public interface WorkbookDefinitionService extends RestService {
    /**
     * List all workbook definitions with details.
     *
     * @return List of workbook definitions.
     */
    List<? extends WorkbookDefinition> list();

    /**
     * Create a new workbook definition.
     *
     * @param wbText Text in YAML format (Mistral language) with a workbook definition.
     * @param scope  Scope of newly created workbook.
     * @return Created workbook definition.
     */
    WorkbookDefinition create(InputStream wbText, Scope scope);

    /**
     * Get workbook definition by its identifier.
     *
     * @param identifier Workbook definition identifier (either ID or name).
     * @return Workbook definition.
     */
    WorkbookDefinition get(String identifier);

    /**
     * Delete workbook definition by its identifier.
     *
     * @param identifier Workbook definition identifier (either ID or name).
     * @return Action response from the server.
     */
    ActionResponse delete(String identifier);
}
