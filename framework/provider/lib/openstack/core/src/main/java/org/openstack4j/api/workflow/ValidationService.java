package org.openstack4j.api.workflow;

import org.openstack4j.common.RestService;
import org.openstack4j.model.workflow.WorkflowDefinition;

/**
 * TODO
 *
 * @author Renat Akhmerov
 */
public interface ValidationService extends RestService {

    /**
     * Validate workflow definition.
     *
     * @param workflowDefinition Workflow definition.
     * @return TODO: What should be the return type?
     */
    String validate(WorkflowDefinition workflowDefinition);
}
