package org.openstack4j.api.networking;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.Resource;
import org.openstack4j.openstack.networking.domain.NeutronResourceTag;

/**
 * @author bboyHan
 */
public interface NeutronResourceTagService extends RestService {

    /**
     * Confirms a given tag is set on the resource.
     *
     * @param resource   resource type
     * @param resourceId resource id
     * @param tag        tag
     * @return the ActionResponse
     */
    ActionResponse check(Resource resource, String resourceId, String tag);

    /**
     * Obtains the (Neutron) resource tags for a resource.
     *
     * @param resource   resource type
     * @param resourceId resource id
     * @return the NeutronResourceTag
     */
    NeutronResourceTag list(Resource resource, String resourceId);

    /**
     * Add tag to the resource.
     *
     * @param resourceId resource id
     * @param tag        tag
     * @return the ActionResponse
     */
    ActionResponse addSingle(Resource resource, String resourceId, String tag);

    /**
     * reset all tags from the resource
     *
     * @param resourceId resource id
     * @param tags       tags
     * @return the NeutronResourceTag
     */
    NeutronResourceTag replace(Resource resource, String resourceId, NeutronResourceTag tags);

    /**
     * Delete tag from the resource.
     *
     * @param resourceId resource id
     * @param tag        tag
     * @return the ActionResponse
     */
    ActionResponse delete(Resource resource, String resourceId, String tag);

    /**
     * Remove all tags from the resource
     *
     * @param resourceId resource id
     * @return the ActionResponse
     */
    ActionResponse deleteAll(Resource resource, String resourceId);

}
