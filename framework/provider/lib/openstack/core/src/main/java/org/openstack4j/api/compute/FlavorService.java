package org.openstack4j.api.compute;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.FlavorAccess;

import java.util.List;
import java.util.Map;

/**
 * Flavor service provides CRUD capabilities for Flavor(s).  A flavor is an available hardware configuration/template for a server
 *
 * @author Jeremy Unruh, whaon
 */
public interface FlavorService extends RestService {

    /**
     * List all Flavors with details
     *
     * @return List of Flavor
     */
    List<? extends Flavor> list();

    /**
     * Get a Flavor by it's identifier
     *
     * @param flavorId the flavor identifier
     * @return Flavor
     */
    Flavor get(String flavorId);

    /**
     * Deletes a Flavor by it's identifier
     *
     * @param flavorId the flavor identifier
     * @return the action response
     */
    ActionResponse delete(String flavorId);

    /**
     * Creates a new Flavor
     *
     * @param flavor the flavor to create
     * @return the created flavor
     */
    Flavor create(Flavor flavor);

    /**
     * Creates a new Flavor
     *
     * @param name       the descriptive name of the flavor
     * @param ram        the Memory in MB for the flavor
     * @param vcpus      the Number of VCPUs for the flavor
     * @param disk       the size of the local disk in GB
     * @param the        space in GB that will disappear when the VM is terminated (default is 0) [OPTIONAL]
     * @param swap       the Swap space in MB
     * @param rxtxFactor the RX/TX factor (default is 1) [OPTIONAL]
     * @return the created flavor
     */
    Flavor create(String name, int ram, int vcpus, int disk, int ephemeral, int swap, float rxtxFactor, boolean isPublic);

    /**
     * list extra specs
     *
     * @return all extra specs for this flavor
     */
    Map<String, String> listExtraSpecs(String flavorId);

    /**
     * post a key-value map, if key exist, value will be updated, if not ,new
     * extra spec created. openstack provides one api to support both create and
     * update extra spec
     *
     * @return spec
     */
    Map<String, String> createAndUpdateExtraSpecs(String flavorId, Map<String, String> spec);

    /**
     * delete the extra spec with the key and flavorId
     */
    void deleteExtraSpecs(String flavorId, String key);

    /**
     * get the extra spec's value by the key
     *
     * @return value
     */
    String getSpec(String flavorId, String key);

    /**
     * List tenants with access to private flavor
     *
     * @return List tenants with access to private flavor
     */
    List<? extends FlavorAccess> listFlavorAccess(String flavorId);

    /**
     * Add access to private flavor
     *
     * @return List tenants with access to private flavor
     */
    List<? extends FlavorAccess> addTenantAccess(String flavorId, String tenantId);

    /**
     * Delete access from private flavor
     *
     * @return List tenants with access to private flavor
     */
    List<? extends FlavorAccess> removeTenantAccess(String flavorId, String tenantId);

    /**
     * list flavors
     *
     * @param detail          is detailed
     * @param filteringParams parameters affect the response data,availbed are:sort_key,sort_dir,limit,marker,minDisk,minRam,is_public
     */
    List<? extends Flavor> list(boolean detail, Map<String, String> filteringParams);

    /**
     * list flavors with detailed
     */
    List<? extends Flavor> list(Map<String, String> filteringParams);

    /**
     * list flavors with non filtering parameters
     */
    List<? extends Flavor> list(boolean detail);

}
