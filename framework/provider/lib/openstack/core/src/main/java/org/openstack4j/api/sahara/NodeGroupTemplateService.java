package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.NodeGroupTemplate;

import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author Ekasit Kijsipongse
 */
public interface NodeGroupTemplateService extends RestService {

    /**
     * List all node group templates
     *
     * @return list of node group templates or empty
     */
    List<? extends NodeGroupTemplate> list();

    /**
     * Get a node group template by ID
     *
     * @param templateId the template identifier
     * @return the node group template or null if not found
     */
    NodeGroupTemplate get(String templateId);

    /**
     * Create a new node group template
     *
     * @param template the node group template to create
     * @return the created node group template
     */
    NodeGroupTemplate create(NodeGroupTemplate template);

    /**
     * Delete the specified node group template
     *
     * @param templateId the template identifier
     * @return the action response
     */
    ActionResponse delete(String templateId);

}
