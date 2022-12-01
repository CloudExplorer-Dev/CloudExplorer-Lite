package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.Vip;
import org.openstack4j.model.network.ext.VipUpdate;

import java.util.List;
import java.util.Map;

/**
 * Networking (Neutron) Lbaas vip Extension API
 *
 * @author liujunpeng
 */
public interface VipService extends RestService {
    /**
     * List all vipss  that the current tenant has access to
     *
     * @return list of all vip
     */
    List<? extends Vip> list();

    /**
     * Returns list of vip filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return list of vip fitered by filteringParams
     */
    List<? extends Vip> list(Map<String, String> filteringParams);


    /**
     * Get the specified vip by ID
     *
     * @param vipId the vip identifier
     * @return the vip or null if not found
     */
    Vip get(String vipId);

    /**
     * Delete the specified vip by ID
     *
     * @param vipId the vip identifier
     * @return the action response
     */
    ActionResponse delete(String vipId);

    /**
     * Create a vip
     *
     * @param vip vip
     * @return Vip
     */
    Vip create(Vip vip);

    /**
     * Update a vip
     *
     * @param vipId the vip identifier
     * @param vip   VipUpdate
     * @return Vip
     */
    Vip update(String vipId, VipUpdate vip);
}
