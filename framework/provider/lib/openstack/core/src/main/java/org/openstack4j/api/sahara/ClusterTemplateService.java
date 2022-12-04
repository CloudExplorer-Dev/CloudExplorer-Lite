package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.ClusterTemplate;

import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author Ekasit Kijsipongse
 */
public interface ClusterTemplateService extends RestService {

    /**
     * List all cluster templates
     *
     * @return list of cluster templates or empty
     */
    List<? extends ClusterTemplate> list();

    /**
     * Get a cluster template by ID
     *
     * @param templateId the template identifier
     * @return the cluster template or null if not found
     */
    ClusterTemplate get(String templateId);

    /**
     * Create a new cluster template
     *
     * @param template the cluster template to create
     * @return the created cluster template
     */
    ClusterTemplate create(ClusterTemplate template);

    /**
     * Delete the specified cluster template
     *
     * @param templateId the template identifier
     * @return the action response
     */
    ActionResponse delete(String templateId);

}
